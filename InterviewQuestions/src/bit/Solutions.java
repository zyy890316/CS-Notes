package bit;

public class Solutions {
	// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20位运算.md
	// https://www.baeldung.com/java-bitwise-operators

	// 统计两个数的二进制表示有多少位不同
	// https://leetcode.com/problems/hamming-distance
	public int hammingDistance(int x, int y) {
		return Integer.bitCount(x ^ y);
	}

	// 数组中唯一一个不重复的元素
	// 两个相同的数异或的结果为 0，对所有数进行异或操作，最后的结果就是单独出现的那个数。
	// https://leetcode.com/problems/single-number
	public int singleNumber(int[] nums) {
		int ans = 0;
		for (int num : nums) {
			ans = ans ^ num;
		}
		return ans;
	}

	// 找出数组中缺失的那个数
	// 和上题类似，只要额外把数字遍历异或操作就能得出
	// https://leetcode.com/problems/missing-number/description/
	public int missingNumber(int[] nums) {
		int ans = 0;
		for (int i = 0; i < nums.length; i++) {
			ans = ans ^ i ^ nums[i];
		}
		ans = ans ^ nums.length;
		return ans;
	}

	// 不用额外变量交换两个整数
	// a = a ^ b;
	// b = a ^ b;
	// a = a ^ b;

	// 判断一个数是不是 2 的 n 次方
	public boolean isPowerOfTwo(int n) {
		return n > 0 && Integer.bitCount(n) == 1;
	}

	// 判断一个数是不是 4 的 n 次方
	// 这种数在二进制表示中有且只有一个奇数位为 1，例如 16（10000）。
	// https://leetcode.com/problems/power-of-four
	public boolean isPowerOfFour(int n) {
		return n > 0 && (n & (n - 1)) == 0 && (n & 0b01010101010101010101010101010101) != 0;
	}

	// 判断一个数的位级表示是否不会出现连续的 0 和 1
	// https://leetcode.com/problems/binary-number-with-alternating-bits/description/
	public boolean hasAlternatingBits(int n) {
		int ans = n ^ (n >> 1);
		return (ans & (ans + 1)) == 0;
	}

	// 求一个数的补码
	// 对于 00000101，要求补码可以将它与 00000111 进行异或操作。那么问题就转换为求掩码 00000111。
	// https://leetcode.com/problems/number-complement/description/
	public int findComplement(int num) {
		// if num is 00000101, mask = 00000100
		int mask = Integer.highestOneBit(num);
		// mask << 1 is 00001000, mask - 1 = 00000111
		mask = (mask << 1) - 1;
		return mask ^ num;
	}

	// 字符串数组最大乘积
	// 本题主要问题是判断两个字符串是否含相同字符，由于字符串只含有小写字符，总共 26 位，因此可以用一个 32 位的整数来存储每个字符是否出现过。
	// https://leetcode.com/problems/maximum-product-of-word-lengths/description/
	public int maxProduct(String[] words) {
		int[] map = new int[words.length];
		for (int i = 0; i < words.length; i++) {
			int length = words[i].length();
			int num = 0;
			for (int j = 0; j < length; j++) {
				int position = words[i].charAt(j) - 'a';
				num = num | (1 << position);
			}
			map[i] = num;
		}

		int max = 0;
		for (int i = 0; i < words.length; i++) {
			for (int j = i + 1; j < words.length; j++) {
				if ((map[i] & map[j]) == 0) {
					max = Math.max(max, words[i].length() * words[j].length());
				}
			}
		}
		return max;
	}

	// 统计从 0 ~ n 每个数的二进制表示中 1 的个数
	// https://leetcode.com/problems/counting-bits/description/
	public int[] countBits(int n) {
		int[] sum = new int[n + 1];
		for (int i = 0; i <= n; i++) {
			sum[i] = Integer.bitCount(i);
		}
		return sum;
	}
}
