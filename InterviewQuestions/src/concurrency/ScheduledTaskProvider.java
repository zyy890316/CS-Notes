package concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.inject.Inject;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class ScheduledTaskProvider {

	private final ScheduledExecutorService delayer;

	/**
	 * No propagation, for testing purposes only.
	 */
	public ScheduledTaskProvider() {
		this(Executors.newScheduledThreadPool(1));
	}

	@Inject
	public ScheduledTaskProvider(ScheduledExecutorService delayer) {
		this.delayer = delayer;
	}

	/**
	 * <b>NOTICE</b> delayed tasks run on a separate thread pool, that does not
	 * propagate ThreadLocal content, i.e. do not rely on
	 * {@link com.amazon.homepage.service.request.RequestStore} to be present in
	 * delayed tasks.
	 *
	 * @param task
	 * @param onCancel
	 * @param delayMillis
	 * @return
	 */
	<V> Cancellable<V> schedule(Callable<V> task, Consumer<Boolean> onCancel, long delayMillis) {
		return new Cancellable<>(delayer.schedule(task, delayMillis, TimeUnit.MILLISECONDS), onCancel);
	}

	@Deprecated
	// Only used for temporary experiment. Please do not use for any other usecase!
	Cancellable<?> schedule(Runnable task, Consumer<Boolean> onCancel, long delayMillis) {
		return new Cancellable<>(delayer.schedule(task, delayMillis, TimeUnit.MILLISECONDS), onCancel);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Cancellable<V> {
		@NonNull
		@Getter
		private final ScheduledFuture<V> delayed;
		@NonNull
		private final Consumer<Boolean> onCancel;

		boolean cancel() {
			final boolean canceled = delayed.cancel(false);
			onCancel.accept(canceled);
			return canceled;
		}

		boolean isDone() {
			return delayed.isDone();
		}
	}
}
