package pieces;

import java.util.ArrayList;
import java.util.List;

/**
 * A bástya bábut megvalósító osztály.
 */
public class Rook extends Piece {
    /**
     * Rook konstruktor.
     * Létrehoz egy bástyát az adott színnel és pozícióval.
     * 
     * @param c
     * @param p
     */
    public Rook(PieceColor c, Position p) {
        this.color = c;
        this.position = p;

        if (c == PieceColor.WHITE) {
            this.imageName = "white_rook.png";
        } else {
            this.imageName = "black_rook.png";
        }

        type = PieceType.ROOK;
        inStartingPosition = true;
    }

    /**
     * Lehetséges lépések:
     * függőlegesen vagy vízszintesen annyit, amíg akadályba nem ütközik
     * 
     * Visszatérési értéke egy lista a lehetséges lépésekkel.
     * 
     * @return List<Position>
     */
    @Override
    public List<Position> getEveryMove() {
        List<Position> positions = new ArrayList<>();

        Position move = position.forward(color);

        while (move != null) {
            positions.add(move);
            move = move.forward(color);
        }

        move = position.backwards(color);

        while (move != null) {
            positions.add(move);
            move = move.backwards(color);
        }

        move = position.right(color);

        while (move != null) {
            positions.add(move);
            move = move.right(color);
        }

        move = position.left(color);

        while (move != null) {
            positions.add(move);
            move = move.left(color);
        }

        return positions;
    }
}
