import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Array {
	// 560. Subarray Sum Equals K
	// Given an array of integers nums and an integer k, return the total number of
	// continuous subarrays whose sum equals to k.
	public int subarraySum(int[] nums, int k) {
		int[] prefixSum = new int[nums.length];
		Map<Integer, Integer> frequency = new HashMap<>();
		// start the frequency as 0 already appear once
		frequency.put(0, 1);
		int sum = 0;
		int result = 0;
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			if (frequency.containsKey(sum - k)) {
				result += frequency.get(sum - k);
			}
			prefixSum[i] = sum;
			frequency.put(sum, frequency.getOrDefault(sum, 0) + 1);
		}
		return result;
	}

	// 523. Continuous Subarray Sum, 求是否有连续元素，和为k的整数倍
	// 如果从i到j的和为k的整数倍，说明sum[i] % k = sum[j] % k
	public boolean checkSubarraySum(int[] nums, int k) {
		int[] prefixSum = new int[nums.length];
		// key为该点前缀和与k取模，value为该点index
		Map<Integer, Integer> position = new HashMap<>();
		position.put(0, -1);
		int sum = 0;
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			int mod = sum % k;
			if (position.containsKey(mod) && (i - position.get(mod) > 1)) {
				return true;
			}
			prefixSum[i] = sum;
			position.putIfAbsent(mod, i);
		}
		return false;
	}

	// 使得数组相邻差值的个数为k
	// https://leetcode.com/problems/beautiful-arrangement-ii/submissions/
	public int[] constructArray(int n, int k) {
		int[] ans = new int[n];
		// 前n-k元素差值均为1
		for (int i = 0; i < n - k; i++) {
			ans[i] = i + 1;
		}

		// 后k个元素一大一小排列
		for (int i = 0; i < k; i++) {
			if (i % 2 == 0) {
				ans[n - k + i] = n - i / 2;
			} else {
				ans[n - k + i] = n - k + (i / 2 + 1);
			}
		}
		return ans;
	}

	// 数组的度：[1,2,2,3,1,4,2]
	// 数组的度定义为元素出现的最高频率，例如上面的数组度为 3。要求找到一个最小的子数组，这个子数组的度和原数组一样。
	public int findShortestSubArray(int[] nums) {
		Map<Integer, NodeInfo> map = new HashMap<Integer, NodeInfo>();
		int maxCount = Integer.MIN_VALUE;
		int maxNumber = Integer.MIN_VALUE;
		for (int i = 0; i < nums.length; i++) {
			NodeInfo info = null;
			if (map.containsKey(nums[i])) {
				info = map.get(nums[i]);
				info.endIndex = i;
				info.count += 1;
				map.put(nums[i], info);
			} else {
				info = new NodeInfo(nums[i], i);
				map.put(nums[i], info);
			}

			if (info.count > maxCount) {
				maxCount = info.count;
				maxNumber = nums[i];
			} else if (info.count == maxCount) {
				NodeInfo currMax = map.get(maxNumber);
				if (info.endIndex - info.startIndex < currMax.endIndex - currMax.startIndex) {
					maxCount = info.count;
					maxNumber = nums[i];
				}
			}
		}
		NodeInfo maxNode = map.get(maxNumber);
		return maxNode.endIndex - maxNode.startIndex + 1;
	}

	public class NodeInfo {
		int val;
		int startIndex;
		int endIndex;
		int count;

		public NodeInfo(int val, int startIndex, int endIndex, int count) {
			this.val = val;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.count = count;
		}

		public NodeInfo(int val, int startIndex) {
			this.val = val;
			this.startIndex = startIndex;
			this.endIndex = startIndex;
			this.count = 1;
		}
	}

	// 嵌套数组
	// 题目描述：S[i] 表示一个集合，集合的第一个元素是 A[i]，第二个元素是 A[A[i]]，如此嵌套下去。求最大的 S[i]。
	// https://leetcode.com/problems/array-nesting/solution/
	// 每个起始点进行嵌套，最终会形成一个环，回到起始点位（数组中无重复元素），环上所有点位长度均为环的长度。
	public int arrayNesting(int[] nums) {
		int maxCount = -1;
		Boolean[] visited = new Boolean[nums.length];
		Arrays.fill(visited, Boolean.FALSE);
		for (int i = 0; i < nums.length; i++) {
			int j = i;
			if (visited[j])
				continue;
			int count = 1;
			visited[j] = Boolean.TRUE;
			while (true) {
				int currNum = nums[j];
				if (visited[currNum]) {
					break;
				}
				visited[currNum] = true;
				j = currNum;
				count++;
			}
			maxCount = Math.max(maxCount, count);
		}
		return maxCount;
	}

	// 分隔数组，使得对每部分排序后数组就为有序，求最大分割段数
	// https://www.youtube.com/watch?v=twYLu4hEKnQ
	// 只要i元素以前最大值为i，说明从i开始分割没问题，有效分割数+1
	public int maxChunksToSorted(int[] arr) {
		int maxChunks = 0;
		int currMax = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			currMax = Math.max(currMax, arr[i]);
			if (currMax == i) {
				maxChunks++;
			}
		}
		return maxChunks;
	}

	// 135. Candy
	// 给数组里每个孩子糖果，最少给一个，index高的需要比相邻的拿得多，问最少需要多少糖果
	// 本质上就是看上升和下降区间分别多长，可以两个数组，一个left2rigth，一个right2left
	// 分别从两边扫描，都只关注上升区间，不上升的就设为1，最后两个数组去最大值得出结果数组
}
