public class IllegalMapCharacterException extends Exception {

    public IllegalMapCharacterException() {
        super("Map has illegal character(s) - the only permitted characters are '.', '@', 'w', '$', and '|'");
    }
}
