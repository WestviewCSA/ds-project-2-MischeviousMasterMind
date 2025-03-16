
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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

    public static char[][][] solve(char map[][][], Flag approach) {

        char solution[][][] = new char[map.length][map[0].length][map[0][0].length];

        for (int i = 0; i < map.length; i++) {
            for (int ii = 0; ii < map[0].length; ii++) {
                System.arraycopy(map[i][ii], 0, solution[i][ii], 0, map[0][0].length);
            }
        }

        switch (approach) {
            case STACK ->
                solveUsingStack(solution);
            case QUEUE -> {
                if (!solveUsingQueue(solution)) {
                    return null;
                }
            }
            case OPT ->
                solveUsingOpt(solution);
        }

        return solution;
    }

    private static void solveUsingStack(char map[][][]) {

        System.err.println("Not implemented yet!");
        System.exit(-1);
    }

    private static boolean solveUsingQueue(char solution[][][]) {

        for (int level = 0; level < solution.length; level++) {

            Queue<PathTile> q = new LinkedList<>();

            PathTile wolverine = findTile(solution[level], 'W');

            q.add(wolverine);

            PathTile path = iterativeSearchUsingQueue(solution[level], q, '$');

            if (path == null) {

                if(level == solution.length - 1) {
                    return false;
                }

                q.clear();
                q.add(wolverine);
                path = iterativeSearchUsingQueue(solution[level], q, '|');

                if (path == null) {
                    return false;
                }
            }

            while (path.previous != null) {

                solution[level][path.row][path.col] = '+';
                path = path.previous;

            }
        }

        return true;

        /*
        for (int level = 0; level < map.length; level++) {

            Queue<Tile> q = new LinkedList<>();

            q.add(findTile(map[level], 'W'));

            boolean found = false;

            Tile current;

            while (!found) {

                current = q.remove();

                for (int direction = NORTH; direction <= WEST && !found; direction++) {

                    Tile adjacent = getAdjacent(map[level], current, direction);

                    if (adjacent == null) {
                        continue;
                    }

                    if (tileType(map[level], adjacent, '*')) {
                        continue;
                    }

                    if (tileType(map[level], adjacent, '$')) {
                        q.clear();
                        q.add(current);
                        found = true;
                    }

                    if (tileType(map[level], adjacent, '.')) {
                        q.add(adjacent);
                        solution[level][adjacent.row][adjacent.col] = '*';
                    }
                }

            }

            current = findTile(map[level], '$');
            int direction = WEST;

            while (true) {

                print(solution);

                Tile adjacent = getAdjacent(solution[level], current, direction);

                if (tileType(map[level], adjacent, 'W')) {

                    break;
                }

                if (tileType(solution[level], adjacent, '*')) {
                    current = adjacent;
                    solution[level][current.row][current.col] = '+';
                } else {

                    direction--;
                    if (direction < NORTH) {
                        direction = WEST;
                    }

                }

            }

        }

        merge(solution, map);
         */
    }

    private static PathTile iterativeSearchUsingQueue(char[][] level, Queue<PathTile> q, char type) {

        boolean explored[][] = new boolean[level.length][level[0].length];

        while (!q.isEmpty()) {

            PathTile current = q.remove();
            explored[current.row][current.col] = true;

            for (int direction = NORTH; direction <= WEST; direction++) {

                PathTile adjacent = new PathTile(current, direction);

                char tile = adjacent.charAt(level);

                if (tile == type) {
                    return current;
                }

                if (tile == '.' && !explored[adjacent.row][adjacent.col]) {
                    q.add(adjacent);
                }
            }
        }

        return null;
    }

    private static void solveUsingOpt(char map[][][]) {

        System.err.println("Not implemented yet!");
        System.exit(-1);

        for (char[][] level : map) {

            Stack<PathTile> t = new Stack<>(), explored = new Stack<>();

        }

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

    private static boolean tileType(char level[][], PathTile tile, char type) {

        return (level[tile.row][tile.col] == type);
    }

    private static PathTile getAdjacent(char level[][], PathTile tile, int direction) {

        if (!((0 <= tile.row) && (tile.row < level.length) && (0 <= tile.col) && (tile.col < level[0].length))) {
            return null;
        }

        PathTile adjacentTile = null;

        switch (direction) {

            case NORTH ->
                adjacentTile = new PathTile(tile.row - 1, tile.col);

            case SOUTH ->
                adjacentTile = new PathTile(tile.row + 1, tile.col);

            case EAST ->
                adjacentTile = new PathTile(tile.row, tile.col + 1);

            case WEST ->
                adjacentTile = new PathTile(tile.row, tile.col - 1);

        }

        return adjacentTile;
    }

    private static void merge(char[][][] solution, char[][][] map) {

        for (int i = 0; i < solution.length; i++) {
            for (int ii = 0; ii < solution[0].length; ii++) {
                for (int iii = 0; iii < solution[0][0].length; iii++) {
                    if (solution[i][ii][iii] == '\0' || solution[i][ii][iii] == '*') {
                        solution[i][ii][iii] = map[i][ii][iii];
                    }
                }
            }
        }
    }

    private static void print(char[][][] map) {
        for (char[][] level : map) {
            for (char[] row : level) {
                for (char tile : row) {
                    if (tile == '\0') {
                        System.out.print(' ');
                    } else {
                        System.out.print(tile);
                    }
                }

                System.out.println();
            }
        }
    }
}
