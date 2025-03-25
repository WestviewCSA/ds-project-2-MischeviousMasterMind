
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class p2 {

	private static Approach approach;
	private static File inputFile;
	private static boolean printTime, inCoordinate, outCoordinate, secret;
	private static char[][][] maze;

	/**
	 *
	 * @param args
	 * @throws IncompleteMapException
	 * @throws IncompleteMapException
	 * @throws IncorrectMapFormatException
	 * @throws IllegalCommandLineInputsException
	 */
	public static void main(String[] args) throws IncompleteMapException, IncompleteMapException,
			IncorrectMapFormatException, IllegalCommandLineInputsException, IllegalMapCharacterException {

		try {

			readArguments(args);
			maze = Maze.read(inputFile, inCoordinate);

		} catch (IllegalCommandLineInputsException | FileNotFoundException | IncompleteMapException
				| IncorrectMapFormatException e) {
			System.err.println("Error: " + e.getMessage());
			System.err.println("Caused by: " + e.getClass().getName());
			System.exit(-1);
		}

		System.out.println("\n=== Problem ===");
		Maze.print(maze, System.out, inCoordinate);

		long startTime = System.nanoTime();
		boolean solvable = PathFinder.solve(maze, approach);
		long endTime = System.nanoTime();

		if (!solvable) {

			System.err.println("The Wolverine Store is closed.");

		} else {

			System.out.println("\n=== Solution ===");
			Maze.print(maze, System.out, outCoordinate);
			System.out.println();

			if (secret) {
				Maze.secretFeature(maze, System.out);
			}
		}

		if (printTime) {
			System.out.println();
			System.out.println("Total Runtime: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");
		}
	}

	private static void readArguments(String[] args) throws IllegalCommandLineInputsException {

		if (args.length == 0) {
			throw new IllegalCommandLineInputsException("No input arguments given");
		}

		if (args[0].equals("--Help")) {
			printUsage(System.out);
			System.exit(0);
		}

		inputFile = new File(args[0]);

		int flags = 0;

		for (int i = 1; i < args.length; i++) {

			switch (args[i]) {

			case "--Stack" -> flags += 1;
			case "--Queue" -> flags += 2;
			case "--Opt" -> flags += 4;
			case "--Time" -> printTime = true;
			case "--Incoordinate" -> inCoordinate = true;
			case "--Outcoordinate" -> outCoordinate = true;
			case "--Help" -> printUsage(System.out);
			case "--Secret" -> secret = true;
			default -> throw new IllegalCommandLineInputsException(args[i] + " is not a valid argument");
			}
		}

		switch (flags) {

		case 0 -> throw new IllegalCommandLineInputsException(
				"use either --Stack, --Queue, or --Opt as one of your arguments");

		case 1 -> approach = Approach.STACK;
		case 2 -> approach = Approach.QUEUE;
		case 4 -> approach = Approach.OPT;

		default -> throw new IllegalCommandLineInputsException(
				"cannot use multiple approaches (use only either --Stack, --Queue, or --Opt)");
		}
	}

	/**
	 * Prints the following help message to <code>output</code>:
	 * 
	 * <pre>
	 * <codeblock>
	 * can i have a uhhhhhhhhhh...
	 * </codeblock>
	 * </pre>
	 *
	 * @param output the PrintStream object to print the help message to
	 */
	private static void printUsage(PrintStream output) {

		output.println("Usage: java p2 <inputfile> [optional] <--Stack|--Queue|--Opt> [optional]");
		output.println();
		output.println(" REQUIRED arguments:");
		output.println();
		output.println("    <inputfile>");
		output.println("                Path to the input map file used by the program. Must be");
		output.println("                the first argument inputted to the program.");
		output.println("    <--Stack|--Queue|--Opt>");
		output.println("                The approach the program will use to solve the maze.");
		output.println("                Exactly one of these flags must be enabled.");
		output.println();
		output.println(" OPTIONAL arguments (can be included in any order after <inputfile>):");
		output.println();
		output.println("    --Help");
		output.println("                print this help message to the output stream");
		output.println("    --Incoordinate");
		output.println("                Set the input format to the coordinate-based format when");
		output.println("                reading <inputfile>. By default, the text-map format is used.");
		output.println("    --Outcoordinate");
		output.println("                Set the output format to the coordinate-based format when");
		output.println("                printing the solution. By default, the text-map format is used.");
		output.println("    --Secret");
		output.println("                Enter in this argument for a surprise ;)");
		output.println();
	}
}
