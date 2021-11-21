
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import linkedlist.ListNode;

public class DoublePointers {
	public static void main(String[] args) {
		maxArea(new int[] { 1, 8, 6, 2, 5, 4, 8, 3, 7 });
		trapDP(new int[] { 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1 });
		minWindow("ADOBECODEBANC", "ABC");
	}

	// 反转string
	// https://leetcode.com/problems/reverse-string
	public char[] reverseString(char[] str) {
		// initialize
		int i = 0;
		int j = str.length - 1;
		// two pointers opposite direction
		while (i < j) {
			// swap str[i] and str[j]
			char tmp = str[i];
			str[i] = str[j];
			str[j] = tmp;
			i++;
			j--;
		}
		return str;
	}

	// 数组去除重复元素
	// https://leetcode.com/problems/remove-duplicates-from-sorted-array
	// slow指向数组当前排好的位置
	public int removeDuplicates(int[] nums) {
		if (nums.length < 2)
			return nums.length;
		int slow = 1;
		int fast = 1;
		while (fast < nums.length) {
			if (nums[slow] > nums[slow - 1]) {
				slow++;
				fast++;
				continue;
			} else if (nums[fast] > nums[slow - 1]) {
				nums[slow] = nums[fast];
				slow++;
				fast++;
				continue;
			}
			fast++;
		}
		return slow;
	}

	// Remove Duplicates From Sorted Array II (80)
	// 已经排序的数组，重复元素最多出现2次
	// https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
	// slow的意义:第一个意义是下一个valid的number应该被填在slow的位置
	// 第二个意义是slow以前的array是一个符合题意的valid的array，
	// 所以对于每个fast指向的number，我们只需要和在slow-2这个位置的数对比，如果相同，则此数重复，移动fast到下一位；
	// 如果不同，则此数valid，需要填在slow这个index里，然后同时移动slow和fast
	public int removeDuplicatesII(int[] nums) {
		if (nums.length <= 2)
			return nums.length;
		int slow = 2;
		int fast = 2;
		while (fast < nums.length) {
			if (isValid(nums, fast)) {
				nums[slow] = nums[fast];
				slow++;
				fast++;
				continue;
			}
			if (isValid(nums, slow)) {
				slow++;
				fast++;
				continue;
			}
			fast++;
		}
		return slow;
	}

	public boolean isValid(int[] nums, int index) {
		return ((nums[index] != nums[index - 1] || nums[index] != nums[index - 2]) && nums[index] >= nums[index - 1]
				&& nums[index] >= nums[index - 2]);
	}

	// 有序数组的 Two Sum
	// https://leetcode.com/problems/two-sum-ii-input-array-is-sorted
	public int[] twoSum(int[] numbers, int target) {
		if (numbers == null)
			return null;
		int i = 0;
		int j = numbers.length - 1;
		while (i < j) {
			if (numbers[i] + numbers[j] == target) {
				return new int[] { i + 1, j + 1 };
			}
			if (numbers[i] + numbers[j] < target) {
				i++;
			} else {
				j--;
			}
		}
		return new int[0];
	}

	// 两数平方和
	// 本题的关键是右指针的初始化，实现剪枝，从而降低时间复杂度。设右指针为 x，左指针固定为 0，为了使 02 + x2 的值尽可能接近 target
	// 我们可以将 x 取为 sqrt(target)。
	// https://leetcode.com/problems/sum-of-square-numbers/description/
	public boolean judgeSquareSum(int target) {
		if (target < 0)
			return false;
		int i = 0, j = (int) Math.ceil(Math.sqrt(target));
		while (i <= j) {
			int powSum = i * i + j * j;
			if (powSum == target) {
				return true;
			} else if (powSum > target) {
				j--;
			} else {
				i++;
			}
		}
		return false;
	}

