
import java.io.File;
import java.io.FileNotFoundException;

public class p3 {

    public static enum Flag {
        STACK, QUEUE, OPT
    }

    private static Flag flag;

    public static void main(String[] args) throws IncompleteMapException, IncompleteMapException, IncorrectMapFormatException, IllegalCommandLineInputsException {

		setFlags(args);

        try {
            Maze newMaze = new Maze(new File("input/text-based"), Maze.TEXT_BASED);

            newMaze.print(new File("output/coordinate-based"), Maze.COORDINATE_BASED);
            newMaze.print(System.out, Maze.COORDINATE_BASED);

        } catch (FileNotFoundException | IncompleteMapException | IncorrectMapFormatException e) {
            e.printStackTrace();
        }

        try {
            Maze newMaze = new Maze(new File("input/coordinate-based"), Maze.COORDINATE_BASED);

            newMaze.print(new File("output/text-based"), Maze.TEXT_BASED);
            newMaze.print(System.out, Maze.TEXT_BASED);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void setFlags(String[] args) throws IllegalCommandLineInputsException {

        int flags = 0;

        if (argsContains(args, "--Stack")) {
            flags += 1;
        }

		if (argsContains(args, "--Queue")) {
            flags += 2;
        }

		if (argsContains(args, "--Opt")) {
            flags += 4;
        }

		switch(flags) {

			case 0 -> throw new IllegalCommandLineInputsException("use either --Stack, --Queue, or --Opt as one of your arguments");
			
			case 1 -> flag = Flag.STACK;
			case 2 -> flag = Flag.QUEUE;
			case 4 -> flag = Flag.OPT;

			default -> throw new IllegalCommandLineInputsException("cannot have multiple flags set on (use only either --Stack, --Queue, or --Opt)");
		}
    }

    private static boolean argsContains(String[] args, String argument) {
        for (String arg : args) {
            if (arg.equals(argument)) {
                return true;
            }
        }

        return false;
    }
}
