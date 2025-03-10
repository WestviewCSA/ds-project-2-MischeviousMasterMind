import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.util.Queue;
import java.util.Stack;

public class p2 {

	public static void main(String[] args) {

		try {
			// TODO Auto-generated method stub
			Maze newMaze = new Maze(new File("example-map"), Maze.TEXT_BASED);

			PrintStream output = new PrintStream(new File("output"));

			newMaze.print(output, Maze.TEXT_BASED);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
	}

	
	
}
