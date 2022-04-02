package concurrency;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Concurrency {
	public static void main(String[] args) {
	}

	// 1242. Web Crawler Multithreaded
	// https://leetcode.com/problems/web-crawler-multithreaded/discuss/725902/AsyncCrawler-with-CompletableFuture-s
	interface HtmlParser {
		public List<String> getUrls(String url);
	}

	public List<String> crawl(String startUrl, HtmlParser htmlParser) {
		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(cores - 1);
		AsyncCrawler crawler = new AsyncCrawler(htmlParser, executor);
		Set<String> visited = crawler.crawl(startUrl);
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {

		}
		return new ArrayList<String>(visited);
	}

	class AsyncCrawler {
		private final HtmlParser htmlParser;
		private final ExecutorService executor;
		private final Set<String> visited = ConcurrentHashMap.newKeySet();

		AsyncCrawler(HtmlParser htmlParser, ExecutorService executor) {
			this.htmlParser = htmlParser;
			this.executor = executor;
		}

		public Set<String> crawl(String startUrl) {
			String host = getHostname(startUrl);
			// System.out.println("Crawl asynchronously. Host: " + host);
			return async(startUrl, host).join();
		}

		CompletableFuture<Set<String>> async(String url, String host) {
			// System.out.println("Async crawling. Url: " + url);
			visited.add(url);

			return CompletableFuture.supplyAsync(() -> htmlParser.getUrls(url), executor).thenCompose(subUrls -> {
				final CompletableFuture<Set<String>> result = new CompletableFuture<>();
				List<CompletableFuture<Set<String>>> futures = new ArrayList<>();

				for (String subUrl : subUrls) {
					// System.out.println("Parse subUrl: " + subUrl);
					if (visited.contains(subUrl) || !isSameHostname(subUrl, host)) {
						continue;
					}
					futures.add(async(subUrl, host));
				}
				CompletableFuture<Void> combinedFuture = CompletableFuture
						.allOf(futures.toArray(new CompletableFuture[futures.size()]));
				combinedFuture.whenComplete((input, exception) -> {
					for (CompletableFuture<Set<String>> cfu : futures) {
						visited.addAll(cfu.join());
					}
					result.complete(visited);
				});

				return result;
			});
		}

		String getHostname(String url) {
			int idx = url.indexOf('/', 7);
			return (idx != -1) ? url.substring(0, idx) : url;
		}

		boolean isSameHostname(String url, String hostname) {
			if (!url.startsWith(hostname)) {
				return false;
			}
			return url.length() == hostname.length() || url.charAt(hostname.length()) == '/';
		}
	}

	// 1188. Design Bounded Blocking Queue
	class BoundedBlockingQueue {
		private int capacity;
		private Queue<Integer> queue;

		public BoundedBlockingQueue(int capacity) {
			this.capacity = capacity;
			this.queue = new LinkedList<>();
		}

		public void enqueue(int element) throws InterruptedException {
			synchronized (queue) {
				while (queue.size() == capacity)
					queue.wait();

				queue.add(element);
				queue.notifyAll();
			}
		}

		public int dequeue() throws InterruptedException {
			synchronized (queue) {
				while (queue.isEmpty())
					queue.wait();

				int elem = queue.remove();
				queue.notifyAll();

				return elem;
			}
		}

		public int size() {
			return queue.size();
		}
	}

	// 1117. Building H2O
	class H2O {
		Semaphore hSemaphore;
		Semaphore oSemaphore;

		public H2O() {
			hSemaphore = new Semaphore(2);
			// O start with 0, so only H at beginning, after 2H, we can do O
			oSemaphore = new Semaphore(0);
		}

		public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
			hSemaphore.acquire();
			// releaseHydrogen.run() outputs "H". Do not change or remove this line.
			releaseHydrogen.run();
			// always make sure we got 2 H before we release O
			if (hSemaphore.availablePermits() == 0) {
				oSemaphore.release();
			}
		}

		public void oxygen(Runnable releaseOxygen) throws InterruptedException {
			oSemaphore.acquire();
			// releaseOxygen.run() outputs "O". Do not change or remove this line.
			releaseOxygen.run();
			hSemaphore.release(2);
		}
	}

	// Design a delayed Scheduler
	// https://silhding.github.io/2021/03/13/How-to-design-a-delayed-scheduler-in-Java/
}
