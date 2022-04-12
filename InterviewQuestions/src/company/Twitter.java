package company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

// 1. design health monitor system
// 2. Design a networked service to allow other networked services to acquire exclusive rights to a string key
// 3. We want to build a feed reader application similar to Google Reader
// 4. design slack
// 5. 给两个stream，一个是tweet的stream一个是tweet_view 这个action的stream，格式是(tweet_id, viewer_id, timestamp)，想要实现一个API给企‍‍‌‌‌‍‌‌‌‍‌‌‍‌‌‌‌‌‌‍业用户，显示实时的某广告tweet的views数量并做成折线图之类的output
// 6. distrubuted message queue
// 7. 常见的在线游戏
// 8. rate limiter
public class Twitter {
	public static void main(String[] args) {
		maxEvents(new int[][] { { 1, 2 }, { 1, 2 }, { 3, 3 }, { 1, 5 }, { 1, 5 } });
	}

	// 1817. Finding the Users Active Minutes
	public int[] findingUsersActiveMinutes(int[][] logs, int k) {
		HashMap<Integer, Set<Integer>> map = new HashMap<>();
		for (int[] log : logs) {
			map.computeIfAbsent(log[0], z -> new HashSet<>()).add(log[1]);
		}

		int[] ans = new int[k];
		for (int id : map.keySet()) {
			ans[map.get(id).size() - 1]++;
		}
		return ans;
	}

