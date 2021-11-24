import java.util.Arrays;

public class Sort {
	// 324. Wiggle Sort II
	public void wiggleSort(int[] nums) {
		Arrays.sort(nums);
		int n = nums.length;
		int[] temp = new int[nums.length];
		// 此题难点在于中位数两边的数有可能相同，所以需要尽量放远一点
		// Small half: M . S . S . 递减
		// Large half: . L . L . M 递减
		// -Together : M L S L S M
		int medianIndex = (n - 1) / 2;
		int index = 0;
		for (int i = 0; i <= medianIndex; i++) {
			temp[index] = nums[medianIndex - i];
			if (index + 1 < n) {
				temp[index + 1] = nums[n - 1 - i];
			}
			index += 2;
		}
		for (int m = 0; m < nums.length; m++) {
			nums[m] = temp[m];
		}
	}
}
