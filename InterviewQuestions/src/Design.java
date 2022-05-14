import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

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
	// LinkedHashMap<>();
	@SuppressWarnings("serial")
	class LRUCache<K, V> extends LinkedHashMap<K, V> {
		private int capacity;

		public LRUCache(int capacity) {
			super(capacity, 0.75F, true);
			this.capacity = capacity;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
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

	// 271. Encode and Decode Strings
	// use 4 bytes to represent the length of the next chunck
	// Encodes string length to bytes string
	public String intToString(String s) {
		int x = s.length();
		char[] bytes = new char[4];
		for (int i = 0; i <= 3; i++) {
			bytes[i] = (char) (x >> ((3 - i) * 8) & 0xff);
		}
		return new String(bytes);
	}

	// Encodes a list of strings to a single string.
	public String encode(List<String> strs) {
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			sb.append(intToString(s));
			sb.append(s);
		}
		return sb.toString();
	}

	// Decodes bytes string to integer
	public int stringToInt(String bytesStr) {
		int result = 0;
		for (char b : bytesStr.toCharArray())
			result = (result << 8) + (int) b;
		return result;
	}

	// Decodes a single string to a list of strings.
	public List<String> decode(String s) {
		int i = 0, n = s.length();
		List<String> output = new ArrayList<>();
		while (i < n) {
			int length = stringToInt(s.substring(i, i + 4));
			i += 4;
			output.add(s.substring(i, i + length));
			i += length;
		}
		return output;
	}

	// 642. Design Search Autocomplete System
	// Use Trie to store the auto complete tree
	// use a frequency map to record frequency of each pattern
	// when search, put all founded string into a PriorityQueue
	class AutocompleteSystem {
		Trie trie; // Trie DS to look-up for prefix searches
		Map<String, Integer> freqMap; // Map to maintain the frequencies of the sentences
		StringBuilder currStr; // Current input string being searched

		// Load the freqMap & Trie with sentences & their frequencies
		public AutocompleteSystem(String[] sentences, int[] times) {
			trie = new Trie();
			freqMap = new HashMap<>();
			currStr = new StringBuilder();

			for (int i = 0; i < sentences.length; i++) {
				freqMap.put(sentences[i], times[i]);
				trie.insert(sentences[i]);
			}
		}

		// Update the currrent prefix string with the current input character & return
		// the result as expected
		public List<String> input(char c) {

			// If end of current input sentence then add it to our freqMap & Trie and reset
			// the current prefix string
			if (c == '#') {
				freqMap.put(currStr.toString(), freqMap.getOrDefault(currStr.toString(), 0) + 1);
				trie.insert(currStr.toString());
				currStr = new StringBuilder();
				return new ArrayList<>();
			}

			// If input char is NOT '#' then we append it to the current prefix string & do
			// a prefix search on Trie with the updated prefix string
			currStr.append(c);
			List<String> strs = trie.search(currStr.toString());

			// The result returned from Trie search is added to a priority queue which sorts
			// the data as expected
			PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
					(a, b) -> b.getValue() == a.getValue() ? a.getKey().compareTo(b.getKey())
							: b.getValue() - a.getValue());
			for (String str : strs)
				pq.add(Map.entry(str, freqMap.get(str)));

			// Finally we filter the result as expected and return it
			List<String> result = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				if (pq.size() > 0)
					result.add(pq.poll().getKey());
			}
			return result;
		}
	}

	class Trie {
		TrieNode root;

		Trie() {
			this.root = new TrieNode(false);
		}

		public void insert(String word) {
			TrieNode curr = root;
			for (char c : word.toCharArray()) {
				if (!curr.nodes.containsKey(c))
					curr.nodes.put(c, new TrieNode(false));
				curr = curr.nodes.get(c);
			}
			curr.isEnd = true;
		}

		public List<String> search(String prefix) {
			TrieNode curr = root;

			for (char c : prefix.toCharArray()) {
				if (!curr.nodes.containsKey(c))
					return new ArrayList<>();
				curr = curr.nodes.get(c);
			}

			// We perform a DFS on the TrieNode on which the prefix String's last character
			// ends
			// All sentences formed from the DFS are potential results
			List<String> result = new ArrayList<>();
			dfs(curr, result, new StringBuilder(prefix.toString()));
			return result;
		}

		void dfs(TrieNode curr, List<String> strs, StringBuilder prefix) {
			if (curr.isEnd)
				strs.add(new String(prefix.toString()));

			for (char c : curr.nodes.keySet()) {
				dfs(curr.nodes.get(c), strs, prefix.append(c));
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
	}

	class TrieNode {
		Map<Character, TrieNode> nodes = new HashMap<>();
		boolean isEnd;

		TrieNode(boolean isEnd) {
			this.isEnd = isEnd;
		}
	}

	// 635. Design Log Storage System
	// parse the date into a single long to make store/ retrieve easier
	// Use tailMap function of TreeMap for retrieve path, which stores the entries
	// in the form of a sorted navigable binary tree.
	public class LogSystem {
		TreeMap<Long, Integer> map;

		public LogSystem() {
			map = new TreeMap<>();
		}

		public void put(int id, String timestamp) {
			int[] st = Arrays.stream(timestamp.split(":")).mapToInt(Integer::parseInt).toArray();
			map.put(convert(st), id);
		}

		// Firstly, we split the given timestamp based on : and store the individual
		// components obtained into an array. Now, in order to put this log's entry into
		// the storage, firstly, we convert this timestamp, now available as individual
		// components in the array into a single number.
		// To obtain a number which is unique for each
		// timestamp, the number chosen is such that it represents the timestamp in
		// terms of seconds. But, doing so for the Year values can lead to very large
		// numbers, which could lead to a potential overflow. Since, we know that the
		// Year's value can start from 2000 only, we subtract 1999 from the Year's value
		public long convert(int[] st) {
			st[1] = st[1] - (st[1] == 0 ? 0 : 1);
			st[2] = st[2] - (st[2] == 0 ? 0 : 1);
			return (st[0] - 1999L) * (31 * 12) * 24 * 60 * 60 + st[1] * 31 * 24 * 60 * 60 + st[2] * 24 * 60 * 60
					+ st[3] * 60 * 60 + st[4] * 60 + st[5];
		}

		public List<Integer> retrieve(String s, String e, String gra) {
			ArrayList<Integer> res = new ArrayList<>();
			long start = granularity(s, gra, false);
			long end = granularity(e, gra, true);
			for (long key : map.tailMap(start).keySet()) {
				if (key >= start && key < end)
					res.add(map.get(key));
			}
			return res;
		}

		public long granularity(String s, String gra, boolean end) {
			HashMap<String, Integer> h = new HashMap<>();
			h.put("Year", 0);
			h.put("Month", 1);
			h.put("Day", 2);
			h.put("Hour", 3);
			h.put("Minute", 4);
			h.put("Second", 5);
			String[] res = new String[] { "1999", "00", "00", "00", "00", "00" };
			String[] st = s.split(":");
			for (int i = 0; i <= h.get(gra); i++) {
				res[i] = st[i];
			}
			int[] t = Arrays.stream(res).mapToInt(Integer::parseInt).toArray();
			if (end)
				t[h.get(gra)]++;
			return convert(t);
		}
	}

	// 1348. Tweet Counts Per Frequency
	// Use TreeMap.subMap()
	class TweetCounts {
		// tweetName : <time : frequency>
		private Map<String, TreeMap<Integer, Integer>> tweetMap;

		public TweetCounts() {
			tweetMap = new HashMap<>();
		}

		public void recordTweet(String tweetName, int time) {
			tweetMap.putIfAbsent(tweetName, new TreeMap<>());
			TreeMap<Integer, Integer> timeFreqMap = tweetMap.get(tweetName);
			timeFreqMap.put(time, timeFreqMap.getOrDefault(time, 0) + 1);
		}

		public List<Integer> getTweetCountsPerFrequency(String freq, String tweetName, int startTime, int endTime) {
			int interval = freq.equals("minute") ? 60 : freq.equals("hour") ? 3600 : 86400;
			int size = (endTime - startTime) / interval;
			int[] count = new int[size + 1];
			TreeMap<Integer, Integer> timeFreqMap = tweetMap.get(tweetName);
			for (Map.Entry<Integer, Integer> entry : timeFreqMap.subMap(startTime, endTime + 1).entrySet()) {
				int index = (entry.getKey() - startTime) / interval;
				count[index] += entry.getValue();
			}
			List<Integer> res = new ArrayList<>();
			for (int num : count)
				res.add(num);
			return res;
		}
	}

	// 362. Design Hit Counter
	class HitCounter {
		private Queue<Integer> hits;

		/** Initialize your data structure here. */
		public HitCounter() {
			this.hits = new LinkedList<Integer>();
		}

		// record a hit
		public void hit(int timestamp) {
			this.hits.add(timestamp);
		}

		// Return the number of hits in the past 5 minutes.
		public int getHits(int timestamp) {
			while (!this.hits.isEmpty()) {
				int diff = timestamp - this.hits.peek();
				if (diff >= 300)
					this.hits.remove();
				else
					break;
			}
			return this.hits.size();
		}
	}
}
