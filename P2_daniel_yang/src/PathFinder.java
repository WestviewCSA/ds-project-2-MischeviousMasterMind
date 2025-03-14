
import java.util.Stack;

public class PathFinder {

    private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
    private static char solution[][][];

    private static class Location {

        final int row, col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", row, col);
        }
    }

    public static void solve(char map[][][], Flag approach) {

        switch (approach) {
            case STACK ->
                solveUsingStack(map);
            case QUEUE ->
                solveUsingQueue(map);
            case OPT ->
                solveUsingOpt(map);
        }
    }

    private static void solveUsingStack(char map[][][]) {

        System.err.println("Not implemented yet!");
        System.exit(-1);
    }

    private static void solveUsingQueue(char map[][][]) {

        

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

    private static void solveUsingOpt(char map[][][]) {

        System.err.println("Not implemented yet!");
        System.exit(-1);

        for (char[][] level : map) {

            Stack<Location> t = new Stack<>(), explored = new Stack<>();

        }

    }

    private static Location findTile(char level[][], char tileType) {

        for (int i = 0; i < level.length; i++) {
            for (int ii = 0; ii < level[0].length; ii++) {
                if (level[i][ii] == tileType) {
                    return new Location(i, ii);
                }
            }
        }

        return null;
    }

    private static boolean tileType(char level[][], Location tile, char type) {

        return (level[tile.row][tile.col] == type);
    }

    private static Location getAdjacent(char level[][], Location tile, int direction) {

        if (!((0 <= tile.row) && (tile.row < level.length) && (0 <= tile.col) && (tile.col < level[0].length))) {
            return null;
        }

        Location adjacentTile = null;

        switch (direction) {

            case NORTH ->
                adjacentTile = new Location(tile.row - 1, tile.col);

            case SOUTH ->
                adjacentTile = new Location(tile.row + 1, tile.col);

            case EAST ->
                adjacentTile = new Location(tile.row, tile.col + 1);

            case WEST ->
                adjacentTile = new Location(tile.row, tile.col - 1);

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
