package stackAndQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javafx.util.Pair;

public class StackAndQueue {
	public static void main(String args[]) {
		decodeString("2[abc]3[cd]ef");
		largestRectangleArea(new int[] { 2, 1, 5, 6, 2, 3 });
		find132pattern(new int[] { 2, 4, 3, 1 });
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
				int result = asteroids[i] + stack.peek();
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
			while (!stack.isEmpty() && heights[stack.peek()] > height) {
				int index = stack.pop();
				indexTillLowerBarFromLeft[index] = i - index;
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int index = stack.pop();
			indexTillLowerBarFromLeft[index] = heights.length - index;
		}

		for (int i = heights.length - 1; i >= 0; i--) {
			int height = heights[i];
			while (!stack.isEmpty() && heights[stack.peek()] > height) {
				int index = stack.pop();
				indexTillLowerBarFromRight[index] = index - i;
			}
			stack.push(i);
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

	// 480. Sliding Window Median
	// 跟295类似，可以用两个PriorityQueue，其中用到的remove和priority大小时间成正比。
	// 优化的话需要一个额外的hashmap，存储应该移除的元素和个数，用来lazy remove和balance两个queue

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

	// 759. Employee Free Time
	// 和56 Merge Intervals类似，直接把所有的interval放到一个数组里面，不断merge，发现gap就为全部employee的free
	// time

	// 456. 132 Pattern: 能否找到这个pattern: i < j < k and nums[i] < nums[k] < nums[j]
	// 此题关键是把k找到，让k为次大值
	// 从后往前扫描数组，用一个递减单调栈，只要元素出栈，一定说明迫使该元素出栈的值更大，那么迫使该元素出栈的就是j，出栈的元素就是k
	// 之后扫描过程中只要遇到一个比k小的，就找到了
	public static boolean find132pattern(int[] nums) {
		Stack<Integer> stack = new Stack<>();
		int secondMax = Integer.MIN_VALUE;
		for (int i = nums.length - 1; i >= 0; i--) {
			if (nums[i] < secondMax)
				return true;

			while (!stack.isEmpty() && nums[i] > stack.peek()) {
				secondMax = Math.max(secondMax, stack.pop());
			}
			stack.add(nums[i]);
		}
		return false;
	}

	// 857. Minimum Cost to Hire K Workers
	// 假设工人的薪资能力比值 x[i]= wage[i]/quality[i],按照x[i]从小到大陪序之后，就可按照顺序枚举x[i]
	// x[i]最小时，说明所有的工人都能选择，这时可用一个maxheap找到quality最低的k个工人。

	// 1472. Design Browser History
	// 用两个stack分别存back和forward history即可

	// 402. Remove K Digits
	// 去掉k位，使得数字最小
	// greedy,用单调栈，从左边最高位开始扫描，只要当前数字比之前的小，之前的就出栈，保证是单调栈

	// 373. Find K Pairs with Smallest Sums
	// https://www.youtube.com/watch?v=K2WdEIyXpv0
	public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
		List<List<Integer>> ans = new ArrayList<>();
		int m = nums1.length;
		int n = nums2.length;
		if (m == 0 || n == 0 || k == 0)
			return ans;
		Set<Pair<Integer, Integer>> visited = new HashSet<>();
		PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(
				(p1, p2) -> nums1[p1.getKey()] + nums2[p1.getValue()] - nums1[p2.getKey()] - nums2[p2.getValue()]);
		// arrays are sorted, so nums1[0], nums[2] are smallest, pair 存index
		pq.add(new Pair<Integer, Integer>(0, 0));
		visited.add(new Pair<Integer, Integer>(0, 0));
		while (!pq.isEmpty() && ans.size() < k) {
			Pair<Integer, Integer> pair = pq.poll();
			int a = pair.getKey();
			int b = pair.getValue();
			ans.add(Arrays.asList(nums1[a], nums2[b]));
			Pair<Integer, Integer> newPair = new Pair<Integer, Integer>(a + 1, b);
			if (a < m - 1 && !visited.contains(newPair)) {
				pq.add(newPair);
				visited.add(newPair);
			}
			newPair = new Pair<Integer, Integer>(a, b + 1);
			if (b < n - 1 && !visited.contains(newPair)) {
				pq.add(newPair);
				visited.add(newPair);
			}
		}
		return ans;
	}

	// 678. Valid Parenthesis String
	public boolean checkValidString(String s) {
		Stack<Integer> open = new Stack<>();
		Stack<Integer> star = new Stack<>();
		char[] sArray = s.toCharArray();
		for (int i = 0; i < sArray.length; i++) {
			char c = sArray[i];
			if (c == '(') {
				open.push(i);
			} else if (c == '*') {
				star.push(i);
			} else if (c == ')') {
				if (!open.isEmpty()) {
					open.pop();
				} else if (!star.isEmpty()) {
					star.pop();
				} else {
					return false;
				}
			}
		}
		while (!open.isEmpty()) {
			if (star.isEmpty()) {
				return false;
			} else {
				if (open.peek() < star.peek()) {
					open.pop();
					star.pop();
				} else
					return false;
			}
		}
		return true;
	}
}
