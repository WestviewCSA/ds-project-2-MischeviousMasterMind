
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Class responsible for all the pathfinding in the maze. Has three approaches:
 * Stack, Queue, and Optimal.
 *
 * @author Daniel Yang
 */
public class PathFinder {

    /**
     * Cardinal direction as an integer such that it can be iterated in a for
     * loop.
     */
    private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;

    /**
     * Used to keep track the tiles on a path. Contains a pointer to the
     * previous path tile such that Wolverine can backtrack once he found the
     * coin.
     */
    private static class PathTile {

        // coordinates of the tile and pointer to the previous tile
        final int row, col;
        PathTile previous = null;

        /**
         * Used to construct the starting tile of the path. The pointer to the
         * previous tile is left as null.
         *
         * @param row row coordinate
         * @param col column coordinate
         */
        PathTile(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * Constructs a tile that is relative to the previous tile through a
         * cardinal direction. The previous tile is stored in the pointer of
         * this tile and the position of this tile is set to NORTH, SOUTH, EAST,
         * or WEST of the previous tile.
         *
         * @param previous the previous tile
         * @param direction one of the four cardinal directions (as an integer)
         */
        PathTile(PathTile previous, int direction) {

            // points to the previous tile
            this.previous = previous;

            // sets its direction relative to the previous tile depending on what cardinal direction is passed to the parameter
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

        /**
         * Returns the character at the position of the tile in the provided 2D
         * char array
         *
         * @param level
         * @return the character at the position of the tile in the provided 2D
         * char array
         */
        char charAt(char level[][]) {

            if (row < 0 || row >= level.length || col < 0 || col >= level[0].length) {
                return '\0';
            }

            return level[row][col];
        }
    }

    /**
     * Finds and returns the coordinate of the tile with the character parameter
     * as a PathTile. Only returns the first occurence of the character
     * (top-down left-to-right). Returns null if a tile with the character
     * cannot be found.
     *
     * @param level the 2D character array of the maze floor
     * @param tileType the tile type as a character
     * @return the PathTile with the same coordinate as the tile with the
     * character
     */
    private static PathTile findTile(char level[][], char tileType) {

        // nested for loop to traverse the 2D character array
        for (int i = 0; i < level.length; i++) {
            for (int ii = 0; ii < level[0].length; ii++) {
                if (level[i][ii] == tileType) {
                    return new PathTile(i, ii);
                }
            }
        }

        return null;
    }

    /**
     * One of only two methods that are public. The passed in character array
     * {@code map} is modified by this method to include the path represented by
     * the {@code '+'} character. Depending on what {@code approach} is passed,
     * it will use that approach to solve the maze. Different approaches may
     * result in different paths. Returns true if a coin can be found and false
     * otherwise.
     *
     * @param map 3D character array of the maze
     * @param approach what approach that is used to solve
     * @return true if a coin can be found, false otherwise
     */
    public static boolean solve(char map[][][], Approach approach) {

        // solves each floor seperately
        for (int level = 0; level < map.length; level++) {

            // initializes a Deque object which can act either as a Queue or a Stack depending on the approach
            Deque<PathTile> q = new ArrayDeque<>();

            // finds and sets this variable to the PathTile with the corresponding coordinates of Wolverine
            PathTile wolverine = findTile(map[level], 'W');

            // if Wolverine does not appear on the floor, immediately exits the method by returning false (cannot be solved)
            if (wolverine == null) {
                return false;
            }

            // enqueues or pushes Wolverine's starting position
            q.add(wolverine);

            // this PathTile will be the tile adjacent to the coin or an open walkway
            // when either is found, it will be used to retrace the path back to Wolverine
            PathTile path = null;

            // searches the maze if there is a coin using the given appraoch
            if (findTile(map[level], '$') != null) {

                switch (approach) {
                    case QUEUE ->
                        path = searchUsingStack(map[level], q, '$');
                    case STACK ->
                        path = searchUsingQueue(map[level], q, '$');
                    case OPT ->
                        path = searchUsingDepth(map[level], q, '$');
                }
            }

            // if the coin does not exist at the maze floor at all OR it cannot be found by Wolverine, the method will then search for a walkway
            if (path == null) {

                // if this floor is the last floor of the maze or there is no walkway on this floor, immediately return false (cannot be solved)
                if (level == map.length - 1 || findTile(map[level], '|') == null) {
                    return false;
                }

                // clears the queue/stack of PathTiles to begin the search again
                q.clear();

                // eqneue/pop Wolverine's starting position again
                q.add(wolverine);

                // now searches for the walkway with the given approach
                switch (approach) {

                    case QUEUE ->
                        path = searchUsingStack(map[level], q, '|');
                    case STACK ->
                        path = searchUsingQueue(map[level], q, '|');
                    case OPT ->
                        path = searchUsingDepth(map[level], q, '|');
                }

                // if the walkway still cannot be found, return false
                if (path == null) {
                    return false;
                }
            }

            // once the coin or walkway of the floor has been found, the PathTiles will then retrace the pathway by 
            // continuously iterating to the previous PathTile and marking the path on the map with the '+' character
            //
            // this works sort of like a linked list where each Node points to the next can be traversed using the linked chain of nodes
            while (path.previous != null) {

                map[level][path.row][path.col] = '+';
                path = path.previous;
            }
        }

        // returns true to show that the coin can be found
        return true;
    }

    /**
     * Uses a queue to enqueue and dequeue tiles as Wolverine traverses through
     * the maze. Precondition is that Wolverine's starting position is already
     * enqueued when the queue is passed in the parameter. Searches for the tile
     * of the specified {@code type}.
     *
     * First tile is dequeued and all adjacent open tiles immediately NORTH,
     * SOUTH, EAST, and WEST (in that order) of the dequeued tile are enqueued.
     * If the tile has already been explored, it cannot be enqueued. This
     * process is repreated until either an adjacent tile is next to the target
     * tile, or there are no more searchable tiles in the queue. If the target
     * tile cannot be found, returns {@code null}.
     *
     * This method usually finds a shorter path than
     * {@link #searchUsingStack(char[][], Deque<PathTile>, char)}, however it is
     * awfully slower and memory inefficient. Using this appraoch for enormous
     * mazes is highly discouraged.
     *
     * @param level 2D character array of the floor of the maze
     * @param q Queue of tiles that will be searched
     * @param type character of the target tile
     * @return the {@link PathTile} that is adjacent to the tile of the given
     * type if it is found, {@code null} otherwise
     * @see #searchUsingStack(char[][], Deque, char)
     */
    private static PathTile searchUsingQueue(char[][] level, Deque<PathTile> q, char type) {

        // keeps track of the tiles that are already explored
        boolean explored[][] = new boolean[level.length][level[0].length];

        // loops keeps running until there are no more searchable tiles or it is broken out if the target tile has been found
        while (!q.isEmpty()) {

            // dequeues the next tile to be searched
            // removeFirst() is equivalent to dequeue()
            PathTile current = q.removeFirst();

            // marks the dequeued tile as explored
            explored[current.row][current.col] = true;

            // iterates through the adjacent tiles NORTH, SOUTH, EAST, and WEST of the current tile
            for (int direction = NORTH; direction <= WEST; direction++) {

                // constructs a new PathTile object that is adjacent to the current tile in the corresponding direction
                PathTile adjacent = new PathTile(current, direction);

                // gets the character of the corresponding tile of the maze
                char tile = adjacent.charAt(level);

                // if the tile is open and unexplored, it will enqueue it to the queue to be searched later
                if (tile == '.') {
                    if (!explored[adjacent.row][adjacent.col]) {
                        // add() is equivalent to enqueue()
                        q.add(adjacent);
                    }

                    // if the adjacent tile is the target tile, then it will return the CURRENT tile (not the adjacent tile) to retrace the path
                } else if (tile == type) {

                    return current;
                }
            }
        }

        // this return statement can only be reached if all the tiles are searched and none of them have the target tile
        return null;
    }

    /**
     * Literally <b>EXACTLY</b> the same as
     * {@link #searchUsingQueue(char[][], Deque, char)} EXCEPT that the tiles
     * are POPPED instead of DEQUEUED. These two methods only have ONE LINE that
     * are different from each other.
     *
     * In {@code searchUsingStack} (this method)
     * <pre>
     * <codeblock>
     *      // dequeues the next tile to be searched
     *      // removeFirst() is equivalent to dequeue()
     *      PathTile current = s.removeLast();
     * </codeblock>
     * </pre> In {@code searchUsingQueue} (the other method)
     * <pre>
     * <codeblock>
     *      // pops the next tile to be searched
     *      // removeLast() is equivalent to pop()
     *      PathTile current = q.removeFirst();
     * </codeblock>
     * </pre>
     *
     * This method finds a path much faster than
     * {@link #searchUsingStack(char[][], Deque<PathTile>, char)}, however the
     * path it finds is sometimes arduously long.
     *
     * For high efficiency and optimal paths, use
     * {@link #searchUsingDepth(char[][], Deque<PathTile>, char)}.
     *
     * The reason why I seperated the two methods is for efficiency. I don't
     * want to check one extra condition each iteration, plus it looks cleaner.
     * But yeah, there is almost no different in the code, yet there may be
     * major changes in resulting path.
     *
     * @param level 2D character array of the floor of the maze
     * @param s Stack of tiles that will be searched
     * @param type character of the target tile
     * @return the {@link PathTile} that is adjacent to the tile of the given
     * type if it is found, {@code null} otherwise
     * @see #searchUsingQueue(char[][], Deque, char)
     */
    private static PathTile searchUsingStack(char[][] level, Deque<PathTile> s, char type) {

        boolean explored[][] = new boolean[level.length][level[0].length];

        while (!s.isEmpty()) {

            // pops the next tile to be searched
            // removeLast() is equivalent to pop()
            PathTile current = s.removeLast(); // <- literally the only line that is different from searchUsingQueue

            explored[current.row][current.col] = true;

            for (int direction = NORTH; direction <= WEST; direction++) {

                PathTile adjacent = new PathTile(current, direction);

                char tile = adjacent.charAt(level);

                if (tile == '.') {
                    if (!explored[adjacent.row][adjacent.col]) {
                        // equivalent to push()
                        s.add(adjacent);
                    }

                } else if (tile == type) {

                    return current;
                }
            }
        }

        return null;
    }

    /**
     * Method used for optimal approach. This is effectively a modified Stack
     * approach. However, instead of only keeping track of which tiles have been
     * traversed, it stores how far away each tile is from Wolverine's starting
     * position in a 2D integer array (which I call depth).
     *
     * As each adjacent tile is searched, it checks to see if the current depth
     * of the tile is less than the previoiusly determined depth. If the depth
     * hasn't been determined yet, then the tile will be pushed and the depth
     * will be set to the current depth plus one. This way, when searching
     * through a pathway, it ensures that the path to the current tile is the
     * shortest path and prematurely stops the search if the tile already has a
     * shorter path to it.
     *
     * When the target tile has been found, the method will continue searching
     * to see if there is even a shorter path and minimum depth. It will set a
     * new minimum depth and any tile that exceeds the minimum depth will not be
     * searched, saving time and increasing efficiency. Each time a shorter path
     * is found, the minimum depth will decrease even more. This loop will stop
     * when there are no more searchable tiles that are within the minimum
     * depth. The {@link PathTile} at the end of the shortest path will then be
     * returned.
     *
     * @param level 2D character array of the floor of the maze
     * @param s Stack of tiles that will be searched
     * @param type character of the target tile
     * @return the {@link PathTile} that is adjacent to the tile of the given
     * type if it is found, {@code null} otherwise
     * @see #searchUsingStack(char[][], Deque, char)
     */
    private static PathTile searchUsingDepth(char[][] level, Deque<PathTile> s, char type) {

        // the minimum depth will be initialized with the maximum integer value
        // when a path to the target tile is found, a new minimum depth will be set
        int minDepth = Integer.MAX_VALUE;

        // 2D integer array that keeps track the depth of each tile (distance of steps away from start position)
        int depth[][] = new int[level.length][level[0].length];

        // keeps track of the shortest path
        PathTile shortestPath = null;

        // loops while there are tiles to be searched
        while (!s.isEmpty()) {

            // pops the next tile to be searched
            PathTile current = s.removeLast();

            for (int direction = NORTH; direction <= WEST; direction++) {

                PathTile adjacent = new PathTile(current, direction);

                char tile = adjacent.charAt(level);

                // if the tile is empty AND it is still within the minimum depth AND it has not been searched before OR it has a larger depth than the current tile 
                // then it will be pushed and the depth of the tile will be updated
                if (tile == '.') {

                    //
                    // In general, I hate nesting code. But in this case, it is necessary for two
                    // major reasons:
                    //
                    // (1) If adjacent tile is out of bounds, then adjacent.row and adjacent.col
                    // would also be outside the bounds of depth[][]. Putting all the conditions in
                    // one statement will then return an error since all three conditions would be
                    // checked simultaneously such that even if the adjacent tile is out of bounds,
                    // the other two conditions would be checked anyways.
                    //
                    // (2) It is extremely optimal as if (tile != '.'), then the other two
                    // conditions needn't to be checked. The less conditions checked each iteration,
                    // the more efficient the program is.
                    //
                    if ((depth[current.row][current.col] < minDepth)
                            && (depth[adjacent.row][adjacent.col] > depth[current.row][current.col] + 1
                            || depth[adjacent.row][adjacent.col] == 0)) {

                        depth[adjacent.row][adjacent.col] = depth[current.row][current.col] + 1;
                        s.add(adjacent);
                    }

                    // if the target tile is found, it will set the new minimum depth and the shortest path
                } else if (tile == type) {

                    minDepth = depth[current.row][current.col];
                    shortestPath = current;

                }
            }
        }

        // returns the PathTile at the end of the shortest path or null if the target tile cannot be found
        return shortestPath;
    }

    /**
     * This method currently does not work as intended :(. Although it can find
     * the path to all the coins in the maze, the path is not the most optimal,
     * i.e. it takes up more tiles than it needs to. I'm too tired to find a
     * solution right now, but maybe in the future I can fix this.
     *
     * @param map
     * @return
     */
    public static boolean solveExtraCredit(char map[][][]) {

        for (int level = 0; level < map.length; level++) {

            Deque<PathTile> q = new ArrayDeque<>();

            PathTile wolverine = findTile(map[level], 'W');

            if (wolverine == null) {
                return false;
            }

            q.add(wolverine);

            Deque<PathTile> paths = searchExtraCredit(map[level], q);

            for (PathTile path : paths) {

                while (path.previous != null && path.charAt(map[level]) != '+') {

                    if (path.charAt(map[level]) == '.') {
                        map[level][path.row][path.col] = '+';
                    }

                    path = path.previous;
                }

            }

        }

        return true;
    }

    private static Deque<PathTile> searchExtraCredit(char level[][], Deque<PathTile> q) {

        boolean walkwayFound = false;
        int depth[][] = new int[level.length][level[0].length];
        Deque<PathTile> paths = new ArrayDeque<>();

        while (!q.isEmpty()) {

            PathTile current = q.removeFirst();

            for (int direction = NORTH; direction <= WEST; direction++) {

                PathTile adjacent = new PathTile(current, direction);

                char tile = adjacent.charAt(level);

                if (tile == '@' || tile == '\0') {
                    continue;
                }

                if ((depth[adjacent.row][adjacent.col] > depth[current.row][current.col] + 1)
                        || depth[adjacent.row][adjacent.col] == 0) {

                    depth[adjacent.row][adjacent.col] = depth[current.row][current.col] + 1;
                    q.add(adjacent);

                    if (tile == '$') {

                        depth[adjacent.row][adjacent.col] = -1;
                        paths.add(adjacent);

                    }

                    if (tile == '|' && !walkwayFound) {

                        depth[adjacent.row][adjacent.col] = -1;
                        walkwayFound = true;
                        paths.add(adjacent);

                    }
                }
            }
        }

        return paths;
    }
}
