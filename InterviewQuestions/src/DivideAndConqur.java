
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
}
