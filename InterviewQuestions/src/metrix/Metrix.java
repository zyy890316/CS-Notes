package metrix;

import java.util.PriorityQueue;

public class Metrix {
	// 把数组中的 0 移到末尾，并保持非零元素顺序
	public static void moveZeroes(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			int index = i;
			for (int j = i + 1; j < nums.length; j++) {
				if (nums[index] == 0) {
					swap(nums, index, j);
					index++;
				}
			}
		}
	}

	private static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}

	// 改变矩阵维度
	public int[][] matrixReshape(int[][] mat, int r, int c) {
		int x = mat[0].length;
		int y = mat.length;

		if (x * y != r * c)
			return mat;

		int[][] newMat = new int[r][c];
		int[] temp = new int[x * y];
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				temp[i * x + j] = mat[i][j];
			}
		}

		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				newMat[i][j] = temp[i * c + j];
			}
		}
		return newMat;
	}

	// 找出数组中最长的连续 1
	public int findMaxConsecutiveOnes(int[] nums) {
		int max = 0, cur = 0;
		for (int x : nums) {
			cur = x == 0 ? 0 : cur + 1;
			max = Math.max(max, cur);
		}
		return max;
	}

	// 有序矩阵查找
	// 从矩阵右上角入手，这样向左值就会减少，向下就会增加
	public boolean searchMatrix(int[][] matrix, int target) {
		if (matrix[0][0] > target)
			return false;
		int col = matrix[0].length;
		int row = matrix.length;
		int rowS = 0;
		int colS = col - 1;

		while (colS >= 0 && rowS < row) {
			if (matrix[rowS][colS] == target) {
				return true;
			}
			if (matrix[rowS][colS] > target) {
				colS--;
			} else if (matrix[rowS][colS] < target) {
				rowS++;
			}
		}
		return false;
	}

	// 有序矩阵的 Kth Element
	// https://www.youtube.com/watch?v=Lo23qFLhJNY
	// 从矩阵左上角开始，从小到大查找，用minheap存储访问过的节点的邻居
	// 这样保证每次访问都是最小的，访问k-1次
	public int kthSmallest(int[][] matrix, int k) {
		PriorityQueue<Coordinate> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
		boolean[][] visited = new boolean[matrix.length][matrix[0].length];

		minHeap.offer(new Coordinate(matrix[0][0], 0, 0));
		for (int i = 0; i < k - 1; i++) {
			Coordinate currNum = minHeap.poll();
			while (isVisited(currNum, visited)) {
				currNum = minHeap.poll();
			}
			visited[currNum.x][currNum.y] = true;
			int x = currNum.x;
			int y = currNum.y;
			if (x < matrix.length - 1 && !visited[x + 1][y]) {
				minHeap.offer(new Coordinate(matrix[x + 1][y], x + 1, y));
			}
			if (y < matrix[0].length - 1 && !visited[x][y + 1]) {
				minHeap.offer(new Coordinate(matrix[x][y + 1], x, y + 1));
			}
		}
		return minHeap.peek().val;
	}

	private boolean isVisited(Coordinate currNum, boolean[][] visited) {
		return visited[currNum.x][currNum.y];
	}

	public class Coordinate {
		int val;
		int x;
		int y;

		Coordinate(int val, int x, int y) {
			this.val = val;
			this.x = x;
			this.y = y;
		}
	}

	// 一个数组元素在 [1, n] 之间，其中一个数被替换为另一个数，找出重复的数和丢失的数
	// 最直接的方法是先对数组进行排序，这种方法时间复杂度为 O(NlogN)。本题可以以 O(N) 的时间复杂度、O(1) 空间复杂度来求解。
	// 主要思想是通过交换数组元素，使得数组上的元素在正确的位置上。
	public int[] findErrorNums(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			while (nums[i] != i + 1 && nums[nums[i] - 1] != nums[i]) {
				swap(nums, i, nums[i] - 1);
			}
		}
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != i + 1) {
				return new int[] { nums[i], i + 1 };
			}
		}
		return null;
	}
}
