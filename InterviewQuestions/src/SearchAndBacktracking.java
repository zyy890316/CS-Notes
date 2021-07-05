import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import tree.TreeNode;

public class SearchAndBacktracking {
	public static void main(String[] args) {
		shortestPathBinaryMatrix(new int[][] { { 0, 1 }, { 1, 0 } });
		int[][] heights = new int[][] { { 1, 2, 2, 3, 5 }, { 3, 2, 3, 4, 4 }, { 2, 4, 5, 3, 1 }, { 6, 7, 1, 4, 5 },
				{ 5, 1, 1, 2, 4 } };
		int[][] heights1 = new int[][] { { 1, 2, 3 }, { 8, 9, 4 }, { 7, 6, 5 } };
		pacificAtlantic(heights);
		restoreIpAddresses("1111");
	}

	// 1091 计算在网格中从原点到特定点的最短路径长度: 0 表示可以经过某个位置，求解从左上角到右下角的最短路径长度
	// 直接bfs即可
	public static int shortestPathBinaryMatrix(int[][] grid) {
		Queue<NodeInMatrix> queue = new LinkedList<NodeInMatrix>();
		int gridRow = grid.length;
		int gridCol = grid[0].length;
		boolean[][] visited = new boolean[gridRow][gridCol];
		int[][] direction = { { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };
		int row = 0;
		int col = 0;
		int level = 0;
		if (grid[row][col] == 1) {
			return -1;
		}
		queue.add(new NodeInMatrix(row, col));

		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				NodeInMatrix cNode = queue.poll();
				if (grid[cNode.row][cNode.col] == 0 && visited[cNode.row][cNode.col] == false) {
					visited[cNode.row][cNode.col] = true;
					if (cNode.row == gridRow - 1 && cNode.col == gridCol - 1) {
						return level + 1;
					}

					for (int[] d : direction) {
						int newRow = cNode.row + d[0];
						int newCol = cNode.col + d[1];
						if (newRow < 0 || newRow >= gridRow || newCol < 0 || newCol >= gridCol) {
							continue;
						}
						queue.add(new NodeInMatrix(newRow, newCol));
					}

				}
			}
			level++;
		}
		return -1;
	}

	public static class NodeInMatrix {
		int row, col;

		public NodeInMatrix(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

	// 279 组成整数的最小平方数数量
	// 可以将每个整数看成图中的一个节点，如果两个整数之差为一个平方数，那么这两个整数所在的节点就有一条边。
	// 要求解最小的平方数数量，就是求解从节点 n 到节点 0 的最短路径。
	public int numSquares(int n) {
		List<Integer> squares = generateSquares(n);
		Queue<Integer> queue = new LinkedList<>();
		boolean[] marked = new boolean[n + 1];
		queue.add(n);
		marked[n] = true;
		int level = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			level++;
			while (size-- > 0) {
				int cur = queue.poll();
				for (int s : squares) {
					int next = cur - s;
					if (next < 0) {
						break;
					}
					if (next == 0) {
						return level;
					}
					if (marked[next]) {
						continue;
					}
					marked[next] = true;
					queue.add(next);
				}
			}
		}
		return n;
	}

	/**
	 * 生成小于 n 的平方数序列
	 * 
	 * @return 1,4,9,...
	 */
	private List<Integer> generateSquares(int n) {
		List<Integer> squares = new ArrayList<>();
		int square = 1;
		int diff = 3;
		while (square <= n) {
			squares.add(square);
			square += diff;
			diff += 2;
		}
		return squares;
	}

	// 127. 最短单词路径
	// 找出一条从 beginWord 到 endWord 的最短路径，每次移动规定为改变一个字符，并且改变之后的字符串必须在 wordList 中。
	// 提前查好从每个单词能变到哪个单词，存在graphic里面，然后bfs
	public int ladderLength(String beginWord, String endWord, List<String> wordList) {
		Queue<String> queue = new LinkedList<>();
		Set<String> visited = new HashSet<>();
		Map<String, Set<String>> graphic = buildGraphic(beginWord, wordList);
		queue.add(beginWord);
		int level = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				String s = queue.poll();
				visited.add(s);
				if (endWord.equals(s)) {
					return level + 1;
				}
				for (String w : graphic.get(s)) {
					if (isConnect(s, w) && !visited.contains(w)) {
						queue.add(w);
					}
				}
			}
			level++;
		}
		return 0;
	}

	private Map<String, Set<String>> buildGraphic(String beginWord, List<String> wordList) {
		int N = wordList.size();
		Map<String, Set<String>> graphic = new HashMap<String, Set<String>>();
		for (int i = 0; i < N; i++) {
			Set<String> set = new HashSet<>();
			for (int j = 0; j < N; j++) {
				if (isConnect(wordList.get(i), wordList.get(j))) {
					set.add(wordList.get(j));
				}
			}
			graphic.put(wordList.get(i), set);
		}
		Set<String> set = new HashSet<>();
		for (int j = 0; j < N; j++) {
			if (isConnect(beginWord, wordList.get(j))) {
				set.add(wordList.get(j));
			}
		}
		graphic.put(beginWord, set);
		return graphic;
	}

	private boolean isConnect(String s1, String s2) {
		int diffCnt = 0;
		for (int i = 0; i < s1.length() && diffCnt <= 1; i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				diffCnt++;
			}
		}
		return diffCnt == 1;
	}

	// 695 查找最大的连通面积
	// 用dfs查面积，查过的区块直接填成0就不会重复访问了
	private static int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

	public int maxAreaOfIsland(int[][] grid) {
		int m = grid.length;
		int n = grid[0].length;
		int maxArea = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == '1') {
					maxArea = Math.max(maxArea, dfs(grid, i, j));
				}
			}
		}
		return maxArea;
	}

	private int dfs(int[][] grid, int r, int c) {
		int m = grid.length;
		int n = grid[0].length;
		if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0) {
			return 0;
		}
		grid[r][c] = 0;
		int area = 1;
		for (int[] d : directions) {
			area += dfs(grid, r + d[0], c + d[1]);
		}
		return area;
	}

	// 200 矩阵中的连通分量数目
	// 同上，找过的区块直接填充0
	public int numIslands(char[][] grid) {
		int m = grid.length;
		int n = grid[0].length;
		int count = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 1) {
					dfs(grid, i, j);
					count++;
				}
			}
		}
		return count;
	}

	private void dfs(char[][] grid, int r, int c) {
		int m = grid.length;
		int n = grid[0].length;
		if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == '0') {
			return;
		}
		grid[r][c] = '0';
		for (int[] d : directions) {
			dfs(grid, r + d[0], c + d[1]);
		}
		return;
	}

	// 547 好友关系的连通分量数目
	public int findCircleNum(int[][] isConnected) {
		int n = isConnected.length;
		int count = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (isConnected[i][j] == 1) {
					dfsFindCircleNum(isConnected, i, j);
					count++;
				}
			}
		}
		return count;
	}

	private void dfsFindCircleNum(int[][] grid, int r, int c) {
		int m = grid.length;
		int n = grid[0].length;
		if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0) {
			return;
		}
		grid[r][c] = 0;
		for (int i = 0; i < n; i++) {
			dfsFindCircleNum(grid, r, i);
			dfsFindCircleNum(grid, i, c);
		}
		return;
	}

	// 130 填充封闭区域:使被 'X' 包围的 'O' 转换为 'X'。
	public void solve(char[][] board) {
		if (board == null || board.length == 0) {
			return;
		}

		int m = board.length;
		int n = board[0].length;

		// 先沿着四条边做dfs，联通区块全部标为 Exit
		for (int i = 0; i < m; i++) {
			dfsFillInX(board, i, 0, 'E');
			dfsFillInX(board, i, n - 1, 'E');
		}
		for (int i = 0; i < n; i++) {
			dfsFillInX(board, 0, i, 'E');
			dfsFillInX(board, m - 1, i, 'E');
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 'E') {
					board[i][j] = 'O';
				} else if (board[i][j] == 'O') {
					board[i][j] = 'X';
				}
			}
		}
	}

	private void dfsFillInX(char[][] grid, int r, int c, char ch) {
		int m = grid.length;
		int n = grid[0].length;
		if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] != 'X') {
			return;
		}
		grid[r][c] = ch;
		for (int[] d : directions) {
			dfsFillInX(grid, r + d[0], c + d[1], ch);
		}
		return;
	}

	// 417 能到达的太平洋和大西洋的区域:左边和上边是太平洋，右边和下边是大西洋，内部的数字代表海拔，
	// 海拔高的地方的水能够流到低的地方，求解水能够流到太平洋和大西洋的所有位置。
	public static List<List<Integer>> pacificAtlantic(int[][] heights) {
		List<List<Integer>> ans = new ArrayList<>();
		if (heights == null || heights.length == 0) {
			return ans;
		}

		int m = heights.length;
		int n = heights[0].length;
		boolean[][] canReachP = new boolean[m][n];
		boolean[][] canReachA = new boolean[m][n];

		for (int i = 0; i < m; i++) {
			dfs(heights, i, 0, canReachP);
			dfs(heights, i, n - 1, canReachA);
		}
		for (int i = 0; i < n; i++) {
			dfs(heights, 0, i, canReachP);
			dfs(heights, m - 1, i, canReachA);
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (canReachP[i][j] && canReachA[i][j]) {
					ans.add(Arrays.asList(i, j));
				}
			}
		}
		return ans;
	}

	private static void dfs(int[][] heights, int r, int c, boolean[][] canReach) {
		if (canReach[r][c]) {
			return;
		}
		canReach[r][c] = true;
		int m = heights.length;
		int n = heights[0].length;
		for (int[] d : directions) {
			int nextR = d[0] + r;
			int nextC = d[1] + c;
			if (nextR < 0 || nextR >= m || nextC < 0 || nextC >= n || heights[r][c] > heights[nextR][nextC]) {
				continue;
			}
			dfs(heights, nextR, nextC, canReach);
		}
	}

	// 17 数字键盘组合
	// divide and conquer
	private static final String[] KEYS = { "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };

	public List<String> letterCombinations(String digits) {
		List<String> ans = new ArrayList<>();
		if (digits == null || digits.length() == 0) {
			return ans;
		}
		if (digits.length() == 1) {
			String key = KEYS[Integer.parseInt(digits)];
			for (int i = 0; i < key.length(); i++) {
				String s = String.valueOf(key.charAt(i));
				ans.add(s);
			}
			return ans;
		} else {
			String key = KEYS[Integer.parseInt(Character.toString(digits.charAt(0)))];
			List<String> subAns = letterCombinations(digits.substring(1, digits.length()));
			for (int i = 0; i < key.length(); i++) {
				String s = String.valueOf(key.charAt(i));
				for (int j = 0; j < subAns.size(); j++) {
					ans.add(s + subAns.get(j));
				}
			}
		}
		return ans;
	}

	// 93: IP 地址划分
	// divide and conquer
	public static List<String> restoreIpAddresses(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0) {
			return ans;
		}
		ans = restoreIpAddresses(s, 4);
		return ans;
	}

	public static List<String> restoreIpAddresses(String s, int k) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0 || s.length() < k) {
			return ans;
		}

		if (k == 1) {
			if (s.startsWith("0") && s.length() > 1) {
				return ans;
			}
			if (s.length() > 3) {
				return ans;
			}
			int result = Integer.parseInt(s);
			if (result <= 255 && result >= 0) {
				ans.add(s);
				return ans;
			}
		}

		for (int i = 0; i < Math.min(3, s.length()); i++) {
			String curr = s.substring(0, i + 1);
			if (curr.startsWith("0") && curr.length() > 1) {
				continue;
			}
			int num = Integer.parseInt(curr);
			String next = s.substring(i + 1, s.length());
			List<String> tempAns = new ArrayList<>();
			if (num <= 255 && num >= 0 && next.length() >= k - 1 && k - 1 >= 0) {
				tempAns = restoreIpAddresses(next, k - 1);
			}
			for (int j = 0; j < tempAns.size(); j++) {
				ans.add(s.substring(0, i + 1) + "." + tempAns.get(j));
			}
		}
		return ans;
	}

	// 79 在矩阵中寻找字符串
	private final static int[][] direction = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
	private int m;
	private int n;

	public boolean exist(char[][] board, String word) {
		if (word == null || word.length() == 0) {
			return true;
		}
		if (board == null || board.length == 0 || board[0].length == 0) {
			return false;
		}

		m = board.length;
		n = board[0].length;
		boolean[][] hasVisited = new boolean[m][n];

		for (int r = 0; r < m; r++) {
			for (int c = 0; c < n; c++) {
				if (backtracking(0, r, c, hasVisited, board, word)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean backtracking(int curLen, int r, int c, boolean[][] visited, final char[][] board,
			final String word) {
		if (curLen == word.length()) {
			return true;
		}
		if (r < 0 || r >= m || c < 0 || c >= n || board[r][c] != word.charAt(curLen) || visited[r][c]) {

			return false;
		}

		visited[r][c] = true;

		for (int[] d : direction) {
			if (backtracking(curLen + 1, r + d[0], c + d[1], visited, board, word)) {
				return true;
			}
		}

		visited[r][c] = false;

		return false;
	}

	// 257 输出二叉树中所有从根到叶子的路径
	// DFS, 用堆栈记录路径
	public List<String> binaryTreePaths(TreeNode root) {
		List<String> ans = new ArrayList<String>();
		Stack<Integer> stack = new Stack<Integer>();
		if (root == null) {
			return ans;
		}

		dfsBackTracking(root, stack, ans);
		return ans;
	}

	public void dfsBackTracking(TreeNode root, Stack<Integer> stack, List<String> ans) {
		if (root == null) {
			return;
		}
		if (root.left == root.right) {
			stack.push(root.val);
			ans.add(buildPath(stack));
			stack.pop();
			return;
		} else {
			stack.push(root.val);
			dfsBackTracking(root.left, stack, ans);
			dfsBackTracking(root.right, stack, ans);
			stack.pop();
			return;
		}
	}

	private String buildPath(Stack<Integer> stack) {
		List<Integer> values = new ArrayList<Integer>(stack);
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < values.size(); i++) {
			str.append(values.get(i));
			if (i != values.size() - 1) {
				str.append("->");
			}
		}
		return str.toString();
	}

	// 46 排列
	public List<List<Integer>> permute(int[] nums) {
		Set<Integer> avaliable = new HashSet<Integer>();
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		for (int i = 0; i < nums.length; i++) {
			avaliable.add(nums[i]);
		}

		permute(avaliable, path, ans);
		return ans;
	}

	private void permute(Set<Integer> avaliable, Stack<Integer> path, List<List<Integer>> ans) {
		if (avaliable.size() == 1) {
			Object[] myArr = avaliable.toArray();
			int num = Integer.parseInt(myArr[0].toString());
			path.add(num);
			ans.add(new ArrayList<Integer>(path));
			path.pop();
			return;
		} else {
			List<Integer> currAvaliable = new ArrayList<>(avaliable);
			Iterator<Integer> i = currAvaliable.iterator();
			while (i.hasNext()) {
				int num = i.next();
				avaliable.remove(num);
				path.push(num);
				permute(avaliable, path, ans);
				path.pop();
				avaliable.add(num);
			}
			return;
		}
	}
}
