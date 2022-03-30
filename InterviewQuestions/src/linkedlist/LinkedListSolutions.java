package linkedlist;

import java.util.HashMap;
import java.util.Map;

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

	// 2. Add Two Numbers
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		return addTwoNumbers(l1, l2, 0);
	}

	public ListNode addTwoNumbers(ListNode l1, ListNode l2, int carry) {
		if (l1 == null && l2 == null && carry == 0) {
			return null;
		}
		int val = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + carry;
		ListNode nextNode = new ListNode(val % 10);
		carry = val / 10;
		nextNode.next = addTwoNumbers(l1 == null ? null : l1.next, l2 == null ? null : l2.next, carry);
		return nextNode;
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

	// 25. Reverse Nodes in k-Group
	// https://leetcode.com/problems/reverse-nodes-in-k-group/

	// 138. Copy List with Random Pointer
	public Node copyRandomList(Node head) {
		if (head == null) {
			return null;
		}

		// Creating a new weaved list of original and copied nodes.
		Node ptr = head;
		while (ptr != null) {
			// Cloned node
			Node newNode = new Node(ptr.val, null, null);

			// Inserting the cloned node just next to the original node.
			// If A->B->C is the original linked list,
			// Linked list after weaving cloned nodes would be A->A'->B->B'->C->C'
			newNode.next = ptr.next;
			ptr.next = newNode;
			ptr = newNode.next;
		}
		ptr = head;
		// Now link the random pointers of the new nodes created.
		// Iterate the newly created list and use the original nodes' random pointers,
		// to assign references to random pointers for cloned nodes.
		while (ptr != null) {
			ptr.next.random = (ptr.random != null) ? ptr.random.next : null;
			ptr = ptr.next.next;
		}

		// Unweave the linked list to get back the original linked list
		// i.e. A->A'->B->B'->C->C' would be broken to A->B->C and A'->B'->C'
		Node ptr_old_list = head; // A->B->C
		Node ptr_new_list = head.next; // A'->B'->C'
		Node head_old = head.next;
		while (ptr_old_list != null) {
			ptr_old_list.next = ptr_old_list.next.next;
			ptr_new_list.next = (ptr_new_list.next != null) ? ptr_new_list.next.next : null;
			ptr_old_list = ptr_old_list.next;
			ptr_new_list = ptr_new_list.next;
		}
		return head_old;
	}

	class Node {
		public int val;
		public Node next;
		public Node random;

		public Node() {
		}

		public Node(int _val, Node _next, Node _random) {
			val = _val;
			next = _next;
			random = _random;
		}
	};
}
