import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
		isMatch2("aa", "a*");
		increasingTripletDP(new int[] { 4, 5, 2147483647, 1, 2 });
	}

	// 70. 上楼梯：有 N 阶楼梯，每次可以上一阶或者两阶，求有多少种上楼梯的方法。
	public int climbStairs(int n) {
		if (n == 1)
			return 1;
		int dp[] = new int[n + 1];
		dp[0] = 1;
		dp[1] = 1;
		for (int i = 2; i <= n; i++) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}
		return dp[n];
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

	// 740. Delete and Earn
	// 转换成house rob即可：几号数字即为几号房子，拿了该数字的分数，该数字两侧的分数就都不能拿
	public int deleteAndEarn(int[] nums) {
		Map<Integer, Integer> points = new HashMap<>();
		int max = Integer.MIN_VALUE;
		// 把数组转化成 房子和对应分数的mapping
		for (int i : nums) {
			if (points.containsKey(i)) {
				points.put(i, points.get(i) + i);
			} else {
				points.put(i, i);
			}
			max = Math.max(i, max);
		}
		int[] dp = new int[max + 1];
		dp[0] = 0;
		dp[1] = points.getOrDefault(1, 0);
		for (int i = 2; i <= max; i++) {
			dp[i] = Math.max(dp[i - 1], dp[i - 2] + points.getOrDefault(i, 0));
		}
		return dp[max];
	}

	// 55. Jump Game
	public boolean canJump(int[] nums) {
		int n = nums.length;
		// min steps to rech n-1 from i
		boolean[] dp = new boolean[n];
		dp[n - 1] = true;
		for (int i = n - 2; i >= 0; i--) {
			for (int j = 1; j <= nums[i]; j++) {
				// 剪枝，i+j太大就没必要算了
				if (i + j > n - 1)
					break;
				int jumpFromIndex = Math.min(i + j, n - 1);
				dp[i] = dp[i] || dp[jumpFromIndex];
			}
		}
		return dp[0];
	}

	// 45. Jump Game II
	public int jump(int[] nums) {
		int n = nums.length;
		// min steps to rech n-1 from i
		int[] dp = new int[n];
		Arrays.fill(dp, n);
		dp[n - 1] = 0;
		for (int i = n - 2; i >= 0; i--) {
			for (int j = 1; j <= nums[i]; j++) {
				int jumpFromIndex = Math.min(i + j, n - 1);
				dp[i] = Math.min(dp[i], dp[jumpFromIndex] + 1);
			}
		}
		return dp[0];
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
				dp[i] = Math.max(dp[i], j * dp[i - j]);
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
	// 完全背包：dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - weight] + value);
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
		for (int i = 0; i < nums.length; i++) {
			// 需从后往前遍历，否则假设nums[i]为1，从前往后遍历的话所有dp值均为true
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
	// Return the size of the largest subset of strs such that there are at most m
	// 0's and n 1's in the subset.
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

	// 139 Word Break
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

	// DFS + memorization
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

	// 464 can I win
	// https://www.bilibili.com/video/BV1KW411o7m2
	// 状态压缩dp，N为可选数字范围，用一个N位二进制数字表示某个数字是否被选过
	private int dfs(int dp[][], int current, int max, int desire, int mask, int player) {
		if (dp[player][mask] != -1) {
			// already calculated state
			return dp[player][mask];
		}

		for (int i = 0; i < max; i++) {
			int mk = (1 << i); // Set ith bit to 1
			if ((mask & mk) == 0) { // ith number is not selected
				// current value + i + 1 >= desired, then "player" wins
				if (current + i + 1 >= desire
						|| (dfs(dp, current + i + 1, max, desire, (mask | mk), 1 - player) != 1)) {
					return dp[player][mask] = 1;
				}
			}
		}
		return dp[player][mask] = 0; // "player" does not win
	}

	public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
		// desired total can't be more that the possible max sum i.e. n*(n+1)/2
		if ((maxChoosableInteger * (maxChoosableInteger + 1) / 2) < desiredTotal) {
			return false;
		}
		int[][] dp = new int[2][1 << maxChoosableInteger];
		Arrays.fill(dp[0], -1);
		Arrays.fill(dp[1], -1);
		return dfs(dp, 0, maxChoosableInteger, desiredTotal, 0, 0) == 1;

	}

	// 486 predict winner
	// 区间dp，f[i][j]表示在区间i到j范围内，第一个player能得到的最高分
	// https://www.bilibili.com/video/BV1Lb411w7X4?p=1

	// 334. Increasing Triplet Subsequence
	// Given an integer array nums, return true if there exists a triple of indices
	// (i, j, k) such that i < j < k and nums[i] < nums[j] < nums[k]
	// 看能否找出三个数为升序即可
	public static boolean increasingTripletDP(int[] nums) {
		int[] dp = new int[nums.length];
		dp[0] = 1;
		for (int i = 1; i < nums.length; i++) {
			int max = 1;
			for (int j = 0; j < i; j++) {
				if (nums[i] > nums[j]) {
					max = Math.max(max, dp[j] + 1);
				}
			}
			dp[i] = max;
		}
		return Arrays.stream(dp).max().getAsInt() >= 3;
	}

	// 此题还可用nlog(n)方法做
	public boolean increasingTriplet(int[] nums) {
		List<Integer> lis = new ArrayList<>();
		lis.add(nums[0]);
		for (int i = 1; i < nums.length; i++) {
			if (lis.get(lis.size() - 1) < nums[i]) {
				lis.add(nums[i]);
			}
			if (lis.get(lis.size() - 1) > nums[i]) {
				lis.set(increasingTripletBS(lis, nums[i]), nums[i]);
			}
		}
		return lis.size() >= 3;
	}

	// Binary seach find the right position to insert
	public int increasingTripletBS(List<Integer> lis, int num) {
		int low = 0;
		int high = lis.size() - 1;
		while (low <= high) {
			int mid = low + (high - low + 1) / 2;
			if (lis.get(mid) >= num) {
				high = mid;
			} else {
				low = mid;
			}
		}
		return low;
	}

	// 746. Min Cost Climbing Stairs
	public int minCostClimbingStairs(int[] cost) {
		int[] dp = new int[cost.length + 1];
		if (cost.length <= 1)
			return 0;
		if (cost.length == 2)
			return Math.min(cost[0], cost[1]);

		dp[0] = 0;
		dp[1] = 0;
		for (int i = 2; i < cost.length; i++) {
			dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
		}

		return Math.min(dp[cost.length - 1] + cost[cost.length - 1], dp[cost.length - 2] + cost[cost.length - 2]);
	}

	// 44. Wildcard Matching
	public boolean isMatch(String s, String p) {
		boolean dp[][] = new boolean[s.length() + 1][p.length() + 1];
		dp[0][0] = true;
		for (int j = 1; j <= p.length(); j++) {
			if (p.charAt(j - 1) == '*') {
				dp[0][j] = dp[0][j - 1];
			}
		}

		for (int i = 1; i <= s.length(); i++) {
			for (int j = 1; j <= p.length(); j++) {
				char a = s.charAt(i - 1);
				char b = p.charAt(j - 1);
				if (a == b || b == '?') {
					dp[i][j] = dp[i - 1][j - 1];
				} else if (b == '*') {
					boolean curr = false;
					// k = i 时代表*作为空字符也可以match
					for (int k = 0; k <= i; k++) {
						curr = curr || dp[k][j - 1];
					}
					dp[i][j] = curr;
				}
			}
		}
		return dp[s.length()][p.length()];
	}

	// 152. Maximum Product Subarray
	public int maxProduct(int[] nums) {
		if (nums.length == 1)
			return nums[0];
		int n = nums.length;
		// dp[i] means using ith number as part of subarry, the max postivie/negative
		int[][] dp = new int[n + 1][2];

		for (int i = 1; i <= n; i++) {
			int prevPositive = dp[i - 1][0];
			int prevNegative = dp[i - 1][1];
			int curr = nums[i - 1];
			if (curr > 0) {
				dp[i][0] = Math.max(curr, prevPositive * curr);
				dp[i][1] = Math.min(0, prevNegative * curr);
			} else if (curr < 0) {
				dp[i][0] = Math.max(0, prevNegative * curr);
				dp[i][1] = Math.min(curr, prevPositive * curr);
			} else {
				dp[i][0] = 0;
				dp[i][1] = 0;
			}
		}
		int ans = Integer.MIN_VALUE;
		for (int i = 1; i <= n; i++) {
			ans = Math.max(ans, dp[i][0]);
		}
		return ans;
	}

	// 312. Burst Balloons
	// https://www.youtube.com/watch?v=VFskby7lUbw
	public int maxCoins(int[] nums) {
		int n = nums.length;
		// dp[l][r] 表示从l到r这个区间得分最大值
		int[][] dp = new int[n][n];
		return maxCoinsDFS(nums, 0, n - 1, dp);
	}

	public int maxCoinsDFS(int[] nums, int l, int r, int[][] dp) {
		if (l > r || l < 0 || r >= nums.length) {
			return 0;
		}
		if (dp[l][r] > 0) {
			return dp[l][r];
		}
		// 最后戳破第i个气球
		// dp[l][r] = nums[l-1] * nums[i] * nums[r+1] + dp[i+1][r] + dp[l][i-1]
		int left = l - 1 < 0 ? 1 : nums[l - 1];
		int right = r + 1 >= nums.length ? 1 : nums[r + 1];
		for (int i = l; i <= r; i++) {
			dp[l][r] = Math.max(dp[l][r],
					maxCoinsDFS(nums, l, i - 1, dp) + maxCoinsDFS(nums, i + 1, r, dp) + left * nums[i] * right);
		}
		return dp[l][r];
	}

	// 10. Regular Expression Matching
	// https://www.acwing.com/solution/content/736/
	public static boolean isMatch2(String s, String p) {
		// dp[i][j]表示p从j开始到结尾，是否能匹配s从i开始到结尾
		boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
		dp[s.length()][p.length()] = true;

		for (int i = s.length(); i >= 0; i--) {
			for (int j = p.length() - 1; j >= 0; j--) {
				boolean first_match = (i < s.length() && (p.charAt(j) == s.charAt(i) || p.charAt(j) == '.'));
				if (j + 1 < p.length() && p.charAt(j + 1) == '*') {
					dp[i][j] = dp[i][j + 2] || first_match && dp[i + 1][j];
				} else {
					dp[i][j] = first_match && dp[i + 1][j + 1];
				}
			}
		}
		return dp[0][0];
	}

	// 32. Longest Valid Parentheses
	public int longestValidParentheses(String s) {
		int sum = 0;
		int startIndex = 0;
		int max = 0;
		// left to right scan
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(') {
				sum++;
			} else {
				sum--;
			}
			if (sum == 0) {
				max = Math.max(max, i - startIndex + 1);
			}
			if (sum < 0) {
				sum = 0;
				startIndex = i + 1;
			}
		}

		// right to left scan
		int endIndex = s.length() - 1;
		sum = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			char c = s.charAt(i);
			if (c == ')') {
				sum++;
			} else {
				sum--;
			}
			if (sum == 0) {
				max = Math.max(max, endIndex - i + 1);
			}
			if (sum < 0) {
				sum = 0;
				endIndex = i - 1;
			}
		}
		return max;
	}

	// 329. Longest Increasing Path in a Matrix
	// 做dfs,并用一个cache矩阵记录每个找过的位置的最长链长度避免重复

	// 1235. Maximum Profit in Job Scheduling
	// 根据结束时间对所有工作排序
	// dp[i]表示只考虑前i个工作的情况下，最大的profit是多少
	// 可以用binary search来看在dp[i]的时候，哪个dp[j]可以支持当前的job
	public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
		List<List<Integer>> jobs = new ArrayList<>();

		// storing job's details into one list
		// this will help in sorting the jobs while maintaining the other parameters
		int length = profit.length;
		int[] dp = new int[length];
		for (int i = 0; i < length; i++) {
			ArrayList<Integer> currJob = new ArrayList<>();
			currJob.add(startTime[i]);
			currJob.add(endTime[i]);
			currJob.add(profit[i]);
			jobs.add(currJob);
		}
		jobs.sort(Comparator.comparingInt(a -> a.get(1)));
		dp[0] = jobs.get(0).get(2);
		for (int i = 1; i < length; i++) {
			int currStart = jobs.get(i).get(0);
			int currProfit = jobs.get(i).get(2);
			dp[i] = Math.max(dp[i - 1], currProfit); // 有可能只做当前工作
			int latestPreviousJob = jobSchedulingBS(0, i, jobs, currStart);
			if (jobs.get(latestPreviousJob).get(1) <= currStart) {
				dp[i] = Math.max(dp[i], dp[latestPreviousJob] + currProfit);
			}
		}
		return Arrays.stream(dp).max().getAsInt();
	}

	public int jobSchedulingBS(int l, int r, List<List<Integer>> jobs, int startTime) {
		while (l < r) {
			int mid = l + (r - l) / 2 + 1;
			int currEndTime = jobs.get(mid).get(1);
			if (startTime >= currEndTime) {
				l = mid;
			} else {
				r = mid - 1;
			}
		}
		return r;
	}

	// 1359. Count All Valid Pickup and Delivery Options
	public int countOrders(int n) {
		long[][] dp = new long[n + 1][n + 1];
		int MOD = 1_000_000_007;

		for (int unpicked = 0; unpicked <= n; unpicked++) {
			for (int undelivered = unpicked; undelivered <= n; undelivered++) {
				// If all orders are picked and delivered then,
				// for remaining '0' orders we have only one way.
				if (unpicked == 0 && undelivered == 0) {
					dp[unpicked][undelivered] = 1;
					continue;
				}

				// There are some unpicked elements left.
				// We have choice to pick any one of those orders.
				if (unpicked > 0) {
					dp[unpicked][undelivered] += unpicked * dp[unpicked - 1][undelivered];
				}

				// Number of deliveries done is less than picked orders.
				// We have choice to deliver any one of (undelivered - unpicked) orders.
				if (undelivered > unpicked) {
					dp[unpicked][undelivered] += (undelivered - unpicked) * dp[unpicked][undelivered - 1];
				}
				dp[unpicked][undelivered] %= MOD;
			}
		}
		return (int) dp[n][n];
	}

	// 718. Maximum Length of Repeated Subarray
	public int findLength(int[] A, int[] B) {
		int ans = 0;
		int[][] dp = new int[A.length][B.length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B.length; j++) {
				if (A[i] == B[j]) {
					if (i == 0 || j == 0) {
						dp[i][j] = 1;
					} else {
						dp[i][j] = dp[i - 1][j - 1] + 1;
					}
					if (ans < dp[i][j]) {
						ans = dp[i][j];
					}
				} else {
					dp[i][j] = 0;
				}
			}
		}
		return ans;
	}

	// 1155. Number of Dice Rolls With Target Sum
	public int numRollsToTarget(int n, int k, int target) {
		// dp[i][j] = 扔i次骰子是，总和为j的次数
		int[][] dp = new int[n + 1][target + 1];
		for (int i = 1; i <= Math.min(k, target); i++) {
			dp[1][i] = 1;
		}
		for (int i = 2; i <= n; i++) {
			for (int j = i; j <= target; j++) {
				for (int num = 1; num <= k; num++) {
					if (j - num >= i - 1) {
						dp[i][j] += dp[i - 1][j - num];
						dp[i][j] %= 1_000_000_007;
					}
				}
			}
		}
		return dp[n][target];
	}

	// 688. Knight Probability in Chessboard
	public double knightProbability(int n, int k, int row, int col) {
		// dp[i][j] means after a round, the probablity land on (i, j)
		double[][] dp = new double[n][n];
		int[] dr = new int[] { 2, 2, 1, 1, -1, -1, -2, -2 };
		int[] dc = new int[] { 1, -1, 2, -2, 2, -2, 1, -1 };

		dp[row][col] = 1.0;
		for (int step = 0; step < k; step++) {
			// to save temp result
			double[][] dp2 = new double[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (dp[i][j] > 0) {
						for (int d = 0; d < 8; d++) {
							int cr = i + dr[d];
							int cc = j + dc[d];
							if (0 <= cr && cr < n && 0 <= cc && cc < n) {
								dp2[cr][cc] += dp[i][j] / 8.0;
							}
						}
					}
				}
			}
			dp = dp2;
		}
		double ans = 0.0;
		for (double[] rows : dp) {
			for (double x : rows)
				ans += x;
		}
		return ans;
	}

	// 1937. Maximum Number of Points with Cost
	public long maxPoints(int[][] points) {
		int m = points.length;
		int n = points[0].length;
		// dp[i][j] means when picking i, j, the max value until there
		long[][] dp = new long[m][n];
		for (int j = 0; j < n; j++) {
			dp[0][j] = points[0][j];
		}
		for (int i = 1; i < m; i++) {
			/*
			 * naive solution, time complexity will be O(m*n*n) for(int j = 0; j < n; j++){
			 * for(int previousRowIndex = 0; previousRowIndex < n; previousRowIndex++){
			 * dp[i][j] = Math.max(dp[i][j], dp[i-1][previousRowIndex] + points[i][j] -
			 * Math.abs(previousRowIndex - j)); } }
			 */
			// https://www.youtube.com/watch?v=STEJHYc9rMw
			long[] left = new long[n];
			long[] right = new long[n];
			for (int j = 0; j < n; j++) {
				if (j == 0) {
					left[j] = dp[i - 1][j];
				} else {
					left[j] = Math.max(dp[i - 1][j], left[j - 1] - 1);
				}
			}
			for (int j = n - 1; j >= 0; j--) {
				if (j == n - 1) {
					right[j] = dp[i - 1][j];
				} else {
					right[j] = Math.max(dp[i - 1][j], right[j + 1] - 1);
				}
			}
			for (int j = 0; j < n; j++) {
				dp[i][j] = Math.max(left[j], right[j]) + points[i][j];
			}
		}
		long result = 0;
		for (int j = 0; j < n; j++) {
			result = Math.max(dp[m - 1][j], result);
		}
		return result;
	}

	// 552. Student Attendance Record II
	// dp[i][2][1], means in day i, the status is 2 consecutive L with 1 A before

	// 664. Strange Printer
	public int strangePrinter(String s) {
		int len = s.length();
		int[][] dp = new int[len][len];
		return strangePrinterSection(s.toCharArray(), 0, len - 1, dp);
	}

	public int strangePrinterSection(char[] s, int l, int r, int[][] dp) {
		if (l > r) {
			return 0;
		}
		if (dp[l][r] > 0) {
			return dp[l][r];
		}
		if (l == r) {
			dp[l][r] = 1;
			return 1;
		}
		int ans = Math.min(strangePrinterSection(s, l + 1, r, dp), strangePrinterSection(s, l, r - 1, dp)) + 1;
		for (int i = l; i < r; i++) {
			// 说明i到r肯定可以全部覆盖，所以算出i到r-1即可
			if (s[i] == s[r]) {
				ans = Math.min(ans, strangePrinterSection(s, l, i - 1, dp) + strangePrinterSection(s, i, r - 1, dp));
			}
		}
		dp[l][r] = ans;
		return ans;
	}

	// 1218. Longest Arithmetic Subsequence of Given Difference
	public int longestSubsequence(int[] arr, int diff) {
		int ans = 1;
		// <a, b>, where b is the length of the subsequence ends at a
		Map<Integer, Integer> dpMap = new HashMap<>();
		dpMap.put(arr[0], 1);
		for (int i = 1; i < arr.length; i++) {
			int len = dpMap.getOrDefault(arr[i] - diff, 0) + 1;
			dpMap.put(arr[i], len);
			ans = Math.max(ans, len);
		}
		return ans;
	}

	// 1696. Jump Game VI
	public int maxResult(int[] nums, int k) {
		int n = nums.length;
		int[] dp = new int[n]; // max score when get at position i
		dp[0] = nums[0];
		int score = nums[0];
		Deque<int[]> deque = new ArrayDeque<>(); // store i-k 到 i-1 的单调递减队列，头为最大值
		deque.offerLast(new int[] { 0, score });
		for (int i = 1; i < n; i++) {
			// naive做法，这部分就是sliding window max，可以优化，否则会超时
			// int max = Integer.MIN_VALUE;
			// for(int j = Math.max(0, i - k); j <= i - 1; j++){
			// max = Math.max(max, dp[j]);
			// }
			while (!deque.isEmpty() && deque.peekFirst()[0] < i - k) {
				deque.removeFirst();
			}
			score = deque.peek()[1] + nums[i];
			while (!deque.isEmpty() && deque.peekLast()[1] <= score) {
				deque.removeLast();
			}
			deque.offerLast(new int[] { i, score });
			dp[i] = score;
		}
		return dp[n - 1];
	}

	// 256. Paint House
	public int minCost(int[][] costs) {
		int houseCount = costs.length;
		// only 3 colors, dp[i][j] means min cost of paint until house i, with color j
		int[][] dp = new int[houseCount][3];
		for (int i = 0; i < houseCount; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == 0) {
					dp[i][j] = costs[i][j];
					continue;
				}
				if (j == 0)
					dp[i][j] = Math.min(dp[i - 1][1], dp[i - 1][2]) + costs[i][0];
				else if (j == 1)
					dp[i][j] = Math.min(dp[i - 1][0], dp[i - 1][2]) + costs[i][1];
				else
					dp[i][j] = Math.min(dp[i - 1][0], dp[i - 1][1]) + costs[i][2];
			}
		}
		int ans = Math.min(dp[houseCount - 1][0], dp[houseCount - 1][1]);
		ans = Math.min(ans, dp[houseCount - 1][2]);
		return ans;
	}

	// 516. Longest Palindromic Subsequence
	public int longestPalindromeSubseq(String s) {
		int n = s.length();
		// dp[i][j]: the longest palindromic subsequence's length of substring(i, j),
		// here i, j represent left, right indexes in the string
		int[][] dp = new int[n][n];

		for (int len = 1; len <= n; len++) {
			for (int i = 0; i < n; i++) {
				int j = i + len - 1;
				if (j >= n) {
					continue;
				}
				// base case
				if (len == 1) {
					dp[i][j] = 1;
					continue;
				}
				if (len == 2) {
					if (s.charAt(i) == s.charAt(j)) {
						dp[i][j] = 2;
					} else {
						dp[i][j] = 1;
					}
					continue;
				}
				// general case
				if (s.charAt(i) == s.charAt(j)) {
					dp[i][j] = dp[i + 1][j - 1] + 2;
				} else {
					dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
				}
			}
		}
		return dp[0][n - 1];
	}

	// 1312. Minimum Insertion Steps to Make a String Palindrome
	public int minInsertions(String s) {
		int n = s.length();
		int[][] dp = new int[n][n];
		for (int len = 2; len <= n; len++) {
			for (int i = 0; i < n; i++) {
				int j = i + len - 1;
				if (j >= n) {
					continue;
				}
				// base case
				if (len == 2) {
					if (s.charAt(i) == s.charAt(j)) {
						dp[i][j] = 0;
					} else {
						dp[i][j] = 1;
					}
				}

				if (s.charAt(i) == s.charAt(j)) {
					dp[i][j] = dp[i + 1][j - 1];
				} else {
					dp[i][j] = Math.min(dp[i + 1][j], dp[i][j - 1]) + 1;
				}
			}
		}
		return dp[0][n - 1];
	}

	// 730. Count Different Palindromic Subsequences
	// https://leetcode.com/problems/count-different-palindromic-subsequences/discuss/109509/Accepted-Java-Solution-using-memoization
	// it will first check the string [a...a, b...b, c...c, d...d], then goes to
	// next level.
	// For a...a in next level, it will check [aa...a, ab...a, ac...a, ad...a].
	public int countPalindromicSubsequences(String S) {
		TreeSet<Integer>[] characters = (TreeSet<Integer>[]) new TreeSet[26];
		int len = S.length();

		for (int i = 0; i < 26; i++)
			characters[i] = new TreeSet<Integer>();

		for (int i = 0; i < len; ++i) {
			int c = S.charAt(i) - 'a';
			characters[c].add(i);
		}
		Integer[][] dp = new Integer[len + 1][len + 1];
		return memo(characters, dp, 0, len);
	}

	public int memo(TreeSet<Integer>[] characters, Integer[][] dp, int start, int end) {
		int div = 1000000007;
		if (start >= end)
			return 0;
		if (dp[start][end] != null)
			return dp[start][end];

		long ans = 0;

		for (int i = 0; i < 26; i++) {
			Integer new_start = characters[i].ceiling(start);
			Integer new_end = characters[i].lower(end);
			if (new_start == null || new_start >= end)
				continue;
			// means we at least get a single char palindrome like "a"
			ans++;
			if (new_start != new_end)
				// means we at least get double char palindrome like "aa"
				ans++;
			ans += memo(characters, dp, new_start + 1, new_end);

		}
		dp[start][end] = (int) (ans % div);
		return dp[start][end];
	}
}
