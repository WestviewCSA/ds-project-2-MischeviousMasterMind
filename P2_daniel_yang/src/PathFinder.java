
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class PathFinder {

    private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;

    private static class Tile {

        final int row, col;
        final char type;

        Tile(int row, int col, char type) {
            this.row = row;
            this.col = col;
            this.type = type;
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

        for (char[][] level : map) {

            for (int i = 0; i < map[0].length; i++) {
                for (int ii = 0; ii < map[0][0].length; ii++) {
                    if (level[i][ii] == '.') {
                        level[i][ii] = '+';
                    }
                }
            }

        }
    }

    private static void solveUsingQueue(char map[][][]) {

        for (char[][] level : map) {

            Queue<Tile> q = new LinkedList<>();

            q.add(findTile(level, 'W'));

            boolean found = false;

            while (!found) {

                Tile current = q.remove();

                for (int direction = NORTH; direction <= WEST && !found; direction++) {

                    Tile adjacent = getAdjacent(level, current, direction);

                    if (adjacent == null) {
                        continue;
                    }

                    if(tileType(level, adjacent, '*')) {
                        continue;
                    }

                    if (tileType(level, adjacent, '$')) {
                        q.clear();
                        q.add(current);
                        found = true;
                    }

                    if (tileType(level, adjacent, '.')) {
                        q.add(adjacent);
                        level[adjacent.row][adjacent.col] = '*';
                    }
                }

            }

            
        }
    }

    private static void solveUsingOpt(char map[][][]) {

        System.err.println("Not implemented yet!");
        System.exit(-1);

        for (char[][] level : map) {

            Stack<Tile> t = new Stack<>(), explored = new Stack<>();

        }

    }

    private static Tile findTile(char level[][], char tileType) {

        for (int i = 0; i < level.length; i++) {
            for (int ii = 0; ii < level[0].length; ii++) {
                if (level[i][ii] == tileType) {
                    return new Tile(i, ii, tileType);
                }
            }
        }

        return null;
    }

    private static boolean tileType(char level[][], Tile tile, char type) {

        return (level[tile.row][tile.col] == type);
    }

    private static Tile getAdjacent(char level[][], Tile tile, int direction) {

        if (!((0 <= tile.row) && (tile.row < level.length) && (0 <= tile.col) && (tile.col < level[0].length))) {
            return null;
        }

        Tile adjacentTile = null;

        switch (direction) {

            case NORTH ->
                adjacentTile = new Tile(tile.row - 1, tile.col, level[tile.row - 1][tile.col]);

            case SOUTH ->
                adjacentTile = new Tile(tile.row + 1, tile.col, level[tile.row + 1][tile.col]);

            case EAST ->
                adjacentTile = new Tile(tile.row, tile.col + 1, level[tile.row][tile.col + 1]);

            case WEST ->
                adjacentTile = new Tile(tile.row, tile.col - 1, level[tile.row][tile.col - 1]);

        }

        return adjacentTile;
    }
}
