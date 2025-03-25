public class IncorrectMapFormatException extends Exception {

	public IncorrectMapFormatException() {
		super("Map file is incorrectly formatted");
	}

	public IncorrectMapFormatException(String message) {
		super("Map file is incorrectly formatted - " + message);
	}
}