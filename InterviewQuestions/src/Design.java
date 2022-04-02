import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class Design {
	public static void main(String[] args) {
		AllOne allOne = new AllOne();
		allOne.inc("a");
		allOne.inc("a");
		allOne.inc("a");
		allOne.dec("a");
		allOne.inc("c");
		allOne.getMinKey();
		allOne.getMaxKey();
		RandomizedCollection c = new RandomizedCollection();
		c.insert(0);
		c.remove(0);
	}

	// 146. LRU Cache
	// 或者用 Hashmap + DoubleLinkedList, Map<Integer, DLinkedNode> cache = new
	// HashMap<>();
	@SuppressWarnings("serial")
	class LRUCache extends LinkedHashMap<Integer, Integer> {
		private int capacity;

		public LRUCache(int capacity) {
			super(capacity, 0.75F, true);
			this.capacity = capacity;
		}

		public int get(int key) {
			return super.getOrDefault(key, -1);
		}

		public void put(int key, int value) {
			super.put(key, value);
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
			return size() > capacity;
		}
	}

	// 460. LFU Cache
	// https://leetcode.com/problems/lfu-cache/discuss/1864305/Java-or-82-or-Extremely-Easy-Explanation-and-Comments-or-(Good-enough-for-GoogleMetaAmazon-On-sites)
	// TreeMap<Integer, LinkedHashMap<Integer, Integer>> map
	// frequency ：出现frequency次的所有key value
	// 用LinkedHashMap相当于LRU，相同次数的最早用的先remove

	// 380. Insert Delete GetRandom O(1)
	// HashMap + ArrayList
	class RandomizedSet {
		final Map<Integer, Integer> map;
		final List<Integer> list;
		final Random random;

		public RandomizedSet() {
			map = new HashMap<>();
			list = new ArrayList<>();
			random = new Random();
		}

		public boolean insert(int val) {
			if (map.containsKey(val)) {
				return false;
			}
			map.put(val, list.size());
			list.add(val);
			return true;
		}

		public boolean remove(int val) {
			if (!map.containsKey(val)) {
				return false;
			}
			int index = map.get(val);
			int lastElement = list.get(list.size() - 1);
			map.put(lastElement, index);
			list.set(index, lastElement);
			list.remove(list.size() - 1);
			map.remove(val);
			return true;
		}

		public int getRandom() {
			int rand = random.nextInt(list.size());
			return list.get(rand);
		}
	}

	// 381. Insert Delete GetRandom O(1) - Duplicates allowed
	// 和380区别在于map里面要存某val所有的index
	static class RandomizedCollection {
		final HashMap<Integer, Set<Integer>> map;
		final List<Integer> list;
		final Random random = new Random();

		public RandomizedCollection() {
			map = new HashMap<>();
			list = new ArrayList<>();
		}

		// Inserts a value to the collection. Returns true if the collection did not
		// already contain the specified element.
		public boolean insert(int val) {
			map.computeIfAbsent(val, z -> new HashSet<Integer>()).add(list.size());
			list.add(val);
			return map.get(val).size() == 1;
		}

		public boolean remove(int val) {
			if (!map.containsKey(val)) {
				return false;
			}
			// remove val from map
			Set<Integer> indexSet = map.get(val);
			int index = indexSet.iterator().next();
			indexSet.remove(index);
			if (indexSet.size() == 0) {
				map.remove(val);
			}
			int lastElement = list.get(list.size() - 1);
			// add lastElement to right set, also remove the last occurance
			Set<Integer> lastIndexSet = map.computeIfAbsent(lastElement, z -> new HashSet<Integer>());
			lastIndexSet.add(index);
			lastIndexSet.remove(list.size() - 1);
			if (lastIndexSet.size() == 0) {
				map.remove(lastElement);
			}
			// adjust the list
			list.set(index, lastElement);
			list.remove(list.size() - 1);
			return true;
		}

		public int getRandom() {
			int rand = random.nextInt(list.size());
			return list.get(rand);
		}
	}

	/**
	 * Your RandomizedCollection object will be instantiated and called as such:
	 * RandomizedCollection obj = new RandomizedCollection(); boolean param_1 =
	 * obj.insert(val); boolean param_2 = obj.remove(val); int param_3 =
	 * obj.getRandom();
	 */

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

	// 895. Maximum Frequency Stack
	class FreqStack {
		// number : frequency
		public Map<Integer, Integer> map;
		// frequency : stack of numbers, to return the most recent max frequency number
		public Map<Integer, Stack<Integer>> group;
		public int maxFrequency;

		public FreqStack() {
			this.map = new HashMap<>();
			this.group = new HashMap<>();
			maxFrequency = 0;
		}

		public void push(int val) {
			int frequency = map.getOrDefault(val, 0) + 1;
			map.put(val, frequency);
			// for each push, say frequency is 2, the frequency 1 stack also contains val
			// this makes pop method below easy
			group.computeIfAbsent(frequency, z -> new Stack<>()).push(val);
			if (frequency > maxFrequency) {
				maxFrequency = frequency;
			}
		}

		public int pop() {
			Stack<Integer> maxStack = group.get(maxFrequency);
			int ans = maxStack.pop();
			map.put(ans, maxFrequency - 1);
			if (maxStack.size() == 0) {
				maxFrequency--;
			}
			return ans;
		}
	}
}
