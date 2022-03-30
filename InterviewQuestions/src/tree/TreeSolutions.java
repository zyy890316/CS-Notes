package tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

import javafx.util.Pair;
import linkedlist.ListNode;

// range query 用segment tree：
// https://www.youtube.com/watch?v=rYBtViWXYeI
/* SegmentTreeNode{
 * 	int startIndex;
 *  int endIndex;
 *  int sum/max/min;
 *  SegnentTreeNode left;
 *  SegmentTreeNode right;
 * }
 */

// https://github.com/CyC2018/CS-Notes/blob/master/notes/Leetcode%20题解%20-%20树.md
public class TreeSolutions {
	public static void main(String[] args) {
	}

	// 树的高度
	public int maxDepth(TreeNode root) {
		if (root == null)
			return 0;
		return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
	}

	// 是否为平衡树
	public boolean isBalanced(TreeNode root) {
		if (root == null)
			return true;
		return Math.abs(maxDepth(root.left) - maxDepth(root.right)) <= 1 && isBalanced(root.left)
				&& isBalanced(root.right);
	}

	// 两节点的最长路径
	int max = Integer.MIN_VALUE;

	public int diameterOfBinaryTree(TreeNode root) {
		if (root == null)
			return 0;
		maxDepthForDiameter(root);
		return max;
	}

	private int maxDepthForDiameter(TreeNode root) {
		if (root == null)
			return 0;
		int leftDepth = maxDepth(root.left);
		int rightDepth = maxDepth(root.right);
		max = Math.max(max, leftDepth + rightDepth);
		return Math.max(leftDepth, rightDepth) + 1;
	}

	// 左右翻转树
	// BFS then revert left to right
	private static Queue<TreeNode> queue = new LinkedList<TreeNode>();

	public TreeNode invertTree(TreeNode root) {
		if (root == null)
			return null;
		queue.add(root);
		while (!queue.isEmpty()) {
			TreeNode cNode = queue.poll();
			if (cNode.left != null)
				queue.add(cNode.left);
			if (cNode.right != null)
				queue.add(cNode.right);
			TreeNode temp = cNode.left;
			cNode.left = cNode.right;
			cNode.right = temp;
		}
		return root;
	}

	// 归并两棵树
	public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
		if (root1 == null || root2 == null)
			return root1 == null ? root2 : root1;

