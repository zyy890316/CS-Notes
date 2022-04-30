package string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20字符串.md
public class StringSolutions {
	public static void main(String[] args) {
		moveRight("abcd123", 3);
		reverseWords("I am a student");
		groupAnagrams(new String[] { "eat", "tea", "tan", "ate", "nat", "bat" });
	}

	// 字符串循环移位, 将字符串向右循环移动 k 位
	// s = "abcd123" k = 3, Return "123abcd"
	// 将 abcd123 中的 abcd 和 123 单独翻转，得到 dcba321，然后对整个字符串进行翻转，得到 123abcd。
	public static String moveRight(String str, int k) {
		String head = new StringBuilder(str.substring(0, str.length() - k)).reverse().toString();
		String tail = new StringBuilder(str.substring(str.length() - k, str.length())).reverse().toString();

		String result = head + tail;
		return new StringBuilder(result).reverse().toString();
	}

	// 字符串中单词的翻转
	// s = "I am a student"
	// Return "student a am I"
	public static String reverseWords(String words) {
		String[] splits = words.split(" ");
		Collections.reverse(Arrays.asList(splits));
		String result = String.join(" ", splits);
		return result;
	}

	// 两个字符串包含的字符是否完全相同
	// 由于本题的字符串只包含 26 个小写字符，因此可以使用长度为 26 的整型数组对字符串出现的字符进行统计，不再使用 HashMap
	public boolean isAnagram(String s, String t) {
		int[] counts = new int[26];
		for (char c : s.toCharArray()) {
			counts[c - 'a']++;
		}
		for (char c : t.toCharArray()) {
			counts[c - 'a']--;
		}

		for (int i : counts) {
			if (i != 0)
				return false;
		}
		return true;
	}

	// 计算一组字符集合可以组成的回文字符串的最大长度
	public int longestPalindromeInString(String s) {
		int[] cnts = new int[256];
		for (char c : s.toCharArray()) {
			cnts[c]++;
		}
		int palindrome = 0;
		for (int cnt : cnts) {
			palindrome += (cnt / 2) * 2;
		}
		if (palindrome < s.length()) {
			palindrome++; // 这个条件下 s 中一定有单个未使用的字符存在，可以把这个字符放到回文的最中间
		}
		return palindrome;
	}

	// 字符串同构
	// 记录一个字符上次出现的位置，如果两个字符串中的字符上次出现的位置一样，那么就属于同构。
	public boolean isIsomorphic(String s, String t) {
		int[] sPreIndex = new int[256];
		int[] tPreIndex = new int[256];

		if (s.length() != t.length()) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			char sc = s.charAt(i);
			char tc = t.charAt(i);
			if (sPreIndex[sc] != tPreIndex[tc]) {
				return false;
			}
			sPreIndex[sc] = i + 1;
			tPreIndex[tc] = i + 1;
		}
		return true;
	}

	// 最长回文substring, Longest Palindromic Substring
	// https://www.youtube.com/watch?v=g3R-pjUNa3k
	public String longestPalindrome(String s) {
		int start = 0;
		int maxLength = 0;
		for (int i = 0; i < s.length(); i++) {
			int currLength = Math.max(getLen(s, i, i), getLen(s, i, i + 1));
			if (currLength > maxLength) {
				maxLength = currLength;
				start = i - (currLength - 1) / 2;
			}
		}
		return s.substring(start, start + maxLength);
	}

	// l and r are start index, if they equal, we are checking "ese" pattern
	// if not equal, we are checking "esse" pattern
	private int getLen(String s, int l, int r) {
		while (l >= 0 && r < s.length()) {
			if (s.charAt(l) == s.charAt(r)) {
				l--;
				r++;
			} else {
				break;
			}
		}
		return r - l - 1;
	}

	// 647. Palindromic Substrings 回文子字符串个数
	public int countSubstrings(String s) {
		int sum = 0;
		for (int i = 0; i < s.length(); i++) {
			sum += getCount(s, i, i) + getCount(s, i, i + 1);
		}
		return sum;
	}

	private int getCount(String s, int l, int r) {
		int count = 0;
		while (l >= 0 && r < s.length()) {
			if (s.charAt(l) == s.charAt(r)) {
				l--;
				r++;
				count++;
			} else {
				break;
			}
		}
		return count;
	}

	// 判断一个整数是否是回文数
	// 将整数分成左右两部分，右边那部分需要转置，然后判断这两部分是否相等。
	public boolean isPalindrome(int x) {
		if (x == 0) {
			return true;
		}
		if (x < 0 || x % 10 == 0) {
			return false;
		}

		int right = 0;
		while (x > right) {
			right = right * 10 + x % 10;
			if (right == x)
				return true;
			x /= 10;
			if (right == x)
				return true;
		}
		return false;
	}

	// 统计二进制字符串中连续 1 和连续 0 数量相同的子字符串个数
	// 由于子字符串里0和1必须连续，所以首先都连续的0和1分组
	// If we have groups[i] = 2, groups[i+1] = 3, then it represents either "00111"
	// or "11000".
	// 然后遍历所有的分组即可， min(groups[i], groups[i+1])即为i组起始的子字符串个数
	public int countBinarySubstrings(String s) {
		int group[] = new int[s.length()];
		Arrays.fill(group, 0); // 0 means no more element there.
		int index = 0; // index pointing to the end of the array
		group[0] = 1;
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == s.charAt(i - 1)) {
				group[index]++;
			} else {
				index++;
				group[index]++;
			}
		}

		int count = 0;
		for (int i = 0; i < group.length - 1; i++) {
			if (group[i] == 0) {
				break;
			}
			count += Math.min(group[i], group[i + 1]);
		}
		return count;
	}

	// 49. Group Anagrams
	public static List<List<String>> groupAnagrams(String[] strs) {
		Map<String, List<String>> map = new HashMap<>();
		List<List<String>> ans = new ArrayList<>();
		for (String str : strs) {
			char[] c = str.toCharArray();
			Arrays.sort(c);
			String key = String.valueOf(c);
			List<String> values = map.get(key);
			if (values == null) {
				values = new ArrayList<>();
			}
			values.add(str);
			map.put(key, values);
		}
		for (List<String> value : map.values()) {
			ans.add(value);
		}
		return ans;
	}

	// String to Integer (atoi)
	public static int myAtoi(String s) {
		long ans = 0;
		s = s.trim();
		boolean isNegative = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (i == 0 && s.charAt(i) == '-') {
				isNegative = true;
				continue;
			}
			if (i == 0 && s.charAt(i) == '+') {
				isNegative = false;
				continue;
			}
			if (Character.isDigit(c)) {
				ans = ans * 10 + (c - '0');
				if (ans >= Integer.MAX_VALUE) {
					break;
				}
			} else {
				break;
			}
		}
		ans = isNegative ? -ans : ans;
		if (ans > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		if (ans < Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		}
		return (int) ans;
	}

	// Roman to Integer
	// 从后往前容易
	public static int romanToInt(String s) {
		Map<Character, Integer> map = new HashMap<>();
		map.put('I', 1);
		map.put('V', 5);
		map.put('X', 10);
		map.put('L', 50);
		map.put('C', 100);
		map.put('D', 500);
		map.put('M', 1000);
		int ans = 0;
		int preValue = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			int currValue = map.get(s.charAt(i));
			if (preValue > currValue) {
				ans -= currValue;
			} else {
				ans += currValue;
			}
			preValue = currValue;
		}
		return ans;
	}
}
