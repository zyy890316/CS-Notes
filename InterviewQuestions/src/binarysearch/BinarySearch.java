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

	public static void main(String[] args) {
		mySqrt(6);
		singleNonDuplicate(new int[] { 3, 3, 7, 7, 10, 11, 11 });
	}

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

	// 69 求开方
	// 对于 x = 8，它的开方是 2.82842...，最后应该返回 2 而不是 3。在循环条件为l <= h且循环退出时，h 总是比 l 小1,
	// 也就是说 h = 2，l = 3，因此最后的返回值应该为 h 而不是 l。
	public static int mySqrt(int x) {
		if (x <= 1) {
			return x;
		}
		int low = 1;
		int high = x;
		while (low <= high) {
			int mid = low + (high - low) / 2;
			int sqrt = x / mid;
			if (sqrt < mid) {
				high = mid - 1;
			} else if (sqrt == mid) {
				return mid;
			} else {
				low = mid + 1;
			}
		}
		return high;
	}

	// 744 大于给定元素的最小元素:给定一个有序的字符数组 letters 和一个字符 target，要求找出 letters 中大于 target
	// 的最小字符，如果找不到就返回第 1 个字符。
	public char nextGreatestLetter(char[] letters, char target) {
		int low = 0;
		int high = letters.length - 1;
		if (letters[high] < target)
			return letters[0];
		while (low <= high) {
			int mid = low + (high - low + 1) / 2;
			char ch = letters[mid];
			if (ch <= target) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return low < letters.length ? letters[low] : letters[0];
	}

	// 540 有序数组的 Single Element: 一个有序数组只有一个数不出现两次，找出这个数。
	// 该数组只有一个数字单独出现，总数必为奇数，所以如果当前元素重复，且他之前的总数为偶数，单独元素肯定在后面
	public static int singleNonDuplicate(int[] nums) {
		int low = 0;
		int high = nums.length - 1;
		while (low <= high) {
			int mid = low + (high - low) / 2;
			if (nums[mid - 1] != nums[mid] && nums[mid + 1] != nums[mid]) {
				return nums[mid];
			}
			int indexPair = mid;
			if (nums[mid + 1] == nums[mid]) {
				indexPair = mid + 1;
			}
			if (indexPair % 2 == 1) {
				low = indexPair + 1;
			} else {
				high = indexPair - 1;
			}
		}
		return nums[low];
	}
}
