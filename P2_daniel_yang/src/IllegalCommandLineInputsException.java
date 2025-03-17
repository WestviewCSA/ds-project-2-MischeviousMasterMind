public class IllegalCommandLineInputsException extends Exception {

    public IllegalCommandLineInputsException() {
        super("Invalid command line argument(s) for program");
    }

    public IllegalCommandLineInputsException(String message) {
        super("Invalid command line argument(s) for program - " + message);
    }
}
