package pieces;

import java.util.ArrayList;
import java.util.List;

/**
 * A ló bábut megvalósító osztály.
 */
public class Knight extends Piece {
    /**
     * Knight konstruktor.
     * Létrehoz egy lovat az adott színnel és pozícióval
     * 
     * @param c
     * @param p
     */
    public Knight(PieceColor c, Position p) {
        this.color = c;
        this.position = p;

        if (c == PieceColor.WHITE) {
            this.imageName = "white_knight.png";
        } else {
            this.imageName = "black_knight.png";
        }

        type = PieceType.KNIGHT;
        inStartingPosition = true;
    }

    /**
     * Lehetséges lépések:
     * "L" alakban
     * 
     * Visszatérési értéke egy lista a lehetséges lépésekkel.
     * 
     * @return List<Position>
     */
    @Override
    public List<Position> getEveryMove() {
        List<Position> positions = new ArrayList<>();

        Position move = position.forward(color);

        if (move != null) {
            move = move.forward(color);

            // ha két lépés előre lehetséges, akkor utána egy jobbra és egy balra
            if (move != null) {
                Position p1 = move.right(color);
                Position p2 = move.left(color);

                if (p1 != null)
                    positions.add(p1);
                if (p2 != null)
                    positions.add(p2);
            }
        }

        move = position.backwards(color);
        if (move != null) {
            move = move.backwards(color);

            // ha két lépés hátra lehetséges, akkor utána egy jobbra és egy balra
            if (move != null) {
                Position p1 = move.right(color);
                Position p2 = move.left(color);

                if (p1 != null)
                    positions.add(p1);
                if (p2 != null)
                    positions.add(p2);
            }
        }

        move = position.right(color);

        if (move != null) {
            move = move.right(color);

            // ha két lépés jobbra lehetséges, akkor utána egy fel és egy le
            if (move != null) {
                Position p1 = move.forward(color);
                Position p2 = move.backwards(color);

                if (p1 != null)
                    positions.add(p1);
                if (p2 != null)
                    positions.add(p2);
            }
        }

        move = position.left(color);

        if (move != null) {
            move = move.left(color);

            // ha két lépés balra lehetséges, akkor utána egy fel és egy le
            if (move != null) {
                Position p1 = move.forward(color);
                Position p2 = move.backwards(color);

                if (p1 != null)
                    positions.add(p1);
                if (p2 != null)
                    positions.add(p2);
            }
        }

        return positions;
    }
}
