import java.util.Stack;

public class Simulation {
	public static void main(String[] args) {
		gameOfLife(new int[][] { { 0, 1, 0 }, { 0, 0, 1 }, { 1, 1, 1 }, { 0, 0, 0 } });
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

	// Leetcode 227 & 772 Basic Calculator II & III
	// https://www.youtube.com/watch?v=7rmxDqXf5vQ
	// 需要保持前一个符号和数字，来处理乘除法优先的情况，推迟加减法
	// 227. Basic Calculator II
	public int calculate(String s) {
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
}
