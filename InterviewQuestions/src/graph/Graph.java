package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

// 图数据结构和算法：https://space.bilibili.com/1369507485/favlist?fid=1302158485
// https://space.bilibili.com/21630984/channel/detail?cid=162962&ctype=0
// MST: Prim 从一点起始，或从最小边起始 https://www.youtube.com/watch?v=jsmMtJpPnhU
// 单元最短路径： Dijkstra 和Prim类似,greedy从起点开始扩展 https://www.youtube.com/watch?v=GazC3A4OQTE
// 单元最短路径：A*: Dijkstra is a special case for A* (when the heuristics is zero).
// 单元最短路径： Bellman-Ford 跑n-1个循环，执行两次可查negative cycles https://www.youtube.com/watch?v=lyw4FaxrwHg
// 多元最短路径： Floyd-Warshall, 基于dp更新所有节点间最小距离 https://www.youtube.com/watch?v=4NQ3HnhyNfQ
// 强连通分量：Tarjan 用stack逐个查可联通的最小id https://www.youtube.com/watch?v=wUgWX0nc4NY
public class Graph {
	public static void main(String[] args) {
		int[][] graph = { { 4, 1 }, { 2, 0 }, { 3, 1 }, { 4, 2 }, { 0, 3 } };
		int[][] dislikes = { { 1, 2 }, { 3, 4 }, { 1, 3 }, { 1, 4 } };
		isBipartite(graph);
		possibleBipartition(4, dislikes);
		int[][] courses = { { 1, 0 } };
		findOrder(2, courses);
		crackSafe(2, 2);
	}

	// 判断是否为二分图
	// 如果可以用两种颜色对图中的节点进行着色，并且保证相邻的节点颜色不同，那么这个图就是二分图。
	// https://leetcode.com/problems/is-graph-bipartite/
	public static boolean isBipartite(int[][] graph) {
		// 0 is not colored, 1 is red, -1 is blue
		int[] coloredMap = new int[graph.length];
		Arrays.fill(coloredMap, 0);
		// start with red
		coloredMap[0] = 1;

		Stack<Integer> stack = new Stack<Integer>();
		// prevent nodes that has no connection to be added to stack first
		for (int i = graph.length - 1; i >= 0; i--) {
			stack.add(i);
		}

		boolean[] visited = new boolean[graph.length];
		Arrays.fill(visited, false);
		while (!stack.isEmpty()) {
			int node = stack.pop();

			if (visited[node]) {
				continue;
			}
			visited[node] = true;
			// node with no color can have red for default
			int color = coloredMap[node] == 0 ? 1 : coloredMap[node];

			for (int num : graph[node]) {
				if (coloredMap[num] == color) {
					return false;
				}
				coloredMap[num] = -color;
				stack.add(num);
			}
		}
		return true;
	}

	// 判断是否为二分图: 可否把人分成两组，两组内没有互相不喜欢的成员
	// https://leetcode.com/problems/possible-bipartition
	public static boolean possibleBipartition(int n, int[][] dislikes) {
		// 0 is not colored, 1 is red, -1 is blue
		int[] coloredMap = new int[n + 1];
		Arrays.fill(coloredMap, 0);

		Map<Integer, Set<Integer>> graph = new HashMap<Integer, Set<Integer>>();

		for (int i = 0; i < dislikes.length; i++) {
			int a = dislikes[i][0];
			int b = dislikes[i][1];
			graph.putIfAbsent(a, new HashSet<Integer>());
			graph.putIfAbsent(b, new HashSet<Integer>());
			graph.get(a).add(b);
			graph.get(b).add(a);
		}

		Stack<Integer> stack = new Stack<Integer>();
		// prevent nodes that has no connection to be added to stack first
		for (int i = 1; i <= n; i++) {
			stack.add(i);
		}
		boolean[] visited = new boolean[n + 1];
		Arrays.fill(visited, false);

		while (!stack.isEmpty()) {
			int node = stack.pop();

			if (visited[node]) {
				continue;
			}
			visited[node] = true;
			// node with no color can have red for default
			int color = coloredMap[node] == 0 ? 1 : coloredMap[node];

			if (graph.get(node) == null)
				continue;
			for (int num : graph.get(node)) {
				if (coloredMap[num] == color) {
					return false;
				}
				coloredMap[num] = -color;
				stack.add(num);
			}
		}

		return true;
	}

