import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

// 残酷刷题群算法小讲座：动态规划的套路 - https://www.youtube.com/watch?v=FLbqgyJ-70I
public class DP {
	public static void main(String[] args) {
		int[] test = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0 };
		rob(test);
		numDecodings("230");
		int[][] test1 = new int[][] { { -6, 9 }, { 1, 6 }, { 8, 10 }, { -1, 4 }, { -6, -2 }, { -9, 8 }, { -5, 3 },
				{ 0, 3 } };
		String[] test2 = new String[] { "leet", "code" };
		wordBreak("leetcode", Arrays.asList(test2));
		combinationSum4(new int[] { 1, 2, 3 }, 4);
	}

	// 70. 上楼梯：有 N 阶楼梯，每次可以上一阶或者两阶，求有多少种上楼梯的方法。
	public int climbStairs(int n) {
		List<List<Integer>> paths = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		int[] memo = new int[n + 1];
		return climbStairs(n, memo, path, paths);
	}

	public int climbStairs(int n, int[] memo, Stack<Integer> path, List<List<Integer>> paths) {
		if (n == 0) {
			paths.add(path);
			return 1;
		}
		if (n == 1) {
			path.push(1);
			paths.add(path);
			path.pop();
			return 1;
		}
		if (memo[n] > 0) {
			return memo[n];
		}

		int ans = climbStairs(n - 1, memo, path, paths) + climbStairs(n - 2, memo, path, paths);
		memo[n] = ans;
		return ans;
	}

	// 198. House Robber (Easy) 抢劫一排住户，但是不能抢邻近的住户，求最大抢劫量。
	public static int rob1(int[] nums) {
		int[] memoCanRob = new int[nums.length];
		int[] memoNoRob = new int[nums.length];
		Arrays.fill(memoCanRob, -1);
		Arrays.fill(memoNoRob, -1);

		return rob1(nums, 0, true, memoCanRob, memoNoRob);
	}

	public static int rob1(int[] nums, int start, boolean canRob, int[] memoCanRob, int[] memoNoRob) {
		if (canRob && memoCanRob[start] > -1) {
			return memoCanRob[start];
		}
		if (!canRob && memoNoRob[start] > -1) {
			return memoNoRob[start];
		}
		if (start == nums.length - 1) {
			return canRob ? nums[start] : 0;
		}

		int maxValue = 0;
		if (canRob) {
			maxValue = Math.max(nums[start] + rob1(nums, start + 1, false, memoCanRob, memoNoRob),
					rob1(nums, start + 1, true, memoCanRob, memoNoRob));
			memoCanRob[start] = maxValue;
		} else {
			maxValue = rob1(nums, start + 1, true, memoCanRob, memoNoRob);
			memoNoRob[start] = maxValue;
		}
		return canRob ? memoCanRob[start] : memoNoRob[start];
	}

	// 213. House Robber II (Medium) 环型社区
	// 考虑环的影响，首位和末位不能同时为yes。这说明至少有一个的选择是no。
	// (1) 如果首位我们选择no，那么从nums[1]到nums[n-1]的选择就没有环形的首尾制约
	// (2) 如果末位我们选择no，那么从nums[0]到nums[n-2]的选择就没有环形的首尾制约
	// 注意，(1)和(2)并不是互斥的。他们是有交叠的。但是它们的并集一定是全集。
	public static int rob(int[] nums) {
		int[] memoCanRob = new int[nums.length];
		int[] memoNoRob = new int[nums.length];
		Arrays.fill(memoCanRob, -1);
		Arrays.fill(memoNoRob, -1);
		if (nums == null || nums.length == 0) {
			return 0;
		}
		int n = nums.length;
		if (n == 1) {
			return nums[0];
		}

		int max1 = rob(nums, 0, n - 1, true, memoCanRob, memoNoRob);
		Arrays.fill(memoCanRob, -1);
		Arrays.fill(memoNoRob, -1);
		int max2 = rob(nums, 1, n, true, memoCanRob, memoNoRob);
		return Math.max(max1, max2);
	}

	public static int rob(int[] nums, int start, int end, boolean canRob, int[] memoCanRob, int[] memoNoRob) {
		if (canRob && memoCanRob[start] > -1) {
			return memoCanRob[start];
		}
		if (!canRob && memoNoRob[start] > -1) {
			return memoNoRob[start];
		}
		if (start == end - 1) {
			return canRob ? nums[start] : 0;
		}

		int maxValue = 0;
		if (canRob) {
			maxValue = Math.max(nums[start] + rob(nums, start + 1, end, false, memoCanRob, memoNoRob),
					rob(nums, start + 1, end, true, memoCanRob, memoNoRob));
			memoCanRob[start] = maxValue;
		} else {
			maxValue = rob(nums, start + 1, end, true, memoCanRob, memoNoRob);
			memoNoRob[start] = maxValue;
		}
		return canRob ? memoCanRob[start] : memoNoRob[start];
	}

	// 64. Minimum Path Sum (Medium) 求从矩阵的左上角到右下角的最小路径和，每次只能向右和向下移动。
	int row = 0;
	int col = 0;

	public int minPathSum(int[][] grid) {
		this.row = grid.length;
		this.col = grid[0].length;
		int[][] pathSum = new int[this.row][this.col];

		return minPathSum(0, 0, grid, pathSum);
	}

	public int minPathSum(int rowS, int colS, int[][] grid, int[][] pathSum) {
		if (pathSum[rowS][colS] > 0) {
			return pathSum[rowS][colS];
		}
		if (rowS == this.row - 1 && colS == this.col - 1) {
			pathSum[rowS][colS] = grid[rowS][colS];
			return pathSum[rowS][colS];
		}

		int sum = 0;
		if (rowS == this.row - 1) {
			for (int i = colS; i < this.col; i++) {
				sum += grid[rowS][i];
			}
			pathSum[rowS][colS] = sum;
			return sum;
		}
		sum = 0;
		if (colS == this.col - 1) {
			for (int i = rowS; i < this.row; i++) {
				sum += grid[i][colS];
			}
			pathSum[rowS][colS] = sum;
			return sum;
		}
		pathSum[rowS][colS] = Math.min(minPathSum(rowS + 1, colS, grid, pathSum) + grid[rowS][colS],
				minPathSum(rowS, colS + 1, grid, pathSum) + grid[rowS][colS]);
		return pathSum[rowS][colS];
	}

	// 62 矩阵的总路径数: 统计从矩阵左上角到右下角的路径总数，每次只能向右或者向下移动。
	public int uniquePaths(int m, int n) {
		int[][] pathSum = new int[m + 1][n + 1];

		return uniquePaths(m, n, pathSum);
	}

	public int uniquePaths(int m, int n, int[][] pathSum) {
		if (pathSum[m][n] > 0) {
			return pathSum[m][n];
		}
		if (m == 1 || n == 1) {
			pathSum[m][n] = 1;
			return pathSum[m][n];
		}

		pathSum[m][n] = uniquePaths(m - 1, n, pathSum) + uniquePaths(m, n - 1, pathSum);
		return pathSum[m][n];
	}

	// 303. Range Sum Query - Immutable (Easy)
	// 求区间 i ~ j 的和，可以转换为 sum[j + 1] - sum[i]，其中 sum[i] 为 0 ~ i - 1 的和。

	// 413. Arithmetic Slices 数组中等差子区间的个数
	public int numberOfArithmeticSlices(int[] nums) {
		// dp[i] 表示以 nums[i] 为结尾的等差子区间的个数
		int[] dp = new int[nums.length];
		if (nums.length < 3) {
			return 0;
		}
		for (int i = 2; i < nums.length; i++) {
			if (nums[i] - nums[i - 1] == nums[i - 1] - nums[i - 2]) {
				// dp[i]包含所有dp[i-1]应有的子区间，额外还会有一个以自己结尾的长度为3的子区间
				dp[i] = dp[i - 1] + 1;
			}
		}
		return Arrays.stream(dp).sum();
	}

	// 343 分割整数的最大乘积
	// https://leetcode.com/problems/integer-break/
	public int integerBreak(int n) {
		int[] dp = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j < i; j++) {
				// i 依次分为 j 和 i-j两数
				dp[i] = Math.max(dp[i], dp[j] * dp[i - j]);
				dp[i] = Math.max(dp[i], dp[j] * (i - j));
				dp[i] = Math.max(dp[i], j * (i - j));
			}
		}
		return dp[n];
	}

	// 279 组成整数的最小平方数数量
	// 题目描述given n = 12, return 3 because 12 = 4 + 4 + 4; given n = 13, return 2
	// because 13 = 4 + 9.
	// 此题用搜索更简单一点
	public int numSquares(int n) {
		List<Integer> squareList = generateSquareList(n);
		int[] dp = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			int min = Integer.MAX_VALUE;
			for (int square : squareList) {
				if (square > i) {
					break;
				}
				min = Math.min(min, dp[i - square] + 1);
			}
			dp[i] = min;
		}
		return dp[n];
	}

	private List<Integer> generateSquareList(int n) {
		List<Integer> squareList = new ArrayList<>();
		int diff = 3;
		int square = 1;
		while (square <= n) {
			squareList.add(square);
			square += diff;
			diff += 2;
		}
		return squareList;
	}

	// 91. Decode Ways (Medium)
	public static int numDecodings(String s) {
		int[] memo = new int[s.length()];
		return numDecodings(s, 0, memo);
	}

	public static int numDecodings(String s, int start, int[] memo) {
		String c = s.substring(start, s.length());
		if (c.length() == 0) {
			return 1;
		}
		if (c.charAt(0) == '0') {
			memo[start] = 0;
			return memo[start];
		}

		if (c.length() == 1) {
			if (c.charAt(0) == '0') {
				memo[start] = 0;
				return memo[start];
			}
			memo[start] = 1;
			return memo[start];
		}
		if (c.length() == 2 && Integer.parseInt(c) > 26 && c.charAt(1) == '0') {
			memo[start] = 0;
			return memo[start];
		}

		if (memo[start] > 0) {
			return memo[start];
		}

		if (c.charAt(0) == '1' || (c.charAt(0) == '2' && c.charAt(1) - '0' <= 6)) {
			memo[start] = numDecodings(s, start + 1, memo) + numDecodings(s, start + 2, memo);
			return memo[start];
		} else {
			memo[start] = numDecodings(s, start + 1, memo);
			return memo[start];
		}
	}

	// 300. Longest Increasing Subsequence
	// DP implementation
	public int lengthOfLISDP(int[] nums) {
		if (nums.length < 2) {
			return nums.length;
		}
		int[] dp = new int[nums.length];
		int res = 0;
		for (int i = 0; i < nums.length; i++) {
			int max = 1;
			for (int j = 0; j < i; j++) {
				if (nums[i] > nums[j]) {
					max = Math.max(max, dp[j] + 1);
				}
			}
			dp[i] = max;
			res = Math.max(res, max);
		}
		return res;
	}

	// construct Array smartly
	// Approach 2
	// https://leetcode.com/problems/longest-increasing-subsequence/solution/
	public int lengthOfLIS(int[] nums) {
		int n = nums.length;
		if (n < 2) {
			return n;
		}
		List<Integer> subsequence = new ArrayList<>();
		subsequence.add(nums[0]);
		for (int i = 1; i < nums.length; i++) {
			int num = nums[i];
			if (num > subsequence.get(subsequence.size() - 1)) {
				subsequence.add(num);
			} else {
				for (int j = 0; j < subsequence.size(); j++) {
					if (num <= subsequence.get(j)) {
						subsequence.set(j, num);
						break;
					}
				}
			}
		}
		return subsequence.size();
	}

	// 646 一组整数对能够构成的最长链
	// dp[i]是到i号pair为止的最长链
	public int findLongestChain(int[][] pairs) {
		if (pairs == null || pairs.length == 0) {
			return 0;
		}
		Arrays.sort(pairs, new PairsComparator());
		int[] dp = new int[pairs.length];
		Arrays.fill(dp, 1);
		for (int i = 1; i < pairs.length; i++) {
			for (int j = 0; j < i; j++) {
				if (pairs[i][0] > pairs[j][1]) {
					dp[i] = Math.max(dp[i], dp[j] + 1);
				} else {
					dp[i] = Math.max(dp[i], dp[j]);
				}
			}
		}
		return Arrays.stream(dp).max().getAsInt();
	}

	public class PairsComparator implements Comparator<int[]> {
		@Override
		public int compare(int[] p1, int[] p2) {
			if (p1[0] == p2[0]) {
				return p1[1] - p2[1];
			}
			return p1[0] - p2[0];
		}
	}

	// 376 最长摆动子序列
	// https://github.com/wisdompeak/LeetCode/tree/master/Dynamic_Programming/376.Wiggle-Subsequence
	public int wiggleMaxLength(int[] nums) {
		if (nums.length < 2) {
			return nums.length;
		}
		if (nums.length == 2) {
			return nums[1] - nums[0] == 0 ? 1 : 2;
		}
		// up表示截止目前为止，最后一个元素是上升趋势的最长wiggle子序列；
		// down表示截止目前为止，最后一个元素时下降趋势的最长wiggle子序列。
		int[] up = new int[nums.length];
		int[] down = new int[nums.length];
		Arrays.fill(up, 1);
		Arrays.fill(down, 1);
		for (int i = 1; i < nums.length; i++) {
			int diff = nums[i] - nums[i - 1];
			if (diff > 0) {
				up[i] = down[i - 1] + 1;
				down[i] = down[i - 1];
			} else if (diff == 0) {
				up[i] = up[i - 1];
				down[i] = down[i - 1];
			} else {
				down[i] = up[i - 1] + 1;
				up[i] = up[i - 1];
			}
		}
		return Math.max(up[nums.length - 1], down[nums.length - 1]);
	}

	// 1143 最长公共子序列
	// 2d dp: https://turingplanet.org/2020/08/28/dynamic-programming-2dleetcode15/
	public int longestCommonSubsequence(String text1, String text2) {
		int[][] dp = new int[text1.length()][text2.length()];
		Arrays.stream(dp).forEach(a -> Arrays.fill(a, -1));
		return longestCommonSubsequence(text1, text2, text1.length() - 1, text2.length() - 1, dp);
	}

	public int longestCommonSubsequence(String text1, String text2, int start1, int start2, int[][] dp) {

		if (dp[start1][start2] >= 0) {
			return dp[start1][start2];
		}

		char ch1 = text1.charAt(start1);
		char ch2 = text2.charAt(start2);
		if (start1 == 0 && start2 == 0) {
			dp[start1][start2] = ch1 == ch2 ? 1 : 0;
			return dp[start1][start2];
		}
		if (start1 == 0) {
			dp[start1][start2] = ch1 == ch2 ? 1 : longestCommonSubsequence(text1, text2, start1, start2 - 1, dp);
			return dp[start1][start2];
		}
		if (start2 == 0) {
			dp[start1][start2] = ch1 == ch2 ? 1 : longestCommonSubsequence(text1, text2, start1 - 1, start2, dp);
			return dp[start1][start2];
		}

		if (ch1 == ch2) {
			dp[start1][start2] = longestCommonSubsequence(text1, text2, start1 - 1, start2 - 1, dp) + 1;
		} else {
			dp[start1][start2] = Math.max(longestCommonSubsequence(text1, text2, start1 - 1, start2, dp),
					longestCommonSubsequence(text1, text2, start1, start2 - 1, dp));
		}
		return dp[start1][start2];
	}

	// 0-1 背包
	// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20动态规划.md#0-1-背包
	// https://www.youtube.com/watch?v=CO0r6kcwHUU
	public int knapsack(int W, int N, int[] weights, int[] values) {
		// 其中 dp[i][j] 表示前 i 件物品在背包承重不超过 j 的情况下能达到的最大价值
		int[][] dp = new int[N + 1][W + 1];
		for (int i = 1; i < N; i++) {
			// 第i件物品的重量和价值
			int weight = weights[i - 1], value = values[i - 1];
			for (int j = 1; j < W; j++) {
				if (j >= weight) {
					// 第 i 件物品没添加到背包时，总体积不超过 j 的前 i 件物品的最大价值就是总体积不超过 j 的前 i-1 件物品的最大价值
					// 第 i 件物品添加到背包中，dp[i][j] = dp[i-1][j-w] + v
					dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight] + value);
				} else {
					// 第 i 件物品由于太重，没添加到背包
					dp[i][j] = dp[i - 1][j];
				}
			}
		}
		return dp[N][W];
	}

	// 416 划分数组为和相等的两部分
	public boolean canPartition(int[] nums) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		if (sum % 2 != 0) {
			return false;
		}
		int target = sum / 2;
		boolean[] dp = new boolean[target + 1];
		dp[0] = true;
		for (int i = 1; i < nums.length; i++) {
			for (int j = target; j >= nums[i]; j--) {
				dp[j] = dp[j] || dp[j - nums[i]];
			}
		}
		return dp[target];
	}

	// 494 改变一组数的正负号使得它们的和为一给定数
	// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20动态规划.md#2-改变一组数的正负号使得它们的和为一给定数
	// 可以将这组数看成两部分，P 和 N，其中 P 使用正号，N 使用负号，有以下推导：
	// sum(P) - sum(N) = target
	// sum(P) + sum(N) + sum(P) - sum(N) = target + sum(P) + sum(N)
	// 2 * sum(P) = target + sum(nums)
	public int findTargetSumWays(int[] nums, int S) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		if (sum < S || (sum + S) % 2 == 1) {
			return 0;
		}
		int W = (sum + S) / 2;
		int[] dp = new int[W + 1];
		dp[0] = 1;
		for (int num : nums) {
			for (int i = W; i >= num; i--) {
				dp[i] = dp[i] + dp[i - num];
			}
		}
		return dp[W];
	}

	// 474. 01字符构成最多的字符串
	public int findMaxForm(String[] strs, int m, int n) {
		if (strs == null || strs.length == 0) {
			return 0;
		}
		int[][] dp = new int[m + 1][n + 1];
		for (String s : strs) { // 每个字符串只能用一次
			int ones = 0, zeros = 0;
			for (char c : s.toCharArray()) {
				if (c == '0') {
					zeros++;
				} else {
					ones++;
				}
			}
			for (int i = m; i >= zeros; i--) {
				for (int j = n; j >= ones; j--) {
					dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
				}
			}
		}
		return dp[m][n];
	}

	// 322. Coin Change:给一些面额的硬币，要求用这些硬币来组成给定面额的钱数，并且使得硬币数量最少。硬币可以重复使用。
	// 因为硬币可以重复使用，因此这是一个完全背包问题。完全背包只需要将 0-1 背包的逆序遍历 dp 数组改为正序遍历即可。
	public int coinChange(int[] coins, int amount) {
		if (amount == 0 || coins == null)
			return 0;
		int[] dp = new int[amount + 1];
		Arrays.fill(dp, Integer.MAX_VALUE - 1);
		for (int coin : coins) {
			if (coin > amount) {
				continue;
			}
			dp[coin] = 1;
			for (int i = coin; i <= amount; i++) {
				dp[i] = Math.min(dp[i], dp[i - coin] + 1);
			}
		}
		return dp[amount] == Integer.MAX_VALUE - 1 ? -1 : dp[amount];
	}

	// 518. Coin Change 2:给一些面额的硬币，要求用这些硬币来组成给定面额的钱数,给出可组合出的数量
	public int change(int amount, int[] coins) {
		if (coins == null) {
			return 0;
		}
		int[] dp = new int[amount + 1];
		Arrays.fill(dp, 0);
		dp[0] = 1;
		for (int coin : coins) {
			if (coin > amount) {
				continue;
			}
			for (int i = coin; i <= amount; i++) {
				dp[i] = dp[i] + dp[i - coin];
			}
		}
		return dp[amount];
	}

	// 139 字符串按单词列表分割
	// 背包问题
	public static boolean wordBreak(String s, List<String> wordDict) {
		int n = s.length();
		boolean[] dp = new boolean[n + 1];
		dp[0] = true;
		for (int i = 1; i <= n; i++) {
			for (String word : wordDict) {
				if (word.length() <= i) {
					if (s.substring(i - word.length(), i).equals(word)) {
						dp[i] = dp[i] || dp[i - word.length()];
					}
				}
			}
		}
		return dp[n];
	}

	// DFS 法
	public static boolean wordBreakDFS(String s, List<String> wordDict) {
		if (s.length() == 0) {
			return true;
		}
		Boolean[] dp = new Boolean[s.length() + 1];
		wordBreakDFS(s, 0, wordDict, dp);
		return dp[0];
	}

	public static boolean wordBreakDFS(String s, int start, List<String> wordDict, Boolean[] dp) {
		if (s.length() == start) {
			return true;
		}
		if (dp[start] != null) {
			return dp[start];
		}
		boolean found = false;
		for (String w : wordDict) {
			if (s.substring(start).startsWith(w) && w.length() <= s.length() - start) {
				found = found || wordBreakDFS(s, start + w.length(), wordDict, dp);
			}
		}
		dp[start] = found;
		return dp[start];
	}

	// 377. Combination Sum IV 总和为target的组合数量
	// 优先循环从1到target，确保每个总和的组合数量都求出来，再求更大的总和组合数量
	public static int combinationSum4(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		int[] dp = new int[target + 1];
		dp[0] = 1;
		for (int i = 1; i <= target; i++) {
			for (int num : nums) {
				if (num > i) {
					continue;
				}
				dp[i] += dp[i - num];
			}
		}
		return dp[target];
	}

	// 309 需要冷却期的股票交易
	public int maxProfitCoolDown(int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		int[] hold = new int[prices.length]; // 第i天手上有股票的最大profit
		int[] unhold = new int[prices.length];// 第i天手上没有股票的最大profit
		hold[0] = -prices[0];
		unhold[0] = 0;
		hold[1] = -Math.min(prices[0], prices[1]);
		unhold[1] = prices[1] > prices[0] ? prices[1] - prices[0] : 0;
		for (int i = 2; i < prices.length; i++) {
			// 第i天买入或没买入的最大值
			hold[i] = Math.max(unhold[i - 2] - prices[i], hold[i - 1]);
			// 第i天有卖出或没有卖出的最大值
			unhold[i] = Math.max(hold[i - 1] + prices[i], unhold[i - 1]);
		}
		return Math.max(hold[prices.length - 1], unhold[prices.length - 1]);
	}

	// 714 需要交易费用的股票交易
	public int maxProfitFee(int[] prices, int fee) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		int[] hold = new int[prices.length]; // 第i天手上有股票的最大profit
		int[] unhold = new int[prices.length];// 第i天手上没有股票的最大profit
		hold[0] = -prices[0];
		unhold[0] = 0;
		hold[1] = -Math.min(prices[0], prices[1]) - fee;
		unhold[1] = prices[1] > prices[0] && prices[1] - prices[0] > fee ? prices[1] - prices[0] - fee : 0;
		for (int i = 2; i < prices.length; i++) {
			// 第i天买入或没买入的最大值
			hold[i] = Math.max(unhold[i - 1] - prices[i] - fee, hold[i - 1]);
			// 第i天有卖出或没有卖出的最大值
			unhold[i] = Math.max(hold[i - 1] + prices[i], unhold[i - 1]);
		}
		return Math.max(hold[prices.length - 1], unhold[prices.length - 1]);
	}

	// 123 只能进行两次的股票交易
	// https://www.youtube.com/watch?v=a8xKiVTpdks
	public static int maxProfit(int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		// 第一次交易从左往右看，first[i]表示在i处分割，从0到i个价格，交易的最大利益
		int[] first = new int[prices.length];
		int low = prices[0];
		first[0] = 0;
		for (int i = 1; i < prices.length; i++) {
			low = Math.min(prices[i], low);
			first[i] = Math.max(first[i - 1], prices[i] - low);
		}

		// 第二次交易从右往左看，second[i]表示在i处分割，从i到末尾的价格，交易的最大利益
		int[] second = new int[prices.length];
		int high = prices[prices.length - 1];
		second[prices.length - 1] = 0;
		for (int i = prices.length - 2; i >= 0; i--) {
			high = Math.max(high, prices[i]);
			second[i] = Math.max(high - prices[i], second[i + 1]);
		}

		int max = 0;
		for (int i = 0; i < prices.length - 1; i++) {
			max = Math.max(max, first[i] + second[i]);
		}
		return max;
	}

	// 188 只能进行 k 次的股票交易
	// https://www.youtube.com/watch?v=ZMgTmDvAp6g
	public int maxProfit(int k, int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		int n = prices.length;
		int[][] dp = new int[n][k + 1]; // 只考虑前n个价格时，交易k次的最大值

		for (int trans = 1; trans <= k; trans++) {
			int maxRemaining = -prices[0];
			for (int day = 1; day < n; day++) { // j为0时dp[0][*]肯定为0
				dp[day][trans] = Math.max(dp[day - 1][trans], maxRemaining + prices[day]);
				maxRemaining = Math.max(maxRemaining, dp[day][trans - 1] - prices[day]);
			}
		}

		return dp[n - 1][k];
	}

	// 这个算法O(kn^2) too slow, just for 思路
	public int maxProfitK(int k, int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		int n = prices.length;
		int[][] dp = new int[n][k + 1]; // 只考虑前n个价格时，交易k次的最大值
		dp[0][1] = 0;

		for (int trans = 1; trans <= k; trans++) {
			for (int day = 1; day < n; day++) { // j为0时dp[0][*]肯定为0
				int maxProfitForDay = 0;
				for (int lastDay = 0; lastDay < day; lastDay++) {
					maxProfitForDay = Math.max(maxProfitForDay,
							dp[lastDay][trans - 1] + getMaxProfitInRange(prices, lastDay, day));
				}
				dp[day][trans] = maxProfitForDay;
			}
		}

		return dp[n - 1][k];
	}

	public int getMaxProfitInRange(int[] prices, int start, int end) {
		int max = 0;
		int low = prices[start];
		for (int i = start; i <= end; i++) {
			low = Math.min(prices[i], low);
			max = Math.max(prices[i] - low, max);
		}
		return max;
	}

	// 583 删除两个字符串的字符使它们相等
	// 同1143 可以转换为求两个字符串的最长公共子序列问题
	public int minDistance1(String word1, String word2) {
		int m = word1.length(), n = word2.length();
		int[][] dp = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
				}
			}
		}
		return m + n - 2 * dp[m][n];
	}

	// 72 Edit Distance：修改一个字符串成为另一个字符串，使得修改次数最少。一次修改操作包括：插入一个字符、删除一个字符、替换一个字符。
	public int minDistance(String word1, String word2) {
		if (word1 == null || word2 == null) {
			return 0;
		}
		int m = word1.length(), n = word2.length();
		int[][] dp = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			dp[i][0] = i;
		}
		for (int i = 1; i <= n; i++) {
			dp[0][i] = i;
		}
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					// dp[i-1][j]表示删掉第一个单词的一个字符
					// dp[i][j-1]表示插入第一个单词一个新字符
					int minStep = Math.min(dp[i - 1][j], dp[i][j - 1]);
					// dp[i - 1][j - 1]表示替换一个字符
					minStep = Math.min(dp[i - 1][j - 1], minStep);
					dp[i][j] = minStep;
				}
			}
		}
		return dp[m][n];
	}
}
