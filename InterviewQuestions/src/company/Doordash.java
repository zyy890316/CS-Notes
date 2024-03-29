package company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;

public class Doordash {
	// System design:
	// 1. donation system: https://www.youtube.com/watch?v=mNiHK_WK5KM
	// 2. review system
	// 3. news feed
	// 4. distributed logging system
	public static void main(String[] args) {
		Map<Integer, List<Integer>> map = new HashMap<>();
		// from/to/weight
		map.put(0, Arrays.asList(1, 2, 1));
		map.put(1, Arrays.asList(2, 3, 1));
		map.put(2, Arrays.asList(3, 4, 1));
		map.put(3, Arrays.asList(4, 5, 1));
		map.put(4, Arrays.asList(5, 1, 3));
		map.put(5, Arrays.asList(1, 3, 2));
		map.put(6, Arrays.asList(5, 3, 1));
		List<Integer> cities = Arrays.asList(1, 2, 3, 4, 5);
		shortestPath(map, cities);
		kSimilarity("bccaba", "abacbc");
	}

	// 返回横或纵坐标相同的最近城市
	// https://leetcode.com/discuss/interview-question/1379696/DoorDASH-Onsite
	// 还可以进一步优化，使用 Map<Integer, TreeMap<Integer, String>>
	static class CityCoordinate implements Comparable<CityCoordinate> {
		int coordinate;
		String cityname;

		public CityCoordinate(int c, String city) {
			this.coordinate = c;
			this.cityname = city;
		}

		@Override
		public int compareTo(CityCoordinate c) {
			return Integer.compare(this.coordinate, c.coordinate);
		}
	}

	public static List<String> closestStraightCity(List<String> c, List<Integer> x, List<Integer> y,
			List<String> queries) {
		Map<String, int[]> cities = new HashMap<>();
		Map<Integer, List<CityCoordinate>> xMap = new HashMap<>();
		Map<Integer, List<CityCoordinate>> yMap = new HashMap<>();

		int N = c.size();
		for (int i = 0; i < N; i++) {
			List<CityCoordinate> xList = xMap.getOrDefault(x.get(i), new ArrayList<>());
			xList.add(new CityCoordinate(y.get(i), c.get(i)));
			xMap.put(x.get(i), xList);

			List<CityCoordinate> yList = yMap.getOrDefault(y.get(i), new ArrayList<>());
			yList.add(new CityCoordinate(x.get(i), c.get(i)));
			yMap.put(y.get(i), yList);

			cities.put(c.get(i), new int[] { x.get(i), y.get(i) });
		}

		// For each x coordinate, sort the corresponding list of cities on y coordinate
		for (int xCoodinate : xMap.keySet()) {
			Collections.sort(xMap.get(xCoodinate));
		}

		// For each y coordinate, sort the corresponding list of cities on x coordinate
		for (int yCoodinate : yMap.keySet()) {
			Collections.sort(yMap.get(yCoodinate));
		}

		List<String> result = new ArrayList<>();
		for (String q : queries) {
			int[] location = cities.get(q);

			List<CityCoordinate> xList = xMap.get(location[0]);
			List<CityCoordinate> yList = yMap.get(location[1]);

			CityCoordinate closestX = getClosestCityOnAnAxis(xList, location[1], q);
			CityCoordinate closestY = getClosestCityOnAnAxis(yList, location[0], q);

			result.add(getClosestCity(closestX, closestY, location));
		}

		return result;
	}

	private static String getClosestCity(CityCoordinate xCity, CityCoordinate yCity, int[] location) {
		int manhattanDistance1 = Math.abs(location[0] - xCity.coordinate);
		int manhattanDistance2 = Math.abs(location[1] - yCity.coordinate);

		if (manhattanDistance1 < manhattanDistance2) {
			return xCity.cityname;
		} else if (manhattanDistance1 == manhattanDistance2) {
			return xCity.cityname.compareTo(yCity.cityname) < 0 ? xCity.cityname : yCity.cityname;
		} else {
			return yCity.cityname;
		}
	}

	private static CityCoordinate getClosestCityOnAnAxis(List<CityCoordinate> list, int location, String city) {
		int index = Collections.binarySearch(list, new CityCoordinate(location, city));
		CityCoordinate leftCity = null, rightCity = null;
		int leftDist = Integer.MAX_VALUE, rightDist = Integer.MAX_VALUE;

		if (index > 0) {
			leftCity = list.get(index - 1);
			leftDist = Math.abs(leftCity.coordinate - location);
		}

		if (index < list.size() - 1) {
			rightCity = list.get(index + 1);
			rightDist = Math.abs(rightCity.coordinate - location);
		}

		if (leftDist < rightDist) {
			return leftCity;
		} else if (leftDist == rightDist) {
			return leftCity.cityname.compareTo(rightCity.cityname) < 0 ? leftCity : rightCity;
		} else {
			return rightCity;
		}
	}

