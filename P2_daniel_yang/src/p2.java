
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class p2 {

    public static enum Flag {
        STACK, QUEUE, OPT
    }

    private static Flag flag;
    private static File inputFile, outputFile;
    private static boolean inputFormat, outputFormat;
    private static Maze maze;

    /**
     *
     * @param args
     * @throws IncompleteMapException
     * @throws IncompleteMapException
     * @throws IncorrectMapFormatException
     * @throws IllegalCommandLineInputsException
     */
    public static void main(String[] args) throws IncompleteMapException, IncompleteMapException, IncorrectMapFormatException, IllegalCommandLineInputsException {

        try {
            readArguments(args);
        } catch (IllegalCommandLineInputsException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {

            maze = new Maze(inputFile, inputFormat);

        } catch (FileNotFoundException | IncompleteMapException | IncorrectMapFormatException e) {
            e.printStackTrace();
        }

        try {

            maze.print(outputFile, outputFormat);
            maze.print(System.out, outputFormat);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        try {

            inputFile = new File(args[args.length - 1]);

        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {

            throw new IllegalCommandLineInputsException("must provide a map input file in the arguments");
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

        output.println("Usage: java p3 [options] <--Stack|--Queue|--Opt> <inputfile>");
        output.println();
        output.println("where options include:");
        output.println();
        output.println("    --Help");
        output.println("                print this help message to the output stream");
        output.println();
    }
}