	// 课程安排的合法性: 拓扑排序，查看是否有环
	// 图为单向图，只用考察入度来判断环
	// https://leetcode.com/problems/course-schedule/description/
	// https://www.youtube.com/watch?v=fskPWs3Nuhc
	public boolean canFinish(int numCourses, int[][] prerequisites) {
		Map<Integer, Set<Integer>> graph = new HashMap<Integer, Set<Integer>>();
		int[] indegree = new int[numCourses];
		Arrays.fill(indegree, 0);
		for (int i = 0; i < prerequisites.length; i++) {
			int a = prerequisites[i][0];
			int b = prerequisites[i][1];
			indegree[b]++;
			graph.putIfAbsent(a, new HashSet<Integer>());
			graph.get(a).add(b);
		}

		Queue<Integer> queue = new LinkedList<Integer>();
		for (int i = 0; i < numCourses; i++) {
			if (indegree[i] == 0) {
				queue.add(i);
			}
		}

		Boolean[] visited = new Boolean[numCourses];
		Arrays.fill(visited, Boolean.FALSE);
		while (!queue.isEmpty()) {
			int course = queue.poll();
			if (visited[course] || !graph.containsKey(course)) {
				continue;
			}
			visited[course] = true;
			for (int canTake : graph.get(course)) {
				indegree[canTake]--;
				if (indegree[canTake] <= 0) {
					queue.add(canTake);
				}
			}
		}

		// if any course still have prerequisites, there is a loop
		for (int course : indegree) {
			if (course > 0) {
				return false;
			}
		}
		return true;
	}

	// 课程安排的顺序
	// 拓扑排序，图为单向图，有可能有环，所有只用考察入度
	// https://leetcode.com/problems/course-schedule-ii/description/
	public static int[] findOrder(int numCourses, int[][] prerequisites) {
		Map<Integer, Set<Integer>> graph = new HashMap<Integer, Set<Integer>>();
		int[] indegree = new int[numCourses];
		Arrays.fill(indegree, 0);
		for (int i = 0; i < prerequisites.length; i++) {
			int a = prerequisites[i][0];
			int b = prerequisites[i][1];
			indegree[a]++;
			graph.putIfAbsent(b, new HashSet<Integer>());
			graph.get(b).add(a);
		}

		Queue<Integer> ordered = new LinkedList<Integer>();
		Queue<Integer> queue = new LinkedList<Integer>();
		for (int i = 0; i < numCourses; i++) {
			if (indegree[i] == 0) {
				queue.add(i);
				ordered.add(i);
			}
		}

		Boolean[] visited = new Boolean[numCourses];
		Arrays.fill(visited, Boolean.FALSE);
		while (!queue.isEmpty()) {
			int course = queue.poll();
			if (visited[course] || !graph.containsKey(course)) {
				continue;
			}
			visited[course] = true;
			for (int canTake : graph.get(course)) {
				indegree[canTake]--;
				if (indegree[canTake] <= 0) {
					queue.add(canTake);
					ordered.add(canTake);
				}
			}
		}

		// if any course still have prerequisites, there is a loop
		for (int course : indegree) {
			if (course > 0) {
				return new int[0];
			}
		}
		return ordered.stream().mapToInt(Integer::intValue).toArray();
	}

	// 并查集 Disjoint Set Union (Union-Find)
	// 并查集可以动态地连通两个点，并且可以非常快速地判断两个点是否连通。
	class DSU {
		int[] parent;
		int[] rank;

		DSU(int size) {
			this.parent = new int[size];
			for (int i = 0; i < size; i++)
				parent[i] = i;
			this.rank = new int[size];
		}

		public int find(int x) {
			if (parent[x] != x) {
				parent[x] = find(parent[x]);
			}
			return parent[x];
		}

		public boolean union(int x, int y) {
			int xParent = find(x);
			int yParent = find(y);
			if (xParent == yParent) {
				return false;
			}
			if (rank[xParent] < rank[yParent]) {
				parent[xParent] = yParent;
			} else if (rank[xParent] > rank[yParent]) {
				parent[yParent] = xParent;
			} else {
				parent[yParent] = xParent;
				rank[xParent]++;
			}
			return true;
		}
	}

