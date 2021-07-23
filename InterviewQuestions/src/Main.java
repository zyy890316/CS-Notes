import stackAndQueue.StackAndQueue.MedianFinder;

public class Main {
	public static void main(String[] args) {
		MedianFinder m = new MedianFinder();
		m.addNum(1);
		m.addNum(2);
		m.addNum(3);
		double a = m.findMedian();
	}
}
