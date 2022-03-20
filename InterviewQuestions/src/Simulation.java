import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
