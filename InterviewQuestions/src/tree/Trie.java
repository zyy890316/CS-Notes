package tree;

public class Trie {
	private TrieNode root;

	/** Initialize your data structure here. */
	public Trie() {
		root = new TrieNode();
	}

	/** Inserts a word into the trie. */
	public void insert(String word) {
		TrieNode cNode = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (cNode.containsKey(ch)) {
				cNode = cNode.get(ch);
			} else {
				final TrieNode newNode = new TrieNode();
				cNode.put(ch, newNode);
				cNode = newNode;
			}
		}
		cNode.setEnd();
	}

	/** Returns if the word is in the trie. */
	public boolean search(String word) {
		TrieNode cNode = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (!cNode.containsKey(ch)) {
				return false;
			}
			cNode = cNode.get(ch);
		}
		return cNode.isEnd() ? true : false;
	}

	/**
	 * Returns if there is any word in the trie that starts with the given prefix.
	 */
	public boolean startsWith(String prefix) {
		TrieNode cNode = root;
		for (int i = 0; i < prefix.length(); i++) {
			char ch = prefix.charAt(i);
			if (!cNode.containsKey(ch)) {
				return false;
			}
			cNode = cNode.get(ch);
		}
		return true;
	}

	private class TrieNode {

		// R links to node children
		private TrieNode[] links;

		private final int R = 26;

		private boolean isEnd;

		public TrieNode() {
			links = new TrieNode[R];
		}

		public boolean containsKey(char ch) {
			return links[ch - 'a'] != null;
		}

		public TrieNode get(char ch) {
			return links[ch - 'a'];
		}

		public void put(char ch, TrieNode node) {
			links[ch - 'a'] = node;
		}

		public void setEnd() {
			isEnd = true;
		}

		public boolean isEnd() {
			return isEnd;
		}
	}
}
