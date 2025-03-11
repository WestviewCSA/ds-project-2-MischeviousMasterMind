import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class p3 {

	public static void main(String[] args) {

		try {
			Maze newMaze = new Maze(new File("input/text-based"), Maze.TEXT_BASED);

			PrintStream output = new PrintStream(new File("output/text-based"));

			newMaze.print(output, Maze.TEXT_BASED);

			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Maze newMaze = new Maze(new File("input/coordinate-based"), Maze.COORDINATE_BASED);

			PrintStream output = new PrintStream(new File("output/coordinate-based"));

			newMaze.print(output, Maze.COORDINATE_BASED);

			output.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
	}

	
	
}