		TreeNode newRoot = new TreeNode(-1);
		newRoot.left = mergeTrees(root1.left, root2.left);
		newRoot.right = mergeTrees(root1.right, root2.right);
		newRoot.val = root1.val + root2.val;
		return newRoot;
	}

	// 判断路径和是否等于一个数
	// root-to-leaf path only
	public boolean hasPathSum(TreeNode root, int targetSum) {
		if (root == null) {
			return false;
		}

		if (root.left == null && root.right == null) {
			return targetSum == root.val;
		}

		return hasPathSum(root.left, targetSum - root.val) || hasPathSum(root.right, targetSum - root.val);
	}

	// 统计路径和等于一个数的路径数量
	// 路径可为任意起始
	public int pathSum(TreeNode root, int targetSum) {
		if (root == null)
			return 0;
		// root + left + right
		return pathSumFromRoot(root, targetSum) + pathSum(root.left, targetSum) + pathSum(root.right, targetSum);
	}

	private int pathSumFromRoot(TreeNode root, int targetSum) {
		// 此函数只考虑包含当前root的情况
		if (root == null)
			return 0;
		int count = 0;
		if (root.val == targetSum)
			count++;
		count += pathSumFromRoot(root.left, targetSum - root.val) + pathSumFromRoot(root.right, targetSum - root.val);
		return count;
	}

	// 是否为子树
	public boolean isSubtree(TreeNode root, TreeNode subRoot) {
		if (root == null && subRoot == null)
			return true;
		if (root == null)
			return false;
		if (subRoot == null)
			return true;

		return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot) || isSameTree(root, subRoot);
	}

	private boolean isSameTree(TreeNode root, TreeNode subRoot) {
		if (root == null && subRoot == null)
			return true;
		if (root == null)
			return false;
		if (subRoot == null)
			return false;

		return root.val == subRoot.val && isSameTree(root.left, subRoot.left) && isSameTree(root.right, subRoot.right);
	}

	// 判断树是否对称
	public boolean isSymmetric(TreeNode root) {
		if (root == null)
			return true;

		return isMirrorTree(root.left, root.right);
	}

	private boolean isMirrorTree(TreeNode root1, TreeNode root2) {
		if (root1 == null && root2 == null)
			return true;
		if (root1 == null)
			return false;
		if (root2 == null)
			return false;

		return root1.val == root2.val && isMirrorTree(root1.right, root2.left) && isMirrorTree(root1.left, root2.right);
	}

	// 最小子树深度
	public int minDepth(TreeNode root) {
		if (root == null)
			return 0;

		if (root.left == null && root.right == null)
			return 1;
		if (root.left == null)
			return minDepth(root.right) + 1;
		if (root.right == null)
			return minDepth(root.left) + 1;

		return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
	}

	// 统计左叶子节点的和
	int sum = 0;

	public int sumOfLeftLeaves(TreeNode root) {
		if (root == null)
			return 0;

		sumOfLeftLeaves(root.left, true);
		sumOfLeftLeaves(root.right, false);
		return sum;
	}

	public void sumOfLeftLeaves(TreeNode root, boolean isLeft) {
		if (root == null)
			return;

		if (root.left == null && root.right == null && isLeft)
			sum += root.val;
		sumOfLeftLeaves(root.left, true);
		sumOfLeftLeaves(root.right, false);
	}

	// 相同节点值的最大路径长度
	int best = 0; // 最长路径长度

	public int longestUnivaluePath(TreeNode root) {
		calculate(root);

		return best;
	}

	public int calculate(TreeNode root) {
		if (root == null)
			return 0;

		int left = calculate(root.left);
		int right = calculate(root.right);
		int currBest = 0;

		// all 3 nodes are same
		if ((root.left != null && root.left.val == root.val) && (root.right != null && root.right.val == root.val)) {
			best = Math.max(best, left + 1 + right + 1);
			// 最佳路径为左右节点通过当前root联通
			currBest = Math.max(left, right) + 2;
			// 从上一层看来，最佳为左/右两者最长的一条
			return Math.max(left, right) + 1;
		}

		if (root.left != null && root.left.val == root.val) {
			currBest = left + 1;
		}
		if (root.right != null && root.right.val == root.val) {
			currBest = right + 1;
		}
		best = Math.max(best, currBest);

		return currBest;
	}

	// 124. Binary Tree Maximum Path Sum
	public int maxPathSum(TreeNode root) {
		if (root == null)
			return 0;
		maxPathSumDFS(root);
		return max;
	}

	public int maxPathSumDFS(TreeNode root) {
		max = Math.max(root.val, max);
		if (root.left == null && root.right == null) {
			return root.val;
		}
		if (root.left != null && root.right == null) {
			int left = maxPathSumDFS(root.left);
			max = Math.max(left, max);
			max = Math.max(root.val + left, max);
			return Math.max(root.val + left, root.val);
		}
		if (root.right != null && root.left == null) {
			int right = maxPathSumDFS(root.right);
			max = Math.max(right, max);
			max = Math.max(root.val + right, max);
			return Math.max(root.val + right, root.val);
		} else {
			int left = maxPathSumDFS(root.left);
			int right = maxPathSumDFS(root.right);
			int currMax = Math.max(left, right);
			max = Math.max(currMax, max);
			max = Math.max(left + right + root.val, max);
			max = Math.max(currMax + root.val, max);
			max = Math.max(root.val, max);
			return Math.max(currMax + root.val, root.val);
		}
	}

	// 间隔遍历，只能检测不相连节点
	// https://leetcode.com/problems/house-robber-iii/solution/
	private final Map<TreeNode, Integer> robCache = new HashMap<TreeNode, Integer>();
	private final Map<TreeNode, Integer> notRobCache = new HashMap<TreeNode, Integer>();

	public int rob(TreeNode root) {
		return nodeValue(root, false);
	}

	// 每个节点的价值都取决于是否parent已经算过了
	private int nodeValue(TreeNode cNode, boolean parentRobbed) {
		if (cNode == null)
			return 0;
		int rob = robCache.containsKey(cNode) ? robCache.get(cNode)
				: cNode.val + nodeValue(cNode.left, true) + nodeValue(cNode.right, true);
		robCache.put(cNode, rob);
		int notRob = notRobCache.containsKey(cNode) ? notRobCache.get(cNode)
				: nodeValue(cNode.left, false) + nodeValue(cNode.right, false);
		notRobCache.put(cNode, notRob);
		if (!parentRobbed) {
			return Math.max(rob, notRob);
		}
		return notRob;
	}

	// 找出二叉树中第二小的节点
	int min = Integer.MAX_VALUE;
	int secondMin = Integer.MAX_VALUE;
	boolean found = false;

	public int findSecondMinimumValue(TreeNode root) {
		if (root == null)
			return -1;

		if (root.val < min) {
			min = root.val;
		} else if (root.val <= secondMin && root.val > min) {
			found = true;
			secondMin = root.val;
		}
		findSecondMinimumValue(root.left);
		findSecondMinimumValue(root.right);

		return found ? secondMin : -1;
	}

	// 一棵树每层节点值的平均数
	public List<Double> averageOfLevels(TreeNode root) {
		if (root == null)
			return Collections.emptyList();

		final Queue<TreeNode> queue = new LinkedList<TreeNode>();
		final List<Double> avg = new ArrayList<Double>();

		queue.add(root);

		while (!queue.isEmpty()) {
			int count = queue.size();
			Double sum = 0.0;
			for (int i = 0; i < count; i++) {
				TreeNode cNode = queue.poll();
				if (cNode == null)
					break;
				sum += cNode.val;
				if (cNode.left != null)
					queue.add(cNode.left);
				if (cNode.right != null)
					queue.add(cNode.right);
			}
			avg.add(sum / count);
		}
		return avg;
	}

	// 得到最底层靠左节点
	int bottomLeftValue = -1;

	public int findBottomLeftValue(TreeNode root) {
		if (root == null)
			return -1;
		final Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(root);
		while (!queue.isEmpty()) {
			int count = queue.size();
			for (int i = 0; i < count; i++) {
				TreeNode cNode = queue.poll();
				if (i == 0) {
					bottomLeftValue = cNode.val;
				}
				if (cNode.left != null)
					queue.add(cNode.left);
				if (cNode.right != null)
					queue.add(cNode.right);
			}
		}
		return bottomLeftValue;
	}

	// 前中后序遍历 - 递归
	public void dfsPreOrder(TreeNode root) {
		visitNode(root);
		dfsPreOrder(root.left);
		dfsPreOrder(root.right);
	}

	public void dfsInOrder(TreeNode root) {
		dfsInOrder(root.left);
		visitNode(root);
		dfsInOrder(root.right);
	}

	public void dfsPostOrder(TreeNode root) {
		dfsPostOrder(root.left);
		dfsPostOrder(root.right);
		visitNode(root);
	}

	private void visitNode(TreeNode node) {

	}

	// 前中后序遍历 - 非递归
	// 前序
	public List<Integer> preorderTraversal(TreeNode root) {
		final List<Integer> visit = new ArrayList<Integer>();
		if (root == null)
			return visit;
		final Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.add(root);
		while (!stack.isEmpty()) {
			TreeNode cNode = stack.pop();
			visit.add(cNode.val);
			if (cNode.right != null)
				stack.add(cNode.right); // 先右后左，这样出栈就会左子树在前
			if (cNode.left != null)
				stack.add(cNode.left);
		}

		return visit;
	}

	// 前序遍历为 root -> left -> right，后序遍历为 left -> right -> root。
	// 可以修改前序遍历成为 root -> right -> left，那么这个顺序就和后序遍历正好相反。
	public List<Integer> postorderTraversal(TreeNode root) {
		final List<Integer> visit = new ArrayList<Integer>();
		if (root == null)
			return visit;
		final Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.add(root);
		while (!stack.isEmpty()) {
			TreeNode cNode = stack.pop();
			visit.add(cNode.val);
			if (cNode.left != null)
				stack.add(cNode.left); // 先左后右，这样出栈就会右子树在前
			if (cNode.right != null)
				stack.add(cNode.right);
		}
		Collections.reverse(visit);
		return visit;
	}

	// 二叉查找树（BST）：根节点大于等于左子树所有节点，小于等于右子树所有节点。
	// 二叉查找树DFS中序遍历有序。

	// 修剪二叉查找树
	// 只保留值在 L ~ R 之间的节点
	public TreeNode trimBST(TreeNode root, int low, int high) {
		if (root == null)
			return null;

		if (root.val > high)
			return trimBST(root.left, low, high);
		if (root.val < low)
			return trimBST(root.right, low, high);

		root.left = trimBST(root.left, low, high);
		root.right = trimBST(root.right, low, high);

		return root;
	}

	// 寻找二叉查找树的第 k 个元素
	int count = 0;
	int kth = -1;

	public int kthSmallest(TreeNode root, int k) {
		if (root == null)
			return -1;

		dfsInOrder(root, k);
		return kth;
	}

	private void dfsInOrder(TreeNode node, int k) {
		if (node == null)
			return;
		if (node.left != null)
			dfsInOrder(node.left, k);
		count++;
		if (count == k)
			kth = node.val;
		if (node.right != null)
			dfsInOrder(node.right, k);
	}

	// 把二叉查找树每个节点的值都加上比它大的节点的值
	// BST 按照中序遍历即为从小到大，所以从右节点的中序遍历即为从大到小遍历所有值
	// 只要从最大的节点开始加，就可以
	int greatSum = 0;

	public TreeNode convertBST(TreeNode root) {
		if (root == null)
			return null;

		traversRight(root);
		return root;
	}

	private void traversRight(TreeNode root) {
		// 先遍历右子树
		if (root == null)
			return;

		traversRight(root.right);
		greatSum += root.val;
		root.val = greatSum;
		traversRight(root.left);

		return;
	}

	// 二叉查找树的最近公共祖先
	public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null)
			return null;

		if (p.val < root.val && q.val < root.val) {
			return lowestCommonAncestor(root.left, p, q);
		}

		if (p.val > root.val && q.val > root.val) {
			return lowestCommonAncestor(root.right, p, q);
		}

		return root;
	}

	// 236 二叉树的最近公共祖先
	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null)
			return null;

		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.add(root);
		while (!stack.isEmpty()) {
			TreeNode cNode = stack.pop();
			if (cNode != null) {
				// 左右子树任意一个找到一个就有可能
				boolean foundInLeft = foundInTree(cNode.left, p, q);
				boolean foundInRight = foundInTree(cNode.right, p, q);
				if (foundInLeft || foundInRight) {
					// 左右子树有找到的，并且当前节点也是其一
					if (root == p || root == q) {
						return cNode;
					}
					// 左右子树分别找到
					if (foundInLeft && foundInRight) {
						return cNode;
					}
				}
				stack.add(cNode.right);
				stack.add(cNode.left);
			}
		}
		return null;
	}

	private boolean foundInTree(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null)
			return false;

		if (root == p || root == q)
			return true;

		return foundInTree(root.left, p, q) || foundInTree(root.right, p, q);
	}

	// 从有序数组中构造二叉查找树
	public TreeNode sortedArrayToBST(int[] nums) {
		if (nums.length == 0)
			return null;
		return toBST(nums, 0, nums.length - 1);
	}

	private TreeNode toBST(int[] nums, int s, int e) {
		if (e < s)
			return null;

		int mid = (e + s) / 2;
		TreeNode root = new TreeNode(nums[mid], toBST(nums, s, mid - 1), toBST(nums, mid + 1, e));
		return root;
	}

	// 根据有序链表构造平衡的二叉查找树
	public TreeNode sortedListToBST(ListNode head) {
		if (head == null)
			return null;
		if (head.next == null)
			return new TreeNode(head.val);
		if (head.next.next == null)
			return new TreeNode(head.next.val, new TreeNode(head.val), null);

		ListNode slow = head;
		ListNode fast = head.next;
		ListNode pNode = head; // p node is the node before slow

		while (fast != null && fast.next != null) {
			pNode = slow;
			slow = slow.next;
			fast = fast.next.next;
		}
		ListNode newHead = slow.next;
		pNode.next = null;
		return new TreeNode(slow.val, sortedListToBST(head), sortedListToBST(newHead));
	}

	// 在二叉查找树中寻找两个节点，使它们的和为一个给定值
	// 最优解： 使用中序遍历得到有序数组之后，再利用双指针对数组进行查找。
	public boolean findTarget(TreeNode root, int k) {
		if (root == null)
			return false;

		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.add(root);
		while (!stack.isEmpty()) {
			TreeNode cNode = stack.pop();
			if (cNode != null) {
				if (findTargetFromNode(root, cNode, k)) {
					return true;
				}
				stack.add(cNode.left);
				stack.add(cNode.right);
			}
		}
		return false;
	}

	private boolean findTargetFromNode(TreeNode root, TreeNode cNode, int k) {
		if (cNode == null)
			return false;
		if (cNode.val * 2 == k)
			return false;

		return ifTargetExist(root, k - cNode.val);
	}

	private boolean ifTargetExist(TreeNode root, int k) {
		if (root == null)
			return false;
		if (root.val == k)
			return true;

		if (k > root.val) {
			return ifTargetExist(root.right, k);
		}

		return ifTargetExist(root.left, k);
	}

	// 在二叉查找树中查找两个节点之差的最小绝对值
	public int getMinimumDifference(TreeNode root) {
		if (root == null)
			return -1;
		List<Integer> ordered = new ArrayList<Integer>();
		inOrderBST(root, ordered);
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < ordered.size() - 1; i++) {
			min = Math.min(min, Math.abs(ordered.get(i) - ordered.get(i + 1)));
		}
		return min;
	}

	private List<Integer> inOrderBST(TreeNode root, List<Integer> ordered) {
		if (root == null)
			return ordered;
		inOrderBST(root.left, ordered);
		ordered.add(root.val);
		inOrderBST(root.right, ordered);
		return ordered;
	}

	// 寻找二叉查找树中出现次数最多的值
	// 中序遍历，过程中检查出现次数就好

	// 173. Binary Search Tree Iterator
	class BSTIterator {
		private final Stack<TreeNode> stack;

		public BSTIterator(TreeNode root) {
			stack = new Stack<TreeNode>();
			TreeNode currNode = root;
			while (currNode != null) {
				stack.add(currNode);
				currNode = currNode.left;
			}
		}

		public int next() {
			TreeNode currNode = stack.pop();
			int res = currNode.val;
			currNode = currNode.right;
			while (currNode != null) {
				stack.add(currNode);
				currNode = currNode.left;
			}
			return res;
		}

		public boolean hasNext() {
			return stack.size() > 0;
		}
	}

	// 98. Validate Binary Search Tree
	public boolean isValidBST(TreeNode root) {
		long min = Long.MIN_VALUE;
		long max = Long.MAX_VALUE;

		return isValidBSTDfs(root, min, max);
	}

	private boolean isValidBSTDfs(TreeNode root, long min, long max) {
		if (root == null)
			return true;
		boolean ans = false;
		if (root.val > min && root.val < max) {
			ans = true;
		}
		if (root.left != null) {
			ans = ans && isValidBSTDfs(root.left, min, root.val);
		}
		if (root.right != null) {
			ans = ans && isValidBSTDfs(root.right, root.val, max);
		}
		return ans;
	}

	// 116. Populating Next Right Pointers in Each Node
	public Node connect(Node root) {
		Queue<Node> queue = new LinkedList<Node>();
		if (root == null)
			return root;
		queue.add(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				Node currNode = queue.poll();
				if (queue.isEmpty() || i == size - 1) {
					currNode.next = null;
				} else {
					currNode.next = queue.peek();
				}
				if (currNode.left != null)
					queue.add(currNode.left);
				if (currNode.right != null)
					queue.add(currNode.right);
			}
		}
		return root;
	}

	// Flatten Binary Tree to Linked List
	public void flatten(TreeNode root) {
		if (root == null)
			return;
		if (root.left == null) {
			flatten(root.right);
			return;
		}
		if (root.right == null) {
			flatten(root.left);
			root.right = root.left;
			root.left = null;
			return;
		}
		flatten(root.left);
		flatten(root.right);

		// 左子树最终端现在应指向右子树起始位置
		TreeNode currNode = root.left;
		while (currNode != null && currNode.right != null) {
			currNode = currNode.right;
		}
		currNode.right = root.right;
		root.right = root.left;
		root.left = null;
	}

	// Convert Binary Search Tree to Sorted Doubly Linked List
	// the smallest (first) and the largest (last) nodes
	Node first = null;
	Node last = null;

	public Node treeToDoublyList(Node root) {
		if (root == null)
			return root;
		treeToDoublyListDFS(root);
		// close DLL
		last.right = first;
		first.left = last;
		return first;
	}

	public void treeToDoublyListDFS(Node node) {
		if (node != null) {
			// left
			treeToDoublyListDFS(node.left);
			// node
			if (last != null) {
				// link the previous node (last)
				// with the current one (node)
				last.right = node;
				node.left = last;
			} else {
				// keep the smallest node
				// to close DLL later on
				first = node;
			}
			last = node;
			// right
			treeToDoublyListDFS(node.right);
		}
	}

	// Binary Tree Vertical Order Traversal
	// BFS, 每次入栈的时候根据当前节点添加子节点的col值

	// 297. Serialize and Deserialize Binary Tree
	// Encodes a tree to a single string.
	private static final String SEPARATOR = ",";

	public String serialize(TreeNode root) {
		if (root == null)
			return "null";
		StringBuilder sb = new StringBuilder();
		sb.append(root.val);
		sb.append(SEPARATOR);
		sb.append(serialize(root.left));
		sb.append(SEPARATOR);
		sb.append(serialize(root.right));
		return sb.toString();
	}

	// Decodes your encoded data to tree.
	public TreeNode deserialize(String data) {
		Queue<String> queue = new LinkedList<>();
		queue.addAll(Arrays.asList(data.split(SEPARATOR)));
		return deserial(queue);
	}

	private TreeNode deserial(Queue<String> queue) {
		String item = queue.poll();
		if ("null".equals(item)) {
			return null;
		}
		TreeNode root = new TreeNode(Integer.parseInt(item));
		root.left = deserial(queue);
		root.right = deserial(queue);
		return root;
	}

	// 951. Flip Equivalent Binary Trees
	public boolean flipEquiv(TreeNode root1, TreeNode root2) {
		if (root1 == null && root2 == null)
			return true;
		// 只有一个为null，则不相等
		if (root1 == null || root2 == null)
			return false;
		if (root1.val != root2.val)
			return false;

		return (flipEquiv(root1.left, root2.left) && flipEquiv(root1.right, root2.right))
				|| (flipEquiv(root1.right, root2.left) && flipEquiv(root1.left, root2.right));
	}

	// 315. Count of Smaller Numbers After Self
	// 需要Segment Tree 线段树：https://www.youtube.com/watch?v=rYBtViWXYeI

	// 2096. Step-By-Step Directions From a Binary Tree Node to Another
	// find common ancestor first
	class Result {
		int a, b;
		String pathA, pathB;

		Result(int a, int b) {
			this.a = a;
			this.b = b;
		}

		boolean isBothFound() {
			return this.pathA != null && this.pathB != null;
		}
	}

	public String getDirections(TreeNode root, int startValue, int destValue) {
		Result result = new Result(startValue, destValue);
		dfs(root, startValue, destValue, new StringBuilder(), result);
		// find the first match, i means the steps to get to first common ancestor
		int i = 0;
		while (i < Math.min(result.pathA.length(), result.pathB.length())) {
			if (result.pathA.charAt(i) != result.pathB.charAt(i)) {
				break;
			}
			i++;
		}

		StringBuilder sb = new StringBuilder();
		// path from A to common ancestor
		for (int j = result.pathA.length() - 1; j >= i; j--) {
			sb.append("U");
		}

		// path from common ancestor to B
		for (int j = i; j < result.pathB.length(); j++) {
			if (j < result.pathB.length())
				sb.append(result.pathB.charAt(j));
		}

		return sb.toString();
	}

	private void dfs(TreeNode node, int a, int b, StringBuilder sb, Result result) {
		if (node == null)
			return;
		if (node.val == a) {
			result.pathA = sb.toString();
		}
		if (node.val == b) {
			result.pathB = sb.toString();
		}

		int len = sb.length();
		sb.append("L");
		dfs(node.left, a, b, sb, result);
		sb.setLength(len);
		if (result.isBothFound()) {
			return;
		}
		sb.append("R");
		dfs(node.right, a, b, sb, result);
		sb.setLength(len);
	}

	// 156. Binary Tree Upside Down
	public TreeNode upsideDownBinaryTree(TreeNode root) {
		if (root == null)
			return root;

		if (root.left == null) {
			return root;
		}

		TreeNode newRoot = upsideDownBinaryTree(root.left);
		root.left.left = root.right;
		root.left.right = root;
		// Avoid cycle.
		root.left = null;
		root.right = null;
		return newRoot;
	}

	// 366. Find Leaves of Binary Tree
	public List<List<Integer>> findLeaves(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null)
			return ans;
		TreeMap<Integer, List<Integer>> map = new TreeMap<>();
		findLeaves(root, map);
		for (int i : map.keySet()) {
			ans.add(map.get(i));
		}
		return ans;
	}

	public int findLeaves(TreeNode root, TreeMap<Integer, List<Integer>> map) {
		if (root == null)
			return 0;
		int depth = Math.max(findLeaves(root.left, map), findLeaves(root.right, map)) + 1;
		List<Integer> array = map.getOrDefault(depth, new ArrayList<>());
		array.add(root.val);
		map.put(depth, array);
		return depth;
	}

	// 314. Binary Tree Vertical Order Traversal
	public List<List<Integer>> verticalOrder(TreeNode root) {
		List<List<Integer>> output = new ArrayList<>();
		if (root == null) {
			return output;
		}

		Map<Integer, ArrayList<Integer>> columnTable = new HashMap<>();
		// Pair of node and its column offset
		Queue<Pair<TreeNode, Integer>> queue = new ArrayDeque<>();
		int column = 0;
		queue.offer(new Pair<>(root, column));

		int minColumn = 0, maxColumn = 0;

		while (!queue.isEmpty()) {
			Pair<TreeNode, Integer> p = queue.poll();
			root = p.getKey();
			column = p.getValue();

			if (root != null) {
				columnTable.computeIfAbsent(column, z -> new ArrayList<Integer>()).add(root.val);

				minColumn = Math.min(minColumn, column);
				maxColumn = Math.max(maxColumn, column);

				queue.offer(new Pair<>(root.left, column - 1));
				queue.offer(new Pair<>(root.right, column + 1));
			}
		}

		for (int i = minColumn; i < maxColumn + 1; ++i) {
			output.add(columnTable.get(i));
		}

		return output;
	}
}
