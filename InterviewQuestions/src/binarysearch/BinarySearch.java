package binarysearch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BinarySearch {
	// https://www.geeksforgeeks.org/variants-of-binary-search/
	// while 永远是 while(left <= right)
	// 精确搜索，是否contain：mid = low + (high - low) / 2;
	// 模糊搜素：第一次或最后一次occurrence/最接近某个值 mid = low + (high - low + 1) / 2;
	// 永远是 low = middle + 1 和 high = middle - 1

	// 1062. Longest Repeating substring
	// https://www.youtube.com/watch?v=j2_JW3In9PE
	// 把有重复的substring长度作为search对象
	public int longestRepeatingSubsting(String s) {
		int low = 0;
		int high = s.length() - 1;
		while (low <= high) {
			int mid = low + (high - low + 1) / 2;
			boolean found = haveRepeatingSubstringOfLength(s, mid);
			if (found) {
				low = mid;
			} else {
				high = mid - 1;
			}
		}
		return low;
	}

	private boolean haveRepeatingSubstringOfLength(String s, int length) {
		Set<String> seen = new HashSet<String>();
		for (int i = 0; i <= s.length() - length; i++) {
			String currS = s.substring(i, i + length);
			if (seen.contains(s)) {
				return true;
			}
			seen.add(currS);
		}
		return false;
	}

	// 410. Split Array Largest Sum
	// https://leetcode.com/problems/split-array-largest-sum/
	public int splitArray(int[] nums, int m) {
		int low = Arrays.stream(nums).max().getAsInt();
		int sum = Arrays.stream(nums).sum();
		int high = sum;
		while (low <= high) {
			int mid = low + (high - low + 1) / 2;
			int pieces = splitWithSum(nums, mid);
			if (pieces > m) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return low;
	}

	private int splitWithSum(int[] nums, int sum) {
		int count = 1;
		int currSum = 0;
		for (int i = 0; i < nums.length; i++) {
			if (currSum + nums[i] > sum) {
				count++;
				currSum = nums[i];
			} else {
				currSum += nums[i];
			}
		}
		return count;
	}

	// 852. Peak Index in a Mountain Array
	// https://leetcode.com/problems/peak-index-in-a-mountain-array/

	// 1011. Capacity To Ship Packages Within D Days
	// https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/

	// 1292. Maximum Side Length of a Square with Sum Less than or Equal to
	// Threshold
	// https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/
}
