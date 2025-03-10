
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Maze {

    static final boolean TEXT_BASED = false, COORDINATE_BASED = true;

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

        width = input.nextInt();
        height = input.nextInt();
        numberOfLevels = input.nextInt();

        maze = new char[numberOfLevels][height][width];

        if (format == TEXT_BASED) {

            for(int i = 0; i < numberOfLevels; i++)
                for(int ii = 0; ii < height; ii++)
                    for(int iii = 0; iii < width; iii++)
                        maze[i][ii][iii] = input.next().charAt(0);
        }

        if (format == COORDINATE_BASED) {

        }

        return maze;
    }

    public Maze(PrintStream output) {

        for(char[][] : maze)
        {
            
        }
        output.print(maze[1][2]);


    }
}