	// Account Merge
	// https://leetcode.com/problems/accounts-merge/
	// DSU, 每个email先连到当前账户index上，然后
	public List<List<String>> accountsMerge(List<List<String>> accountList) {
		int accountListSize = accountList.size();
		DSU dsu = new DSU(accountListSize);

		// Maps email to their component index
		Map<String, Integer> emailGroup = new HashMap<>();

		for (int i = 0; i < accountListSize; i++) {
			int accountSize = accountList.get(i).size();

			for (int j = 1; j < accountSize; j++) {
				String email = accountList.get(i).get(j);

				// If this is the first time seeing this email then
				// assign component group as the account index
				if (!emailGroup.containsKey(email)) {
					emailGroup.put(email, i);
				} else {
					// If we have seen this email before then union this
					// group with the previous group of the email
					dsu.union(i, emailGroup.get(email));
				}
			}
		}

		// Store emails corresponding to the component's representative
		Map<Integer, List<String>> components = new HashMap<Integer, List<String>>();
		for (String email : emailGroup.keySet()) {
			int group = emailGroup.get(email);
			int groupRep = dsu.find(group);

			if (!components.containsKey(groupRep)) {
				components.put(groupRep, new ArrayList<String>());
			}

			components.get(groupRep).add(email);
		}

		// Sort the components and add the account name
		List<List<String>> mergedAccounts = new ArrayList<>();
		for (int group : components.keySet()) {
			List<String> component = components.get(group);
			Collections.sort(component);
			component.add(0, accountList.get(group).get(0));
			mergedAccounts.add(component);
		}

		return mergedAccounts;
	}

	// 并查集：冗余连接
	// https://leetcode.com/problems/redundant-connection/solution/
	// 直接使用并查集遍历所有边，在有边之前两点已经连接的话，说明这条边多余
	public int[] findRedundantConnection(int[][] edges) {
		int n = edges.length;
		DSU dsu = new DSU(n + 1);
		for (int[] edge : edges) {
			int a = edge[0];
			int b = edge[1];
			// a previous edge already connect a and b, then current edge is dup
			if (!dsu.union(a, b)) {
				return edge;
			}
		}
		return new int[] { -1, -1 };
	}

	// 269. Alien Dictionary
	public String alienOrder(String[] words) {
		if (words.length == 0 || words == null)
			return "";
		Map<Character, Set<Character>> graph = new HashMap<>();
		int[] indegree = new int[26];
		// build graph
		for (int i = 1; i < words.length; i++) {
			String first = words[i - 1];
			String second = words[i];
			for (int j = 0; j < Math.min(first.length(), second.length()); j++) {
				char fromFirst = first.charAt(j);
				char fromSecond = second.charAt(j);
				if (fromFirst != fromSecond) {
					Set<Character> indegreeSet = new HashSet<>();
					if (graph.containsKey(fromSecond)) {
						indegreeSet = graph.get(fromSecond);
					}
					if (!indegreeSet.contains(fromFirst)) {
						indegreeSet.add(fromFirst);
						indegree[fromSecond - 'a']++;
						break;
					}
				}
			}
		}
		// BFS to find order
		StringBuilder sb = new StringBuilder();
		Queue<Character> queue = new LinkedList<>();
		for (char c : graph.keySet()) {
			if (indegree[c - 'a'] == 0) {
				sb.append(c);
				queue.add(c);
			}
		}
		while (!queue.isEmpty()) {
			char curr = queue.poll();
			for (char c : graph.keySet()) {
				Set<Character> indegreeSet = graph.get(c);
				if (indegreeSet.contains(curr)) {
					indegree[c - 'a']--;
					indegreeSet.remove(curr);
					if (indegree[c - 'a'] == 0) {
						queue.add(c);
						sb.append(c);
					}
				}
			}
		}
		String result = sb.toString();
		return result.length() == graph.size() ? result : "";
	}

	// Clone Graph
	// 边DFS边clone
	private HashMap<Node, Node> cloned = new HashMap<>();

	public Node cloneGraph(Node node) {
		if (node == null)
			return null;

		if (cloned.containsKey(node)) {
			return cloned.get(node);
		}
		// 做好新节点马上放入map中，防止死循环
		Node newNode = new Node(node.val, new ArrayList<Node>());
		cloned.put(node, newNode);
		for (Node neightbor : node.neighbors) {
			newNode.neighbors.add(cloneGraph(neightbor));
		}
		return newNode;
	}

