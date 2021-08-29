
import java.util.ArrayList;
import java.util.List;

import tree.TreeNode;

public class DivideAndConqur {
	public static void main(String[] args) {
		generateTrees(3);
	}

	// 241 给表达式加括号:给出每种加括号的结果
	// https://leetcode.com/problems/different-ways-to-add-parentheses/description/
	public List<Integer> diffWaysToCompute(String expression) {
		List<Integer> ans = new ArrayList<Integer>();
		List<Integer> r = new ArrayList<Integer>();
		List<Integer> l = new ArrayList<Integer>();

		for (int i = 0; i < expression.length(); i++) {
			char ch = expression.charAt(i);
			if (ch == '+' || ch == '-' || ch == '*') {
				r = diffWaysToCompute(expression.substring(i + 1));
				l = diffWaysToCompute(expression.substring(0, i));
			}
			for (int j = 0; j < l.size(); j++) {
				for (int k = 0; k < r.size(); k++) {
					switch (ch) {
					case '+':
						ans.add(l.get(j) + r.get(k));
						break;
					case '-':
						ans.add(l.get(j) - r.get(k));
						break;
					case '*':
						ans.add(l.get(j) * r.get(k));
						break;
					}
				}
			}
		}
		if (ans.size() == 0) {
			ans.add(Integer.valueOf(expression));
		}
		return ans;
	}

	// 95 生成不同的二叉搜索树:给定一个数字 n，要求生成所有值为 1...n 的二叉搜索树。
	// https://leetcode.com/problems/unique-binary-search-trees-ii/description/
	public static List<TreeNode> generateTrees(int n) {
		return generateTrees(1, n);
	}

	public static List<TreeNode> generateTrees(int start, int end) {
		List<TreeNode> ans = new ArrayList<TreeNode>();
		if (start == end) {
			ans.add(new TreeNode(start));
			return ans;
		}
		if (start > end) {
			return ans;
		}
		for (int i = start; i <= end; i++) {
			List<TreeNode> l = i - 1 >= start ? generateTrees(start, i - 1) : new ArrayList<TreeNode>();
			List<TreeNode> r = i + 1 <= end ? generateTrees(i + 1, end) : new ArrayList<TreeNode>();
			if (l.size() == 0) {
				for (int k = 0; k < r.size(); k++) {
					ans.add(new TreeNode(i, null, r.get(k)));
				}
				continue;
			}
			if (r.size() == 0) {
				for (int j = 0; j < l.size(); j++) {
					ans.add(new TreeNode(i, l.get(j), null));
				}
				continue;
			}
			for (int j = 0; j < l.size(); j++) {
				for (int k = 0; k < r.size(); k++) {
					ans.add(new TreeNode(i, l.get(j), r.get(k)));
				}
			}
		}
		return ans;
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
}
