package company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

// system design:
// 1. job ad, click remove balance
// 2. design review page
// 3. job alert
// 4. job/resume search

// https://github.com/interviewdiscussion/files/tree/master/Indeed%20Onsite%E8%AE%B0%E5%BD%95
public class Indeed {
	public static void main(String[] args) throws FileNotFoundException {
		String rawTitle = "senior software engineer";
		String[] cleanTitles = { "software engineer", "mechanical engineer", "senior software engineer" };

		String result = getHighestTitle(rawTitle, cleanTitles);
		System.out.println(result);
		query("a | b");
	}

	// Given m sorted lists, find out the elements more than k times. If an element
	// appear more than once in a list, count it only once.

	// m 个stream做词频统计
	// Use a min-heap. 类似于merge k-sorted lists的方法。首先把每个list的第一个元素放入min PQ 中去。
	List<Integer> findMoreThanKTimes(List<List<Integer>> lists, int k) {
		if (lists == null || lists.size() == 0) {
			return null;
		}
		List<Integer> result = new ArrayList<>();
		PriorityQueue<Node> pq = new PriorityQueue<>(new MyNodeComparator());
		// step 1: put the first node of each list into the queue
		for (List<Integer> list : lists) {
			if (list != null && !list.isEmpty()) {
				pq.offer(new Node(list.iterator()));
			}
		}
		while (!pq.isEmpty()) {
			int currVal = pq.peek().val;
			int count = 0;
			removeSmallestRepeatedElement(pq);
			// get all repeated elements from the pq
			while (!pq.isEmpty() && currVal == pq.peek().val) {
				count++;
				removeSmallestRepeatedElement(pq);
			}
			if (count >= k) {
				result.add(currVal);
			}
		}
		return result;
	}

	public void removeSmallestRepeatedElement(PriorityQueue<Node> pq) {
		Node node = pq.poll();
		int nodeVal = node.val;
		// put the next node into pq, skip the repeated elements
		while (node.iterator.hasNext()) {
			int nextNodeVal = node.iterator.next();
			if (nodeVal == nextNodeVal) {
				continue;
			} else {
				node.val = nextNodeVal;
				pq.offer(node);
				break;
			}
		}
	}

