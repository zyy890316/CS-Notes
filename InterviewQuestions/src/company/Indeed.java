package company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class Indeed {
	// Given m sorted lists, find out the elements more than k times. If an element
	// appear more than once in a list, count it only once.
	// m 个stream做词频统计
	// Use a min-heap. 类似于merge k-sorted lists的方法。首先把每个list的第一个元素放入min PQ 中去。
	List<Integer> findMoreThanKTimes(List<List<Integer>> lists, int k) {
		if (lists == null || lists.size() == 0) {
			return null;
		}
		List<Integer> result = new ArrayList<>();
		PriorityQueue<Node> minPQ = new PriorityQueue<>(new MyNodeComparator());
		// step 1: put the first node of each list into the queue
		for (List<Integer> list : lists) {
			if (list != null && !list.isEmpty()) {
				minPQ.offer(new Node(list.iterator()));
			}
		}
		while (!minPQ.isEmpty()) {
			Node curr = minPQ.poll();
			int currVal = curr.val;
			int count = 1;
			// put the next node into pq, skip the repeated element
			while (curr.iterator.hasNext()) {
				int nextVal = curr.iterator.next();
				if (currVal == nextVal) {
					continue;
				} else {
					curr.val = nextVal;
					minPQ.offer(curr);
					break;
				}
			}
			// get all repeated elements from the pq
			while (!minPQ.isEmpty() && currVal == minPQ.peek().val) {
				count++;
				Node node = minPQ.poll();
				int nodeVal = node.val;
				// put the next node into pq, skip the repeated elements
				while (node.iterator.hasNext()) {
					int nextNodeVal = node.iterator.next();
					if (nodeVal == nextNodeVal) {
						continue;
					} else {
						node.val = nextNodeVal;
						minPQ.offer(node);
						break;
					}
				}
			}
			if (count >= k) {
				result.add(currVal);
			}
		}
		return result;
	}

	class MyNodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node a, Node b) {
			return a.val - b.val;
		}
	}

	class Node {
		int val;
		Iterator<Integer> iterator;

		public Node(Iterator<Integer> iterator) {
			this.iterator = iterator;
			val = iterator.next();
		}
	}
}