	class Node {
		public int val;
		public List<Node> neighbors;

		public Node() {
			val = 0;
			neighbors = new ArrayList<Node>();
		}

		public Node(int _val) {
			val = _val;
			neighbors = new ArrayList<Node>();
		}

		public Node(int _val, ArrayList<Node> _neighbors) {
			val = _val;
			neighbors = _neighbors;
		}
	}

	// 399. Evaluate Division
	// 用weighted graph: HashMap<String, HashMap<String, Double>> graph

	// Cracking the Safe
	// 总密码数为k^n个，直接graph做dfs即可
	public static String crackSafe(int n, int k) {
		char[] start = new char[n];
		for (int i = 0; i < n; i++) {
			start[i] = '0';
		}
		Set<String> visited = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		String startString = String.valueOf(start);
		sb.append(startString);
		visited.add(startString);
		return crackSafeBackTracking(n, k, visited, startString, sb) ? sb.toString() : "";
	}

	public static boolean crackSafeBackTracking(int n, int k, Set<String> visited, String preString, StringBuilder sb) {
		int totalPasswords = (int) Math.pow(k, n);
		if (visited.size() == totalPasswords)
			return true;

		String prefix = preString.substring(1, n);
		for (int i = 0; i < k; i++) {
			String next = prefix + i;
			if (visited.contains(next)) {
				continue;
			}
			sb.append(i);
			visited.add(next);
			if (crackSafeBackTracking(n, k, visited, next, sb)) {
				return true;
			}
			visited.remove(next);
			sb.setLength(sb.length() - 1);
		}
		return false;
	}

	// Most Stones Removed with Same Row or Column
	// https://www.youtube.com/watch?v=796HQmL5CO8
	// 最终的无法移除的石头一定位于不同行和不同列
	// 使用DSU,表示不同的行/列坐标，每个石头的横纵坐标可视为连通在一起的
	// 可移除石头数量 = 石头总数 - 连通分量个数

	// 1761. Minimum Degree of a Connected Trio in a Graph
	public int minTrioDegree(int n, int[][] edges) {
		int min = Integer.MAX_VALUE;
		Map<Integer, HashSet<Integer>> graph = new HashMap<Integer, HashSet<Integer>>();
		for (int[] edge : edges) {
			HashSet<Integer> setA = graph.getOrDefault(edge[0], new HashSet<Integer>());
			setA.add(edge[1]);
			graph.put(edge[0], setA);
			HashSet<Integer> setB = graph.getOrDefault(edge[1], new HashSet<Integer>());
			setB.add(edge[0]);
			graph.put(edge[1], setB);
		}

		for (int i = 1; i <= n; i++) {
			for (int j = i + 1; j <= n; j++) {
				HashSet<Integer> setI = graph.getOrDefault(i, new HashSet<Integer>());
				// 剪枝
				if (setI.contains(j)) {
					HashSet<Integer> setJ = graph.getOrDefault(j, new HashSet<Integer>());
					for (int k = j + 1; k <= n; k++) {
						HashSet<Integer> setK = graph.getOrDefault(k, new HashSet<Integer>());
						// found a trio
						if (setK.contains(i) && setK.contains(j)) {
							min = Math.min(setI.size() + setJ.size() + setK.size() - 6, min);
						}
					}
				}
			}
		}
		if (min == Integer.MAX_VALUE)
			return -1;
		return min;
	}

	// 286. Walls and Gates
	static int[][] directions = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

	public void wallsAndGates(int[][] rooms) {
		int m = rooms.length;
		int n = rooms[0].length;
		boolean[][] visited = new boolean[m][n];
		Queue<int[]> queue = new ArrayDeque<>();
		// BFS start with all gates
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (rooms[i][j] == 0) {
					queue.add(new int[] { i, j });
				}
			}
		}
		int length = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				int[] node = queue.poll();
				int a = node[0];
				int b = node[1];
				if (visited[a][b]) {
					continue;
				}
				visited[a][b] = true;
				rooms[a][b] = Math.min(rooms[a][b], length);
				for (int[] d : directions) {
					int nextA = a + d[0];
					int nextB = b + d[1];
					if (nextA < m && nextB < n && nextA >= 0 && nextB >= 0 && rooms[nextA][nextB] != -1) {
						queue.add(new int[] { nextA, nextB });
					}
				}
			}
			length++;
		}
	}
}