	class MyNodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node a, Node b) {
			return a.val - b.val;
		}
	}

	class Node {
		int val;
		Iterator<Integer> iterator;

		public Node(Iterator<Integer> iterator) {
			this.iterator = iterator;
			val = iterator.next();
		}
	}

	/*
	 * Given a stream of input, and a API int getNow() to get the current time
	 * stamp, Finish two methods: 1. void record(int val) to save the record. 2.
	 * double getAvg() to calculate the averaged value of all the records in 5
	 * minutes.
	 */
	public class Moving_Average {
		private Queue<Event> queue = new LinkedList<>();
		private int sum = 0;

		// 这个是每次记录读进来的时间的,这个不用自己写,就是直接返回当前系统时间
		private int getNow() {
			return 0; // 暂时写个0，苟活
		}

		private boolean isExpired(int curTime, int preTime) {
			return curTime - preTime > 300;
		}

		private void removeExpireEvent() {
			while (!queue.isEmpty() && isExpired(getNow(), queue.peek().time)) {
				Event curE = queue.poll();
				sum -= curE.val;
			}
		}

		public void record(int val) {
			Event event = new Event(getNow(), val);
			queue.offer(event);
			sum += val;
			removeExpireEvent();
		}

		public double getAvg() {
			removeExpireEvent();
			if (!queue.isEmpty()) {
				return (double) sum / queue.size();
			}
			return 0.0;
		}
	}

	/*
	 * follow up 1. memory不够大怎么办（数据点非常密集，5 分钟就把内存爆了）2.getMedium方法实现 需要注意的是follow
	 * up都是在原有的代码基础上做改进。
	 * 
	 * 对于1的方法，数据点密集的话，选择10秒的时间段，合并数据，得到一个10秒的和和数据数量， 那么queue
	 * size就被一个int变量替换掉，这样丢掉过期数据的时候要更新sum和数据总和。这样会造成一定的偏差，但是没办法，条件不好内存不够就忍忍吧。
	 * 
	 * 对于2， 就是quick select的find kth in an array的方法。复杂度是O(n).
	 */
	// 应付内存不够的办法。
	public class Moving_Average_2 {
		// queue的容量被限制
		private Deque<Event> queue = new LinkedList<>(); // 改成deque的话，可以从后面查
		private long sum = 0; // 改用long显得严谨点儿
		int dataNum = 0;

		// 这个是每次记录读进来的时间的,这个不用自己写,就是直接返回当前系统时间,假设它返回的是秒
		private int getNow() {
			return 0;
		}

		private boolean isExpired(int curTime, int preTime) {
			return curTime - preTime > 300;
		}

		private void removeExpireEvent() {
			while (!queue.isEmpty() && isExpired(getNow(), queue.peekFirst().time)) {
				Event curE = queue.poll();
				sum -= curE.val;
				dataNum -= curE.size;
			}
		}

		public void record(int val) { // 其实就是record这里有了两种办法，一种是建个新的，另一种就是合起来
			Event last = queue.peekLast();
			if (getNow() - last.time < 10) {
				last.size += 1;
				last.val += val;
			} else {
				Event event = new Event(getNow(), val);
				queue.offer(event);
			}
			dataNum += 1;
			sum += val;
			removeExpireEvent();
		}

		public double getAvg() {
			removeExpireEvent();
			if (!queue.isEmpty()) {
				return (double) sum / dataNum;
			}
			return 0.0;
		}

		// 实现find Median，其实O1操作的话，要始终维护两个heap，这样塞进去会很慢
		// 原有基础上实现的话，那就直接quick select的办法了。
		// 复杂度是On，因为每次average case是去掉一半，就是O(n)+O(n/2)+O(n/4)+... 最后出来是O(2n)
		// 那这个需要把整个queue给倒出来再塞回去。
		public double getMedian() {
			removeExpireEvent();
			int[] temp = new int[queue.size()];
			for (int i = 0; i < queue.size(); i++) {
				temp[i] = queue.poll().val;
			}
			// 这里还得把queue还原回去,先不写了。
			int len = temp.length;
			if (len % 2 == 0) {
				return 0.5 * (findKth(temp, len / 2, 0, len - 1) + findKth(temp, len / 2 - 1, 0, len - 1));
			}
			return (double) findKth(temp, len / 2, 0, len - 1);
		}

		public int findKth(int[] temp, int k, int start, int end) {
			int pivot = temp[start];
			int left = start, right = end;
			while (left < right) {
				while (temp[right] > pivot && left < right) {
					right--;
				}
				while (temp[left] <= pivot && left < right) {
					left++;
				}
				swap(temp, left, right);
			}
			swap(temp, start, right);
			if (k == right) {
				return pivot;
			} else if (k < right) {
				return findKth(temp, k, start, right - 1);
			}

			return findKth(temp, k, right + 1, end);
		}

		public void swap(int[] temp, int left, int right) {
			int i = temp[left];
			temp[left] = temp[right];
			temp[right] = i;
		}
	}

	class Event {
		int val;
		int time;
		int size;

		public Event(int val, int time) {
			this.val = val;
			this.time = time;
			this.size = 1;
		}
	}

	// 压缩树
	// 如果比较full的tree，用heap的实现方式。比较sparse的tree就用map。
	// 介于中间的可以用两个数组，一个表示value，一个表示这个节点在第一种表示方式下的index。
	public int[] compressDenseTree(TreeNode root) {
		int height = getHeight(root);
		if (height == 0) {
			return new int[0];
		}
		// dense tree的情况下,默认null node位置放0。(假设原来的tree里面没有0)
		int len = (int) Math.pow(2, height);
		int[] heap = new int[len];
		// BFS一下就压缩好了
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		Queue<Integer> idxQueue = new LinkedList<>();
		// 这里如果是1开头,那么就是(2i, 2i+1),如果是0开头,就是(2i+1,2i+2),其实1,2,3一下就看出来了。
		idxQueue.offer(1);

		while (!queue.isEmpty()) {
			TreeNode cur = queue.poll();
			Integer curI = idxQueue.poll();
			heap[curI] = cur.val;
			if (cur.left != null) {
				queue.offer(cur.left);
				idxQueue.offer(2 * curI);
			}
			if (cur.right != null) {
				queue.offer(cur.right);
				idxQueue.offer(2 * curI + 1);
			}
		}

		return heap;
	}

	public Map<Integer, Integer> compressSparseTree(TreeNode root) {
		// 前提假设是sparse tree,用map来记录,key是index,value是root的value
		Map<Integer, Integer> record = new HashMap<>();
		if (root == null) {
			return record;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		Queue<Integer> idxQueue = new LinkedList<>();
		queue.offer(root);
		idxQueue.offer(1);

		while (!queue.isEmpty()) {
			TreeNode cur = queue.poll();
			int idx = idxQueue.poll();
			record.put(idx, cur.val);
			if (cur.left != null) {
				queue.offer(cur.left);
				idxQueue.offer(2 * idx);
			}
			if (cur.right != null) {
				queue.offer(cur.right);
				idxQueue.offer(2 * idx + 1);
			}
		}
		return record;
	}

	public int getHeight(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int left = getHeight(root.left);
		int right = getHeight(root.right);
		return Math.max(left, right) + 1;
	}

	class TreeNode {
		int val;
		TreeNode left, right;

		public TreeNode(int val) {
			this.val = val;
			this.left = null;
			this.right = null;
		}
	}

	// Given a git commit (wiki:...), each commit has an id. Find all the commits
	// that we have.
	class GitNode {
		int id;
		List<GitNode> parents;

		public GitNode(int id) {
			this.id = id;
			this.parents = new ArrayList<>();
		}

		public List<GitNode> findAllCommits(GitNode node) {
			List<GitNode> res = new ArrayList<>();
			Queue<GitNode> queue = new LinkedList<>();
			Set<GitNode> visited = new HashSet<>(); // 去重

			queue.offer(node);
			visited.add(node);

			while (!queue.isEmpty()) {
				GitNode cur = queue.poll();
				res.add(cur);
				for (GitNode par : cur.parents) {
					if (!visited.contains(par)) {
						queue.offer(par);
						visited.add(par);
					}
				}
			}
			return res;
		}
	}

	// Follow Up
	// 找到两个commit的最近公共parent commit。两个点同时开始bfs
	public GitNode findLCA(GitNode node1, GitNode node2) {
		if (node1 == null || node2 == null)
			return null;

		Queue<GitNode> q1 = new LinkedList<>();
		q1.offer(node1);
		Queue<GitNode> q2 = new LinkedList<>();
		q2.offer(node2);

		Set<GitNode> s1 = new HashSet<>();
		Set<GitNode> s2 = new HashSet<>();
		s1.add(node1);
		s2.add(node2);
		// int len1 = 1, len2 = 1; //万一是要求最短路径长度呢。

		// while里面是&&,因为一旦其中一个终结那也不用搜了。
		while (!q1.isEmpty() && !q2.isEmpty()) {
			// 每个BFS都是一层一层的扫
			int size1 = q1.size();
			while (size1-- > 0) {
				GitNode cur1 = q1.poll();
				for (GitNode par1 : cur1.parents) {
					if (s2.contains(par1)) {
						return par1;
					}
					if (!s1.contains(par1)) {
						q1.offer(par1);
						s1.add(par1);
					}
				}
			}

			int size2 = q2.size();
			while (size2-- > 0) {
				GitNode cur2 = q2.poll();
				for (GitNode par2 : cur2.parents) {
					if (s1.contains(par2)) {
						return par2;
					}
					if (!s2.contains(par2)) {
						q2.offer(par2);
						s2.add(par2);
					}
				}
			}
		}
		return null;
	}

	// Given a rawTitle, and a list(or array) of clean titles.
	public static String getHighestTitle(String rawTitle, String[] cleanTitles) {
		String res = "";
		int highScore = 0;
		for (String ct : cleanTitles) {
			int curScore = getScore(rawTitle, ct);
			if (curScore > highScore) {
				highScore = curScore;
				res = ct;
			}
		}
		return res;
	}

	// 思路非常简单,两个title分别去查一下就行了。
	public static int getScore(String raw, String ct) {
		String[] rA = raw.split(" ");
		String[] cA = ct.split(" ");
		Set<String> rawSet = new HashSet<>();
		Set<String> cleanSet = new HashSet<>();
		for (String r : rA) {
			rawSet.add(r);
		}
		for (String c : cA) {
			cleanSet.add(c);
		}
		rawSet.retainAll(cleanSet);

		return rawSet.size();
	}

	// Follow Up: raw title和clean title中有duplicate word怎么办
	public String getHightestTitleWithDup(String rawTitle, String[] cleanTitles) {
		String res = "";
		int highScore = 0;
		String[] rA = rawTitle.split(" ");
		for (String ct : cleanTitles) {
			String[] cA = ct.split(" ");
			int temp = getScoreWithDup(rA, cA);
			System.out.println("temp is " + temp);
			if (temp > highScore) {
				highScore = temp;
				res = ct;
			}
		}
		return res;
	}

	// 二维矩阵里面每个位置都要查,因为不一定是从哪个位置开始匹配的,反正复杂度都是一样的。
	public int getScoreWithDup(String[] rA, String[] cA) {
		int col = rA.length;
		int row = cA.length;
		int res = 0;
		int[][] dp = new int[row][col];
		for (int i = 0; i < row; i++) {
			String cCur = cA[i];
			for (int j = 0; j < col; j++) {
				String rCur = rA[j];
				if (rCur.equals(cCur)) {
					if (i == 0 || j == 0) {
						dp[i][j] = 1;
					} else {
						dp[i][j] = Math.max(1, dp[i - 1][j - 1] + 1);
					}
				}
				res = Math.max(res, dp[i][j]);
			}
		}
		return res;
	}

	// 判断一段Python代码是否是合法
	// 1.第一行不能缩进。2.一行冒号结尾的code，下一行必须比这一行缩进的多。
	// stack一下就行，遇到伸出头的就一路弹出
	public boolean validate(String[] lines) {
		// 就用stack来存之前的line就行
		Stack<String> stack = new Stack<>();
		for (String line : lines) {
			int level = getIndent(line);
			// 先检查是不是第一行
			if (stack.isEmpty()) {
				if (level != 0) {
					System.out.println(line);
					return false;
				}
			}
			// 再检查上一行是不是control statement
			else if (stack.peek().charAt(stack.peek().length() - 1) == ':') {
				if (getIndent(stack.peek()) + 1 != level) {
					System.out.println(line);
					return false;
				}
			} else {
				while (!stack.isEmpty() && getIndent(stack.peek()) > level) {
					stack.pop();
				}
				if (getIndent(stack.peek()) != level) {
					System.out.println(line);
					return false;
				}
			}
			stack.push(line);
		}
		return true;
	}

	// 这里如果它说n个空格算一次tab的话，就最后返回的时候res/n好了。
	public int getIndent(String line) {
		String trimedLine = line.trim();
		return line.length() - trimedLine.length();
	}

	// fuzzy search
	public static List<Integer> query(String q) throws FileNotFoundException {
		// https://stackoverflow.com/questions/17105299/regex-to-split-string-based-on-operators-and-retain-operator-in-answer
		String[] queryTokens = q.split("\\s+");
		List<String> cleanTokens = new ArrayList<>();
		boolean isAnd = false;
		boolean isOr = false;
		for (String token : queryTokens) {
			if (token.equals("&")) {
				isAnd = true;
			} else if (token.equals("|")) {
				isOr = true;
			} else {
				cleanTokens.add(token);
			}
		}
		// token : HashMap<docId : frequency>
		Map<String, Map<Integer, Integer>> invertedMap = new HashMap<>();
		File myObj = new File("src/company/text.txt");
		Scanner myReader = new Scanner(myObj);
		int line = 1;
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			String[] tokens = data.split("\\s+");
			for (String token : tokens) {
				for (String cToken : cleanTokens) {
					if (token.equals(cToken)) {
						Map<Integer, Integer> map = invertedMap.getOrDefault(token, new HashMap<>());
						int count = map.getOrDefault(line, 0);
						count++;
						map.put(line, count);
						invertedMap.put(token, map);
					}
				}
			}
			line++;
		}
		myReader.close();

		// grab valid docs first
		Set<Integer> docs = new HashSet<>();
		docs.addAll(invertedMap.get(cleanTokens.get(0)).keySet());
		for (int i = 1; i < cleanTokens.size(); i++) {
			String cToken = cleanTokens.get(i);
			Set<Integer> currDocs = new HashSet<>();
			currDocs.addAll(invertedMap.get(cToken).keySet());
			if (isAnd) {
				docs.retainAll(currDocs);
			} else if (isOr) {
				docs.addAll(currDocs);
			}
		}

		// result
		PriorityQueue<Doc> pq = new PriorityQueue<>();
		for (int docId : docs) {
			int frequency = 0;
			for (String token : cleanTokens) {
				Map<Integer, Integer> map = invertedMap.getOrDefault(token, new HashMap<>());
				frequency += map.getOrDefault(docId, 0);
			}
			pq.add(new Doc(docId, frequency));
		}

		List<Integer> result = new ArrayList<>();
		while (!pq.isEmpty()) {
			result.add(pq.poll().docId);
		}
		return result;
	}

	static class Doc implements Comparable<Doc> {
		int docId;
		int frequency;

		public Doc(int docId, int frequency) {
			this.docId = docId;
			this.frequency = frequency;
		}

		@Override
		public int compareTo(Doc other) {
			if (this.frequency == other.frequency) {
				return this.docId - other.docId;
			}
			return other.frequency - this.frequency;
		}
	}

	// reverse String except HTML entity
	// a HTML entity must start with "&" and end with ";"
	// Step 1: reverse non-html tokens, and store the result into a list. For the
	// HTML entity, do not reverse but just store into the result.
	// Step 2: construct the final result just concatenate the list in a reverse
	// order.
	public String reverseHTML(String s) {
		if (s == null || s.length() <= 1) {
			return s;
		}
		List<String> tokens = new ArrayList<>();
		int i = 0;
		int j = 0;
		while (j < s.length()) {
			char c = s.charAt(j);
			// Case 1: if c != &
			if (c != '&') {
				j++;
			} else {
				// step 1: reverse substring before &
				if (j != 0) {
					String token = reverse(s, i, j - 1);
					tokens.add(token);
				}
				// step 2: put the html entity into the tokens
				StringBuffer sb = new StringBuffer();
				while (j < s.length() && s.charAt(j) != ';') {
					sb.append(s.charAt(j));
					j++;
				}
				if (j < s.length()) {
					sb.append(';');
					tokens.add(sb.toString());
					// step 3: update i
					j++;
					i = j;
				}
			}
		}

		// Reverse the trailing chars
		if (i < j) {
			String token = reverse(s, i, s.length() - 1);
			tokens.add(token);
		}
		// Step 2: concatenate the final result
		StringBuffer result = new StringBuffer();
		for (i = tokens.size() - 1; i >= 0; i--) {
			result.append(tokens.get(i));
		}
		return result.toString();

	}

	private String reverse(String s, int start, int end) {
		StringBuffer sb = new StringBuffer();
		while (start <= end) {
			sb.append(s.charAt(end));
			end--;
		}
		return sb.toString();

	}

	// get bit from integer
	public int getBit(int n, int k) {
		return (n >> k) & 1;
	}

	// set bit in integer
	public static int setBit(int n, int k) {
		// Create mask
		int mask = 1 << k;
		// Set bit
		return n | mask;
	}
}
