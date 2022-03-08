package concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Wraps async task scheduling in a central component and handles timeouts on
 * all created futures.
 */
@Named
@Singleton
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public class FutureFactory implements AfterRequestTask {

	static final String COMPLETED_EXCEPTIONALLY = "Homepage:ServerSideCounters:futureFactory:completedExceptionally";
	static final String CFU_KEY = FutureFactory.class.getName() + ".cfusKey";
	static final String DELAYS_KEY = FutureFactory.class.getName() + ".delaysKey";
	static final String TIMEOUT_KEY = FutureFactory.class.getName() + ".timeoutKey";
	/**
	 * Must be lower than {@link JavaScriptLayout#EXECUTE_TIMEOUT_MS}
	 */
	private static final long MAX_MILLIS = 5500L;

	private final Executor threadPool;
	private final Supplier<BiConsumer<String, Double>> counter;
	private final RequestStore store;
	private final ScheduledTaskProvider delayer;

	@Inject
	public FutureFactory(Executor threadPool, HomepageInstrumentationProvider instrumentation, RequestStore store,
			ScheduledTaskProvider delayer) {
		this(threadPool, () -> instrumentation.get()::addCount, store, delayer);
	}

	public <T> CompletableFuture<T> supplyAsync(Supplier<T> blockingSupplier) {
		return supplyAsync(blockingSupplier, MAX_MILLIS);
	}

	public <T> CompletableFuture<T> withTimeout() {
		return withTimeout(CompletableFuture::new);
	}

	public <T> CompletableFuture<T> withTimeout(Supplier<CompletableFuture<T>> futureSupplier) {
		return withTimeout(MAX_MILLIS, futureSupplier);
	}

	@Beta // JDK 9 comes with schedulable CFU<>, this API will change once upgraded
	public <T> Cancellable<T> schedule(Callable<T> task, Consumer<Boolean> onCancel, long millis) {
		return autoCancelDelay(task, onCancel, millis);
	}

	@Deprecated
	// Only used for temporary experiment. Please do not use for any other usecase!
	public void scheduleBlockingDelay(long millis, HomepageInstrumentation instrumentation)
			throws InterruptedException, ExecutionException {
		final Runnable noopRunnable = () -> {
		};
		final Consumer<Boolean> consumer = b -> logFailure(b, instrumentation);
		autoCancelDelay(noopRunnable, consumer, millis).getDelayed().get();
	}

	private void logFailure(boolean isCancelled, HomepageInstrumentation instrumentation) {
		if (isCancelled) {
			instrumentation.addCount("ScheduledFutureCancelled");
		}
	}

	public static long getMaxMillis() {
		return MAX_MILLIS;
	}

	/**
	 * @param millis the first timeout within {@code RequestStore}'s context wins,
	 *               <b>for testing only<b>
	 * @return
	 */
	@VisibleForTesting
	<T> CompletableFuture<T> supplyAsync(Supplier<T> blockingSupplier, long millis) {
		return withTimeout(millis, () -> CompletableFuture.supplyAsync(blockingSupplier, threadPool));
	}

	/**
	 * @param millis the first timeout within {@code RequestStore}'s context wins,
	 *               <b>for testing only<b>
	 * @return
	 */
	@VisibleForTesting
	<T> CompletableFuture<T> withTimeout(long millis) {
		return withTimeout(millis, CompletableFuture::new);
	}

	public <T> CompletableFuture<T> withTimeout(long millis, Supplier<CompletableFuture<T>> futureSupplier) {
		if (millis > MAX_MILLIS) {
			throw new IllegalArgumentException("Max timeout is " + MAX_MILLIS + "ms, supplied " + millis + "ms");
		}
		final CompletableFuture<T> timeout = timeout(() -> scheduleTimeout(millis));
		if (timeout.isDone()) {
			return timeout;
		}
		final CompletableFuture<T> cfu = futureSupplier.get();
		cfus().add(cfu);
		if (timeout.isDone()) {
			return timeout;
		}
		return cfu;
	}

	private ConcurrentLinkedDeque<CompletableFuture<?>> cfus() {
		return store.computeIfAbsent(CFU_KEY, k -> new ConcurrentLinkedDeque<>());
	}

	private ConcurrentLinkedQueue<Cancellable<?>> delays() {
		return store.computeIfAbsent(DELAYS_KEY, k -> new ConcurrentLinkedQueue<>());
	}

	private <T> CompletableFuture<T> timeout(Supplier<CompletableFuture<T>> creator) {
		return store.computeIfAbsent(TIMEOUT_KEY, k -> creator.get());
	}

	private <T> CompletableFuture<T> scheduleTimeout(long millis) {
		final ConcurrentLinkedDeque<CompletableFuture<?>> cfus = cfus();
		final ScheduledCompletableFuture<T> result = new ScheduledCompletableFuture<>();
		cfus.add(result);
		autoCancelDelay(() -> cleanup(cfus, millis), t -> cleanup(cfus, -1), millis);
		return result;
	}

	private <V> Cancellable<V> autoCancelDelay(Callable<V> task, Consumer<Boolean> onCancel, long millis) {
		final Cancellable<V> delayed = delayer.schedule(task, onCancel, millis);
		delays().add(delayed);
		return delayed;
	}

	private Cancellable<?> autoCancelDelay(Runnable task, Consumer<Boolean> onCancel, long millis) {
		final Cancellable<?> delayed = delayer.schedule(task, onCancel, millis);
		delays().add(delayed);
		return delayed;
	}

	private Void cleanup(ConcurrentLinkedDeque<CompletableFuture<?>> cfus, long millis) {
		final Exception e = new FutureTimeoutException(millis);
		int n = 0;
		while (!cfus.isEmpty()) {
			final CompletableFuture<?> cfu = cfus.pollLast(); // complete in LIFO fashion, 'deep' CFU<> first
			if (cfu != null && completeExceptionally(cfu, e)) {
				++n;
			}
		}
		counter.get().accept(COMPLETED_EXCEPTIONALLY, new Double(n));
		if (n > 0) {
			log.warn("Completed {} futures exceptionally, timeout={}ms", n, millis);
		}
		return null;
	}

	private static boolean completeExceptionally(CompletableFuture<?> cfu, Exception e) {
		// NOTICE: canceling a scheduled task after the request completes is a cleanup
		// w/o any warning
		return cfu.completeExceptionally(e) && !(cfu instanceof ScheduledCompletableFuture);
	}

	@Override
	public void afterRequest() {
		final ConcurrentLinkedQueue<Cancellable<?>> delays = delays();
		int n = 0;
		while (!delays.isEmpty()) {
			final Cancellable<?> c = delays.poll();
			if (c != null && c.cancel()) {
				++n;
			}
		}
		if (n > 0) {
			log.debug("Canceled {} delays", n);
		}
	}

	private static final class FutureTimeoutException extends TimeoutException {
		private static final long serialVersionUID = -6755965429252200033L;

		private FutureTimeoutException(long millis) {
			super("timeout=" + millis + "ms");
		}

		@Override
		public synchronized Throwable fillInStackTrace() {
			// Suppress stack trace https://issues.amazon.com/issues/GW-2605
			return this;
		}
	}

	private static final class ScheduledCompletableFuture<T> extends CompletableFuture<T> {
	}

	@Named
	@Singleton
	@RequiredArgsConstructor(onConstructor = @__(@Inject))
	public static class HomepageInstrumentationProvider {
		private final ApplicationContext ctx;

		/**
		 * Circular dependency workaround, HomepageInstrumentation -> PCS -> FuFa <br>
		 * Provider<?> is a performance hit
		 */
		public HomepageInstrumentation get() {
			return ctx.getBean(HomepageInstrumentation.class);
		}
	}
}
