import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class Sort {
	// 324. Wiggle Sort II
	public void wiggleSort(int[] nums) {
		Arrays.sort(nums);
		int n = nums.length;
		int[] temp = new int[nums.length];
		// 此题难点在于中位数两边的数有可能相同，所以需要尽量放远一点
		// Small half: M . S . S . 递减
		// Large half: . L . L . M 递减
		// -Together : M L S L S M
		int medianIndex = (n - 1) / 2;
		int index = 0;
		for (int i = 0; i <= medianIndex; i++) {
			temp[index] = nums[medianIndex - i];
			if (index + 1 < n) {
				temp[index + 1] = nums[n - 1 - i];
			}
			index += 2;
		}
		for (int m = 0; m < nums.length; m++) {
			nums[m] = temp[m];
		}
	}

	// 179. Largest Number
	// 转成String 排序即可
	public String largestNumber(int[] nums) {
		String[] sNums = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			sNums[i] = String.valueOf(nums[i]);
		}
		Arrays.sort(sNums, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				String s1 = a + b;
				String s2 = b + a;
				return s2.compareTo(s1);
			}
		});

		// 如果给的nums全都是0，返回一个0就好
		if (sNums[0].equals("0"))
			return "0";

		StringBuilder sb = new StringBuilder();
		for (String s : sNums) {
			sb.append(s);
		}
		return sb.toString();
	}

	// 按颜色进行排序： 只有 0/1/2 三种颜色
	// https://leetcode.com/problems/sort-colors
	public static void sortColors(int[] nums) {
		int zero = -1;
		int one = 0;
		int two = nums.length - 1;
		while (one <= two) {
			if (nums[one] == 0) {
				zero++;
				swap(nums, zero, one);
				one++;
			} else if (nums[one] == 2) {
				swap(nums, one, two);
				two--;
			} else {
				one++;
			}
		}
	}

	private static void swap(int[] nums, int i, int j) {
		int t = nums[i];
		nums[i] = nums[j];
		nums[j] = t;
	}

	// 349. Intersection of Two Arrays
	public int[] intersection(int[] nums1, int[] nums2) {
		HashSet<Integer> set1 = new HashSet<Integer>();
		for (Integer n : nums1)
			set1.add(n);
		HashSet<Integer> set2 = new HashSet<Integer>();
		for (Integer n : nums2)
			set2.add(n);

		set1.retainAll(set2);

		int[] output = new int[set1.size()];
		int idx = 0;
		for (int s : set1)
			output[idx++] = s;
		return output;
	}
}
