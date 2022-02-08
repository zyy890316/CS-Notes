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
		pacificAtlantic(heights);
		restoreIpAddresses("1111");
		subsetsWithDup(new int[] { 1, 2, 2 });
		char[][] board = new char[][] { { '5', '3', '.', '.', '7', '.', '.', '.', '.' },
				{ '6', '.', '.', '1', '9', '5', '.', '.', '.' }, { '.', '9', '8', '.', '.', '.', '.', '6', '.' },
				{ '8', '.', '.', '.', '6', '.', '.', '.', '3' }, { '4', '.', '.', '8', '.', '3', '.', '.', '1' },
				{ '7', '.', '.', '.', '2', '.', '.', '.', '6' }, { '.', '6', '.', '.', '.', '.', '2', '8', '.' },
				{ '.', '.', '.', '4', '1', '9', '.', '.', '5' }, { '.', '.', '.', '.', '8', '.', '.', '7', '9' } };
		char[][] board1 = new char[][] { { '.', '.', '9', '7', '4', '8', '.', '.', '.' },
				{ '7', '.', '.', '.', '.', '.', '.', '.', '.' }, { '.', '2', '.', '1', '.', '9', '.', '.', '.' },
				{ '.', '.', '7', '.', '.', '.', '2', '4', '.' }, { '.', '6', '4', '.', '1', '.', '5', '9', '.' },
				{ '.', '9', '8', '.', '.', '.', '3', '.', '.' }, { '.', '.', '.', '8', '.', '3', '.', '2', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '6' }, { '.', '.', '.', '2', '7', '5', '9', '.', '.' } };
		solveSudoku(board);
		solveSudoku(board1);
		permuteUnique(new int[] { 1, 1, 2 });
		generateParenthesis(3);
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
		int level = 0;
		Queue<Integer> queue = new LinkedList<>();
		queue.add(n);
		while (!queue.isEmpty()) {
			level++;
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				int curr = queue.poll();
				for (int j = 1; j * j <= curr; j++) {
					int remain = curr - j * j;
					if (remain == 0) {
						return level;
					}
					queue.add(remain);
				}
			}
		}
		return level;
	}

	/**
	 * 生成小于 n 的平方数序列
	 * 
	 * @return 1,4,9,...
	 */
	public List<Integer> generateSquares(int n) {
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

	// 22. Generate Parentheses
	private static final char[] parentheses = new char[] { '(', ')' };

	public static List<String> generateParenthesis(int n) {
		List<String> ans = new ArrayList<>();
		Stack<Character> path = new Stack<>();
		generateParenthesisDFS(n * 2, ans, path, 0);
		return ans;
	}

	public static void generateParenthesisDFS(int n, List<String> ans, Stack<Character> path, int sum) {
		if (path.size() == n && sum == 0) {
			List<Character> result = new ArrayList<>(path);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < result.size(); i++) {
				sb.append(result.get(i));
			}
			ans.add(sb.toString());
		}

		int remain = n - path.size();
		// already invalid if right parenthesis present too much
		if (sum < 0)
			return;
		// too many left or right parenthesis
		if (sum > remain)
			return;

		for (char p : parentheses) {
			path.add(p);
			generateParenthesisDFS(n, ans, path, p == '(' ? sum + 1 : sum - 1);
			path.pop();
		}

		return;
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

	// 47 含有相同元素求排列: 数组元素可能含有相同的元素，进行排列时就有可能出现重复的排列，要求重复的排列只返回一个
	// 在实现上，和 Permutations 不同的是要先排序，然后在添加一个元素时，判断这个元素是否等于前一个元素，
	// 如果等于，并且前一个元素还未访问，那么就跳过这个元素，这样就能保证相同元素的相对顺序不变。
	public static List<List<Integer>> permuteUnique(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		Arrays.sort(nums);
		boolean[] hasVisited = new boolean[nums.length];
		permuteUnique(path, ans, hasVisited, nums);
		return ans;
	}

	private static void permuteUnique(Stack<Integer> path, List<List<Integer>> ans, boolean[] visited,
			final int[] nums) {
		if (path.size() == nums.length) {
			ans.add(new ArrayList<>(path)); // 重新构造一个 List
			return;
		}
		for (int i = 0; i < visited.length; i++) {
			if (visited[i]) {
				continue;
			}

			// 和上题唯一区别就是这个判断
			// 当前元素等于前一个元素，前一个没访问过，说明当前轮次，前一个已经被使用了，后续的相同元素都要跳过
			if (i - 1 >= 0 && nums[i - 1] == nums[i] && !visited[i - 1]) {
				continue; // 防止重复
			}

			visited[i] = true;
			path.push(nums[i]);
			permuteUnique(path, ans, visited, nums);
			path.pop();
			visited[i] = false;
		}
	}

	// Next Permutation
	public static void nextPermutation(int[] nums) {
		if (nums.length == 1)
			return;
		// 从右开始找何时终止升序，一直升序说明已经是最大的排列了
		// 找到升序终止点，说明i-1处需要替换为一个较小值：
		// 比如124653， 653从右开始升序，说明之前的4需要替换才能拿到更大值
		int index = nums.length - 1;
		while (index > 0) {
			if (nums[index] <= nums[index - 1]) {
				index--;
				continue;
			} else {
				index--;
				break;
			}
		}
		// 没找到升序终止，说明已经到了最大的组合，下一个只能是最小的，反转整个array
		if (index == 0 && nums[0] > nums[1]) {
			reverse(nums, 0, nums.length - 1);
			return;
		}
		// 找到升序终止，开始找可替换的最小值
		// 比如124653, index指向4,4应替换为5
		for (int i = nums.length - 1; i > index; i--) {
			if (nums[i] > nums[index]) {
				int temp = nums[index];
				nums[index] = nums[i];
				nums[i] = temp;
				break;
			}
		}
		// 现在数组为125643，需要把643反转，这是一个降序，应反转为346
		reverse(nums, index + 1, nums.length - 1);
	}

	public static void reverse(int[] data, int left, int right) {
		while (left < right) {
			// swap the values at the left and right indices
			int temp = data[left];
			data[left] = data[right];
			data[right] = temp;

			// move the left and right index pointers in toward the center
			left++;
			right--;
		}
	}

	// 77 组合
	public List<List<Integer>> combine(int n, int k) {
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();

		combine(n, k, ans, path, 1);
		return ans;
	}

	public void combine(int n, int k, List<List<Integer>> ans, Stack<Integer> path, int start) {
		if (path.size() == k) {
			ans.add(new ArrayList<>(path));
			return;
		}
		for (int i = start; i < n + 1; i++) {
			path.push(i);
			combine(n, k, ans, path, i + 1); // 剪枝
			path.pop();
		}
	}

	// 39 组合求和
	public List<List<Integer>> combinationSum(int[] candidates, int target) {
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		combinationSum(candidates, target, ans, path, 0);
		return ans;
	}

	public void combinationSum(int[] candidates, int target, List<List<Integer>> ans, Stack<Integer> path, int start) {
		if (target == 0) {
			ans.add(new ArrayList<>(path));
			return;
		}
		for (int i = start; i < candidates.length; i++) {
			int num = candidates[i];
			if (target - num >= 0) {
				path.add(num);
				// 以i=0为例，i=0时意味着第一个数字可以背多次重复使用，这个循环结束后，第一个数字出现的所有可能就都遍历过了
				combinationSum(candidates, target - num, ans, path, i); // 剪枝
				path.pop();
			}
		}
	}

	// 40 含有相同元素的组合求和：和上题区别在于，给出的解不能多次用candidates里面某个元素了
	// 因为有重复元素，所以当前深度相同元素只能取值一次
	public List<List<Integer>> combinationSum2(int[] candidates, int target) {
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		Arrays.sort(candidates);
		combinationSum2(candidates, target, ans, path, 0);
		return ans;
	}

	public void combinationSum2(int[] candidates, int target, List<List<Integer>> ans, Stack<Integer> path, int start) {
		if (target == 0) {
			ans.add(new ArrayList<>(path));
			return;
		}
		for (int i = start; i < candidates.length; i++) {
			int num = candidates[i];
			// 当前深度相同元素只能取值一次
			// 不过下一层循环还是可以再取值一次的
			if (i - 1 >= start && num == candidates[i - 1]) {
				continue;
			}
			if (target - num >= 0) {
				path.add(num);
				combinationSum2(candidates, target - num, ans, path, i + 1); // 剪枝
				path.pop();
			}
		}
	}

	// 216 1-9 数字的组合求和：只能用1-9，每数字用一次，求用k个数得出和n的所有组合
	// 和上题类似，至是多加了一个最多k个数的限制
	public List<List<Integer>> combinationSum3(int k, int n) {
		int[] candidates = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		combinationSum3(candidates, n, ans, path, 0, k);
		return ans;
	}

	public void combinationSum3(int[] candidates, int target, List<List<Integer>> ans, Stack<Integer> path, int start,
			int deepth) {
		if (target == 0 && deepth == path.size()) {
			ans.add(new ArrayList<>(path));
			return;
		}
		for (int i = start; i < candidates.length; i++) {
			int num = candidates[i];
			if (target - num >= 0) {
				path.add(num);
				combinationSum3(candidates, target - num, ans, path, i + 1, deepth); // 剪枝
				path.pop();
			}
		}
	}

	// 78 找出集合的所有子集，子集不能重复，[1, 2] 和 [2, 1] 这种子集算重复
	public List<List<Integer>> subsets(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		for (int i = 0; i <= nums.length; i++) {
			subsets(nums, ans, path, 0, i);
		}
		return ans;
	}

	public void subsets(int[] nums, List<List<Integer>> ans, Stack<Integer> path, int start, int deepth) {
		if (deepth == path.size()) {
			ans.add(new ArrayList<>(path));
			return;
		}
		for (int i = start; i < nums.length; i++) {
			int num = nums[i];
			path.add(num);
			subsets(nums, ans, path, i + 1, deepth); // 剪枝
			path.pop();
		}
	}

	// 90 含有相同元素求子集
	// 同 77 题类似，不过给出的元素可能有duplicate
	public static List<List<Integer>> subsetsWithDup(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		Arrays.sort(nums);
		for (int i = 3; i <= nums.length; i++) {
			subsetsWithDup(nums, ans, path, 0, i);
		}
		return ans;
	}

	public static void subsetsWithDup(int[] nums, List<List<Integer>> ans, Stack<Integer> path, int start, int deepth) {
		if (deepth == path.size()) {
			ans.add(new ArrayList<>(path));
			return;
		}
		for (int i = start; i < nums.length; i++) {
			int num = nums[i];
			if (i - 1 >= start && num == nums[i - 1]) {
				continue;
			}
			path.add(num);
			subsetsWithDup(nums, ans, path, i + 1, deepth); // 剪枝
			path.pop();
		}
	}

	// 131 分割字符串使得每个部分都是回文字符串
	public List<List<String>> partition(String s) {
		List<List<String>> ans = new ArrayList<>();
		Stack<String> path = new Stack<>();
		partition(s, ans, path);
		return ans;
	}

	public List<List<String>> partition(String s, List<List<String>> ans, Stack<String> path) {
		if (s.length() == 0) {
			ans.add(new ArrayList<>(path));
			return ans;
		}
		for (int i = 0; i < s.length(); i++) {
			if (isPalindrome(s, 0, i)) {
				String current = s.substring(0, i + 1);
				path.add(current);
				partition(s.substring(i + 1, s.length()), ans, path);
				path.pop();
			}
		}
		return ans;
	}

	private boolean isPalindrome(String s, int begin, int end) {
		while (begin < end) {
			if (s.charAt(begin++) != s.charAt(end--)) {
				return false;
			}
		}
		return true;
	}

	// 37 数独
	private static boolean[][] rowsUsed = new boolean[9][10];
	private static boolean[][] colsUsed = new boolean[9][10];
	private static boolean[][] cubesUsed = new boolean[9][10];

	public static void solveSudoku(char[][] board) {
		int n = 9;
		Stack<Coordinate> path = new Stack<>();
		List<List<Coordinate>> ans = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				if (board[i][j] == '.') {
					continue;
				}
				int num = board[i][j] - '0';
				rowsUsed[i][num] = true;
				colsUsed[j][num] = true;
				cubesUsed[cubeNum(i, j)][num] = true;
			}
		}

		int[] next = getNextEmptyChar(board);
		if (next[0] >= 0) {
			getSudoku(board, next[0], next[1], path, ans);
		}
		if (ans.size() > 0) {
			List<Coordinate> result = ans.get(0);
			for (Coordinate c : result) {
				board[c.row][c.col] = c.ch;
			}
		}
		return;
	}

	public static void getSudoku(char[][] board, int row, int col, Stack<Coordinate> path, List<List<Coordinate>> ans) {
		int[] next;
		next = getNextEmptyChar(board);
		if (next[0] < 0) {
			ans.add(new ArrayList<Coordinate>(path));
			return;
		}

		for (int i = 1; i <= 9; i++) {
			if (!rowsUsed[row][i] && !colsUsed[col][i] && !cubesUsed[cubeNum(row, col)][i]) {
				char ch = Character.forDigit(i, 10);
				rowsUsed[row][i] = true;
				colsUsed[col][i] = true;
				cubesUsed[cubeNum(row, col)][i] = true;
				board[row][col] = ch;
				path.push(new Coordinate(row, col, ch));

				next = getNextEmptyChar(board);
				getSudoku(board, next[0], next[1], path, ans);

				path.pop();
				rowsUsed[row][i] = false;
				colsUsed[col][i] = false;
				cubesUsed[cubeNum(row, col)][i] = false;
				board[row][col] = '.';
			}
		}
	}

	private static int[] getNextEmptyChar(char[][] board) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == '.') {
					return new int[] { i, j };
				}
			}
		}
		return new int[] { -1, -1 };
	}

	private static int cubeNum(int i, int j) {
		int r = i / 3;
		int c = j / 3;
		return r * 3 + c;
	}

	public static class Coordinate {
		int row;
		int col;
		char ch;

		public Coordinate(int row, int col, char ch) {
			this.row = row;
			this.col = col;
			this.ch = ch;
		}
	}

	// 51. N-Queens (Hard) 在 n*n 的矩阵中摆放 n 个皇后，并且每个皇后不能在同一行，同一列，同一对角线上，求所有的 n 皇后的解。
	// 一行一行地摆放，在确定一行中的那个皇后应该摆在哪一列时，需要用三个标记数组来确定某一列是否合法，
	// 这三个标记数组分别为：列标记数组、45 度对角线标记数组和 135 度对角线标记数组。
	// 45 度对角线标记数组的长度为 2 * n - 1, (r, c) 的位置所在的数组下标为 r + c
	// 135 度对角线标记数组的长度也是 2 * n - 1，(r, c) 的位置所在的数组下标为 n - 1 - (r - c)
	private List<List<String>> ans;
	private char[][] nQueens;
	private boolean[] colUsed;
	private boolean[] diagonals45Used;
	private boolean[] diagonals135Used;

	public List<List<String>> solveNQueens(int n) {
		ans = new ArrayList<>();
		nQueens = new char[n][n];
		Stack<Queen> path = new Stack<>();
		for (int i = 0; i < n; i++) {
			Arrays.fill(nQueens[i], '.');
		}
		colUsed = new boolean[n];
		diagonals45Used = new boolean[2 * n - 1];
		diagonals135Used = new boolean[2 * n - 1];

		solveNQueens(0, n, ans, path);

		return ans;
	}

	public void solveNQueens(int row, int n, List<List<String>> ans, Stack<Queen> path) {
		if (row >= n) {
			List<String> list = new ArrayList<>();
			for (char[] chars : nQueens) {
				list.add(new String(chars));
			}
			ans.add(list);
			return;
		}

		for (int i = 0; i < n; i++) {
			if (!colUsed[i] && !diagonals45Used[row + i] && !diagonals135Used[n - 1 - row + i]) {
				colUsed[i] = true;
				diagonals45Used[row + i] = true;
				diagonals135Used[n - 1 - row + i] = true;
				path.add(new Queen(row, i));
				nQueens[row][i] = 'Q';

				solveNQueens(row + 1, n, ans, path);

				colUsed[i] = false;
				diagonals45Used[row + i] = false;
				diagonals135Used[n - 1 - row + i] = false;
				path.pop();
				nQueens[row][i] = '.';
			}
		}
	}

	class Queen {
		int row;
		int col;

		public Queen(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

	// 526 Beautiful Arrangement
	// https://www.youtube.com/watch?v=xf8qAkqDr8Y
	// 用DFS搜索所有组合

	// 419. Battleships in a Board
	public int countBattleships(char[][] board) {
		int m = board.length;
		int n = board[0].length;
		int ans = 0;
		boolean[][] visited = new boolean[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (!visited[i][j] && board[i][j] == 'X') {
					countBattleshipsDFS(board, i, j, visited);
					ans++;
				}
			}
		}
		return ans;
	}

	public void countBattleshipsDFS(char[][] board, int i, int j, boolean[][] visited) {
		int[][] directions = new int[][] { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
		int m = board.length;
		int n = board[0].length;

		if (i < 0 || i >= m || j < 0 || j >= n || visited[i][j]) {
			return;
		}
		visited[i][j] = true;

		for (int[] d : directions) {
			int nextM = i + d[0];
			int nextN = j + d[1];
			if (nextM < 0 || nextM >= m || nextN < 0 || nextN >= n) {
				continue;
			}
			if (!visited[nextM][nextN] && board[nextM][nextN] == 'X') {
				countBattleshipsDFS(board, nextM, nextN, visited);
			}
		}
		return;
	}

	// 15. 3Sum 从nums中取三数，要求其和为0
	// 从小到大取数，取两数之后，可以按照哈希表直接找剩下的数里有没有需要的
	public List<List<Integer>> threeSum(int[] nums) {
		Arrays.sort(nums);
		Map<Integer, Integer> counts = new HashMap<>();
		for (int num : nums) {
			int count = counts.getOrDefault(num, 0);
			counts.put(num, count + 1);
		}
		boolean[] used = new boolean[nums.length];
		List<List<Integer>> ans = new ArrayList<>();
		Stack<Integer> stack = new Stack<>();
		threeSumDfs(nums, 0, 0, 0, stack, ans, used, counts);
		return ans;
	}

	public void threeSumDfs(int[] nums, int depth, int index, int sum, Stack<Integer> stack, List<List<Integer>> ans,
			boolean[] used, Map<Integer, Integer> counts) {
		// -sum 即为需要找的数，stack.peek()是之前取的最大的数，因为要去掉重复的，所以不能找比它小的了
		if (depth == 2 && counts.get(-sum) != null && counts.get(-sum) > 0 && -sum >= stack.peek()) {
			stack.add(-sum);
			ans.add(new ArrayList<Integer>(stack));
			stack.pop();
			return;
		}
		if (depth == 2) {
			return;
		}

		for (int i = index; i < nums.length; i++) {
			if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
				continue;
			}
			if (used[i])
				continue;

			used[i] = true;
			counts.put(nums[i], counts.get(nums[i]) - 1);
			stack.add(nums[i]);
			threeSumDfs(nums, depth + 1, i, sum + nums[i], stack, ans, used, counts);
			stack.pop();
			counts.put(nums[i], counts.get(nums[i]) + 1);
			used[i] = false;
		}
		return;
	}

	// 212. Word Search II
	// DFS + Trie, 用trie来来加速搜索，这样如果有很多前缀一致的单词可以更快搜到

	// 301. Remove Invalid Parentheses
	// 此题关键是找出到底最少需要删掉多少左括号，多少右括号，知道这个信息，然后dfs即可
	// 核心在于先扫描一遍string，如果遇到'('，left++，因为这个'('可能会和后面的某个右括号匹配。left只会>=0。
	// 遇到')'：如果left>0，直接left--。如果left==0，那么说明这个右括号一定需要移除，right++，。
	// 扫描完的left和right分别代表总共需要移除的左括号和右括号数量。
}
