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

        type = PieceType.KING;
        inStartingPosition = true;
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

        // sáncolás (a király még nem mozdult meg az alap pozícióból)
        if (this.inStartingPosition) {
            if (this.color == PieceColor.WHITE && this.getPosition().equals(new Position("e", 1))) {
                positions.add(new Position("g", 1));
                positions.add(new Position("c", 1));
            } else if (this.color == PieceColor.BLACK && this.getPosition().equals(new Position("e", 8))) {
                positions.add(new Position("g", 8));
                positions.add(new Position("c", 8));
            }   
        }

        return positions;
    }
}
