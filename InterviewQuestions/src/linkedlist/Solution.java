package linkedlist;

import java.util.Stack;

// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20链表.md
public class Solution {
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
		ListNode pHead, preNode = head;
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

	// 归并两个有序的链表
	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if (l1 == null)
			return l2;
		if (l2 == null)
			return l1;
		ListNode merged = new ListNode(-1);
		ListNode cNode = new ListNode(-1);
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
		ListNode oddHead = new ListNode(-1, head);
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
}
