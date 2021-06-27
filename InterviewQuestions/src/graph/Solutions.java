package graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Solutions {
	public static void main(String[] args) {
		int[][] graph = { { 4, 1 }, { 2, 0 }, { 3, 1 }, { 4, 2 }, { 0, 3 } };
		int[][] dislikes = { { 1, 2 }, { 3, 4 }, { 1, 3 }, { 1, 4 } };
		isBipartite(graph);
		possibleBipartition(4, dislikes);
		int[][] courses = { { 1, 0 } };
		findOrder(2, courses);
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

	// 并查集
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
}
