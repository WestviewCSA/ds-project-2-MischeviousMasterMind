
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

    public static char[][][] solve(char map[][][], Approach approach) {

        char solution[][][] = new char[map.length][map[0].length][map[0][0].length];

        for (int i = 0; i < map.length; i++) {
            for (int ii = 0; ii < map[0].length; ii++) {
                System.arraycopy(map[i][ii], 0, solution[i][ii], 0, map[0][0].length);
            }
        }

        for (int level = 0; level < solution.length; level++) {

            Deque<PathTile> q = new ArrayDeque<>();

            PathTile wolverine = findTile(solution[level], 'W');

            q.add(wolverine);

            PathTile path = null;

            switch(approach) {

                case QUEUE -> path = searchUsingDeque(solution[level], q, '$', true);
                case STACK -> path = searchUsingDeque(solution[level], q, '$', false);
                case OPT -> path = searchUsingDepth(solution[level], q, '$');
            }

            if (path == null) {

                if (level == solution.length - 1) {
                    return null;
                }

                q.clear();
                q.add(wolverine);

                switch(approach) {

                    case QUEUE -> path = searchUsingDeque(solution[level], q, '|', true);
                    case STACK -> path = searchUsingDeque(solution[level], q, '|', false);
                    case OPT -> path = searchUsingDepth(solution[level], q, '|');
                }

                if (path == null) {
                    return null;
                }
            }

            while (path.previous != null) {

                solution[level][path.row][path.col] = '+';
                path = path.previous;

            }
        }

        return solution;
    }

    private static PathTile searchUsingDeque(char[][] level, Deque<PathTile> q, char type, boolean useQueue) {

        boolean explored[][] = new boolean[level.length][level[0].length];

        while (!q.isEmpty()) {

            // equivalent to dequeue() or pop()
            PathTile current = q.removeFirst();

            explored[current.row][current.col] = true;

            for (int direction = NORTH; direction <= WEST; direction++) {

                PathTile adjacent = new PathTile(current, direction);

                char tile = adjacent.charAt(level);

                if (tile == type) {
                    return current;
                }

                if (tile == '.' && !explored[adjacent.row][adjacent.col]) {
                    if (useQueue) {
                        // equivalent to queue()
                        q.addLast(adjacent);
                    } else {
                        // equivalent to push()
                        q.addFirst(adjacent);
                    }
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

                if(tile == '\0') {
                    continue;
                }

                if (tile == type) {
                    minDepth = depth[current.row][current.col];
                    shortestPath = current;
                    break;
                }

                if (depth[current.row][current.col] >= minDepth) {
                    break;
                }

                if (tile == '.' && (depth[adjacent.row][adjacent.col] > depth[current.row][current.col] + 1) || depth[adjacent.row][adjacent.col] == 0) {

                    depth[adjacent.row][adjacent.col] = depth[current.row][current.col] + 1;
                    q.addFirst(adjacent);
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