	// 查看菜单的变化
	// https://leetcode.com/discuss/interview-question/1367130/doordash-phone-interview/1026887
	public static class Node {
		String key;
		int value;
		boolean isActive;
		List<Node> children;

		public Node(String key, int value, boolean isActive) {
			this.key = key;
			this.value = value;
			this.isActive = isActive;
			this.children = new ArrayList<>();
		}

		public boolean equals(Node node) {
			if (node == null) {
				return false;
			}

			return this.key == node.key && this.value == node.value && this.isActive == node.isActive;
		}

		public String toString() {
			return this.key;
		}
	}

	public static int getModifiedItems(Node oldMenu, Node newMenu) {
		if (oldMenu == null && newMenu == null) {
			return 0;
		}
		int count = 0;
		if (oldMenu == null || newMenu == null || !oldMenu.equals(newMenu)) {
			System.out.println(oldMenu + " " + newMenu);
			count++;
		}

		Map<String, Node> children1 = getChildNodes(oldMenu);
		Map<String, Node> children2 = getChildNodes(newMenu);

		for (String key : children1.keySet()) {
			count += getModifiedItems(children1.get(key), children2.getOrDefault(key, null));
		}

		for (String key : children2.keySet()) {
			if (!children1.containsKey(key)) {
				count += getModifiedItems(null, children2.get(key));
			}
		}

		return count;
	}

	private static Map<String, Node> getChildNodes(Node menu) {
		Map<String, Node> map = new HashMap<>();
		if (menu == null) {
			return map;
		}

		for (Node node : menu.children) {
			map.put(node.key, node);
		}

		return map;
	}

	// Encode Hours
	static class Time {
		int day = -1;
		int hour = 0;
		int min = 0;
		boolean am = false;

		public Time(int day, int hour, int min, boolean am) {
			this.day = day;
			this.hour = hour;
			this.min = min;
			this.am = am;
		}

		void add(int mins) {
			hour += (mins + min) / 60;
			min = (mins + min) % 60;
			if (hour >= 13) {
				am = !am;
				hour = hour % 12;
				if (am)
					day += 1;
			}
		}

		int getNumeric() {
			return (((day * 100) + hour) * 100) + min;
		}

		boolean equals(Time t2) {
			return day == t2.day && hour == t2.hour && min == t2.min && Boolean.compare(am, t2.am) == 0;
		}
	}

	static List<Integer> getIntervals(String start, String end) {
		List<Integer> intervals = new ArrayList<>();
		Time startTime = getTime(start);
		Time endTime = getTime(end);
		while (!startTime.equals(endTime)) {
			startTime.add(5);
			System.out.println(startTime.getNumeric());
			intervals.add(startTime.getNumeric());
		}
		return intervals;
	}

	static Time getTime(String time) {
		String[] info = time.split(" ");
		String[] hrMin = info[1].split(":");
		boolean am = info[2].equals("am");
		Map<String, Integer> mapDays = new HashMap<>();
		mapDays.put("mon", 1);
		mapDays.put("tue", 2);
		mapDays.put("wed", 3);
		mapDays.put("thu", 4);
		mapDays.put("fri", 5);
		mapDays.put("sat", 6);
		mapDays.put("sun", 7);
		return new Time(mapDays.get(info[0]), Integer.parseInt(hrMin[0]), Integer.parseInt(hrMin[1]), am);
	}

	// Caculate active time
	// https://leetcode.com/discuss/interview-question/1302606/DoorDash-onsite-interview-
	/*
	 * input : 8:30am | pickup 9:10am | dropoff 10:20am| pickup 12:15pm| pickup
	 * 12:45pm| dropoff 2:25pm | dropoff
	 */
	@SuppressWarnings("unused")
	private static int getActiveTimes(List<String> activity) {
		int timer = 0;
		int startTime = 0;
		int endTime = 0;
		int result = 0;
		for (String currentActivity : activity) {
			String[] split = currentActivity.split("\\s+");
			String timing = split[0];
			String method = split[1];
			if (method.equals("pickup")) {
				timer++;
				if (timer == 1) {
					startTime = getMinutes(timing);
				}
			} else {
				timer--;
				if (timer == 0) {
					endTime = getMinutes(timing);
				}
			}
			if (timer == 0) {
				result += endTime - startTime;
			}
		}
		return result;
	}

