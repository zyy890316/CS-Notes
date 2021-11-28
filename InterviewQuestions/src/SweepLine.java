import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class SweepLine {
	// 扫描线合集：https://www.bilibili.com/video/BV1Po4y1Z7sm
	// Leetcode 435
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
}
