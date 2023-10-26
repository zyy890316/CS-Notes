
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BinarySearch {
	// https://www.acwing.com/blog/content/31/
	// while 永远是 while(l < f)
	// 两者都是通过缩小区间，当l == r时 ， 找到目标值。
	// 两种分法都是追求在l = r - 1时，仍能继续缩小范围：
	// [l, r]划分成[l, mid]和[mid + 1, r], mid = l + r >> 1
	// [l, r]划分成[l, mid - 1]和[mid, r]，mid = l + r + 1 >> 1
	// return l;

	// 如果l r可以为负值，则需另外模板：
	// while (l <= r)
	// mid = (l + r) / 2
	// l r 分别取 mid + 1 和 mid - 1

	public static void main(String[] args) {
		kthSmallestProduct(new int[] { -4, -2, 0, 3 }, new int[] { 2, 4 }, 6);
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
			if (seen.contains(currS)) {
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
	public int mySqrt(int x) {
		if (x <= 1) {
			return x;
		}
		int low = 1;
		int high = x;
		while (low < high) {
			int mid = low + (high - low + 1) / 2;
			if ((long) mid * mid < x) {
				low = mid;
			} else if (mid * mid == x) {
				return mid;
			} else {
				high = mid - 1;
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
	public int singleNonDuplicate(int[] nums) {
		int low = 0;
		int high = nums.length - 1;
		while (low < high) {
			int mid = low + (high - low) / 2;
			if (mid % 2 == 1) {
				mid--; // 保证 mid 在偶数位
			}
			if (nums[mid + 1] == nums[mid]) {
				low = mid + 2;
			} else {
				// 因为 h 的赋值表达式为 h = m，那么循环条件也就只能使用 l < h 这种形式。
				high = mid;
			}
		}
		return nums[low];
	}

	// 153 旋转有序数组的最小数字
	public int findMin(int[] nums) {
		int low = 0;
		int high = nums.length - 1;
		int start = nums[0];
		if (start < nums[high]) {
			// 说明旋转完回到了原来的有序数组
			return nums[0];
		}
		while (low < high) {
			int mid = low + (high - low) / 2;
			if (nums[mid] >= start) {
				low = mid + 1;
			} else {
				high = mid;
			}
		}
		return nums[low];
	}

	// 34 查找区间:给定一个有序数组 nums 和一个目标 target，要求找到 target 在 nums 中的第一个位置和最后一个位置
	public int[] searchRange(int[] nums, int target) {
		if (nums.length == 0) {
			return new int[] { -1, -1 };
		}
		int start = 0;
		int end = 0;
		int low = 0;
		int high = nums.length - 1;
		while (low < high) {
			int mid = low + (high - low) / 2;
			if (nums[mid] < target) {
				low = mid + 1;
			} else {
				high = mid;
			}
		}
		if (nums[low] != target) {
			return new int[] { -1, -1 };
		}
		start = low;

		high = nums.length - 1;
		while (low <= high) {
			int mid = low + (high - low + 1) / 2;
			if (nums[mid] == target) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		end = high;

		return new int[] { start, end };
	}

	// 找出数组中重复的数，数组值在 [1, n] 之间
	// 利用规律：如果没有重复数字，那么小于等于x的元素个数应为x
	// https://www.youtube.com/watch?v=u_gg0uVZdsE
	public int findDuplicate(int[] nums) {
		int n = nums.length;
		int l = 1;
		int r = n - 1;
		int ans = -1;

		while (l <= r) {
			int mid = (r + l) / 2;
			int count = 0;
			for (int num : nums) {
				count += num <= mid ? 1 : 0;
			}
			if (count > mid) {
				r = mid - 1;
				ans = mid;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	// 2040. Kth Smallest Product of Two Sorted Arrays
	public static long kthSmallestProduct(int[] nums1, int[] nums2, long k) {
		int m = nums1.length;
		int n = nums2.length;
		// Do binary search on product value range
		long l = Math.min(Math.min((long) nums1[m - 1] * nums2[n - 1], (long) nums1[0] * nums2[0]),
				Math.min((long) nums1[0] * nums2[n - 1], (long) nums2[0] * nums1[m - 1]));
		long r = Math.max(Math.max((long) nums1[m - 1] * nums2[n - 1], (long) nums1[0] * nums2[0]),
				Math.max((long) nums1[0] * nums2[n - 1], (long) nums2[0] * nums1[m - 1]));
		long res = 0;
		// l can be negative, so the l < r template have loop at l=-1, r=0
		while (l <= r) {
			long mid = (l + r) / 2;
			// checking whether we have k at least k elements whose product is less than the
			// current product
			// if yes then we know we have to move left
			if (check(mid, nums1, nums2, k)) {
				res = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return res;
	}

	public static boolean check(long mid, int[] nums1, int[] nums2, long k) {
		long cnt = 0;
		for (int i = 0; i < nums1.length; i++) {
			long val = (long) nums1[i];
			// if current element is 0 and we out current product is >= 0 then we can add
			// the whole second array because 0*anything will be 0
			if (val == 0 && mid >= 0) {
				cnt += nums2.length;
			} else if (val < 0) {
				// If we encounter negative value we find the minimum index in the second array
				// such that this element * minIndexElement <= current product
				cnt += findMinIndex(val, mid, nums2);
			} else if (val > 0) {
				// if we encounter positive element we find max index in the second array such
				// that this element * maxIndexElement <= current product
				cnt += findMaxIndex(val, mid, nums2);
			}
		}
		// if we have atleast k elements whose product is less than current product
		return cnt >= k;
	}

	public static long findMaxIndex(long val, long mid, int[] nums2) {
		int l = 0;
		int r = nums2.length - 1;
		// when no element in second array when multiplied by the current element of the
		// first array yields result less that current product we will return res+1
		// hence -1 + 1 hence 0 coz there are no elements which when multiplied by
		// current element of the first array yield result less than current product.
		long res = -1;
		while (l <= r) {
			int m = (l + r) / 2;
			if (val * nums2[m] <= mid) {
				// if our current product is >= val*current element then we move the window to
				// right side in order to find max index with which when multiplied the element
				// of the first array is still less than current product.
				res = (long) m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		// so we have found res+1 elements who satisfy our condition. +1 coz res would
		// give index
		return res + 1;
	}

	public static long findMinIndex(long val, long mid, int[] nums2) {
		int l = 0;
		int r = nums2.length - 1;
		// when all elements of second array multiplied by current element of first
		// array yield result > current product then we return 0 coz there are no
		// elements which would yield us result.
		long res = r + 1;
		while (l <= r) {
			int m = (l + r) / 2;
			if (val * nums2[m] <= mid) {
				r = m - 1;
				res = (long) m;
			} else {
				l = m + 1;
			}
		}
		return nums2.length - res;
	}
}
