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
        this(letters.indexOf(c) + 1, r);
    }

    public void set(int r, int c) {
        row = r;
        column = c;
    }

    public String columnToString() {
        return letters.get(column - 1);
    }

    public boolean isOutOfBounds() {
        return row > 8 || row < 1 || column > 8 || column < 1;
    }

    public Position forward(PieceColor color) {
        Position position;

        if (color == PieceColor.WHITE) {
            position = new Position(column, row + 1);
        } else {
            position = new Position(column, row - 1);
        }

        if (position.isOutOfBounds()) {
            return null;
        }

        return position;
    }

    public Position backwards(PieceColor color) {
        Position position;

        if (color == PieceColor.WHITE) {
            position = new Position(column, row - 1);
        } else {
            position = new Position(column, row + 1);
        }

        if (position.isOutOfBounds()) {
            return null;
        }

        return position;
    }

    public Position right(PieceColor color) {
        Position position;

        if (color == PieceColor.WHITE) {
            position = new Position(column + 1, row);
        } else {
            position = new Position(column - 1, row);
        }

        if (position.isOutOfBounds()) {
            return null;
        }

        return position;
    }

    public Position left(PieceColor color) {
        Position position;

        if (color == PieceColor.WHITE) {
            position = new Position(column - 1, row);
        } else {
            position = new Position(column + 1, row);
        }

        if (position.isOutOfBounds()) {
            return null;
        }

        return position;
    }

    public Position rightDiagonal(PieceColor color, Direction direction) {
        Position position;
        int shifting = -1;

        if (direction == Direction.FORWARD) {
            shifting = 1;
        }

        if (color == PieceColor.WHITE) {
            position = new Position(column + shifting, row + shifting);
        } else {
            position = new Position(column - shifting, row - shifting);
        }

        if (position.isOutOfBounds()) {
            return null;
        }

        return position;
    }

    public Position leftDiagonal(PieceColor color, Direction direction) {
        Position position;
        int shifting = -1;

        if (direction == Direction.FORWARD) {
            shifting = 1;
        }

        if (color == PieceColor.WHITE) {
            position = new Position(column - shifting, row + shifting);
        } else {
            position = new Position(column + shifting, row - shifting);
        }

        if (position.isOutOfBounds()) {
            return null;
        }

        return position;
    }

    @Override
    public boolean equals(Object rhs) {
        Position other = (Position) rhs;
        return (other.getColumn() == this.column && other.getRow() == this.row);
    }

    public String toString() {
        return columnToString() + row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
