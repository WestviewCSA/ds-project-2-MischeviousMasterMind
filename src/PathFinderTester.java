import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class PathFinderTester {

	@Test
	public void test01() throws FileNotFoundException, IncompleteMapException, IncorrectMapFormatException,
			IllegalMapCharacterException {

//		assertTrue(test("TEST/test01/test01", "TEST/test01/test01-queue", Approach.QUEUE, Maze.TEXT_BASED));
		assertTrue(test("TEST/test01/test01", "TEST/test01/test01-stack", Approach.STACK, Maze.TEXT_BASED));
		assertTrue(test("TEST/test01/test01", "TEST/test01/test01-queue", Approach.OPT, Maze.TEXT_BASED));

	}

	private static boolean test(String inputFile, String solutionFile, Approach approach, boolean format)
			throws FileNotFoundException, IncompleteMapException, IncorrectMapFormatException,
			IllegalMapCharacterException {

		char[][][] output = Maze.read(new File(inputFile), format);
		char[][][] solution = Maze.read(new File(solutionFile), format, true);

		System.out.println("Input: ");
		Maze.print(output, System.out, format);

		long startTime = System.nanoTime();
		PathFinder.solve(output, approach);
		long endTime = System.nanoTime();
		
		System.out.println("\nOutput: ");
		Maze.print(output, System.out, format);

		System.out.println("\nSolution: ");
		Maze.print(solution, System.out, format);

		Maze.secretFeature(output, System.out);
		
		System.out.print("\nTotal time to solve: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");

		return equals(output, solution);
	}

	private static boolean equals(char[][][] array1, char[][][] array2) {

		for (int i = 0; i < array1.length; i++) {

			for (int ii = 0; ii < array1[i].length; ii++) {

				for (int iii = 0; iii < array1[i][ii].length; iii++) {

					if (array1[i][ii][iii] != array2[i][ii][ii])
						return false;
				}

			}

		}

		return true;
	}

}
