
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class handles all the file I/O and converting the maze to a 3D character
 * array to be used by the {@link PathFinder} class
 *
 * @author Daniel Yang
 */
public class Maze {

    // sets input/output format as a boolean
    public static final boolean TEXT_BASED = false, COORDINATE_BASED = true;

    /**
     * Equivalent to
     * <pre>{@code read(input, format, false);}</pre>
     *
     * @param input Scanner of the input with the maze
     * @param format format used to read the file
     * @return 3D character array of the maze
     * @throws IncompleteMapException
     * @throws IncorrectMapFormatException
     * @throws IllegalMapCharacterException
     */
    public static char[][][] read(Scanner input, boolean format)
            throws IncompleteMapException, IncorrectMapFormatException, IllegalMapCharacterException {
        return read(input, format, false);
    }

    /**
     * Reads the maze from an output stream using a scanner in the specified
     * {@code format}
     *
     * @param input Scanner of the input with the maze
     * @param format format used to read the file
     * @param isSolution whether to include '+' or not
     * @return 3D character array of the maze
     * @throws IncompleteMapException
     * @throws IncorrectMapFormatException
     * @throws IllegalMapCharacterException
     */
    public static char[][][] read(Scanner input, boolean format, boolean isSolution)
            throws IncompleteMapException, IncorrectMapFormatException, IllegalMapCharacterException {

        // dimensions of the maze
        final int height, width, numberOfLevels;

        try {

            // reads the first three numbers in the first line to set the dimensions of the maze
            height = input.nextInt();
            width = input.nextInt();
            numberOfLevels = input.nextInt();

        } catch (InputMismatchException e) {

            // throws an exception if the dimensions cannot be found
            throw new IncorrectMapFormatException(
                    "height, width, and number of levels of this maze must be included in the first line as three positive nonzero integers");
        }

        // if any of the dimensions are less than or equal to zero, it will throw an exception
        if (height <= 0) {
            throw new IncorrectMapFormatException("the height must be a positive nonzero integer");
        }
        if (width <= 0) {
            throw new IncorrectMapFormatException("the width must be a positive nonzero integer");
        }
        if (numberOfLevels <= 0) {
            throw new IncorrectMapFormatException("the number of levels must be a positive nonzero integer");
        }

        // advances the Scanner to the next line in case there are other miscellaneous tokens in the first line
        input.nextLine();

        // initializes the maze that will be returned
        char maze[][][] = new char[numberOfLevels][height][width];

        // if the format is text based
        if (format == TEXT_BASED) {

            // for each floor of the maze
            for (int i = 0; i < numberOfLevels; i++) {

                // for each row of the maze
                for (int ii = 0; ii < height; ii++) {

                    // if there is no row where it is expected to be, throw an exception
                    if (!input.hasNextLine()) {
                        throw new IncompleteMapException("map does not have enough rows specified");
                    }

                    // reads the next row as a String
                    String row = input.nextLine();

                    // if the length of the String is less than the number of columns, throw an exception
                    if (width > row.length()) {
                        throw new IncompleteMapException("row " + (i * ii + 1)
                                + " does not have enough characters for the number of columns specified");
                    }

					// write each character of the string into the maze
                    for (int iii = 0; iii < width; iii++) {

						// if there is any invalid tiles, throw an exception
                        if (!isValidTile(row.charAt(iii)) || (row.charAt(iii) == '+' && !isSolution)) {
                            throw new IllegalMapCharacterException();
                        }

                        maze[i][ii][iii] = row.charAt(iii);
                    }
                }
            }
        }

		// if the format is coordinate based
        if (format == COORDINATE_BASED) {

			// initializes every character in the 3D array to '.'
            for (int i = 0; i < numberOfLevels; i++) {
                for (int ii = 0; ii < height; ii++) {
                    for (int iii = 0; iii < width; iii++) {
                        maze[i][ii][iii] = '.';
                    }
                }
            }

			// loops until there are no more lines
            while (input.hasNext()) {

				// reads the first character in next line
                char tile = input.next().charAt(0);

				// if the character is invalid, throw an exception
                if (!isValidTile(tile) || (tile == '+' && !isSolution)) {
                    throw new IllegalMapCharacterException();
                }

                try {

					// reads the coordinate of the tile
                    int row = input.nextInt();
                    int column = input.nextInt();
                    int level = input.nextInt();
                    input.nextLine();

					// if the coordinates are outside the dimensions of the maze, throw an error
                    if (row < 0 || row >= height) {
                        throw new IncorrectMapFormatException(
                                "row coordinate does not fit inside the dimensions of the maze");
                    }
                    if (column < 0 || column >= width) {
                        throw new IncorrectMapFormatException(
                                "column coordinate does not fit inside the dimensions of the maze");
                    }
                    if (level < 0 || level >= numberOfLevels) {
                        throw new IncorrectMapFormatException("level coordinate is outside the levels in the maze");
                    }

                    maze[level][row][column] = tile;

                } catch (InputMismatchException e) {

					// if there aren't enough numbers for the coordinates and the Scanner reads the next character, throw an error
                    throw new IncorrectMapFormatException("location(s) of maze element missing");
                }
            }
        }

		// returns the 3D character array of the maze
        return maze;
    }

