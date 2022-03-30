package company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
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

	// 611. Valid Triangle Number
	// greedy，首先排序，给定两边之后，一旦第三边不满足条件，即可跳过剩下的结果
	public int triangleNumber(int[] nums) {
		int count = 0;
		Arrays.sort(nums);
		for (int i = 0; i < nums.length - 2; i++) {
			// k的取值是单调的，一旦i，j，k不满足条件了，在i不变的情况下，k不动，j向右移动即可
			// j，k组成双指针, j和k中间的取值都能满足条件
			int k = i + 2;
			for (int j = i + 1; j < nums.length - 1 && nums[i] != 0; j++) {
				while (k < nums.length && nums[i] + nums[j] > nums[k])
					k++;
				count += k - j - 1;
			}
		}
		return count;
	}

	// 341. Flatten Nested List Iterator
	public class NestedIterator implements Iterator<Integer> {
		// a single queue can do, we can use a Pair<Index : NestedInteger>
		private Deque<List<NestedInteger>> listDeque = new ArrayDeque<>();
		private Deque<Integer> indexDeque = new ArrayDeque<>();

		public NestedIterator(List<NestedInteger> nestedList) {
			listDeque.addFirst(nestedList);
			indexDeque.addFirst(0);
		}

		@Override
		public Integer next() {
			makeStackTopAnInteger();
			int currentPosition = indexDeque.removeFirst();
			indexDeque.addFirst(currentPosition + 1);
			return listDeque.peekFirst().get(currentPosition).getInteger();
		}

		@Override
		public boolean hasNext() {
			makeStackTopAnInteger();
			return !indexDeque.isEmpty();
		}

		private void makeStackTopAnInteger() {
			while (!indexDeque.isEmpty()) {
				// If the top list is used up, pop it and its index.
				if (indexDeque.peekFirst() >= listDeque.peekFirst().size()) {
					indexDeque.removeFirst();
					listDeque.removeFirst();
					continue;
				}

				// Otherwise, if it's already an integer, we don't need to do anything.
				if (listDeque.peekFirst().get(indexDeque.peekFirst()).isInteger()) {
					break;
				}

				// Otherwise, it must be a list. We need to update the previous index
				// and then add the new list with an index of 0.
				int index = indexDeque.pollFirst();
				List<NestedInteger> nextList = listDeque.peekFirst().get(index).getList();
				listDeque.addFirst(nextList);
				indexDeque.addFirst(index + 1);
				// the nextList start at index = 0
				indexDeque.addFirst(0);
			}
		}
	}
}
