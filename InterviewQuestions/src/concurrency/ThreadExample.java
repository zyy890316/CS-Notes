package concurrency;

public class ThreadExample {
	public static void main(String[] args) throws InterruptedException {
		Runnable runnable = () -> {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + " running");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(threadName + " finished");
		};
		Thread thread = new Thread(runnable);
		thread.run();

		StoppableRunnable stoppableRunnable = new StoppableRunnable();
		Thread stoppablethread = new Thread(stoppableRunnable, "stoppableRunnable thread");
		stoppablethread.run();
		Thread.sleep(1000);
		stoppableRunnable.requestStop();
	}

	public static class StoppableRunnable implements Runnable {

		private boolean stopped = false;

		public synchronized void requestStop() {
			this.stopped = true;
		}

		public synchronized boolean isStopped() {
			return this.stopped;
		}

		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + " running");
			while (!stopped) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("......");
			}
		}

	}
}
