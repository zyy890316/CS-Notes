package linkedlist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20链表.md
public class LinkedListSolutions {
	public static void main(String args[]) {
		ListNode head = new ListNode(1);
		head.next = new ListNode(2);
		head.next.next = new ListNode(3);
		head.next.next.next = new ListNode(4);
		head.next.next.next.next = new ListNode(5);
		reverseBetween(head, 2, 4);
	}

	// 找出两个链表的交点
	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
		// 当访问 A 链表的指针访问到链表尾部时，令它从链表 B 的头部开始访问链表 B；
		// 同样地，当访问 B 链表的指针访问到链表尾部时，令它从链表 A 的头部开始访问链表 A。这样就能控制访问 A 和 B 两个链表的指针能同时访问到交点。
		ListNode currA = headA;
		ListNode currB = headB;
		while (currA != currB) {
			if (currA != null) {
				currA = currA.next;
			} else {
				currA = headB;
			}

			if (currB != null) {
				currB = currB.next;
			} else {
				currB = headA;
			}
		}
		return currA;
	}

	// 链表反转
	// 用三个指针分别指向当前节点，和其前后节点
	public ListNode reverseList(ListNode head) {
		if (head == null || head.next == null)
			return head;
		ListNode preNode = head;
		ListNode currNode = head.next;
		while (currNode != null) {
			ListNode nextNode = currNode.next;
			currNode.next = preNode;
			preNode = currNode;
			currNode = nextNode;
		}
		head.next = null;
		return preNode;
	}

	// 递归链表反转
	public ListNode reverseListRecursive(ListNode head) {
		if (head == null || head.next == null)
			return head;

		ListNode reversed_head = reverseListRecursive(head.next);
		// reversed_head will be the new head of reversed LinkedList
		// head.next is not changed.
		head.next.next = head;
		head.next = null;
		return reversed_head;
	}

	// 反转部分链表
	// https://leetcode.com/problems/reverse-linked-list-ii/
	public static ListNode reverseBetween(ListNode head, int left, int right) {
		if (head == null || head.next == null || left == right)
			return head;
		ListNode currNode = head;
		ListNode leftLeftNode = new ListNode(Integer.MIN_VALUE, head);
		ListNode leftNode = null;
		ListNode rightNode = null;
		ListNode rightRightNode = null;
		int count = 1;
		while (currNode != null) {
			if (count == left - 1) {
				leftLeftNode = currNode;
			}
			if (count == left) {
				leftNode = currNode;
			}
			if (count == right) {
				rightNode = currNode;
				break;
			}
			currNode = currNode.next;
			count++;
		}
		rightRightNode = rightNode.next;
		leftLeftNode.next = reverseListTillStop(leftNode, rightNode);
		leftNode.next = rightRightNode;
		return left == 1 ? leftLeftNode.next : head;
	}

	public static ListNode reverseListTillStop(ListNode head, ListNode stop) {
		if (head.next == stop) {
			stop.next = head;
			head.next = null;
			return stop;
		}

		ListNode reversed_head = reverseListTillStop(head.next, stop);
		// reversed_head will be the new head of reversed LinkedList
		// head.next is not changed.
		head.next.next = head;
		head.next = null;
		return reversed_head;
	}

	// 归并两个有序的链表
	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if (l1 == null)
			return l2;
		if (l2 == null)
			return l1;
		ListNode merged = new ListNode(-1);
		if (l1.val <= l2.val) {
			merged = l1;
			l1 = l1.next;
		} else {
			merged = l2;
			l2 = l2.next;
		}
		merged.next = mergeTwoLists(l1, l2);
		return merged;
	}

	// 从有序链表中删除重复节点
	public ListNode deleteDuplicates(ListNode head) {
		if (head == null || head.next == null)
			return head;
		ListNode currNode = head;
		while (currNode != null && currNode.next != null) {
			if (currNode.val == currNode.next.val) {
				currNode.next = currNode.next.next;
			} else {
				currNode = currNode.next;
			}
		}
		return head;
	}

	// 链表删除节点，只给了需删除节点，没给头
	// https://leetcode.com/problems/delete-node-in-a-linked-list/
	public void deleteNode(ListNode node) {
		if (node == null)
			return;
		if (node.next == null) {
			node = null;
			return;
		}

		node.val = node.next.val;
		node.next = node.next.next;
		node = node.next;
		return;
	}

	// 删除链表的倒数第 n 个节点
	public ListNode removeNthFromEnd(ListNode head, int n) {
		// 双指针，重点是让slow指针走过：总长-n 步
		// fast先走，走过n步之后，正好只剩下：总长-n 步
		ListNode fast = head;
		ListNode slow = head;
		while (n-- > 0) {
			fast = fast.next;
		}
		if (fast == null)
			return head.next;

		while (fast.next != null) {
			fast = fast.next;
			slow = slow.next;
		}
		slow.next = slow.next.next;

		return head;
	}

	// 交换链表中的相邻结点
	public ListNode swapPairs(ListNode head) {
		ListNode dummyHead = new ListNode(-1, head);
		ListNode preNode = dummyHead;
		ListNode currNode = head;
		while (currNode != null && currNode.next != null) {
			ListNode nextNode = currNode.next;
			// swap
			preNode.next = nextNode;
			currNode.next = nextNode.next;
			nextNode.next = currNode;

			preNode = currNode;
			currNode = currNode.next;
		}

		return dummyHead.next;
	}

	// 两链表求和
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		Stack<Integer> stack1 = new Stack<Integer>();
		Stack<Integer> stack2 = new Stack<Integer>();

		while (l1 != null) {
			stack1.add(l1.val);
			l1 = l1.next;
		}
		while (l2 != null) {
			stack2.add(l2.val);
			l2 = l2.next;
		}

		Stack<Integer> result = new Stack<Integer>();
		int carry = 0;
		while (!stack1.isEmpty() || !stack2.isEmpty()) {
			int x = stack1.isEmpty() ? 0 : stack1.pop();
			int y = stack2.isEmpty() ? 0 : stack2.pop();
			int sum = x + y + carry;
			if (sum > 9) {
				carry = 1;
			} else {
				carry = 0;
			}
			result.add(sum % 10);
		}

		if (carry > 0) {
			result.add(carry);
		}

		ListNode head = new ListNode(-1);
		ListNode cNode = head;
		while (!result.isEmpty()) {
			cNode.val = result.pop();
			if (result.isEmpty())
				break;
			cNode.next = new ListNode(-1);
			cNode = cNode.next;
		}
		return head;
	}

	// 判断回文链表
	// 切成两半，把后半段反转，然后比较两半是否相等。
	public boolean isPalindrome(ListNode head) {
		if (head == null || head.next == null)
			return true;
		ListNode slow = head, fast = head.next;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		if (fast != null)
			slow = slow.next; // 偶数节点，让 slow 指向下一个节点
		cut(head, slow); // 切成两个链表
		return isEqual(head, reverseList(slow));
	}

	private void cut(ListNode head, ListNode cutNode) {
		while (head.next != cutNode) {
			head = head.next;
		}
		head.next = null;
	}

	private boolean isEqual(ListNode l1, ListNode l2) {
		while (l1 != null && l2 != null) {
			if (l1.val != l2.val)
				return false;
			l1 = l1.next;
			l2 = l2.next;
		}
		return true;
	}

	// 分隔链表成k段
	// 遍历链表，求出总个数，然后按照商和余数得出每部分长度
	public ListNode[] splitListToParts(ListNode head, int k) {
		ListNode[] resultList = new ListNode[k];
		ListNode cNode = head;
		ListNode pNode = head;
		int size = 0;
		while (cNode != null) {
			size++;
			cNode = cNode.next;
		}
		int result = size / k;
		int remainder = size % k;
		for (int i = 0; i < k; i++) {
			int currSize = remainder-- >= 0 ? result + 1 : result;
			if (currSize > 0) {
				resultList[i] = cNode;
				while (currSize > 0) {
					pNode = cNode;
					cNode = cNode.next;
				}
				pNode.next = null;
			} else {
				resultList[i] = null;
			}
		}
		return resultList;
	}

	// 链表元素按奇偶聚集
	public ListNode oddEvenList(ListNode head) {
		if (head == null || head.next == null)
			return head;
		ListNode evenHead = new ListNode(-1, head.next);

		ListNode oddNode = head;
		ListNode evenNode = head.next;

		while (oddNode != null && evenNode != null && oddNode.next != null && evenNode.next != null) {
			oddNode.next = evenNode.next;
			evenNode.next = evenNode.next.next;

			oddNode = oddNode.next;
			evenNode = evenNode.next;
		}
		oddNode.next = evenHead.next;
		return head;
	}

	// 链表是否有环
	// https://leetcode.com/problems/linked-list-cycle/
	// 直接用hashmap，会使用额外空间
	// 不用额外空间：https://www.youtube.com/watch?v=bxCb37nLXWM 快慢双指针，相遇则有环
	public boolean hasCycle(ListNode head) {
		Map<ListNode, Boolean> visited = new HashMap<ListNode, Boolean>();
		while (head != null) {
			if (visited.containsKey(head)) {
				return true;
			}
			visited.put(head, true);
			head = head.next;
		}
		return false;
	}

	// 查找链表环的位置
	// https://leetcode.com/problems/linked-list-cycle-ii/
	// 分析：https://www.cnblogs.com/hiddenfox/p/3408931.html
	public ListNode detectCycle(ListNode head) {
		if (head == null || head.next == null)
			return null;
		ListNode slow = head;
		ListNode fast = head;
		while (true) {
			if (fast == null || fast.next == null) {
				return null; // 遇到null了，说明不存在环
			}
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast)
				break;
		}

		slow = head;
		while (slow != fast) {
			slow = slow.next;
			fast = fast.next;
		}
		return slow;
	}

	// 数组相邻差值的个数
	// https://leetcode.com/problems/beautiful-arrangement-ii/submissions/
	public int[] constructArray(int n, int k) {
		int[] ans = new int[n];
		// 前n-k元素差值均为1
		for (int i = 0; i < n - k; i++) {
			ans[i] = i + 1;
		}

		// 后k个元素一大一小排列
		for (int i = 0; i < k; i++) {
			if (i % 2 == 0) {
				ans[n - k + i] = n - i / 2;
			} else {
				ans[n - k + i] = n - k + (i / 2 + 1);
			}
		}
		return ans;
	}

	// 数组的度：[1,2,2,3,1,4,2]
	// 数组的度定义为元素出现的最高频率，例如上面的数组度为 3。要求找到一个最小的子数组，这个子数组的度和原数组一样。
	public int findShortestSubArray(int[] nums) {
		Map<Integer, NodeInfo> map = new HashMap<Integer, NodeInfo>();
		int maxCount = Integer.MIN_VALUE;
		int maxNumber = Integer.MIN_VALUE;
		for (int i = 0; i < nums.length; i++) {
			NodeInfo info = null;
			if (map.containsKey(nums[i])) {
				info = map.get(nums[i]);
				info.endIndex = i;
				info.count += 1;
				map.put(nums[i], info);
			} else {
				info = new NodeInfo(nums[i], i);
				map.put(nums[i], info);
			}

			if (info.count > maxCount) {
				maxCount = info.count;
				maxNumber = nums[i];
			} else if (info.count == maxCount) {
				NodeInfo currMax = map.get(maxNumber);
				if (info.endIndex - info.startIndex < currMax.endIndex - currMax.startIndex) {
					maxCount = info.count;
					maxNumber = nums[i];
				}
			}
		}
		NodeInfo maxNode = map.get(maxNumber);
		return maxNode.endIndex - maxNode.startIndex + 1;
	}

	public class NodeInfo {
		int val;
		int startIndex;
		int endIndex;
		int count;

		public NodeInfo(int val, int startIndex, int endIndex, int count) {
			this.val = val;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.count = count;
		}

		public NodeInfo(int val, int startIndex) {
			this.val = val;
			this.startIndex = startIndex;
			this.endIndex = startIndex;
			this.count = 1;
		}
	}

	// 检查是否为对角元素相等的矩阵
	// 只需从第一排和第一列每个元素作为起始，分别开始顺序检验以它为起始的对角线即可
	public boolean isToeplitzMatrix(int[][] matrix) {
		for (int i = 0; i < matrix[0].length; i++) {
			if (!check(matrix, matrix[0][i], 0, i)) {
				return false;
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			if (!check(matrix, matrix[i][0], i, 0)) {
				return false;
			}
		}
		return true;
	}

	private boolean check(int[][] matrix, int expectValue, int row, int col) {
		if (row >= matrix.length || col >= matrix[0].length) {
			return true;
		}
		if (matrix[row][col] != expectValue) {
			return false;
		}
		return check(matrix, expectValue, row + 1, col + 1);
	}

	// 嵌套数组
	// 题目描述：S[i] 表示一个集合，集合的第一个元素是 A[i]，第二个元素是 A[A[i]]，如此嵌套下去。求最大的 S[i]。
	// https://leetcode.com/problems/array-nesting/solution/
	// 每个起始点进行嵌套，最终会形成一个环，回到起始点位（数组中无重复元素），环上所有点位长度均为环的长度。
	public int arrayNesting(int[] nums) {
		int maxCount = -1;
		Boolean[] visited = new Boolean[nums.length];
		Arrays.fill(visited, Boolean.FALSE);
		for (int i = 0; i < nums.length; i++) {
			int j = i;
			if (visited[j])
				continue;
			int count = 1;
			visited[j] = Boolean.TRUE;
			while (true) {
				int currNum = nums[j];
				if (visited[currNum]) {
					break;
				}
				visited[currNum] = true;
				j = currNum;
				count++;
			}
			maxCount = Math.max(maxCount, count);
		}
		return maxCount;
	}

	// 分隔数组，使得对每部分排序后数组就为有序，求最大分割段数
	// https://www.youtube.com/watch?v=twYLu4hEKnQ
	// 只要i元素以前最大值为i，说明从i开始分割没问题，有效分割数+1
	public int maxChunksToSorted(int[] arr) {
		int maxChunks = 0;
		int currMax = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			currMax = Math.max(currMax, arr[i]);
			if (currMax == i) {
				maxChunks++;
			}
		}
		return maxChunks;
	}

	// 25. Reverse Nodes in k-Group
	// https://leetcode.com/problems/reverse-nodes-in-k-group/
}
