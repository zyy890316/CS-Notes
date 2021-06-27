package stackAndQueue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Solutions {
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

	// Kth Largest Element in an Array
	// use minHeap, 只放比栈顶大的，方k个，这样栈顶肯定是kth largest
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
}
