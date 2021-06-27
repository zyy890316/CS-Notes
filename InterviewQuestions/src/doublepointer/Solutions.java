package doublepointer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solutions {
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
	// i指向数组当前排好的位置
	public int removeDuplicates(int[] nums) {
		if (nums.length < 2)
			return nums.length;
		int i = 1;
		int j = 1;
		while (j < nums.length && i < nums.length) {
			if (nums[i] > nums[i - 1]) {
				i++;
				j++;
			} else {
				if (nums[j] > nums[i - 1]) {
					nums[i] = nums[j];
				} else {
					j++;
				}
			}
		}
		return i;
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
}
