package pieces;

import java.util.ArrayList;
import java.util.List;

import pieces.*;

public class Rook extends Piece {
    public Rook(PieceColor c, Position p) {
        this.color = c;
        this.position = p;
    }

    /*
     * Lehetséges lépések:
     * minden irányba annyit, amíg akadályba nem ütközik
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

        for (Position p : positions) {
            System.out.println(p);
        }

        return positions;
    }
}
