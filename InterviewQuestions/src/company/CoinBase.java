package company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// https://www.1point3acres.com/bbs/tag-2869-3.html
// https://www.1point3acres.com/bbs/thread-848922-1-1.html
// https://www.1point3acres.com/bbs/thread-847327-1-1.html
// https://www.1point3acres.com/bbs/thread-823713-1-1.html
// https://www.1point3acres.com/bbs/thread-851967-1-1.html
// https://www.1point3acres.com/bbs/thread-863593-1-1.html
// https://www.1point3acres.com/bbs/thread-858492-1-1.html
// https://www.1point3acres.com/bbs/thread-856065-1-1.html
// https://www.1point3acres.com/bbs/thread-853544-1-1.html
// https://www.1point3acres.com/bbs/thread-858626-1-1.html
// https://www.1point3acres.com/bbs/thread-857190-1-1.html
// https://www.1point3acres.com/bbs/thread-807965-1-1.html
// https://leetcode.com/discuss/interview-experience/923447/coinbase-sde-bay-area-2020-reject
// https://www.1point3acres.com/bbs/thread-642147-1-1.html

//system design
// 比特币交易平台
// 设计信用卡申请的验证系统: https://docs.oracle.com/cd/E69185_01/cwdirect/pdf/180/cwdirect_user_reference/SO16_01.htm

public class CoinBase {

	public static void main(String[] args) {
		List<Integer> a = new ArrayList<>(Arrays.asList(1, 2, 3));
		List<Integer> b = new ArrayList<>(Arrays.asList(4));
		List<Integer> c = new ArrayList<>(Arrays.asList(5, 6));
		List<List<Integer>> input = new ArrayList<>(Arrays.asList(a, b, c));
		interleavePrint(input);
	}

