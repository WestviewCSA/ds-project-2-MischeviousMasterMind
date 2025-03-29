
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * The main class of the program. It solves mazes for Wolverine. What did you expect?
 * @see #main(String[])
 * @author Daniel Yang
 */
public class p2 {

	// keeps track of the flags that are enabled such as --Stack, --Incoordinate, --EC, etc.
    private static Approach approach;
    private static File inputFile;
    private static boolean printTime, inCoordinate, outCoordinate, secret, extraCredit;
    private static char[][][] maze;

    /**
     * The main method of the program. Arguments and usage can be viewed with {@code --Help}
     * @param args
     * @throws IncompleteMapException
     * @throws IncompleteMapException
     * @throws IncorrectMapFormatException
     * @throws IllegalCommandLineInputsException
	 * @see PathFinder
	 * @see Maze
     */
    public static void main(String[] args) throws IncompleteMapException, IncompleteMapException,
            IncorrectMapFormatException, IllegalCommandLineInputsException, IllegalMapCharacterException {

        try {

            // reads the arguments inputed into the program
            readArguments(args);

            // reads the input file with the corresponding format and converts it into a 3D char array
            maze = Maze.read(inputFile, inCoordinate);

        } catch (IllegalCommandLineInputsException | FileNotFoundException | IncompleteMapException
                | IncorrectMapFormatException e) {

            // prints an error message when an exception occurs and exits with code -1
            System.err.println("Error: " + e.getMessage());
            System.err.println("Caused by: " + e.getClass().getName());
            System.exit(-1);
        }

		// prints out the input file's maze
        System.out.println("\n=== Problem ===");
        Maze.print(maze, System.out, inCoordinate);

		// if the maze cannot be solved, this remains false
        boolean solvable = false;

		// starts the time
        long startTime = System.nanoTime();

        if (extraCredit) {

			// extra credit method
            solvable = PathFinder.solveExtraCredit(maze);

        } else {

			// solves the maze using the corresponding approach
            solvable = PathFinder.solve(maze, approach);
        }

		// stops the time
        long endTime = System.nanoTime();

		// the maze cannot be solved, it will print out the message below
		// otherwise, it will print the solution in the corresponding format
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

		// prints the time if --Time is set on
        if (printTime) {
            System.out.println();
            System.out.printf("\nTotal Runtime: %.7f seconds\n", (endTime - startTime) / 1_000_000_000.0);
        }
    }

    private static void readArguments(String[] args) throws IllegalCommandLineInputsException {

		// throws an error if no arguments are given
        if (args.length == 0) {
            throw new IllegalCommandLineInputsException("No input arguments given");
        }

		// if the first argument is --Help, it will print out the help message and exit with code 0
        if (args[0].equals("--Help")) {
            printUsage(System.out);
            System.exit(0);
        }

		// sets the input file to the last argument
        inputFile = new File(args[args.length - 1]);

		// keeps track which approach is used
		// if more than one is inputted, it will be detected
        int flags = 0;

		// enables flags based on argument
        for (int i = 0; i < args.length - 1; i++) {

            switch (args[i]) {

                case "--Stack" ->
                    flags += 1;
                case "--Queue" ->
                    flags += 2;
                case "--Opt" ->
                    flags += 4;
                case "--Time" ->
                    printTime = true;
                case "--Incoordinate" ->
                    inCoordinate = true;
                case "--Outcoordinate" ->
                    outCoordinate = true;
                case "--Help" ->
                    printUsage(System.out);
                case "--Secret" ->
                    secret = true;
                case "--EC" ->
                    extraCredit = true;
                default ->
                    throw new IllegalCommandLineInputsException(args[i] + " is not a valid argument");
            }
        }

		// if --EC is enabled, then --Outcoordinate will be ignored and --Stack, --Queue, and --Opt will be ignored
        if (extraCredit) {

            System.out.println("NOTE: The extra credit switch (--EC) is enabled.");
            System.out.println("The output format will be set to text based (--Outcoordinate will be ignored).");
            System.out.println("The arguments --Stack, --Queue, and --Opt will be ignored as well.");

            outCoordinate = false;
            return;

        }

		// sets only one approach
		// if there are none or more than one, it throws an error
        switch (flags) {

            case 0 ->
                throw new IllegalCommandLineInputsException(
                        "use either --Stack, --Queue, or --Opt as one of your arguments");

            case 1 ->
                approach = Approach.STACK;
            case 2 ->
                approach = Approach.QUEUE;
            case 4 ->
                approach = Approach.OPT;

            default ->
                throw new IllegalCommandLineInputsException(
                        "cannot use multiple approaches (use only either --Stack, --Queue, or --Opt)");
        }
    }

    /**
     * Prints the following help message to <code>output</code>:
     *
     * <pre>
     * <codeblock>
     * Usage: java p2 <--Stack|--Queue|--Opt> [optional] <inputfile>
	 *
	 * REQUIRED arguments:
	 *
	 *	<inputfile>
	 *				Path to the input map file used by the program. Must be        
	 *				the last argument inputted to the program.
	 *	<--Stack|--Queue|--Opt>
	 *				The approach the program will use to solve the maze.
	 *				Exactly one of these flags must be enabled.
	 *
	 * OPTIONAL arguments (can be included in any order before <inputfile>):
	 *
	 *	--Help
	 *				print this help message to the output stream
	 *	--Incoordinate
	 *				Set the input format to the coordinate-based format when       
	 *				reading <inputfile>. By default, the text-map format is used.  
	 *	--Outcoordinate
	 *				Set the output format to the coordinate-based format when      
	 *				printing the solution. By default, the text-map format is used.
	 *	--Secret
	 *				Enter in this argument for a surprise ;)
     * </codeblock>
     * </pre>
     *
     * @param output the PrintStream object to print the help message to
     */
    private static void printUsage(PrintStream output) {

        output.println("Usage: java p2 <--Stack|--Queue|--Opt> [optional] <inputfile>");
        output.println();
        output.println(" REQUIRED arguments:");
        output.println();
        output.println("    <inputfile>");
        output.println("                Path to the input map file used by the program. Must be");
        output.println("                the last argument inputted to the program.");
        output.println("    <--Stack|--Queue|--Opt>");
        output.println("                The approach the program will use to solve the maze.");
        output.println("                Exactly one of these flags must be enabled.");
        output.println();
        output.println(" OPTIONAL arguments (can be included in any order before <inputfile>):");
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
