
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Maze {

    public static final boolean TEXT_BASED = false, COORDINATE_BASED = true;

    private int width, height, numberOfLevels;
    private char maze[][][];

    public Maze(int width, int height, int numberOfLevels) {

        this.width = width;
        this.height = height;
        this.numberOfLevels = numberOfLevels;

        maze = new char[numberOfLevels][height][width];
    }

    public Maze(char maze[][][]) {

        this.width = maze[0][0].length;
        this.height = maze[0].length;
        this.numberOfLevels = maze.length;
        this.maze = maze;
    }

    public Maze(File inputFile, boolean format) throws FileNotFoundException, IncompleteMapException, IncorrectMapFormatException, IllegalMapCharacterException {

        read(inputFile, format);
    }

    public char[][][] read(Scanner input, boolean format) throws IncompleteMapException, IncorrectMapFormatException, IllegalMapCharacterException {

        try {

            height = input.nextInt();
            width = input.nextInt();
            numberOfLevels = input.nextInt();
            
        } catch (InputMismatchException e) {
            throw new IncorrectMapFormatException("height, width, and/or number of levels of this map must be included in the first line as three positive nonzero integers");
        }

        if(height <= 0) throw new IncorrectMapFormatException("the height must be a positive nonzero integer");
        if(width <= 0) throw new IncorrectMapFormatException("the width must be a positive nonzero integer");
        if(numberOfLevels <= 0) throw new IncorrectMapFormatException("the number of levels must be a positive nonzero integer");

        input.nextLine();

        maze = new char[numberOfLevels][height][width];

        if (format == TEXT_BASED) {

            for (int i = 0; i < numberOfLevels; i++) {
                for (int ii = 0; ii < height; ii++) {

                    if(!input.hasNextLine()) throw new IncompleteMapException("not enough rows");

                    String row = input.nextLine();

                    if(width > row.length()) throw new IncompleteMapException("row " + i * ii + " does not have enough characters");

                    for (int iii = 0; iii < width; iii++) {

                        if(!isValidTile(row.charAt(iii))) throw new IllegalMapCharacterException();

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

                if (isValidTile(tile)) {

                    try {

                        int row = input.nextInt();
                        int column = input.nextInt();
                        int level = input.nextInt();
                        maze[level][row][column] = tile;

                    } catch (InputMismatchException e) {
                        throw new IncorrectMapFormatException("location(s) of map element missing");
                    }
                } else {

                    input.nextLine();
                }
            }
        }

        return maze;
    }

    public char[][][] read(File inputFile, boolean format)  throws FileNotFoundException, IncompleteMapException, IncorrectMapFormatException, IllegalMapCharacterException {

        Scanner input = new Scanner(inputFile);
        read(input, format);
        input.close();

        return maze;
    }

    public boolean isValidTile(char tile) {
        return (tile == '.') || (tile == '@') || (tile == 'w') || (tile == '$') || (tile == '|') || (tile == '+');
    }

    public void print(PrintStream output, boolean format) {

        output.printf("%d %d %d\n", width, height, numberOfLevels);

        if (format == TEXT_BASED) {

            for (char level[][] : maze) {
                for (char row[] : level) {
                    output.println(row);
                }
            }
        }

        if (format == COORDINATE_BASED) {

            for (int i = 0; i < numberOfLevels; i++) {
                for (int ii = 0; ii < height; ii++) {
                    for (int iii = 0; iii < width; iii++) {

                        if (maze[i][ii][iii] != '.') {
                            output.printf("%c %d %d %d\n", maze[i][ii][iii], ii, iii, i);
                        }
                    }
                }
            }

        }
    }

    public void print(File outputFile, boolean format) throws FileNotFoundException {

        PrintStream output = new PrintStream(outputFile);
        print(output, format);
        output.close();
    }
}
