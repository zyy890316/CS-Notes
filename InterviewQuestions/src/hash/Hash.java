package hash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Hash {
	// 数组中两个数的和为给定值
	// 可以先对数组进行排序，然后使用双指针方法或者二分查找方法。这样做的时间复杂度为 O(NlogN)，空间复杂度为 O(1)。
	// 用 HashMap 存储数组元素和索引的映射，在访问到 nums[i] 时，判断 HashMap 中是否存在 target -
	// nums[i]，如果存在说明 target - nums[i] 所在的索引和 i 就是要找的两个数。该方法的时间复杂度为 O(N)，空间复杂度为
	// O(N)，使用空间来换取时间。

	// 判断数组是否含有重复元素
	public boolean containsDuplicate(int[] nums) {
		Set<Integer> set = new HashSet<>();
		for (int num : nums) {
			set.add(num);
		}
		return set.size() < nums.length;
	}

	// 最长和谐序列
	// 和谐序列中最大数和最小数之差正好为 1，应该注意的是序列的元素不一定是数组的连续元素。
	// 所有数字放入hashmap中，key为数字，value为出现次数
	// 遍历hashmap可得结果

	// 128. Longest Consecutive Sequence 最长连续序列
	public int longestConsecutive(int[] nums) {
		Map<Integer, Integer> countForNum = new HashMap<>();
		for (int num : nums) {
			countForNum.put(num, 1);
		}

		// 每个元素数出最多的连续序列长度
		for (int num : nums) {
			int count = 1;
			int start = num + 1; // 要看从num开始的最长序列到哪里，先看num+1有没有
			while (countForNum.containsKey(start)) {
				// 大于1，说明之前数过从该元素开始的部分了
				if (countForNum.get(start) > 1) {
					count += countForNum.get(start);
					break;
				}
				count++;
				start++;
			}
			countForNum.put(num, count);
		}

		int max = 0;
		for (int num : nums) {
			max = Math.max(max, countForNum.get(num));
		}
		return max;
	}
}
