package company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedIn {

	public static void main(String[] args) {
		AllOne allOne = new AllOne();
		allOne.inc("a");
		allOne.inc("a");
		allOne.inc("a");
		allOne.dec("a");
		allOne.inc("c");
		allOne.getMinKey();
		allOne.getMaxKey();
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

	// 432. All O`one Data Structure
	// Map 映射到双链表,链表里每个node值为frequency，set是出现这些次数的所有string
	static class AllOne {
		private Map<String, Node> map;
		DoubleLinkedList dll = new DoubleLinkedList();

		public AllOne() {
			this.map = new HashMap<>();
		}

		public void inc(String key) {
			if (map.containsKey(key)) {
				Node cur = map.get(key);
				if (cur.next.val == cur.val + 1) {
					cur.set.remove(key);
					cur.next.set.add(key);

				} else {
					Set<String> set = new HashSet<>();
					set.add(key);
					dll.addAfter(cur, new Node(cur.val + 1, set));
					cur.set.remove(key);
				}
				map.put(key, cur.next);
				if (cur.set.size() == 0)
					dll.delete(cur);
			} else {
				if (dll.head.next.val == 1) {
					Node cur = dll.head.next;
					cur.set.add(key);
					map.put(key, cur);
				} else {
					Set<String> set = new HashSet<>();
					set.add(key);
					dll.addAfter(dll.head, new Node(1, set));
					map.put(key, dll.head.next);
				}
			}
		}

		public void dec(String key) {
			if (map.containsKey(key)) {
				Node cur = map.get(key);
				if (cur.val == 1) {
					cur.set.remove(key);
					map.remove(key);
					if (cur.set.size() == 0)
						dll.delete(cur);
					return;
				}

				if (cur.prev.val == cur.val - 1) {
					cur.set.remove(key);
					cur.prev.set.add(key);
				} else {
					Set<String> set = new HashSet<>();
					set.add(key);
					cur.set.remove(key);
					dll.addFront(cur, new Node(cur.val - 1, set));
				}
				map.put(key, cur.prev);
				if (cur.set.size() == 0)
					dll.delete(cur);
			} else {
				return;
			}
		}

		public String getMaxKey() {
			if (dll.tail.prev.val > 0) {
				return dll.tail.prev.set.iterator().next();
			}
			return "";
		}

		public String getMinKey() {
			if (dll.head.next.val > 0) {
				return dll.head.next.set.iterator().next();
			}
			return "";
		}
	}

	static class DoubleLinkedList {
		Node head;
		Node tail;

		public DoubleLinkedList() {
			this.head = new Node(-1);
			this.tail = new Node(-1);
			this.head.next = tail;
			this.tail.prev = head;
		}

		public void addFront(Node cur, Node node) {
			node.next = cur;
			node.prev = cur.prev;

			cur.prev.next = node;
			cur.prev = node;
		}

		public void addAfter(Node cur, Node node) {
			node.prev = cur;
			node.next = cur.next;

			cur.next.prev = node;
			cur.next = node;
		}

		public void delete(Node node) {
			node.prev.next = node.next;
			node.next.prev = node.prev;
		}
	}

	static class Node {
		public int val;
		public Node prev;
		public Node next;
		public Set<String> set;

		public Node(int val) {
			this.val = val;
			this.set = new HashSet<>();
		}

		public Node(int val, Set<String> set) {
			this.val = val;
			this.set = set;
		}
	}
}
