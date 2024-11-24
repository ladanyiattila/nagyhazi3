package pieces;

import java.util.ArrayList;
import java.util.List;

/**
 * A futó bábut megvalósító osztály.
 */
public class Bishop extends Piece {
    /**
     * Bishop konstruktor.
     * Létrehoz egy futót az adott színnel és pozícióval.
     * 
     * @param c
     * @param p
     */
    public Bishop(PieceColor c, Position p) {
        this.color = c;
        this.position = p;

        if (c == PieceColor.WHITE) {
            this.imageName = "white_bishop.png";
        } else {
            this.imageName = "black_bishop.png";
        }

        type = PieceType.BISHOP;
        inStartingPosition = true;
    }

    
    /**
     * Lehetséges lépések:
     * minden átlón annyit, amíg akadályba nem ütközik
     * 
     * Visszatérési értéke egy lista a lehetséges lépésekkel.
     * 
     * @return List<Position>
     */
    @Override
    public List<Position> getEveryMove() {
        List<Position> positions = new ArrayList<>();

        Position move = position.rightDiagonal(color, Direction.FORWARD);

        while (move != null) {
            positions.add(move);
            move = move.rightDiagonal(color, Direction.FORWARD);
        }

        move = position.rightDiagonal(color, Direction.BACKWARD);

        while (move != null) {
            positions.add(move);
            move = move.rightDiagonal(color, Direction.BACKWARD);
        }

        move = position.leftDiagonal(color, Direction.FORWARD);

        while (move != null) {
            positions.add(move);
            move = move.leftDiagonal(color, Direction.FORWARD);
        }

        move = position.leftDiagonal(color, Direction.BACKWARD);

        while (move != null) {
            positions.add(move);
            move = move.leftDiagonal(color, Direction.BACKWARD);
        }

        return positions;
    }
}
