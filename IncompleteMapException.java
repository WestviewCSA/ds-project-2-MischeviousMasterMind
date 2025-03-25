public class IncompleteMapException extends Exception {

	public IncompleteMapException() {
		super("Map file is incomplete");
	}

	public IncompleteMapException(String message) {
		super("Map file is incomplete - " + message);
	}
}
