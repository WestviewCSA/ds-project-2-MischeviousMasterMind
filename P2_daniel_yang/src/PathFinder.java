
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class PathFinder {

    private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
    private static char solution[][][];

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

    public static char[][][] solve(char map[][][], Flag approach) {

        solution = new char[map.length][map[0].length][map[0][0].length];

        switch (approach) {
            case STACK ->
                solveUsingStack(map);
            case QUEUE ->
                solveUsingQueue(map);
            case OPT ->
                solveUsingOpt(map);
        }

        return solution;
    }

    private static void solveUsingStack(char map[][][]) {

        System.err.println("Not implemented yet!");
        System.exit(-1);
    }

    private static void solveUsingQueue(char map[][][]) {

        for (int level = 0; level < map.length; level++) {

            Queue<Tile> q = new LinkedList<>();

            q.add(findTile(map[level], 'W'));

            boolean found = false;

            while (!found) {

                Tile current = q.remove();

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

        }

        merge(solution, map);
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
}
