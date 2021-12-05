
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class GreedyAlgorithm {
	public static void main(String args[]) {
		eraseOverlapIntervals(new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 1, 3 } });
		reconstructQueue(new int[][] { { 7, 0 }, { 4, 4 }, { 7, 1 }, { 5, 0 }, { 6, 1 }, { 5, 2 } });
		checkPossibility(new int[] { 4, 2, 1 });
		partitionLabels("ababcbacadefegdehijhklij");
	}

	// 题目描述：每个孩子都有一个满足度 grid，每个饼干都有一个大小 size
	// 只有饼干的大小大于等于一个孩子的满足度，该孩子才会获得满足。求解最多可以获得满足的孩子数量。
	// 直接分别排序
	public int findContentChildren(int[] grid, int[] size) {
		if (grid == null || size == null)
			return 0;
		Arrays.sort(grid);
		Arrays.sort(size);
		int gi = 0, si = 0;
		while (gi < grid.length && si < size.length) {
			if (grid[gi] <= size[si]) {
				gi++;
			}
			si++;
		}
		return gi;
	}

	// 题目描述：计算让一组区间不重叠所需要移除的区间个数。
	// 先计算最多能组成的不重叠区间个数，然后用区间总个数减去不重叠区间的个数。
	// 在每次选择中，区间的结尾最为重要，选择的区间结尾越小，留给后面的区间的空间越大，那么后面能够选择的区间个数也就越大。
	// 按区间的结尾进行排序，每次选择结尾最小，并且和前一个区间不重叠的区间。
	// https://leetcode.com/problems/non-overlapping-intervals/description/
	public static int eraseOverlapIntervals(int[][] intervals) {
		if (intervals.length == 0) {
			return 0;
		}
		Arrays.sort(intervals, Comparator.comparingInt(o -> o[1]));
		int currEnd = intervals[0][1];
		int remove = 0;
		for (int i = 1; i < intervals.length; i++) {
			// 和下一个不重叠
			if (intervals[i][0] >= currEnd) {
				currEnd = intervals[i][1];
				continue;
			} else {
				// 重叠则说明这个interval不应该要
				remove++;
			}
		}
		return remove;
	}

	// 452 投飞镖刺破气球：气球在一个水平数轴上摆放，可以重叠，飞镖垂直投向坐标轴，使得路径上的气球都被刺破。求解最小的投飞镖次数使所有气球都被刺破。
	// 类似上题，按右边界排序，然后逐个遍历
	// https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/
	public int findMinArrowShots(int[][] points) {
		if (points.length <= 1)
			return points.length;
		Arrays.sort(points, Comparator.comparingInt(o -> o[1]));
		int currShot = points[0][1];
		int count = 1;
		for (int i = 1; i < points.length; i++) {
			int nextStart = points[i][0];
			// current shot can still handle next ballon
			if (nextStart <= currShot) {
				continue;
			} else {
				count++;
				currShot = points[i][1];
			}
		}
		return count;
	}

	// 406 根据身高和序号重组队列： 一个学生用两个分量 (h, k) 描述，h 表示身高，k 表示排在前面的有 k 个学生的身高比他高或者和他一样高
	// https://leetcode.com/problems/queue-reconstruction-by-height/
	public static int[][] reconstructQueue(int[][] people) {
		if (people.length <= 1)
			return people;
		// 为了使插入操作不影响后续的操作，身高较高的学生应该先排好
		// 然后陆续插入后续学生，从高到矮插入，这样身高较矮的学生顺序不会被打乱
		Arrays.sort(people, (a, b) -> {
			if (a[0] == b[0])
				return a[1] - b[1];
			return b[0] - a[0];
		});
		List<int[]> queue = new ArrayList<>();
		for (int[] p : people) {
			queue.add(p[1], p);
		}
		return queue.toArray(new int[queue.size()][2]);
	}

	// 122 买卖股票的最大收益 II: 可以进行多次交易，多次交易之间不能交叉进行
	// 对于 [a, b, c, d]，如果有 a <= b <= c <= d ，那么最大收益为 d - a。而 d - a = (d - c) + (c -
	// b) + (b - a)
	// 因此当访问到一个 prices[i] 且 prices[i] - prices[i-1] > 0，那么就把 prices[i] - prices[i-1]
	// 添加到收益中。
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/description/
	public int maxProfit(int[] prices) {
		int profit = 0;
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] > prices[i - 1]) {
				profit += (prices[i] - prices[i - 1]);
			}
		}
		return profit;
	}

	// 605 种植花朵: flowerbed 数组中 1 表示已经种下了花朵。花朵之间至少需要一个单位的间隔，求解是否能种下 n 朵花。
	public boolean canPlaceFlowers(int[] flowerbed, int n) {
		int len = flowerbed.length;
		int count = 0;
		int preFlower = -2;
		for (int i = 0; i < len; i++) {
			if (flowerbed[i] == 1) {
				preFlower = i;
			} else {
				if (i - preFlower >= 2) {
					if ((i + 1 < len && flowerbed[i + 1] == 0) || i + 1 == len) {
						preFlower = i;
						count++;
					}
				}
			}
		}
		return count >= n;
	}

	// 392 判断是否为子序列
	public boolean isSubsequence(String s, String t) {
		if (s.length() == 0)
			return true;
		int index = -1;
		for (char c : s.toCharArray()) {
			index = t.indexOf(c, index + 1);
			if (index < 0) {
				return false;
			}
		}
		return true;
	}

	// 665 判断一个数组是否能只修改一个数就成为非递减数组。
	// https://leetcode.com/problems/non-decreasing-array/description/
	public static boolean checkPossibility(int[] nums) {
		int index = 0;
		for (int i = 0; i < nums.length - 1; i++) {
			// 出现非递减，需要更换数字
			if (nums[i] > nums[i + 1]) {
				// i-1比i+1还要大，唯一的办法就是i+1换成i的值
				if (i - 1 >= 0 && nums[i - 1] > nums[i + 1]) {
					nums[i + 1] = nums[i];
				} else {
					// 其他情况i的值换成i+1的值就能保证连续
					nums[i] = nums[i + 1];
				}
				index = i - 1;
				break;
			}
		}
		for (int j = Math.max(index, 0); j < nums.length - 1; j++) {
			if (nums[j] > nums[j + 1]) {
				return false;
			}
		}
		return true;
	}

	// 53 子数组最大的和
	// 只要和出现负值，说明应该抛弃之前的和，从新开始找自数组
	// https://leetcode.com/problems/maximum-subarray/description/
	public int maxSubArray(int[] nums) {
		int sum = 0;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			if (sum > max) {
				max = sum;
			}
			if (sum < 0) {
				sum = 0;
			}
		}
		return max;
	}

	// 918. Maximum Sum Circular Subarray
	// 两种情况：数组可分为三部分，prefix + mid + suffix
	// 情况1： mid最大，这样即可转为53题球子数组的最大和
	// 情况2: prefix+suffix最大，这样最大值即为数组总和sum，剪掉mid的最小值，也就是sum+maxSubArray(-nums)
	public int maxSubarraySumCircular(int[] nums) {
		int n = nums.length;
		int[] negNums = new int[n];
		int sum = 0;
		for (int i = 0; i < n; i++) {
			negNums[i] = -nums[i];
			sum += nums[i];
		}
		int negMid = maxSubArray(negNums);
		int mid = maxSubArray(nums);
		// corner case：mid < 0说明元数组全部为负，这种情况下返回mid即可，mid即为元数组中最大的一个值
		return mid < 0 ? mid : Math.max(sum + negMid, mid);
	}

	// 763 分隔字符串使同种字符出现在一起
	// 存下每个字符最后一次出现的位置
	// https://leetcode.com/problems/partition-labels/description/
	public static List<Integer> partitionLabels(String s) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (!map.containsKey(ch)) {
				map.put(ch, s.lastIndexOf(ch));
				continue;
			}
		}
		List<Integer> queue = new ArrayList<Integer>();
		int start = 0;
		int end = map.get(s.charAt(0));
		while (end < s.length()) {
			for (int i = start; i < end; i++) {
				if (map.get(s.charAt(i)) > end) {
					end = map.get(s.charAt(i));
				}
			}
			queue.add(end - start + 1);
			if (end == s.length() - 1) {
				break;
			} else {
				start = end + 1;
				end = map.get(s.charAt(start));
			}
		}
		return queue;
	}

	// 621. Task Scheduler
	// https://leetcode.com/problems/task-scheduler/discuss/259218/java-solution-using-priority-queue-with-explanation
	public int leastInterval(char[] tasks, int n) {
		int[] frequency = new int[26];
		Queue<Task> pq = new PriorityQueue<>(new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				return o2.frequency - o1.frequency;
			}
		});
		for (char task : tasks) {
			frequency[task - 'A']++;
		}
		for (int i = 0; i < 26; i++) {
			char id = (char) ('A' + i);
			if (frequency[i] > 0) {
				pq.add(new Task(id, frequency[i]));
			}
		}

		// 总共需要的cpu时间
		int intervals = 0;
		while (!pq.isEmpty()) {
			// groupSize是每组task的数量， groupCount是共分成多少组task
			int groupSize = n + 1;
			List<Task> temp = new ArrayList<>();
			// 分配一组task，分配过的task加到temp里面，保证每组内无重复task
			while (groupSize > 0 && !pq.isEmpty()) {
				Task currTask = pq.poll();
				currTask.frequency--;
				temp.add(currTask);
				groupSize--;
				intervals++;
			}
			for (Task tempTask : temp) {
				if (tempTask.frequency > 0) {
					pq.add(tempTask);
				}
			}
			// 全部分配完成，可以直接结束
			if (pq.isEmpty())
				break;
			// 当前组没有填满，需要加idle时间
			if (groupSize > 0) {
				intervals += groupSize;
			}
		}
		return intervals;
	}

	class Task {
		char id;
		int frequency;

		public Task(char id, int frequency) {
			this.id = id;
			this.frequency = frequency;
		}
	}
}