	private static int getMinutes(String timing) {
		String split[] = timing.split(":");
		int hours = Integer.parseInt(split[0]);
		int minutes = Integer.parseInt(split[1].substring(0, split[1].length() - 2));
		if (timing.endsWith("am") || (timing.endsWith("pm") && hours == 12)) {
			return hours * 60 + minutes;
		} else {
			return (hours + 12) * 60 + minutes;
		}
	}

	// Shortest Path
	// Dijkstra's with backtracking
	// https://www.1point3acres.com/bbs/thread-778790-1-1.html
	public static boolean[] shortestPath(Map<Integer, List<Integer>> map, List<Integer> cities) {
		Map<Integer, List<int[]>> graph = new HashMap<>();
		for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
			int road = entry.getKey();
			List<Integer> value = entry.getValue();
			int from = value.get(0);
			int to = value.get(1);
			int weight = value.get(2);
			graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new int[] { to, weight, road });
			graph.computeIfAbsent(to, k -> new ArrayList<>()).add(new int[] { from, weight, road });
		}
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		pq.add(new int[] { cities.get(0), 0 });
		int target = cities.get(cities.size() - 1);
		Map<Integer, int[]> results = new HashMap<>(); // destination : {from, road, weight} for backtracking
		while (!pq.isEmpty()) {
			int[] city = pq.poll();
			int currWeight = city[1];
			List<int[]> neighbours = graph.get(city[0]);
			for (int[] neighbour : neighbours) {
				int to = neighbour[0];
				int weight = neighbour[1];
				int road = neighbour[2];
				pq.add(new int[] { to, currWeight + weight });
				if (results.containsKey(to)) {
					int[] result = results.get(to);
					int oldWeight = result[2];
					if (currWeight + weight < oldWeight) {
						result[0] = city[0];
						result[1] = road;
						result[2] = currWeight + weight;
						results.put(to, result);
					}
				} else {
					results.put(to, new int[] { city[0], road, currWeight + weight });
				}
			}
			if (city[0] == target) {
				break;
			}
		}
		boolean[] res = new boolean[map.size()];
		int currCity = target;
		while (results.containsKey(currCity)) {
			// {from, road, weight}
			int[] path = results.get(currCity);
			int road = path[1];
			res[road] = true;
			currCity = path[0];
			if (currCity == cities.get(0)) {
				break;
			}
		}
		return res;
	}

	// K closet drivers to market
	// https://leetcode.com/discuss/interview-question/1293040/Doordash-or-Phone-Screen-or-Software-Engineer-E4-or-Closest-Drivers-to-Restaurant
	// 用priorityqueue排序结果

	// longest path for duplicate numbers in metrix
	// https://leetcode.com/discuss/interview-question/392780/Doordash-or-Phone-Screen-or-Longest-path-duplicate-numbers-within-a-Matrix
	// 从每个点出发做dfs，注意此题与longest path for increasing
	// number不同，是不能用cache记录结果的，假设[1][1][1]三个点，最左边的点最长路径为3，在中间点dfs不能说中间点长度为左边长度+1

	// validate order path
	// https://leetcode.com/discuss/interview-question/846916/Validate-Orders-Path-(Doordash)
	// follow up: find the longest validate order path, binary search？双指针？

	// 826. Most Profit Assigning Work
	// 暴力排序，可以继续优化，用一个数组存储直到当前难度为止的最大利润，然后用binary search查找某个工人能做什么
	public int maxProfitAssignment(int[] difficulty, int[] profit, int[] worker) {
		List<int[]> jobs = new ArrayList<>();
		for (int i = 0; i < difficulty.length; i++) {
			jobs.add(new int[] { difficulty[i], profit[i] });
		}
		// 按difficulty排序
		Collections.sort(jobs, (a, b) -> a[0] - b[0]);
		Arrays.sort(worker);
		int maxProfit = 0;
		for (int w : worker) {
			int maxProfitForCurrWorker = 0;
			for (int[] job : jobs) {
				if (w >= job[0]) {
					maxProfitForCurrWorker = Math.max(maxProfitForCurrWorker, job[1]);
				} else {
					break;
				}
			}
			maxProfit += maxProfitForCurrWorker;
		}
		return maxProfit;
	}

	// 854. K-Similar Strings
	// BFS
	public static int kSimilarity(String s1, String s2) {
		Queue<String> queue = new LinkedList<>();
		Set<String> seen = new HashSet<>();
		queue.add(s1);
		int level = 0;

		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int queueIndex = 0; queueIndex < size; queueIndex++) {
				String currentS = queue.poll();
				if (currentS.equals(s2)) {
					return level;
				}
				if (seen.contains(currentS)) {
					continue;
				}
				seen.add(currentS);
				for (int i = 0; i < currentS.length(); i++) {
					char c1 = currentS.charAt(i);
					char c2 = s2.charAt(i);
					if (c1 != c2) {
						for (int j = i + 1; j < currentS.length(); j++) {
							char newC1 = currentS.charAt(j);
							char newC2 = s2.charAt(j);
							if (newC1 == c2 && newC2 != s1.charAt(j)) {
								StringBuilder sb = new StringBuilder(currentS);
								sb.setCharAt(i, c2);
								sb.setCharAt(j, c1);
								String newS1 = sb.toString();
								if (!seen.contains(newS1)) {
									queue.add(newS1);
								}
							}
						}
						break;
					}
				}
			}
			level++;
		}
		return -1;
	}

	// 859. Buddy Strings
	// 1. If strings are the same, they're buddies if it has duplicate chars which
	// can be swapped
	// 2. For all other cases, there should be exactly 2 different chars and they
	// swap to be make the strings equal
	public boolean buddyStrings(String s, String goal) {
		if (s.length() != goal.length())
			return false;
		if (s.equals(goal) && hasDuplicateChars(s))
			return true;

		Deque<Integer> diffs = findDiffs(s, goal);
		return diffs.size() == 2 && s.charAt(diffs.getFirst()) == goal.charAt(diffs.getLast())
				&& s.charAt(diffs.getLast()) == goal.charAt(diffs.getFirst());
	}

	private boolean hasDuplicateChars(String s) {
		var chars = new HashSet<Character>();
		return IntStream.range(0, s.length()).anyMatch(i -> !chars.add(s.charAt(i)));
	}

	private Deque<Integer> findDiffs(String s, String goal) {
		var diffs = new ArrayDeque<Integer>();

		for (var i = 0; i < s.length(); i++)
			if (s.charAt(i) != goal.charAt(i)) {
				diffs.add(i);
				if (diffs.size() > 2)
					break;
			}

		return diffs;
	}

	// 839. Similar String Groups
	// 用 union find
	// 用 dfs也可以
	public int numSimilarGroups(String[] strs) {
		int size = strs.length;
		UF uf = new UF(size);
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (isSimilar(strs[i], strs[j])) {
					uf.union(i, j);
				}
			}
		}
		return uf.getSize();
	}

	private boolean isSimilar(String s1, String s2) {
		int n = s1.length();
		for (int i = 0; i < n; i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				for (int p = i + 1; p < n; p++) {
					if (s1.charAt(p) == s2.charAt(i)) {
						// swap
						StringBuilder sb = new StringBuilder(s1);
						char temp = s1.charAt(i);
						sb.setCharAt(p, temp);
						sb.setCharAt(i, s2.charAt(i));
						String tempStr = sb.toString();
						if (tempStr.substring(i + 1).equals(s2.substring(i + 1))) {
							return true;
						}
					}
				}
				return false;
			}
		}
		return true;
	}

	private static class UF {
		int[] parent;
		int[] rank;

		public UF(int size) {
			this.parent = new int[size];
			for (int i = 0; i < size; i++) {
				this.parent[i] = i;
			}
			this.rank = new int[size];
		}

		public void union(int i, int j) {
			int pi = find(i);
			int pj = find(j);
			if (pi != pj) {
				if (rank[pi] > rank[pj]) {
					parent[pj] = i;
				} else if (rank[pi] < rank[pj]) {
					parent[pi] = pj;
				} else {
					parent[pj] = pi;
					rank[pi]++;
				}
			}
		}

		public int find(int a) {
			if (this.parent[a] != a) {
				this.parent[a] = find(this.parent[a]);
			}
			return this.parent[a];
		}

		public int getSize() {
			Set<Integer> group = new HashSet<>();
			for (int i = 0; i < parent.length; i++) {
				group.add(find(parent[i]));
			}
			return group.size();
		}
	}

	// 1347. Minimum Number of Steps to Make Two Strings Anagram
	// s and t same length, just need to find how many chars are different in total
	public int minSteps(String s, String t) {
		int[] freqCount = new int[26];
		for (int i = 0; i < s.length(); i++) {
			freqCount[s.charAt(i) - 'a']++;
			freqCount[t.charAt(i) - 'a']--;
		}
		int count = 0;
		for (int fre : freqCount) {
			count += Math.abs(fre);
		}
		return count / 2;
	}
}