	// 反转字符串中的元音字符
	// https://leetcode.com/problems/reverse-vowels-of-a-string/description/
	public String reverseVowels(String s) {
		final Set<Character> vowels = new HashSet<Character>(
				Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));
		if (s == null)
			return null;
		int i = 0, j = s.length() - 1;
		char[] result = new char[s.length()];
		while (i <= j) {
			char ci = s.charAt(i);
			char cj = s.charAt(j);
			if (!vowels.contains(ci)) {
				result[i++] = ci;
			} else if (!vowels.contains(cj)) {
				result[j--] = cj;
			} else {
				result[i++] = cj;
				result[j--] = ci;
			}
		}
		return new String(result);
	}

	// 可以删除一个字符，判断是否能构成回文字符串。
	// 本题的关键是处理删除一个字符。在使用双指针遍历字符串时，如果出现两个指针指向的字符不相等的情况
	// 我们就试着删除一个字符，再判断删除完之后的字符串是否是回文字符串。
	// https://leetcode.com/problems/valid-palindrome-ii/description/
	public boolean validPalindrome(String s) {
		int i = 0;
		int j = s.length() - 1;
		while (i <= j) {
			if (s.charAt(i) == s.charAt(j)) {
				i++;
				j--;
			} else {
				return isPalindrome(s, i, j - 1) || isPalindrome(s, i + 1, j);
			}
		}
		return true;
	}

	private boolean isPalindrome(String s, int i, int j) {
		while (i < j) {
			if (s.charAt(i++) != s.charAt(j--)) {
				return false;
			}
		}
		return true;
	}

	// 归并两个有序数组，把归并结果存到第一个数组上
	// 需要从尾开始遍历，否则在 nums1 上归并得到的值会覆盖还未进行归并比较的值。
	// https://leetcode.com/problems/merge-sorted-array/description/
	public void merge(int[] nums1, int m, int[] nums2, int n) {
		int index1 = m - 1, index2 = n - 1;
		int indexMerge = m + n - 1;
		while (indexMerge >= 0) {
			if (index1 < 0) {
				nums1[indexMerge--] = nums2[index2--];
			} else if (index2 < 0) {
				break;
			} else if (nums1[index1] > nums2[index2]) {
				// nums1 中因为后面插入了0来补充数组长度，所以改变元素需要交换
				int temp = nums1[index1];
				nums1[index1] = nums1[indexMerge];
				nums1[indexMerge] = temp;
				indexMerge--;
				index1--;
			} else {
				nums1[indexMerge--] = nums2[index2--];
			}
		}
	}

	// 判断链表是否存在环
	// 使用双指针，一个指针每次移动一个节点，一个指针每次移动两个节点，如果存在环，那么这两个指针一定会相遇。
	// https://leetcode.com/problems/linked-list-cycle/description/
	public boolean hasCycle(ListNode head) {
		if (head == null) {
			return false;
		}
		ListNode l1 = head, l2 = head.next;
		while (l1 != null && l2 != null && l2.next != null) {
			if (l1 == l2) {
				return true;
			}
			l1 = l1.next;
			l2 = l2.next.next;
		}
		return false;
	}

	// 最长子序列：删除 s 中的一些字符，使得它构成字符串列表 d 中的一个字符串，找出能构成的最长字符串。如果有多个相同长度的结果，返回字典序的最小字符串
	// 通过删除字符串 s 中的一个字符能得到字符串 t，可以认为 t 是 s 的子序列，我们可以使用双指针来判断一个字符串是否为另一个字符串的子序列。
	// https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/description/
	public String findLongestWord(String s, List<String> dictionary) {
		String longestWord = "";
		for (String target : dictionary) {
			int l1 = longestWord.length(), l2 = target.length();
			if (l1 > l2 || (l1 == l2 && longestWord.compareTo(target) < 0)) {
				continue;
			}
			if (isSubsequence(s, target)) {
				longestWord = target;
			}
		}
		return longestWord;
	}

	private boolean isSubsequence(String s, String target) {
		if (s.length() < target.length()) {
			return false;
		}
		int i = 0;
		int j = 0;
		while (j < target.length() && i < s.length()) {
			if (s.charAt(i) == target.charAt(j)) {
				i++;
				j++;
			} else {
				i++;
			}
		}
		return j == target.length();
	}

	// Container With Most Water (11)
	// 左右指针指向数组两端，遇到高度比之前低的，肯定容积会变小，跳过，并把较低的板子向内侧移动来查找更高的
	// https://leetcode.com/problems/container-with-most-water
	public static int maxArea(int[] height) {
		int max = 0;
		int maxHeight = 0;
		int i = 0;
		int j = height.length - 1;
		while (i < j) {
			int currHeight = Math.min(height[i], height[j]);
			if (maxHeight >= currHeight) {
				if (height[i] <= height[j]) {
					i++;
				} else {
					j--;
				}
				continue;
			}
			int currArea = currHeight * (j - i);
			if (currArea > max) {
				max = currArea;
				maxHeight = currHeight;
			}

			if (height[i] <= height[j]) {
				i++;
			} else {
				j--;
			}
		}
		return max;
	}

	// Trapping Rain Water (42)
	// https://leetcode.com/problems/trapping-rain-water/solution/
	// https://www.youtube.com/watch?v=StH5vntauyQ
	// 双指针：先求左右边界，然后左右指针循环
	// 如果左边最大值小，左边现在的节点能积多少水只取决于左边最大值，
	// 同理，如果右边最大值小，右边现在的节点能积多少水只取决于右边最大值
	public int trap(int[] height) {
		int leftBound = 0;
		int rightBound = height.length - 1;
		while (leftBound < height.length - 1) {
			if (height[leftBound] > 0) {
				break;
			}
			leftBound++;
		}
		while (rightBound > 0) {
			if (height[rightBound] > 0 && height[rightBound] > height[rightBound - 1]) {
				break;
			}
			rightBound--;
		}
		if (leftBound >= rightBound - 1) {
			return 0;
		}
		int i = leftBound;
		int j = rightBound;
		int leftMax = height[leftBound];
		int rightMax = height[rightBound];
		int rain = 0;
		while (i < j) {
			if (leftMax <= rightMax) {
				rain += leftMax - height[i];
				i++;
				leftMax = Math.max(leftMax, height[i]);
			} else {
				rain += rightMax - height[j];
				j--;
				rightMax = Math.max(rightMax, height[j]);
			}
		}
		return rain;
	}

	// DP: 基于brutal force方法
	// 最简单的想法：当前格子能积多少水，只需要查看左右两边最高的板子即可
	// 故记录下两个数组，分别存储从左和从右开始算的最大值，然后只需遍历即可
	public static int trapDP(int[] height) {
		int[] leftMax = new int[height.length];
		int[] rightMax = new int[height.length];
		int max = 0;
		for (int i = 0; i < height.length; i++) {
			max = Math.max(max, height[i]);
			leftMax[i] = max;
		}
		max = 0;
		for (int i = height.length - 1; i >= 0; i--) {
			max = Math.max(max, height[i]);
			rightMax[i] = max;
		}

		int rain = 0;
		for (int i = 0; i < height.length; i++) {
			int leftBar = leftMax[i];
			int rightBar = rightMax[i];
			int currBucketHeight = Math.min(leftBar, rightBar);
			if (currBucketHeight > height[i]) {
				rain += currBucketHeight - height[i];
			}
		}
		return rain;
	}

	// 283. Move Zeroes
	// 含0已经排序的数组，要求把非0数字移到队列最后
	// 双指针分别指向最左的非零和零元素位置，不停交换

	// LeetCode 30. Substring with Concatenation of All Words | 串联所有单词的子串
	// https://www.bilibili.com/video/BV1up4y1h7AC

	// 76. Minimum Window Substring
	public static String minWindow(String s, String t) {
		int m = s.length();
		int n = t.length();
		String ans = "";
		int minLength = Integer.MAX_VALUE;
		Map<Character, Integer> target = new HashMap<>();
		// remaining表示还需要多少个char才能满足条件
		int remaining = n;
		for (char c : t.toCharArray()) {
			target.put(c, target.getOrDefault(c, 0) + 1);
		}
		// i指向当前区间的头，j指向尾
		int i = 0;
		int j = 0;
		// 如果移动了j，说明上一个循环新加入了一个char，需要用这个flag判断一下加入之后是否remaining会有变化
		boolean newCharAddws = true;
		while (j < m) {
			System.out.println("i:" + i);
			System.out.println("j:" + j);
			char c = s.charAt(j);
			if (target.containsKey(c) && newCharAddws) {
				int count = target.get(c);
				if (count > 0) {
					remaining--;
				}
				target.put(c, count - 1);
			}

			if (remaining == 0) {
				int length = j - i;
				if (length < minLength) {
					ans = s.substring(i, j + 1);
					minLength = length;
				}
				char first = s.charAt(i);
				if (target.containsKey(first)) {
					target.put(first, target.get(first) + 1);
					if (target.get(first) > 0) {
						remaining++;
					}
				}
				i++;
				newCharAddws = false;
			} else {
				j++;
				newCharAddws = true;
			}
			System.out.println("remaining:" + remaining);

		}
		return ans;
	}
}
