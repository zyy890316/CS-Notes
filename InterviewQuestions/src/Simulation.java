import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

public class Simulation {
	public static void main(String[] args) {
		gameOfLife(new int[][] { { 0, 1, 0 }, { 0, 0, 1 }, { 1, 1, 1 }, { 0, 0, 0 } });
		calculate("(1+(4+5+2)-3)+(6+8)");
	}

	// 289. Game of Life
	// https://www.youtube.com/watch?v=juGxbF-eadU
	// 最后一位表示当前状态，倒数第二位来表示下一次状态
	public static void gameOfLife(int[][] board) {
		int m = board.length;
		int n = board[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				int liveNeighbour = 0;
				for (int a = Math.max(0, i - 1); a <= Math.min(m - 1, i + 1); a++) {
					for (int b = Math.max(0, j - 1); b <= Math.min(n - 1, j + 1); b++) {
						if (i != a || j != b)
							liveNeighbour += board[a][b] & 0b01;
					}
				}
				if (liveNeighbour < 2 || liveNeighbour >= 4) {
					continue;
				}
				if (((board[i][j] & 0b01) > 0 && liveNeighbour == 2) || (liveNeighbour == 3)) {
					board[i][j] |= 0b10;
				}
			}
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] >>>= 1;
			}
		}
	}

	// 224. Basic Calculator
	public static int calculate(String s) {
		Stack<CalculatorNode> stack = new Stack<>();
		int num = 0;
		int sign = 1;
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (Character.isDigit(ch)) {
				num = num * 10 + (int) (ch - '0');
			} else if (ch != ' ') {
				if (num != 0) {
					// Save the operand on the stack
					// As we encounter some non-digit.
					stack.push(new CalculatorNode(num, false, sign));
					num = 0;
					sign = 1;
				}
				if (ch == '(') {
					// parenthesis node needs to have a sign
					stack.push(new CalculatorNode(0, true, sign));
					sign = 1;
				} else if (ch == ')') {
					int tempSum = 0;
					while (!stack.isEmpty()) {
						CalculatorNode node = stack.pop();
						if (node.isParaenthesis) {
							stack.push(new CalculatorNode(tempSum, false, node.sign));
							break;
						} else {
							tempSum += node.val * node.sign;
						}
					}
				} else if (ch == '+') {
					sign = 1;
				} else {
					sign = -1;
				}
			}
		}
		if (num != 0) {
			stack.push(new CalculatorNode(num, false, sign));
		}
		int sum = 0;
		while (!stack.isEmpty()) {
			CalculatorNode node = stack.pop();
			sum += node.val * node.sign;
		}
		return sum;
	}

	static class CalculatorNode {
		int val;
		boolean isParaenthesis;
		int sign;

		public CalculatorNode(int val, boolean isParaenthesis, int sign) {
			this.val = val;
			this.isParaenthesis = isParaenthesis;
			this.sign = sign;
		}
	}

	// Leetcode 227 & 772 Basic Calculator II & III
	// https://www.youtube.com/watch?v=7rmxDqXf5vQ
	// 需要保持前一个符号和数字，来处理乘除法优先的情况，推迟加减法
	// 227. Basic Calculator II
	public int calculate2(String s) {
		Stack<Integer> stack = new Stack<>();
		int number = 0;
		char sign = '+';

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c)) {
				number = number * 10 + (c - '0');
			}

			// 遇到新运算符，或者是到了string的结尾，都可以开始运算前一个运算符的结果了
			if (c == '+' || c == '-' || c == '*' || c == '/' || i == s.length() - 1) {
				// sign是前一个运算符，遇到新运算符之后，马上可以操作前一个了
				if (sign == '+') {
					stack.push(number);
				}
				if (sign == '-') {
					stack.push(-number);
				}
				if (sign == '*') {
					stack.push(stack.pop() * number);
				}
				if (sign == '/') {
					stack.push(stack.pop() / number);
				}
				sign = c;
				number = 0;
			}
		}

		int sum = 0;
		while (!stack.isEmpty()) {
			sum += stack.pop();
		}
		return sum;
	}

	// Next Closest Time
	// 给定一个时间 "HH:MM"，用当前数字求下一个最近时间是几点几分
	// 直接枚举从当前时间开始的每个时间，看是否可以用现在的数字组成，可以即返回

	// 588. Design In-Memory File System
	class FileSystem {
		Dir root;

		public FileSystem() {
			root = new Dir();
		}

		public List<String> ls(String path) {
			Dir t = root;
			List<String> files = new ArrayList<>();
			if (!path.equals("/")) {
				String[] paths = path.split("/");
				for (int i = 1; i < paths.length - 1; i++) {
					t = t.dirs.get(paths[i]);
				}
				// ls is acting on a file rather than a directory
				if (t.files.containsKey(paths[paths.length - 1])) {
					files.add(paths[paths.length - 1]);
					return files;
				} else {
					t = t.dirs.get(paths[paths.length - 1]);
				}
			}
			files.addAll(new ArrayList<>(t.dirs.keySet()));
			files.addAll(new ArrayList<>(t.files.keySet()));
			Collections.sort(files);
			return files;
		}

		public void mkdir(String path) {
			Dir t = root;
			String[] paths = path.split("/");
			for (int i = 1; i < paths.length; i++) {
				if (!t.dirs.containsKey(paths[i]))
					t.dirs.put(paths[i], new Dir());
				t = t.dirs.get(paths[i]);
			}
		}

		public void addContentToFile(String path, String content) {
			Dir t = root;
			String[] paths = path.split("/");
			for (int i = 1; i < paths.length - 1; i++) {
				t = t.dirs.get(paths[i]);
			}
			t.files.put(paths[paths.length - 1], t.files.getOrDefault(paths[paths.length - 1], "") + content);
		}

		public String readContentFromFile(String path) {
			Dir t = root;
			String[] paths = path.split("/");
			for (int i = 1; i < paths.length - 1; i++) {
				t = t.dirs.get(paths[i]);
			}
			return t.files.get(paths[paths.length - 1]);
		}

		class Dir {
			Map<String, Dir> dirs;
			Map<String, String> files;

			public Dir() {
				this.dirs = new HashMap<>();
				this.files = new HashMap<>();
			}
		}
	}

	// 1146. Snapshot Array
	// 用 List<TreeMap<Integer, Integer>>, TreeMap存 version ： value
	class SnapshotArray {
		List<TreeMap<Integer, Integer>> values;
		int snapId;

		public SnapshotArray(int length) {
			this.snapId = 0;
			this.values = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				TreeMap<Integer, Integer> map = new TreeMap<>();
				map.put(snapId, 0);
				values.add(i, map);
			}
		}

		public void set(int index, int val) {
			values.get(index).put(snapId, val);
		}

		public int snap() {
			return snapId++;
		}

		public int get(int index, int snap_id) {
			return values.get(index).floorEntry(snap_id).getValue();
		}
	}

	// 1834. Single-Threaded CPU
	public int[] getOrder(int[][] tasks) {
		int n = tasks.length;

		// Ordered by enqueueTime.
		Queue<Integer> taskQueue = new PriorityQueue<>((a, b) -> tasks[a][0] - tasks[b][0]);
		for (int i = 0; i < n; i++) {
			taskQueue.add(i);
		}

		// Ordered by processingTime, with collisions resolved by taskId.
		Queue<Integer> executionQueue = new PriorityQueue<>((a, b) -> {
			int cmp = tasks[a][1] - tasks[b][1];
			return cmp != 0 ? cmp : a - b;
		});

		int time = 0;
		int[] order = new int[n];
		for (int i = 0; i < n; i++) {
			// Skip idle time.
			if (executionQueue.isEmpty() && !taskQueue.isEmpty() && time < tasks[taskQueue.peek()][0]) {
				time = tasks[taskQueue.peek()][0];
			}

			// Enqueue by enqueueTime.
			while (!taskQueue.isEmpty() && time >= tasks[taskQueue.peek()][0]) {
				executionQueue.add(taskQueue.remove());
			}

			// Pick a task to execute.
			int taskId = executionQueue.remove();
			time += tasks[taskId][1];
			order[i] = taskId;
		}
		return order;
	}

	// 352. Data Stream as Disjoint Intervals
	// 4 cases in total:
	// 1. The new num is not close to any exising key. Thus, just add a new interval
	// like [val, val];
	// 2. The new num (e.g., b) can connect two exisitng intervals, like this case
	// [a, b-1], b, [b+1, c]
	// 3. The new num b can expand the largest smaller interval, like this case [a,
	// b-1], b, or [a, b], b
	// 4. The new b can expand the smallest larger interval, like b, [b+1, c]
	class SummaryRanges {
		// start : [start, end]
		TreeMap<Integer, int[]> tree;

		public SummaryRanges() {
			tree = new TreeMap<>();
		}

		public void addNum(int val) {
			if (tree.containsKey(val))
				return;
			Integer lowerKey = tree.lowerKey(val); // The greatest key that strictly less than val
			Integer higherKey = tree.higherKey(val); // The smallest key that strictly larger than val

			if (lowerKey != null && higherKey != null && val == tree.get(lowerKey)[1] + 1
					&& val == tree.get(higherKey)[0] - 1) { // Case 2
				tree.get(lowerKey)[1] = tree.get(higherKey)[1];
				tree.remove(higherKey);
			} else if (lowerKey != null && val <= tree.get(lowerKey)[1] + 1) { // Case 3
				tree.get(lowerKey)[1] = Math.max(val, tree.get(lowerKey)[1]);
			} else if (higherKey != null && val == tree.get(higherKey)[0] - 1) { // Case 4
				tree.put(val, new int[] { val, tree.get(higherKey)[1] });
				tree.remove(higherKey);
			} else { // Case 1
				tree.put(val, new int[] { val, val });
			}
		}

		public int[][] getIntervals() {
			return tree.values().toArray(new int[tree.size()][2]);
		}
	}

	// 636. Exclusive Time of Functions 程序执行时间，直接用堆栈即可
	// https://leetcode.com/problems/exclusive-time-of-functions/
	public int[] exclusiveTime(int n, List<String> logs) {
		int[] result = new int[n];
		if (n == 0 || logs == null || logs.size() == 0) {
			return result;
		}

		// This stack will store the function ids
		Stack<Integer> stack = new Stack<>();
		// Previous time = start/resume time of the previous function
		int prevTime = 0;

		for (String log : logs) {
			String[] logParts = log.split(":");
			int curTime = Integer.parseInt(logParts[2]);

			if ("start".equals(logParts[1])) {
				// Function is starting now
				if (!stack.isEmpty()) {
					// Add the exclusive time of previous function
					result[stack.peek()] += curTime - prevTime;
				}
				stack.push(Integer.parseInt(logParts[0]));
				// Setting the start time for next log.
				prevTime = curTime;
			} else {
				// Function is ending now.
				// Make sure to +1 to as end takes the whole unit of time.
				result[stack.pop()] += curTime - prevTime + 1;
				// prevTime = resume time of the function. Thus adding 1.
				prevTime = curTime + 1;
			}
		}
		return result;
	}
}
