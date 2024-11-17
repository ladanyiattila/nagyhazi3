package pieces;

import java.util.ArrayList;
import java.util.List;

import pieces.*;

public class Queen extends Piece {
    public Queen(PieceColor c, Position p) {
        this.color = c;
        this.position = p;

        if (c == PieceColor.WHITE) {
            this.imageName = "white_queen.png";
        } else {
            this.imageName = "black_queen.png";
        }

        type = PieceType.QUEEN;
    }

    /*
     * Lehetséges lépések:
     * függőlegesen, vízszintesen, átlósan annyit, amíg akadályba nem ütközik
     */
    @Override
    public List<Position> getEveryMove() {
        List<Position> positions = new ArrayList<>();

        // függőlegesen és vizszintesen
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

        // átlósan minden irányba
        move = position.rightDiagonal(color, Direction.FORWARD);

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
