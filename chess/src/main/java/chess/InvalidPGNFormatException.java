package chess;

public class InvalidPGNFormatException extends Exception {
    public InvalidPGNFormatException() {
        super("Invalid PGN format");
    }
}
