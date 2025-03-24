
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Maze {

    public static final boolean TEXT_BASED = false, COORDINATE_BASED = true;

    public static char[][][] read(Scanner input, boolean format) throws IncompleteMapException, IncorrectMapFormatException, IllegalMapCharacterException {

        int height, width, numberOfLevels;

        try {

            height = input.nextInt();
            width = input.nextInt();
            numberOfLevels = input.nextInt();

        } catch (InputMismatchException e) {
            throw new IncorrectMapFormatException("height, width, and number of levels of this maze must be included in the first line as three positive nonzero integers");
        }

        if (height <= 0) {
            throw new IncorrectMapFormatException("the height must be a positive nonzero integer");
        }
        if (width <= 0) {
            throw new IncorrectMapFormatException("the width must be a positive nonzero integer");
        }
        if (numberOfLevels <= 0) {
            throw new IncorrectMapFormatException("the number of levels must be a positive nonzero integer");
        }

        input.nextLine();

        char maze[][][] = new char[numberOfLevels][height][width];

        if (format == TEXT_BASED) {

            for (int i = 0; i < numberOfLevels; i++) {
                for (int ii = 0; ii < height; ii++) {

                    if (!input.hasNextLine()) {
                        throw new IncompleteMapException("map does not have enough rows specified");
                    }

                    String row = input.nextLine();

                    if (width > row.length()) {
                        throw new IncompleteMapException("row " + (i * ii + 1) + " does not have enough characters for the number of columns specified");
                    }

                    for (int iii = 0; iii < width; iii++) {

                        if (!isValidTile(row.charAt(iii))) {
                            throw new IllegalMapCharacterException();
                        }

                        maze[i][ii][iii] = row.charAt(iii);
                    }
                }
            }
        }

        if (format == COORDINATE_BASED) {

            for (int i = 0; i < numberOfLevels; i++) {
                for (int ii = 0; ii < height; ii++) {
                    for (int iii = 0; iii < width; iii++) {
                        maze[i][ii][iii] = '.';
                    }
                }
            }

            while (input.hasNext()) {

                char tile = input.next().charAt(0);

                if (!isValidTile(tile)) {
                    throw new IllegalMapCharacterException();
                }

                try {

                    int row = input.nextInt();
                    int column = input.nextInt();
                    int level = input.nextInt();
                    input.nextLine();

                    if (row < 0 || row >= height) {
                        throw new IncorrectMapFormatException("row coordinate does not fit inside the dimensions of the maze");
                    }
                    if (column < 0 || column >= width) {
                        throw new IncorrectMapFormatException("column coordinate does not fit inside the dimensions of the maze");
                    }
                    if (level < 0 || level >= numberOfLevels) {
                        throw new IncorrectMapFormatException("level coordinate is outside the levels in the maze");
                    }

                    maze[level][row][column] = tile;

                } catch (InputMismatchException e) {
                    throw new IncorrectMapFormatException("location(s) of maze element missing");
                }
            }
        }

        return maze;
    }

    public static char[][][] read(File inputFile, boolean format) throws FileNotFoundException, IncompleteMapException, IncorrectMapFormatException, IllegalMapCharacterException {

        Scanner input = new Scanner(inputFile);
        char maze[][][] = read(input, format);
        input.close();

        return maze;
    }

    public static boolean isValidTile(char tile) {
        return (tile == '.') || (tile == '@') || (tile == 'W') || (tile == '$') || (tile == '|');
    }

    public static void print(char[][][] maze, PrintStream output, boolean format) {

        if (format == TEXT_BASED) {

            for (char level[][] : maze) {
                for (char row[] : level) {
                    output.println(row);
                }
            }
        }

        if (format == COORDINATE_BASED) {

            for (int i = 0; i < maze.length; i++) {
                for (int ii = 0; ii < maze[0].length; ii++) {
                    for (int iii = 0; iii < maze[0][0].length; iii++) {

                        if (maze[i][ii][iii] == '+') {
                            output.printf("+ %d %d %d\n", ii, iii, i);
                        }
                    }
                }
            }

        }
    }

    public void print(char[][][] maze, File outputFile, boolean format) throws FileNotFoundException {

        PrintStream output = new PrintStream(outputFile);
        print(maze, output, format);
        output.close();
    }

    public static void secretFeature(char[][][] maze, PrintStream output) {

        for (char level[][] : maze) {
            for (char row[] : level) {
                // output.println(row);

                for(char tile : row) {

                    switch(tile) {

                        default -> output.print(" ");
                        case '+' -> output.print("█");
                        case '@' -> output.print("▒");
                        case 'W' -> output.print("W");
                        case '$' -> output.print("$");
                        case '|' -> output.print("|");
                    }

                }

                output.println();
            }
        }

    }
}
