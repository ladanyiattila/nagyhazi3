package pieces;

import java.util.ArrayList;
import java.util.List;

import pieces.*;

public class Position {
    public int row;
    public int column;
    private static List<String> letters;

    static {
        letters = new ArrayList<>(List.of("a","b","c","d","e","f","g","h"));
    }

    public Position(int c, int r) {
        row = r;
        column = c;
    }

    public Position(String c, int r) {
        this(letters.indexOf(c), r);
    }

    public void set(int r, int c) {
        row = r;
        column = c;
    }

    public String columnToString() {
        return letters.get(column);
    }

    public Position forward(PieceColor color) {
        if (color == PieceColor.WHITE) {
            return new Position(column, row + 1);
        }

        return new Position(column, row - 1);
    }

    public Position backwards(PieceColor color) {
        if (color == PieceColor.WHITE) {
            return new Position(column, row - 1);
        }

        return new Position(column, row + 1);
    }

    public Position rightDiagonal(PieceColor color, Direction direction) {
        int shifting = -1;

        if (direction == Direction.FORWARD) {
            shifting = 1;
        }

        if (color == PieceColor.WHITE) {
            return new Position(column + shifting, row + shifting);
        }

        return new Position(column - shifting, row - shifting);
    }

    public Position leftDiagonal(PieceColor color, Direction direction) {
        int shifting = -1;

        if (direction == Direction.FORWARD) {
            shifting = 1;
        }

        if (color == PieceColor.WHITE) {
            return new Position(column - shifting, row + shifting);
        }

        return new Position(column + shifting, row - shifting);
    }

    @Override
    public boolean equals(Object rhs) {
        Position other = (Position) rhs;
        return other.column == column && other.row == row;
    }

    public String toString() {
        return columnToString() + " " + row;
    }
}
