package pieces;

import java.util.ArrayList;
import java.util.List;

/**
 * A mezők pozíciójáért felelős osztály.
 */
public class Position {
    private int row;
    private int column;
    
    private static List<String> letters;

    static {
        letters = new ArrayList<>(List.of("a","b","c","d","e","f","g","h"));
    }

    /**
     * Position konstruktor.
     * Létrehoz egy pozíciót az adott sorral és oszloppal.
     * 
     * @param c
     * @param r
     */
    public Position(int c, int r) {
        row = r;
        column = c;
    }

    /**
     * Position konstruktor.
     * Létrehoz egy pozíciót az adott (szöveges) sorral és oszloppal.
     * 
     * @param c
     * @param r
     */
    public Position(String c, int r) {
        this(letters.indexOf(c) + 1, r);
    }

    /** 
     * Beállítja a pozíció új értékeit.
     * 
     * @param r
     * @param c
     */
    public void set(int r, int c) {
        row = r;
        column = c;
    }
    
    /** 
     * Az oszlop numerikus értékét betűvé alakítja és visszatér vele.
     * 
     * @return String
     */
    public String columnToString() {
        return letters.get(column - 1);
    }
    
    /** 
     * Igaz, ha a pozíció nem lehetséges, azaz ha a sor vagy oszlop változók értéke
     * nem a [1;8] intervallum elemei
     * 
     * @return boolean
     */
    public boolean isOutOfBounds() {
        return row > 8 || row < 1 || column > 8 || column < 1;
    }

    /**
     * Visszatér egy új pozícióval, ami pozíció előtt van.
     * 
     * @param color
     * @return
     */
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

    /**
     * Visszatér egy pozícióval, ami a pozíció mögött van.
     * 
     * @param color
     * @return
     */
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

    /**
     * Visszatér egy pozícióval, ami a pozíciótól jobbra van.
     * 
     * @param color
     * @return
     */
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

    /**
     * Visszatér egy pozícióval, ami a pozíciótól balra van.
     * 
     * @param color
     * @return
     */
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

    /**
     * Visszatér egy pozícióval, ami a pozíciótól átlósan jobbra van.
     * Az átló mentén lehetséges előre és hátra is mozogni a direction
     * paraméterrel.
     * 
     * @param color
     * @param direction
     * @return
     */
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

    /**
     * Visszatér egy pozícióval, ami a pozíciótól átlósan balra van.
     * Az átló mentén lehetséges előre és hátra is mozogni a direction
     * paraméterrel.
     * 
     * @param color
     * @param direction
     * @return
     */
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

    /**
     * Két pozíció összehasonlításához:
     * akkor egyenlő két pozíció, ha soruk és oszlopuk is megegyezik
     */
    @Override
    public boolean equals(Object rhs) {
        Position other = (Position) rhs;
        return (other.getColumn() == this.column && other.getRow() == this.row);
    }

    /**
     * A pozíció szöveggé alakításához, pl.: e4
     */
    public String toString() {
        return columnToString() + row;
    }

    /**
     * column változó getter függvénye
     * 
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * row változó getter függvénye
     * 
     * @return
     */
    public int getRow() {
        return row;
    }
}
