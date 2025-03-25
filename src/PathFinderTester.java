import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class PathFinderTester {

	@Test
	public void test01() {

		
		
		
		assertTrue(true);

	}
	
	private static void test(String testName) {
		
		try {
			
			char[][][] input = Maze.read(new File(testName + "/" + testName), Maze.TEXT_BASED);
			char[][][] output = input;
			char[][][] solution;
			
			PathFinder.solve(output, Approach.STACK);
			solution = Maze.read(new File(testName + "/" + testName + "-stack"), Maze.TEXT_BASED, true);
			
		} catch (FileNotFoundException | IncompleteMapException | IncorrectMapFormatException
				| IllegalMapCharacterException e) {
			e.printStackTrace();
		}
		
	}

}
