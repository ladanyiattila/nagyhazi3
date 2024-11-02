package pieces;

import java.util.ArrayList;
import java.util.List;

import pieces.*;

public class King extends Piece {
    public King(PieceColor c, Position p) {
        this.color = c;
        this.position = p;

        if (c == PieceColor.WHITE) {
            this.imageName = "white_king.png";
        } else {
            this.imageName = "black_king.png";
        }
    }

    /*
     * Lehetséges lépések:
     * minden irányba egyet, ha lehetséges
     */
    @Override
    public List<Position> getEveryMove() {
        List<Position> positions = new ArrayList<>();

        Position move = position.forward(color);
        if (move != null) {
            positions.add(move);
        }
        
        move = position.backwards(color);
        if (move != null) {
            positions.add(move);
        }

        move = position.left(color);
        if (move != null) {
            positions.add(move);
        }

        move = position.right(color);
        if (move != null) {
            positions.add(move);
        }

        move = position.rightDiagonal(color, Direction.FORWARD);
        if (move != null) {
            positions.add(move);
        }

        move = position.rightDiagonal(color, Direction.BACKWARD);
        if (move != null) {
            positions.add(move);
        }

        move = position.leftDiagonal(color, Direction.FORWARD);
        if (move != null) {
            positions.add(move);
        }

        move = position.leftDiagonal(color, Direction.BACKWARD);
        if (move != null) {
            positions.add(move);
        }

        return positions;
    }
}
