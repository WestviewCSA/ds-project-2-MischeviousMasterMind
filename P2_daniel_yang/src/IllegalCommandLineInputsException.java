public class IllegalCommandLineInputsException extends Exception {

    public IllegalCommandLineInputsException() {
        super("Missing required command line input(s) for program");
    }

    public IllegalCommandLineInputsException(String message) {
        super("Missing required command line input(s) for program - " + message);
    }
}
