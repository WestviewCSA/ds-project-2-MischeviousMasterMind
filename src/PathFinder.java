
import java.util.ArrayDeque;
import java.util.Deque;

public class PathFinder {

	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;

	private static class PathTile {

		final int row, col;
		PathTile previous = null;

		PathTile(int row, int col) {
			this.row = row;
			this.col = col;
		}

		PathTile(PathTile previous, int direction) {

			this.previous = previous;

			switch (direction) {

			case NORTH -> {
				row = previous.row + 1;
				col = previous.col;
			}

			case SOUTH -> {
				row = previous.row - 1;
				col = previous.col;
			}

			case EAST -> {
				row = previous.row;
				col = previous.col + 1;
			}

			case WEST -> {
				row = previous.row;
				col = previous.col - 1;
			}

			default -> {
				row = previous.row;
				col = previous.col;
			}
			}
		}

		char charAt(char level[][]) {

			if (row < 0 || row >= level.length || col < 0 || col >= level[0].length) {
				return '\0';
			}

			return level[row][col];
		}
	}

	public static boolean solve(char map[][][], Approach approach) {

		for (int level = 0; level < map.length; level++) {

			Deque<PathTile> q = new ArrayDeque<>();

			PathTile wolverine = findTile(map[level], 'W');

			if (wolverine == null) {
				return false;
			}

			q.add(wolverine);

			PathTile path = null;

			if (findTile(map[level], '$') != null) {

				switch (approach) {
				case QUEUE -> path = searchUsingStack(map[level], q, '$');
				case STACK -> path = searchUsingQueue(map[level], q, '$');
				case OPT -> path = searchUsingDepth(map[level], q, '$');
				}
			}

			if (path == null) {

				if (level == map.length - 1 || findTile(map[level], '|') == null) {
					return false;
				}

				q.clear();
				q.add(wolverine);

				switch (approach) {

				case QUEUE -> path = searchUsingStack(map[level], q, '|');
				case STACK -> path = searchUsingQueue(map[level], q, '|');
				case OPT -> path = searchUsingDepth(map[level], q, '|');
				}

				if (path == null) {
					return false;
				}
			}

			while (path.previous != null) {

				map[level][path.row][path.col] = '+';
				path = path.previous;
			}
		}

		return true;
	}

	private static PathTile searchUsingQueue(char[][] level, Deque<PathTile> q, char type) {

		boolean explored[][] = new boolean[level.length][level[0].length];

		while (!q.isEmpty()) {

			// equivalent to dequeue()
			PathTile current = q.removeFirst();

			explored[current.row][current.col] = true;

			for (int direction = NORTH; direction <= WEST; direction++) {

				PathTile adjacent = new PathTile(current, direction);

				char tile = adjacent.charAt(level);

				if (tile == '.') {
					if (!explored[adjacent.row][adjacent.col]) {
						// equivalent to queue()
						q.add(adjacent);
					}

				} else if (tile == type) {

					return current;
				}
			}
		}

		return null;
	}

	private static PathTile searchUsingStack(char[][] level, Deque<PathTile> q, char type) {

		boolean explored[][] = new boolean[level.length][level[0].length];

		while (!q.isEmpty()) {

			// equivalent to pop()
			PathTile current = q.removeLast();

			explored[current.row][current.col] = true;

			for (int direction = NORTH; direction <= WEST; direction++) {

				PathTile adjacent = new PathTile(current, direction);

				char tile = adjacent.charAt(level);

				if (tile == '.') {
					if (!explored[adjacent.row][adjacent.col]) {
						// equivalent to push()
						q.add(adjacent);
					}

				} else if (tile == type) {

					return current;
				}
			}
		}

		return null;
	}

	private static PathTile searchUsingDepth(char[][] level, Deque<PathTile> q, char type) {

		int minDepth = Integer.MAX_VALUE;
		int depth[][] = new int[level.length][level[0].length];
		PathTile shortestPath = null;

		while (!q.isEmpty()) {

			PathTile current = q.removeFirst();

			for (int direction = NORTH; direction <= WEST; direction++) {

				PathTile adjacent = new PathTile(current, direction);

				char tile = adjacent.charAt(level);

				if (tile == '.') {

					//
					// In general, I hate nesting code. But in this case, it is necessary for two
					// major reasons:
					//
					// (1) If adjacent tile is out of bounds, then adjacent.row and adjacent.col
					// would also
					// be outside the bounds of depth[][]. Putting all the conditions in one
					// statement will
					// then return an error since all three conditions would be checked
					// simultaneously such
					// that even if the adjacent tile is out of bounds, the other two conditions
					// would
					// be checked anyways.
					//
					// (2) It is extremely optimal as if (tile != '.'), then the other two
					// conditions needn't
					// to be checked. The less conditions checked each iteration, the more efficient
					// the program is.
					//
					if ((depth[current.row][current.col] < minDepth)
							&& (depth[adjacent.row][adjacent.col] > depth[current.row][current.col] + 1
									|| depth[adjacent.row][adjacent.col] == 0)) {

						depth[adjacent.row][adjacent.col] = depth[current.row][current.col] + 1;
						q.addFirst(adjacent);
					}

				} else if (tile == type) {

					minDepth = depth[current.row][current.col];
					shortestPath = current;

				}
			}
		}

		return shortestPath;
	}

	private static PathTile findTile(char level[][], char tileType) {

		for (int i = 0; i < level.length; i++) {
			for (int ii = 0; ii < level[0].length; ii++) {
				if (level[i][ii] == tileType) {
					return new PathTile(i, ii);
				}
			}
		}

		return null;
	}
}