	// 有一些transaction，
	// 每一个transaction都有一个size和fee。我们要从这些transaction中挑出一些，条件是选出来的transaction的size总和不能超过一个limit，同时最大化fee。
	// 0-1背包
	public int knapsack(int W, int N, int[] weights, int[] values) {
		// 其中 dp[i][j] 表示前 i 件物品在背包承重不超过 j 的情况下能达到的最大价值
		int[][] dp = new int[N + 1][W + 1];
		for (int i = 1; i < N; i++) {
			// 第i件物品的重量和价值
			int weight = weights[i - 1], value = values[i - 1];
			for (int j = 1; j < W; j++) {
				if (j >= weight) {
					// 第 i 件物品没添加到背包时，总体积不超过 j 的前 i 件物品的最大价值就是总体积不超过 j 的前 i-1 件物品的最大价值
					// 第 i 件物品添加到背包中，dp[i][j] = dp[i-1][j-w] + v
					dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight] + value);
				} else {
					// 第 i 件物品由于太重，没添加到背包
					dp[i][j] = dp[i - 1][j];
				}
			}
		}
		return dp[N][W];
	}

	// list iterator: https://www.1point3acres.com/bbs/thread-831571-1-1.html
	// https://techdevguide.withgoogle.com/resources/former-interview-question-flatten-iterators/
	class InterleavingFlattener<T> {
		private Queue<Iterator<T>> iterqueue;

		public InterleavingFlattener(Iterator<T>[] iterlist) {
			this.iterqueue = new LinkedList<>();
			for (int i = 0; i < iterlist.length; i++) {
				if (iterlist[i].hasNext())
					this.iterqueue.add(iterlist[i]);
			}
		}

		public T next() throws NoSuchElementException {
			if (!hasNext())
				throw new NoSuchElementException();
			Iterator<T> it = this.iterqueue.poll();
			T val = it.next();
			if (it.hasNext())
				this.iterqueue.add(it);
			return val;
		}

		public boolean hasNext() {
			return !this.iterqueue.isEmpty();
		}
	}

	public static void interleavePrint(List<List<Integer>> input) {
		int numOfLists = input.size();
		int printedLists = 0;
		List<Integer> ans = new ArrayList<>();
		for (int i = 0;; i++) {
			for (int j = 0; j < numOfLists; j++) {
				List<Integer> currList = input.get(j);
				if (currList.size() >= i + 1) {
					ans.add(currList.get(i));
					if (currList.size() == i + 1) {
						// currList has no more element to print
						printedLists++;
					}
				}
			}
			if (printedLists == numOfLists) {
				break;
			}
		}
		System.out.println(ans);
	}

	// 换汇
	// https://leetcode.com/discuss/interview-question/1647092/coinbase-virtual-on-site-interview-questions

	// connect-4
	// https://ssaurel.medium.com/creating-a-connect-four-game-in-java-f45356f1d6ba
	class ConnectFour {
		// dimensions for our board
		private final int width, height;
		// grid for the board
		private final char[][] grid;
		// we store last move made by a player
		private int lastCol = -1, lastTop = -1;

		public ConnectFour(int w, int h) {
			width = w;
			height = h;
			grid = new char[h][];

			// init the grid will blank cell
			for (int i = 0; i < h; i++) {
				Arrays.fill(grid[i] = new char[w], '.');
			}
		}

		// we use Streams to make a more concise method
		// for representing the board
		public String toString() {
			return IntStream.range(0, width).mapToObj(Integer::toString).collect(Collectors.joining()) + "\n"
					+ Arrays.stream(grid).map(String::new).collect(Collectors.joining("\n"));
		}

		// get string representation of the row containing
		// the last play of the user
		public String horizontal() {
			return new String(grid[lastTop]);
		}

		// get string representation fo the col containing
		// the last play of the user
		public String vertical() {
			StringBuilder sb = new StringBuilder(height);

			for (int h = 0; h < height; h++) {
				sb.append(grid[h][lastCol]);
			}

			return sb.toString();
		}

		// get string representation of the "/" diagonal
		// containing the last play of the user
		public String slashDiagonal() {
			StringBuilder sb = new StringBuilder(height);

			for (int h = 0; h < height; h++) {
				int w = lastCol + lastTop - h;

				if (0 <= w && w < width) {
					sb.append(grid[h][w]);
				}
			}

			return sb.toString();
		}

		// get string representation of the "\"
		// diagonal containing the last play of the user
		public String backslashDiagonal() {
			StringBuilder sb = new StringBuilder(height);

			for (int h = 0; h < height; h++) {
				int w = lastCol - lastTop + h;

				if (0 <= w && w < width) {
					sb.append(grid[h][w]);
				}
			}

			return sb.toString();
		}

		// static method checking if a substring is in str
		public boolean contains(String str, String substring) {
			return str.indexOf(substring) >= 0;
		}

		// now, we create a method checking if last play is a winning play
		public boolean isWinningPlay() {
			if (lastCol == -1) {
				System.err.println("No move has been made yet");
				return false;
			}

			char sym = grid[lastTop][lastCol];
			// winning streak with the last play symbol
			String streak = String.format("%c%c%c%c", sym, sym, sym, sym);

			// check if streak is in row, col,
			// diagonal or backslash diagonal
			return contains(horizontal(), streak) || contains(vertical(), streak) || contains(slashDiagonal(), streak)
					|| contains(backslashDiagonal(), streak);
		}

		// prompts the user for a column, repeating until a valid choice is made
		public void chooseAndDrop(char symbol, Scanner input) {
			do {
				System.out.println("\nPlayer " + symbol + " turn: ");
				int col = input.nextInt();

				// check if column is ok
				if (!(0 <= col && col < width)) {
					System.out.println("Column must be between 0 and " + (width - 1));
					continue;
				}

				// now we can place the symbol to the first
				// available row in the asked column
				for (int h = height - 1; h >= 0; h--) {
					if (grid[h][col] == '.') {
						grid[lastTop = h][lastCol = col] = symbol;
						return;
					}
				}

				// if column is full ==> we need to ask for a new input
				System.out.println("Column " + col + " is full.");
			} while (true);
		}
	}
}
