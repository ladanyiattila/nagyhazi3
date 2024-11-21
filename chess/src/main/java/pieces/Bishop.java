package pieces;

import java.util.ArrayList;
import java.util.List;

import pieces.*;

public class Bishop extends Piece {
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

    /*
     * Lehetséges lépések:
     * minden átlón annyit, amíg akadályba nem ütközik
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
