
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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

    public Maze(File inputFile, boolean format) throws FileNotFoundException {

        Scanner input = new Scanner(inputFile);
        readFile(input, format);
        input.close();
    }

    public char[][][] readFile(Scanner input, boolean format) {

        height = input.nextInt();
        width = input.nextInt();
        numberOfLevels = input.nextInt();

        maze = new char[numberOfLevels][height][width];

        if (format == TEXT_BASED) {

            for (int i = 0; i < numberOfLevels; i++) {
                for (int ii = 0; ii < height; ii++) {

                    String row = input.next();
                    int j = 0;

                    for (int iii = 0; iii < width; iii++) {

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
                int row = input.nextInt();
                int column = input.nextInt();
                int level = input.nextInt();

                maze[level][row][column] = tile;
            }
        }

        return maze;
    }

    public boolean isValidTile(char tile) {
        return (tile == '.') || (tile == '@') || (tile == 'w') || (tile == '$') || (tile == '|');
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
}