	// 1326. Minimum Number of Taps to Open to Water a Garden
	// O(NlogN) greedy algorithm, can use DP similar to jump game to reduce to ON()
	// https://www.youtube.com/watch?v=G88X89Eo2C0
	public int minTaps(int n, int[] ranges) {
		int[][] intervals = new int[ranges.length][2];
		// convert the range to intervals
		for (int i = 0; i <= n; i++) {
			int start = Math.max(0, i - ranges[i]);
			int end = i + ranges[i];
			intervals[i][0] = start;
			intervals[i][1] = end;
		}
		// sort by start, if start are same, then larger end will appear early
		Arrays.sort(intervals, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				if (o1[0] == o2[0])
					return Integer.compare(o2[1], o1[1]);
				return Integer.compare(o1[0], o2[0]);
			}
		});
		int count = 1;
		int start = intervals[0][0];
		if (start > 0)
			return -1;
		int end = intervals[0][1];
		int canReach = end;
		int i = 1;
		while (canReach < n) {
			if (canReach >= n) {
				return count;
			}
			// find the best interval, canReach is the likely end for next interval
			while (i < intervals.length && intervals[i][0] <= end) {
				canReach = Math.max(canReach, intervals[i][1]);
				i++;
			}
			if (end == canReach)
				return -1; // can not extand any more
			end = canReach;
			count++;
		}
		return count;
	}

	// Similar to 45. Can Jump II
	public int minTapsDP(int n, int[] ranges) {
		int[] nums = new int[n + 1]; // max index you can jump from i
		// convert the range to intervals
		for (int i = 0; i <= n; i++) {
			int index = Math.max(0, i - ranges[i]);
			int end = i + ranges[i];
			nums[index] = end;
		}
		int count = 0;
		int end = 0;
		int canReach = 0;
		for (int i = 0; i <= n; i++) {
			if (i > canReach)
				return -1;
			// See what we will set our threshold to next
			canReach = Math.max(canReach, nums[i]);
			if (i == end && i < n) {
				end = canReach;
				count++;
			}
		}
		return count;
	}

	// 780. Reaching Points
	// https://www.youtube.com/watch?v=tPr5Uae6Drc
	public boolean reachingPoints(int sx, int sy, int tx, int ty) {
		while (tx >= sx && ty >= sy) {
			if (tx == ty)
				break;
			if (tx > ty) {
				if (ty > sy)
					tx %= ty;
				else
					return (tx - sx) % ty == 0;
			} else {
				if (tx > sx)
					ty %= tx;
				else
					return (ty - sy) % tx == 0;
			}
		}
		return (tx == sx && ty == sy);
	}

	// 358. Rearrange String k Distance Apart
	public static String rearrangeString(String s, int k) {
		if (k == 0)
			return s;
		// int[] = [char, frequency]
		PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) -> {
			if (p1[1] == p2[1]) {
				return p1[0] - p2[0];
			}
			return p2[1] - p1[1];
		});
		int[] counts = new int[26];
		for (int pos = 0; pos < s.length(); pos++) {
			counts[s.charAt(pos) - 'a']++;
		}
		for (int i = 0; i < 26; i++) {
			int count = counts[i];
			if (count == 0)
				continue;
			pq.add(new int[] { i, count });
		}
		StringBuilder sb = new StringBuilder();
		while (!pq.isEmpty()) {
			Queue<Integer> temp = new LinkedList<>();
			for (int i = 0; i < k; i++) {
				int[] pair = pq.poll();
				sb.append((char) (pair[0] + 'a'));
				temp.add(pair[0]);
				if (pq.isEmpty()) {
					// i == k - 1: normal, just break;
					// sb.length() == s.length(): normal;
					// Negate (i == k - 1 && newS.length() == s.length()) --> unnormal
					if (i != k - 1 && sb.length() != s.length())
						return "";

					break;
				}
			}
			while (temp.size() > 0) {
				Integer c = temp.poll();
				counts[c]--;
				if (counts[c] == 0) {
					break;
				}
				pq.add(new int[] { c, counts[c] });
			}
		}
		return sb.toString();
	}

	// 1353. Maximum Number of Events That Can Be Attended
	// https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/discuss/510263
	public static int maxEvents(int[][] events) {
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
		// sort by start time
		Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));
		int i = 0, ans = 0, day = 0, n = events.length;
		while (!pq.isEmpty() || i < n) {
			if (pq.isEmpty())
				day = events[i][0];
			// adding events that has started, since event has started, only end day matters
			while (i < n && events[i][0] <= day)
				pq.offer(events[i++][1]);
			pq.poll();
			// use current day for this event
			ans++;
			day++;
			// remove passed events
			while (!pq.isEmpty() && pq.peek() < day)
				pq.poll();
		}
		return ans;
	}

	// 1868. Product of Two Run-Length Encoded Arrays
	public List<List<Integer>> findRLEArray(int[][] encoded1, int[][] encoded2) {
		List<List<Integer>> ans = new ArrayList<>();
		int index1 = 0, index2 = 0, remaining1 = 0, remaining2 = 0, sameLength = 0;
		while (index1 < encoded1.length && index2 < encoded2.length) {
			int[] e1 = encoded1[index1];
			int[] e2 = encoded2[index2];

			// choose smaller one as the step size depends on whether there are some
			// remainings from previous section
			if (remaining1 > 0) {
				sameLength = Math.min(e2[1], remaining1);
			} else if (remaining2 > 0) {
				sameLength = Math.min(e1[1], remaining2);
			} else {
				sameLength = Math.min(e1[1], e2[1]);
			}

			// if we can merge the result to prev section
			if (ans.size() > 0) {
				List<Integer> prev = ans.get(ans.size() - 1);
				if (e1[0] * e2[0] == prev.get(0)) {
					prev.set(1, prev.get(1) + sameLength);
				} else {
					List<Integer> temp = new ArrayList<Integer>();
					temp.add(e1[0] * e2[0]);
					temp.add(sameLength);
					ans.add(temp);
				}
			} else {
				List<Integer> temp = new ArrayList<Integer>();
				temp.add(e1[0] * e2[0]);
				temp.add(sameLength);
				ans.add(temp);
			}

			if (remaining1 > 0) {
				remaining1 -= sameLength;
				remaining2 = e2[1] - sameLength;
			} else if (remaining2 > 0) {
				remaining2 -= sameLength;
				remaining1 = e1[1] - sameLength;
			} else {
				remaining1 = e1[1] - sameLength;
				remaining2 = e2[1] - sameLength;
			}
			if (remaining1 == 0)
				index1++;
			if (remaining2 == 0)
				index2++;
		}
		return ans;
	}
}
