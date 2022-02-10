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
}
