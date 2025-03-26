import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class PathFinderTester {

	@Test
	public void test01() throws FileNotFoundException, IncompleteMapException,
			IncorrectMapFormatException, IllegalMapCharacterException {

		assertTrue(test("TEST/test01/test01", "TEST/test01/test01-queue", Approach.QUEUE));
		assertTrue(test("TEST/test01/test01", "TEST/test01/test01-stack", Approach.STACK));
		assertTrue(test("TEST/test01/test01", "TEST/test01/test01-opt", Approach.OPT));

	}
	
	private static boolean test(String inputFile, String solutionFile, Approach approach) throws FileNotFoundException, IncompleteMapException,
			IncorrectMapFormatException, IllegalMapCharacterException {
			
		char[][][] output = Maze.read(new File(inputFile), Maze.TEXT_BASED);
		char[][][] solution = Maze.read(new File(solutionFile));
		
		PathFinder.solve(output, approach);

		return (output == solution);
		
	}

}
