import java.util.Arrays;

// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20数学.md
public class MathMatics {
	// 204 n之前的素数个数
	// 在每次找到一个素数时，将能被素数整除的数排除掉。
	public int countPrimes(int n) {
		boolean[] notPrimes = new boolean[n + 1];
		int count = 0;
		// 素数指大於1的自然数中，除了1和該数自身外，無法被其他自然数整除的数
		for (int i = 2; i < n; i++) {
			if (notPrimes[i]) {
				continue;
			}
			count++;

			for (int j = i; j < n; j += i) {
				notPrimes[j] = true;
			}
		}
		return count;
	}

	// 最大公约数
	int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	// 最小公倍数为两数的乘积除以最大公约数。
	int lcm(int a, int b) {
		return a * b / gcd(a, b);
	}

	// 504 7进制转换
	public String convertToBase7(int num) {
		if (num == 0) {
			return "0";
		}
		StringBuilder sb = new StringBuilder();
		boolean isNegative = num < 0;
		if (isNegative) {
			num = -num;
		}
		while (num > 0) {
			sb.insert(0, num % 7);
			num = num / 7;
		}
		String ret = sb.toString();
		return isNegative ? "-" + ret : ret;
	}

	// 405 16进制
	public String toHex(int num) {
		char[] map = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		if (num == 0)
			return "0";
		StringBuilder sb = new StringBuilder();
		while (num != 0) {
			int temp = 0b1111 & num;
			sb.insert(0, map[temp]);
			num = num >>> 4; // 因为考虑的是补码形式，因此符号位就不能有特殊的意义，需要使用无符号右移，左边填 0
		}
		return sb.toString();
	}

	// 168 26进制
	public String convertToTitle(int n) {
		if (n == 0) {
			return "";
		}
		n--;
		return convertToTitle(n / 26) + (char) (n % 26 + 'A');
	}

	// 172 统计阶乘尾部有多少个 0
// 尾部的 0 由 2 * 5 得来，2 的数量明显多于 5 的数量，因此只要统计有多少个 5 即可。
// 对于一个数 N，它所包含 5 的个数为：N/5 + N/5^2 + N/5^3 + ...，其中 N/5 表示不大于 N 的数中 5 的倍数贡献一个 5，N/5^2 表示不大于 N 的数中 5^2 的倍数再贡献一个 5 ...。
	public int trailingZeroes(int n) {
		return n < 5 ? 0 : n / 5 + trailingZeroes(n / 5);
	}

	// 462 改变数组元素使所有的数组元素都相等
	// https://www.youtube.com/watch?v=oiLw3d4qAKg
	// 关键是找到中位数，所有都移动到中位数是最优解
	// 还可以使用快速选择找到中位数，时间复杂度 O(N)
	public int minMoves2(int[] nums) {
		Arrays.sort(nums);
		int median = nums[nums.length / 2];
		int move = 0;
		for (int num : nums) {
			move += Math.abs(num - median);
		}
		return move;
	}

	// 367 检查是否为平方数
	// 平方序列：1,4,9,16,..
	// 间隔：3,5,7,...
	// 间隔为等差数列，使用这个特性可以得到从 1 开始的平方序列。
	public boolean isPerfectSquare(int num) {
		int diff = 3;
		int i = 1;
		while (i <= num) {
			if (i == num) {
				return true;
			}
			i = i + diff;
			diff += 2;
		}
		return false;
	}

	// 326 判断是否为3 的 n 次方
	public boolean isPowerOfThree(int n) {
		while (n >= 3) {
			if (n % 3 == 0) {
				n = n / 3;
			} else {
				return false;
			}
		}
		if (n == 1) {
			return true;
		}
		return false;
	}

	// 238 乘积数组：给定一个数组，创建一个新数组，新数组的每个元素为原始数组中除了该位置上的元素之外所有元素的乘积。
	// 要求时间复杂度为 O(N)，并且不能使用除法。
	public int[] productExceptSelf(int[] nums) {
		int n = nums.length;
		int[] products = new int[n];
		Arrays.fill(products, 1);
		int left = 1;
		for (int i = 1; i < n; i++) {
			left *= nums[i - 1];
			products[i] *= left;
		}
		int right = 1;
		for (int i = n - 2; i >= 0; i--) {
			right *= nums[i + 1];
			products[i] *= right;
		}
		return products;
	}

	// 628 找出数组中的乘积最大的三个数
	// 两种情况，三个最大的正数，或最大的正数和两个最小的负数
	// 可直接排序，或一次扫描找出最大的三个值和最小的两个值
}