package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoinFutures {
	public static void main(String[] args) {
		String threadName = Thread.currentThread().getName();
		System.out.println(threadName + " running");
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("Runtime cores: " + cores);
		ExecutorService executor = Executors.newFixedThreadPool(cores - 1);
		List<CompletableFuture<Integer>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			futures.add(CompletableFuture.supplyAsync(() -> index, executor));
		}
		CompletableFuture<Void> combinedFuture = CompletableFuture
				.allOf(futures.toArray(new CompletableFuture[futures.size()]));
		combinedFuture.join();
		System.out.println(threadName + " finished");
		executor.shutdown();
	}
}
