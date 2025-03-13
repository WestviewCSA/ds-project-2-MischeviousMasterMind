
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

    public Maze(File inputFile, boolean format) throws FileNotFoundException, IncompleteMapException, IncorrectMapFormatException {

        read(inputFile, format);
    }

    public char[][][] read(Scanner input, boolean format) throws IncompleteMapException, IncorrectMapFormatException {

        height = input.nextInt();
        width = input.nextInt();
        numberOfLevels = input.nextInt();
        input.nextLine();

        maze = new char[numberOfLevels][height][width];

        if (format == TEXT_BASED) {

            for (int i = 0; i < numberOfLevels; i++) {
                for (int ii = 0; ii < height; ii++) {

                    if(!input.hasNextLine()) throw new IncompleteMapException("not enough rows");

                    String row = input.nextLine();
                    int j = 0;

                    for (int iii = 0; iii < width; iii++) {

                        if(j >= row.length()) throw new IncompleteMapException("not enough valid characters in the row");

                        while (!isValidTile(row.charAt(j))) {
                            j++;
                        }

                        maze[i][ii][iii] = row.charAt(j);

                        j++;
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

    public char[][][] read(File inputFile, boolean format)  throws FileNotFoundException, IncompleteMapException, IncorrectMapFormatException {

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
