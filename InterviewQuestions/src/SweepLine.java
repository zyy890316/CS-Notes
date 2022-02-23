import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class SweepLine {
	// 扫描线合集：https://www.bilibili.com/video/BV1Po4y1Z7sm
	// Leetcode 435. Non-overlapping Intervals
	// 可用 greedy https://www.youtube.com/watch?v=BTObFnHbD4U

	// 218. The Skyline Problem
	public List<List<Integer>> getSkyline(int[][] buildings) {
		List<List<Integer>> ans = new ArrayList<>();
		// building 拆成左右墙方便后面扫描
		int[][] sweepLine = new int[buildings.length * 2][2];
		for (int i = 0; i < buildings.length; i++) {
			sweepLine[i * 2] = new int[] { buildings[i][0], buildings[i][2] };
			sweepLine[i * 2 + 1] = new int[] { buildings[i][1], -buildings[i][2] };
		}
		Arrays.sort(sweepLine, (a, b) -> {
			if (a[0] == b[0]) {
				return b[1] - a[1];
			}
			return a[0] - b[0];
		});

		Queue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
		int currHeight = 0;
		for (int[] wall : sweepLine) {
			int height = wall[1];
			int x = wall[0];
			if (height > 0) {
				pq.add(height);
			} else {
				pq.remove(-height);
			}

			// 判断当前高度
			if (pq.isEmpty()) {
				currHeight = 0;
				ans.add(List.of(x, 0));
			} else {
				if (pq.peek() != currHeight) {
					ans.add(List.of(x, pq.peek()));
					currHeight = pq.peek();
				}
			}
		}
		return ans;
	}

	// 729. My Calendar I
	class MyCalendar {
		TreeMap<Integer, Integer> map;

		public MyCalendar() {
			this.map = new TreeMap<>();
		}

		public boolean book(int start, int end) {
			if (map.containsKey(start))
				return false;
			Integer preEventStart = map.floorKey(start);
			Integer nextEventStart = map.ceilingKey(start);
			if (preEventStart == null && nextEventStart == null) {
				map.put(start, end);
				return true;
			}
			if (preEventStart == null) {
				int nextEventEnd = map.get(nextEventStart);
				if (hasInterval(start, end, nextEventStart, nextEventEnd)) {
					return false;
				}
				map.put(start, end);
				return true;
			}
			if (nextEventStart == null) {
				int preEventEnd = map.get(preEventStart);
				if (hasInterval(start, end, preEventStart, preEventEnd)) {
					return false;
				}
				map.put(start, end);
				return true;
			}
			int preEventEnd = map.get(preEventStart);
			int nextEventEnd = map.get(nextEventStart);
			if (hasInterval(start, end, preEventStart, preEventEnd)
					|| hasInterval(start, end, nextEventStart, nextEventEnd)) {
				return false;
			} else {
				map.put(start, end);
				return true;
			}
		}

		public boolean hasInterval(int event1S, int event1E, int event2S, int event2E) {
			return !(event2S >= event1E || event2E <= event1S);
		}
	}

	// 731. My Calendar II
	// 类似扫描线的思想，事件开始为1，结束是-1，利用treemap来保证开始时间从小到大排序
	class MyCalendarTwo {
		TreeMap<Integer, Integer> map;

		public MyCalendarTwo() {
			this.map = new TreeMap<>();
		}

		public boolean book(int start, int end) {
			// when there is no event
			if (map.isEmpty()) {
				map.put(start, 1);
				map.put(end, -1);
				return true;
			}
			// considering there willl be no confiction so just put every in coming event
			map.put(start, map.getOrDefault(start, 0) + 1);
			map.put(end, map.getOrDefault(end, 0) - 1);
			int count = 0;
			// map.forEach((a,b)->System.out.println(a+" "+b));
			for (int i : map.values()) {
				// checking count of current event
				count += i;
				if (count > 2) {
					// before return false remove just added events so there remians no confliction.
					int s = map.get(start);
					int e = map.get(end);
					map.put(start, s - 1);
					map.put(end, e + 1);
					return false;
				}
			}

			return true;
		}
	}

	// 732. My Calendar III
	class MyCalendarThree {
		TreeMap<Integer, Integer> map;

		public MyCalendarThree() {
			this.map = new TreeMap<>();
		}

		public int book(int start, int end) {
			// considering there willl be no confiction so just put every in coming event
			map.put(start, map.getOrDefault(start, 0) + 1);
			map.put(end, map.getOrDefault(end, 0) - 1);
			int count = 0;
			int max = 0;
			// map.forEach((a,b)->System.out.println(a+" "+b));
			for (int i : map.values()) {
				// checking count of current event
				count += i;
				max = Math.max(max, count);
			}
			return max;
		}
	}
}
