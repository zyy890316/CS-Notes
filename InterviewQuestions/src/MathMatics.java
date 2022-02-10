import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20数学.md
public class MathMatics {
	// 303. Range Sum Query - Immutable (Easy)
	// 求区间 i ~ j 的和，可以转换为 sum[j + 1] - sum[i]，其中 sum[i] 为 0 ~ i - 1 的和。

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
	// 对于一个数 N，它所包含 5 的个数为：N/5 + N/5^2 + N/5^3 + ...，其中 N/5 表示不大于 N 的数中 5 的倍数贡献一个
	// 5，N/5^2 表示不大于 N 的数中 5^2 的倍数再贡献一个 5 ...。
	public int trailingZeroes(int n) {
		return n < 5 ? 0 : n / 5 + trailingZeroes(n / 5);
	}

	// 462 改变数组元素使所有的数组元素都相等
	// https://www.youtube.com/watch?v=oiLw3d4qAKg
	// 关键是找到中位数，所有都移动到中位数是最优解
	// 还可以使用quick sort找到中位数，时间复杂度 O(N)
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

	// 50. Pow(x, n)
	public double myPow(double x, int n) {
		double ans = pow(x, Math.abs(n));
		if (ans == 0.0)
			return ans;
		return n >= 0 ? ans : 1 / ans;
	}

	public double pow(double x, int n) {
		if (n == 0) {
			return 1;
		}
		double ans = pow(x, n / 2);
		ans *= ans;
		if (n % 2 != 0) { // if n is odd
			ans *= x;
		}

		return ans;
	}

	// 150. Evaluate Reverse Polish Notation
	public int evalRPN(String[] tokens) {
		Stack<Integer> nums = new Stack<>();
		for (String token : tokens) {
			if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
				int ans = 0;
				int a = nums.pop();
				int b = nums.pop();
				if (token.equals("+")) {
					ans = b + a;
				}
				if (token.equals("-")) {
					ans = b - a;
				}
				if (token.equals("*")) {
					ans = b * a;
				}
				if (token.equals("/")) {
					ans = b / a;
				}
				nums.push(ans);
			} else {
				nums.push(Integer.parseInt(token));
			}
		}
		return nums.pop();
	}

	// 29. Divide Two Integers
	// https://leetcode.com/problems/divide-two-integers/discuss/13467/Very-detailed-step-by-step-explanation-(Java-solution)
	// 所有正数都能用2的次方的和来表示

	// 166. Fraction to Recurring Decimal
	public String fractionToDecimal(int numerator, int denominator) {
		if (numerator == 0)
			return "0";
		Map<Long, Integer> map = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		boolean sign = (numerator < 0) == (denominator < 0);
		if (!sign) {
			sb.append("-");
		}

		long divsor = Math.abs((long) denominator);
		long remainder = Math.abs((long) numerator) % divsor;
		long beforeDecimal = Math.abs((long) numerator) / divsor;
		sb.append(beforeDecimal);
		if (remainder == 0)
			return sb.toString();

		sb.append(".");

		while (remainder != 0) {
			remainder = remainder * 10;
			if (map.containsKey(remainder)) {
				sb.insert(map.get(remainder), "(");
				sb.append(")");
				return sb.toString();
			}
			map.put(remainder, sb.length());
			long next = remainder / divsor;
			sb.append(next);
			remainder = remainder % divsor;
		}
		return sb.toString();
	}

	// 454. 4Sum II
	// 两两一组，分别求和，用hashmap看是否出现过
	public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
		Map<Integer, Integer> sumMap = new HashMap<>();
		for (int a : nums1) {
			for (int b : nums2) {
				int sum = a + b;
				sumMap.put(-sum, sumMap.getOrDefault(-sum, 0) + 1);
			}
		}
		int count = 0;
		for (int c : nums3) {
			for (int d : nums4) {
				int sum = c + d;
				if (sumMap.containsKey(sum)) {
					count += sumMap.get(sum);
				}
			}
		}
		return count;
	}

	// 264. Ugly Number II
	public int nthUglyNumber(int n) {
		int i = 0;
		int j = 0;
		int k = 0;
		int[] ugly = new int[n + 1];
		ugly[0] = 1;

		for (int m = 0; m < n; m++) {
			int next = Math.min(2 * ugly[i], 3 * ugly[j]);
			next = Math.min(5 * ugly[k], next);
			if (next == 2 * ugly[i])
				i++;
			if (next == 3 * ugly[j])
				j++;
			if (next == 5 * ugly[k])
				k++;
			ugly[m + 1] = next;
		}

		return ugly[n - 1];
	}

	// 1823. Find the Winner of the Circular Game, Joseph Ring, 约瑟夫环
	public int findTheWinner(int n, int k) {
		// +1 is for converting 0-based indexing to 1-based indexing
		return findTheWinnerRecursive(n, k) + 1;
	}

	public int findTheWinnerRecursive(int n, int k) {
		if (n <= 1) {
			return 0;
		}
		return (findTheWinnerRecursive(n - 1, k) + k) % n;
	}

	// 650 复制粘贴字符：最开始只有一个字符 A，问需要多少次操作能够得到 n 个字符 A，每次操作可以复制当前所有的字符，或者粘贴。
	// https://www.bilibili.com/medialist/play/7836741?from=space&business=space_channel&business_id=92900&desc=1
	// 最小操作数就是所有可分成的质数的成绩，即其质因数的和
	public int minSteps(int n) {
		if (n == 1)
			return 0;
		for (int i = 2; i <= Math.sqrt(n); i++) {
			// n能被i整除，i一定是质数
			// 否则从小到大枚举，i的质因数一定之前被枚举过，而n未被其整除，也不可能被i整除
			if (n % i == 0) {
				return i + minSteps(n / i);
			}
		}
		return n;
	}

	// 29. Divide Two Integers
	private static int HALF_INT_MIN = -1073741824;

	public int divide(int dividend, int divisor) {
		// Special case: overflow.
		if (dividend == Integer.MIN_VALUE && divisor == -1) {
			return Integer.MAX_VALUE;
		}

		/*
		 * We need to convert both numbers to negatives for the reasons explained above.
		 * Also, we count the number of negatives signs.
		 */
		int negatives = 2;
		if (dividend > 0) {
			negatives--;
			dividend = -dividend;
		}
		if (divisor > 0) {
			negatives--;
			divisor = -divisor;
		}
		int quotient = 0;
		while (divisor >= dividend) {
			/*
			 * We know it'll fit at least once as divivend >= divisor. Note: We use a
			 * negative powerOfTwo as it's possible we might have the case divide(INT_MIN,
			 * -1).
			 */
			int powerOfTwo = -1;
			int value = divisor;
			/*
			 * Check if double the current value is too big. If not, continue doubling. If
			 * it is too big, stop doubling and continue with the next step
			 */
			while (value >= HALF_INT_MIN && value + value >= dividend) {
				value += value;
				powerOfTwo += powerOfTwo;
			}
			// We have been able to subtract divisor another powerOfTwo times.
			quotient += powerOfTwo;
			// Remove value so far so that we can continue the process with remainder.
			dividend -= value;
		}

		if (negatives != 1) {
			return -quotient;
		}
		return quotient;
	}
}
