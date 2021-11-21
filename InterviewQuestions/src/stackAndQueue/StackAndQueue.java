package stackAndQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class StackAndQueue {
	public static void main(String args[]) {
		decodeString("2[abc]3[cd]ef");
		largestRectangleArea(new int[] { 2, 1, 5, 6, 2, 3 });
		sortColors(new int[] { 2, 0, 1 });
	}

	// 用两个栈实现队列
	class MyQueue {
		final Stack<Integer> in;
		final Stack<Integer> out;

		/** Initialize your data structure here. */
		public MyQueue() {
			in = new Stack<Integer>();
			out = new Stack<Integer>();
		}

		/** Push element x to the back of queue. */
		public void push(int x) {
			in.add(x);
		}

		/** Removes the element from in front of queue and returns that element. */
		public int pop() {
			if (!out.isEmpty()) {
				return out.pop();
			}
			while (!in.isEmpty()) {
				out.add(in.pop());
			}
			return out.pop();
		}

		/** Get the front element. */
		public int peek() {
			if (!out.isEmpty()) {
				return out.peek();
			}
			while (!in.isEmpty()) {
				out.add(in.pop());
			}
			return out.peek();
		}

		/** Returns whether the queue is empty. */
		public boolean empty() {
			return in.isEmpty() && out.isEmpty();
		}
	}

	// 用队列实现栈
	// 在将一个元素 x 插入队列时，为了维护原来的后进先出顺序，需要让 x 插入队列首部。
	// 而队列的默认插入顺序是队列尾部，因此在将 x 插入队列尾部之后，需要让除了 x 之外的所有元素出队列，再入队列。
	class MyStack {

		private Queue<Integer> queue;

		public MyStack() {
			queue = new LinkedList<>();
		}

		public void push(int x) {
			queue.add(x);
			int cnt = queue.size();
			while (cnt-- > 1) {
				queue.add(queue.poll());
			}
		}

		public int pop() {
			return queue.remove();
		}

		public int top() {
			return queue.peek();
		}

		public boolean empty() {
			return queue.isEmpty();
		}
	}

	// 堆栈实现最小值栈
	class MinStack {
		private Stack<Integer> dataStack;
		private Stack<Integer> minStack;
		private int min;

		public MinStack() {
			dataStack = new Stack<>();
			minStack = new Stack<>();
			min = Integer.MAX_VALUE;
		}

		public void push(int x) {
			dataStack.add(x);
			min = Math.min(min, x);
			minStack.add(min);
		}

		public void pop() {
			dataStack.pop();
			minStack.pop();
			min = minStack.isEmpty() ? Integer.MAX_VALUE : minStack.peek();
		}

		public int top() {
			return dataStack.peek();
		}

		public int getMin() {
			return minStack.peek();
		}
	}

	// 对于实现最小值队列问题，可以先将队列使用栈来实现，然后就将问题转换为最小值栈

	// 数组中元素与下一个比它大的元素之间的距离
	// https://www.youtube.com/watch?v=d4FvlTzzWjQ
	public int[] dailyTemperatures(int[] temperatures) {
		// store a list of indexs, which their results have not been found yet
		final Stack<Integer> indexs = new Stack<Integer>();
		final int dist[] = new int[temperatures.length];

		for (int currIndex = 0; currIndex < temperatures.length; currIndex++) {
			// 堆栈顶topIndex是当前正在找的一天，如果currIndex那天温度较高，则已经找到，可以出栈
			while (!indexs.isEmpty() && temperatures[indexs.peek()] < temperatures[currIndex]) {
				int topTemp = temperatures[indexs.peek()]; // temperature at top of the stack
				int topIndex = indexs.pop();

				dist[topIndex] = currIndex - topIndex;
			}
			// currIndex 那天比栈顶的那天温度还低，那就把currIndex入栈，开始找哪天比他高
			indexs.add(currIndex);
		}

		// if there are days where higher temperature does not exist, return 0s for them
		while (!indexs.isEmpty()) {
			dist[indexs.pop()] = 0;
		}
		return dist;
	}

	// LeetCode 496. Next Greater Element I, 给array，找每个元素下一个比他大的元素的值
	// 直接遍历数组，用栈找比当前元素大的，找到直接放到hashmap里
	// https://www.youtube.com/watch?v=MH-9LmOb4gE
	// https://leetcode.com/problems/next-greater-element-i/

	// 503. Next Greater Element II
	// 循环数组中，查找比当前元素大的下一个元素
	// 和上题类似，区别是：数组是循环数组，并且最后要求的不是距离而是下一个元素。
	public int[] nextGreaterElements(int[] nums) {
		int[] next = new int[nums.length];
		Stack<Integer> indexs = new Stack<Integer>();
		Arrays.fill(next, -1);

		for (int i = 0; i < 2 * nums.length; i++) {
			int n = i % nums.length;
			while (!indexs.isEmpty() && nums[indexs.peek()] < nums[n]) {
				int topIndex = indexs.pop();
				next[topIndex] = nums[n];
			}

			indexs.add(n);
		}
		return next;
	}

	// 215. Kth Largest Element in an Array
	// use minHeap, 只放比栈顶大的，方k个，这样栈顶肯定是kth largest
	// https://leetcode.com/problems/kth-largest-element-in-an-array/description/
	public int findKthLargest(int[] nums, int k) {
		PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>();
		for (int num : nums) {
			if (maxHeap.size() < k || maxHeap.peek() <= num) {
				maxHeap.offer(num);
			}
			if (maxHeap.size() > k) {
				maxHeap.poll();
			}
		}
		return maxHeap.peek();
	}

	// Remove All Adjacent Duplicates In String (1047)
	// 用堆栈最方便
	// https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/
	public String removeDuplicates(String s) {
		Stack<Character> result = new Stack<Character>();
		char[] sArray = s.toCharArray();

		for (int i = 0; i < sArray.length; i++) {
			if (result.isEmpty()) {
				result.add(sArray[i]);
				continue;
			}
			if (result.peek() == sArray[i]) {
				result.pop();
			} else {
				result.push(sArray[i]);
			}
		}
		char[] resultArray = new char[result.size()];
		for (int i = result.size() - 1; i >= 0; i--) {
			resultArray[i] = result.pop();
		}
		return String.valueOf(resultArray);
	}

	// 1209. Remove All Adjacent Duplicates in String II
	// https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/
	// 移除任意k长度的duplicate
	public String removeDuplicates(String s, int k) {
		Stack<CharCount> stack = new Stack<CharCount>();
		char[] sArray = s.toCharArray();
		for (char ch : sArray) {
			if (stack.isEmpty()) {
				stack.push(new CharCount(ch, 1));
				continue;
			}
			if (ch == stack.peek().ch) {
				CharCount current = stack.pop();
				current.count++;
				if (current.count < k) {
					stack.push(current);
				}
			} else {
				stack.push(new CharCount(ch, 1));
			}
		}

		StringBuilder sb = new StringBuilder();
		while (!stack.isEmpty()) {
			CharCount cc = stack.pop();
			for (int i = 0; i < cc.count; i++) {
				sb.append(cc.ch);
			}
		}
		return sb.reverse().toString();
	}

	public class CharCount {
		char ch;
		int count;

		public CharCount(char ch, int count) {
			this.ch = ch;
			this.count = count;
		}
	}

	// 735. Asteroid Collision
	// https://leetcode.com/problems/asteroid-collision/
	public int[] asteroidCollision(int[] asteroids) {
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(asteroids[0]);
		for (int i = 1; i < asteroids.length; i++) {
			int currNum = asteroids[i];
			while (!stack.isEmpty() && Integer.signum(currNum) != Integer.signum(stack.peek()) && currNum < 0) {
				int result = asteroids[i] + (int) stack.peek();
				if (result == 0) {
					stack.pop();
					currNum = result;
					break;
				} else {
					currNum = Integer.signum(result) * Math.max(Math.abs(currNum), Math.abs(stack.pop()));
				}
			}
			if (currNum != 0) {
				stack.push(currNum);
			}
		}
		int[] res = new int[stack.size()];
		for (int i = res.length - 1; i >= 0; i--) {
			res[i] = stack.pop();
		}
		return res;
	}

	// 394. Decode String
	// 用堆栈
	// https://leetcode.com/problems/decode-string/
	public static String decodeString(String s) {
		Stack<EncodeNode> stack = new Stack<EncodeNode>();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c)) {
				int num = c - '0';
				while (i + 1 < n && Character.isDigit(s.charAt(i + 1))) {
					num = num * 10 + s.charAt(i + 1) - '0';
					i++;
				}
				stack.push(new EncodeNode(num, false, ""));
			} else if (Character.isAlphabetic(c)) {
				StringBuilder temp = new StringBuilder();
				temp.append(c);
				while (i + 1 < n && Character.isAlphabetic(s.charAt(i + 1))) {
					temp.append(s.charAt(i + 1));
					i++;
				}
				pushStringToNodeStack(stack, temp);
			} else if (c == ']') {
				EncodeNode node = stack.pop();
				StringBuilder temp = new StringBuilder();
				for (int j = 0; j < node.count; j++) {
					temp.append(node.s);
				}
				pushStringToNodeStack(stack, temp);
			} else { // c == '['
				continue;
			}
		}
		StringBuilder sb = new StringBuilder();
		while (!stack.isEmpty()) {
			EncodeNode node = stack.pop();
			StringBuilder temp = new StringBuilder();
			for (int j = 0; j < node.count; j++) {
				temp.append(node.s);
			}
			sb.insert(0, temp.toString());
		}
		return sb.toString();
	}

	private static void pushStringToNodeStack(Stack<EncodeNode> stack, StringBuilder temp) {
		if (!stack.isEmpty()) {
			EncodeNode nextNode = stack.peek();
			if (!nextNode.isClosed) {
				nextNode = stack.pop();
				nextNode.s += temp.toString();
				stack.push(nextNode);
			} else {
				stack.push(new EncodeNode(1, true, temp.toString()));
			}
		} else {
			stack.push(new EncodeNode(1, true, temp.toString()));
		}
	}

	static class EncodeNode {
		int count;
		boolean isClosed;
		String s;

		public EncodeNode(int count, boolean isClosed, String s) {
			this.count = count;
			this.isClosed = isClosed;
			this.s = s;
		}
	}

	// 636. Exclusive Time of Functions 程序执行时间，直接用堆栈即可
	// https://leetcode.com/problems/exclusive-time-of-functions/

	// 84. Largest Rectangle in Histogram
	// 分别从左右遍历两次，拿到两个index数组，分别对应每个元素向左/右看有多少大于等于当前元素高度的
	// 利用这个两个index数组，就能得到当前元素作为高度的长方形
	// https://leetcode.com/problems/largest-rectangle-in-histogram/
	public static int largestRectangleArea(int[] heights) {
		Stack<Integer> stack = new Stack<Integer>();
		int[] indexTillLowerBarFromLeft = new int[heights.length];
		int[] indexTillLowerBarFromRight = new int[heights.length];
		stack.push(0);
		for (int i = 1; i < heights.length; i++) {
			int height = heights[i];
			if (!stack.isEmpty()) {
				while (!stack.isEmpty()) {
					if (heights[stack.peek()] > height) {
						int index = stack.pop();
						indexTillLowerBarFromLeft[index] = i - index;
					} else {
						stack.push(i);
						break;
					}
				}
				if (stack.isEmpty()) {
					stack.push(i);
				}
			} else {
				stack.push(i);
			}
		}
		while (!stack.isEmpty()) {
			int index = stack.pop();
			indexTillLowerBarFromLeft[index] = heights.length - index;
		}

		for (int i = heights.length - 1; i >= 0; i--) {
			int height = heights[i];
			if (!stack.isEmpty()) {
				while (!stack.isEmpty()) {
					if (heights[stack.peek()] > height) {
						int index = stack.pop();
						indexTillLowerBarFromRight[index] = index - i;
					} else {
						stack.push(i);
						break;
					}
				}
				if (stack.isEmpty()) {
					stack.push(i);
				}
			} else {
				stack.push(i);
			}
		}
		while (!stack.isEmpty()) {
			int index = stack.pop();
			indexTillLowerBarFromRight[index] = index + 1;
		}

		int maxArea = 0;
		for (int i = 0; i < indexTillLowerBarFromLeft.length; i++) {
			int currArea = heights[i] * (indexTillLowerBarFromLeft[i] + indexTillLowerBarFromRight[i] - 1);
			maxArea = Math.max(currArea, maxArea);
		}
		return maxArea;
	}

	// 按颜色进行排序： 只有 0/1/2 三种颜色
	// https://leetcode.com/problems/sort-colors
	public static void sortColors(int[] nums) {
		int zero = -1;
		int one = 0;
		int two = nums.length - 1;
		while (one <= two) {
			if (nums[one] == 0) {
				zero++;
				swap(nums, zero, one);
				one++;
			} else if (nums[one] == 2) {
				swap(nums, one, two);
				two--;
			} else {
				one++;
			}
		}
	}

	private static void swap(int[] nums, int i, int j) {
		int t = nums[i];
		nums[i] = nums[j];
		nums[j] = t;
	}

	// 295. Find Median from Data Stream
	public static class MedianFinder {
		Queue<Integer> minHeap; // 存较大的一半数
		Queue<Integer> maxHeap; // 存较小的一半数

		/** initialize your data structure here. */
		public MedianFinder() {
			this.minHeap = new PriorityQueue<>();
			this.maxHeap = new PriorityQueue<>(Collections.reverseOrder());
		}

		public void addNum(int num) {
			if (maxHeap.size() == 0) {
				maxHeap.add(num);
			} else if (minHeap.size() == 0) {
				if (num > maxHeap.peek()) {
					minHeap.add(num);
				} else {
					maxHeap.add(num);
				}
			} else {
				if (num <= maxHeap.peek()) {
					maxHeap.add(num);
				} else {
					minHeap.add(num);
				}
			}
			balance();
		}

		public void balance() {
			if (Math.abs(minHeap.size() - maxHeap.size()) <= 1) {
				return;
			}
			while (Math.abs(minHeap.size() - maxHeap.size()) >= 2) {
				if (minHeap.size() > maxHeap.size()) {
					maxHeap.add(minHeap.poll());
				} else {
					minHeap.add(maxHeap.poll());
				}
			}
		}

		public double findMedian() {
			if (minHeap.size() == maxHeap.size()) {
				return (maxHeap.peek() + minHeap.peek()) / 2.0;
			} else {
				if (minHeap.size() > maxHeap.size()) {
					return minHeap.peek();
				} else {
					return maxHeap.peek();
				}
			}
		}
	}

	// 239 滑动窗口最大值
	// https://github.com/MisterBooo/LeetCodeAnimation/blob/master/notes/LeetCode%E7%AC%AC239%E5%8F%B7%E9%97%AE%E9%A2%98%EF%BC%9A%E6%BB%91%E5%8A%A8%E7%AA%97%E5%8F%A3%E6%9C%80%E5%A4%A7%E5%80%BC.md
	// 利用一个 双端队列，在队列中存储元素在数组中的位置， 并且维持队列的严格递减,，也就说维持队首元素是 **最大的 **
	// 当遍历到一个新元素时, 如果队列里有比当前元素小的，就将其移除队列，以保证队列的递减。当队列元素位置之差大于 k，就将队首元素移除
	public int[] maxSlidingWindow(int[] nums, int k) {
		if (nums == null || nums.length < k || k == 0)
			return new int[0];

		int[] res = new int[nums.length - k + 1];
		// 双端队列
		Deque<Integer> deque = new LinkedList<>();
		for (int i = 0; i < nums.length; i++) {
			// 在尾部添加元素，并保证左边元素都比尾部大
			while (!deque.isEmpty() && nums[deque.getLast()] < nums[i]) {
				deque.removeLast();
			}
			deque.addLast(i);
			// 在头部移除元素
			if (deque.getFirst() == i - k) {
				deque.removeFirst();
			}
			// 输出结果
			if (i >= k - 1) {
				res[i - k + 1] = nums[deque.getFirst()];
			}
		}
		return res;
	}

	// 56. Merge Intervals
	public int[][] merge(int[][] intervals) {
		Arrays.sort(intervals, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				Integer.compare(o2[0], o1[0]);
				return o1[0] - o2[0];
			}
		});
		List<int[]> ans = new ArrayList<int[]>();
		int[] preInterval = intervals[0];
		for (int i = 1; i < intervals.length; i++) {
			int[] nextInterval = intervals[i];
			if (preInterval[1] < nextInterval[0]) {
				ans.add(preInterval);
				preInterval = nextInterval;
			} else {
				preInterval[0] = Math.min(preInterval[0], nextInterval[0]);
				preInterval[1] = Math.max(preInterval[1], nextInterval[1]);
			}
		}
		ans.add(preInterval);
		int[][] formatedAns = new int[ans.size()][intervals[0].length];
		return ans.toArray(formatedAns);
	}

	// 380. Insert Delete GetRandom O(1)
	class RandomizedSet {
		final Map<Integer, Integer> map;
		final List<Integer> list;
		final Random random;

		public RandomizedSet() {
			map = new HashMap<>();
			list = new ArrayList<>();
			random = new Random();
		}

		public boolean insert(int val) {
			if (map.containsKey(val)) {
				return false;
			}
			map.put(val, list.size());
			list.add(val);
			return true;
		}

		public boolean remove(int val) {
			if (!map.containsKey(val)) {
				return false;
			}
			int index = map.get(val);
			int lastElement = list.get(list.size() - 1);
			map.put(lastElement, index);
			list.set(index, lastElement);
			list.remove(list.size() - 1);
			map.remove(val);
			return true;
		}

		public int getRandom() {
			int rand = random.nextInt(list.size());
			return list.get(rand);
		}
	}
}
