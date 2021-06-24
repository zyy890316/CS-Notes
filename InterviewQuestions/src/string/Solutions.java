package string;

import java.util.Arrays;
import java.util.Collections;

public class Solutions {
	public static void main(String[] args) {
		moveRight("abcd123", 3);
		reverseWords("I am a student");
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
}
