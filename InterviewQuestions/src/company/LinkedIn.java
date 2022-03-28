package company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LinkedIn {

	public static void main(String[] args) {
	}

	// 244. Shortest Word Distance II
	class WordDistance {

		HashMap<String, ArrayList<Integer>> locations;

		public WordDistance(String[] words) {
			this.locations = new HashMap<String, ArrayList<Integer>>();

			// Prepare a mapping from a word to all it's locations (indices).
			for (int i = 0; i < words.length; i++) {
				ArrayList<Integer> loc = this.locations.getOrDefault(words[i], new ArrayList<Integer>());
				loc.add(i);
				this.locations.put(words[i], loc);
			}
		}

		public int shortest(String word1, String word2) {
			ArrayList<Integer> loc1, loc2;

			// Location lists for both the words
			// the indices will be in SORTED order by default
			loc1 = this.locations.get(word1);
			loc2 = this.locations.get(word2);

			int l1 = 0, l2 = 0, minDiff = Integer.MAX_VALUE;
			while (l1 < loc1.size() && l2 < loc2.size()) {
				minDiff = Math.min(minDiff, Math.abs(loc1.get(l1) - loc2.get(l2)));
				if (loc1.get(l1) < loc2.get(l2)) {
					l1++;
				} else {
					l2++;
				}
			}

			return minDiff;
		}
	}

	// 364. Nested List Weight Sum II
	public interface NestedInteger {

		// @return true if this NestedInteger holds a single integer, rather than a
		// nested list.
		public boolean isInteger();

		// @return the single integer that this NestedInteger holds, if it holds a
		// single integer
		// Return null if this NestedInteger holds a nested list
		public Integer getInteger();

		// Set this NestedInteger to hold a single integer.
		public void setInteger(int value);

		// Set this NestedInteger to hold a nested list and adds a nested integer to it.
		public void add(NestedInteger ni);

		// @return the nested list that this NestedInteger holds, if it holds a nested
		// list
		// Return empty list if this NestedInteger holds a single integer
		public List<NestedInteger> getList();
	}

	public int depthSumInverse(List<NestedInteger> nestedList) {
		int maxDepth = findMaxDepth(nestedList);
		int ans = 0;
		for (NestedInteger nestedInt : nestedList) {
			ans += depthSumInverseRecursive(nestedInt, 1, maxDepth);
		}
		return ans;
	}

	public int depthSumInverseRecursive(NestedInteger n, int depth, int maxDepth) {
		if (n.isInteger())
			return n.getInteger() * (maxDepth - depth + 1);

		int ans = 0;
		for (NestedInteger nestedInt : n.getList()) {
			ans += depthSumInverseRecursive(nestedInt, depth + 1, maxDepth);
		}
		return ans;
	}

	private int findMaxDepth(List<NestedInteger> list) {
		int maxDepth = 1;

		for (NestedInteger nested : list) {
			if (!nested.isInteger()) {
				maxDepth = Math.max(maxDepth, 1 + findMaxDepth(nested.getList()));
			}
		}

		return maxDepth;
	}
}