	/**
     * Equivalent to
     * <pre>{@code read(inputFile, format, false);}</pre>
     * @param input input file with the maze
     * @param format format to read the maze
     * @return 3D character array of the maze
     * @throws IncompleteMapException
     * @throws IncorrectMapFormatException
     * @throws IllegalMapCharacterException
     */
    public static char[][][] read(File inputFile, boolean format) throws FileNotFoundException, IncompleteMapException,
            IncorrectMapFormatException, IllegalMapCharacterException {

        return read(inputFile, format, false);
    }

	/**
	 * Equivalent to
     * <pre>
	 * <codeblock>
	 * 		Scanner input = new Scanner(inputFile);
	 * 		char maze[][][] = read(input, format, isSolution);
	 * 		input.close();
	 * </codeblock>
	 * </pre>
	 * @param inputFile input file with the maze
	 * @param format format to read the maze
	 * @param isSolution whether to include '+' or not
	 * @return
	 * @throws FileNotFoundException
	 * @throws IncompleteMapException
	 * @throws IncorrectMapFormatException
	 * @throws IllegalMapCharacterException
	 */
    public static char[][][] read(File inputFile, boolean format, boolean isSolution) throws FileNotFoundException, IncompleteMapException,
            IncorrectMapFormatException, IllegalMapCharacterException {

        Scanner input = new Scanner(inputFile);
        char maze[][][] = read(input, format, isSolution);
        input.close();

        return maze;
    }

	/**
	 * Checks to see if the character is valid i.e. it is either '.', '@', 'W', '$', '|', or '+'.
	 * @param tile character of the tile
	 * @return true if it is a valid character, false otherwise
	 */
    public static boolean isValidTile(char tile) {
        return (tile == '.') || (tile == '@') || (tile == 'W') || (tile == '$') || (tile == '|') || (tile == '+');
    }

	/**
	 * Prints out the maze in the output stream in the output {@code format}. Often used to show the solution of a maze.
	 * @param maze charactery array to print out
	 * @param output output stream to print to
	 * @param format the output format
	 */
    public static void print(char[][][] maze, PrintStream output, boolean format) {

		// if the format is text based, easy as printing out each row of the character array
        if (format == TEXT_BASED) {

            for (char level[][] : maze) {
                for (char row[] : level) {
                    output.println(row);
                }
            }
        }

		// if the format is coordinate based, easy as printing out character and coordinate of each tile in the character array
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

	/**
	 * Legacy feature (╯°□°）╯︵ ┻━┻. Prints out the maze into an output file. No longer used, although I might implement it later.
	 * @param maze charactery array to print out
	 * @param output output file to print to
	 * @param format the output format
	 * @throws FileNotFoundException
	 * @see #print(char[][][], PrintStream, boolean)
	 */
    public void print(char[][][] maze, File outputFile, boolean format) throws FileNotFoundException {

        PrintStream output = new PrintStream(outputFile);
        print(maze, output, format);
        output.close();
    }

	/**
	 * Oooh! Secret...
	 * 
	 * Displays the maze in an infinitely better format than the current character set used for the output. Enabled using {@code --Secret} in {@link p2#main(String[])}.
	 * 
	 * Oh no, I said too much! <i>*sneaks away into the abyss*</i>
	 * 
	 * @param maze charactery array to print out
	 * @param output output stream to print to
	 */
    public static void secretFeature(char[][][] maze, PrintStream output) {

        for (char level[][] : maze) {
            for (char row[] : level) {

                for (char tile : row) {

                    switch (tile) {

                        default ->
                            output.print(" ");
                        case '+' ->
                            output.print("█");
                        case '@' ->
                            output.print("▒");
                        case 'W' ->
                            output.print("W");
                        case '$' ->
                            output.print("$");
                        case '|' ->
                            output.print("|");
                    }

                }

                output.println();
            }
        }

    }
}
