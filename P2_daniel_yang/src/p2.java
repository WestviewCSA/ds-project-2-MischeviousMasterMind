
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class p2 {

    public static enum Flag {
        STACK, QUEUE, OPT
    }

    private static Flag flag;
    private static File inputFile;
    private static boolean inputFormat, outputFormat;

    /**
     *
     * @param args
     * @throws IncompleteMapException
     * @throws IncompleteMapException
     * @throws IncorrectMapFormatException
     * @throws IllegalCommandLineInputsException
     */
    public static void main(String[] args) throws IncompleteMapException, IncompleteMapException, IncorrectMapFormatException, IllegalCommandLineInputsException, IllegalMapCharacterException {

        char[][][] maze = null;

        try {

            readArguments(args);

        } catch (IllegalCommandLineInputsException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Caused by: " + e.getClass().getName());
            System.exit(-1);
        }

        try {

            maze = Maze.read(inputFile, inputFormat);

        } catch (FileNotFoundException | IncompleteMapException | IncorrectMapFormatException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Caused by: " + e.getClass().getName());
            System.exit(-1);
        }

        long startTime = System.nanoTime();
        PathFinder.solve(maze);
        long endTime = System.nanoTime();

        Maze.print(maze, System.out, outputFormat);
        System.out.println();
        System.out.println("Total Runtime: " + (endTime - startTime) / 1000000000.0 + " seconds");
    }

    private static void readArguments(String[] args) throws IllegalCommandLineInputsException {

        if (args.length == 0) {
            printUsage(System.err);
            System.exit(-1);
        }

        if (contains(args, "--help")) {
            printUsage(System.out);
            System.exit(0);
        }

        inputFile = new File(args[0]);

        int flags = 0;

        if (contains(args, "--Stack")) {
            flags += 1;
        }

        if (contains(args, "--Queue")) {
            flags += 2;
        }

        if (contains(args, "--Opt")) {
            flags += 4;
        }

        try {

            switch (flags) {

                case 0 ->
                    throw new IllegalCommandLineInputsException("use either --Stack, --Queue, or --Opt as one of your arguments");

                case 1 ->
                    flag = Flag.STACK;
                case 2 ->
                    flag = Flag.QUEUE;
                case 4 ->
                    flag = Flag.OPT;

                default ->
                    throw new IllegalCommandLineInputsException("cannot have multiple flags set on (use only either --Stack, --Queue, or --Opt)");
            }

        } catch (IllegalCommandLineInputsException e) {
            System.err.println("Error: Legal commandline arguments must include exactly one of --Stack, -- Queue, or --Opt");
            System.err.println("Caused by: " + e.getClass().getName() + ": " + e.getMessage());
            
            System.exit(-1);
        }
    }

    private static boolean contains(String[] args, String argument) {
        for (String arg : args) {
            if (arg.equals(argument)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Prints the following help message to <code>output</code>:
     * <pre><codeblock>
     * can i have a uhhhhhhhhhh...
     * </codeblock></pre>
     *
     * @param output the PrintStream object to print the help message to
     */
    private static void printUsage(PrintStream output) {

        output.println("Usage: java p3 <inputfile> [options] <--Stack|--Queue|--Opt> [options]");
        output.println();
        output.println(" <inputfile> is the path to the input map file used by the program and");
        output.println(" --Stack, --Queue, and --Opt are the approaches the program will use to");
        output.println(" solve the maze. Only one of these flags can be enabled.");
        output.println();
        output.println(" where options include:");
        output.println();
        output.println("    --Help");
        output.println("                print this help message to the output stream");
        output.println();
    }
}
