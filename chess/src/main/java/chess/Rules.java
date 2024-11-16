package chess;

import java.util.List;
import java.util.function.Function;
import java.math.*;

import pieces.*;

public class Rules {
    private static boolean possibleInThatWay(List<Piece> actualPosition, Piece movePiece, Position move, Function<Position, Position> function) {
        Position movePosition = new Position(movePiece.getPosition().getColumn(), movePiece.getPosition().getRow());
        movePosition = function.apply(movePosition);

        while (movePosition != null) {
            if (movePosition.equals(move)) {
                break;
            }

            for (Piece piece : actualPosition) {
                if (piece.getPosition().equals(movePosition)) {
                    return false;
                }
            }

            movePosition = function.apply(movePosition);        
        }
        
        return true;
    }

    private static boolean isAPieceThere(List<Piece> actualPosition, Position position, PieceColor color) {
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(position) && piece.getColor() != color) {
                return true;
            }
        }

        return false;
    }

    public static boolean isMovePossible(List<Piece> actualPosition, Piece movePiece, Position move) {
        // ugyanolyan színű bábut ne lehessen "ütni"
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(move) && movePiece.getColor() == piece.getColor()) {
                return false;
            }
        }

        // "lólépés"
        if (movePiece.getType() == PieceType.KNIGHT) {
            return true;
        }

        // gyalog ütés
        if (movePiece.getType() == PieceType.PAWN) {
            Position diagonalPos = movePiece.getPosition().rightDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (diagonalPos != null && diagonalPos.equals(move)) {
                return isAPieceThere(actualPosition, diagonalPos, movePiece.getColor());
            }

            diagonalPos = movePiece.getPosition().leftDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (diagonalPos != null && diagonalPos.equals(move)) {
                return isAPieceThere(actualPosition, diagonalPos, movePiece.getColor());
            }
        }

        int deltaRow = move.getRow() - movePiece.getPosition().getRow();
        int deltaColumn = move.getColumn() - movePiece.getPosition().getColumn();

        if (movePiece.getColor() == PieceColor.BLACK) {
            deltaRow *= -1;
            deltaColumn *= -1;
        }

        // vízszintes mozgás jobbra
        if (deltaRow == 0 && deltaColumn > 0) {
            Function<Position, Position> right = x -> x.right(movePiece.getColor());
            if (!possibleInThatWay(actualPosition, movePiece, move, right)) {
                return false;
            }
        }

        // vízszintes mozgás balra
        if (deltaRow == 0 && deltaColumn < 0) {
            Function<Position, Position> left = x -> x.left(movePiece.getColor());        
            if (!possibleInThatWay(actualPosition, movePiece, move, left)) {
                return false;
            } 
        }

        // függőleges mozgás felfelé
        if (deltaRow > 0 && deltaColumn == 0) {
            Function<Position, Position> forward = x -> x.forward(movePiece.getColor());
            if (!possibleInThatWay(actualPosition, movePiece, move, forward)) {
                return false;
            }
        }

        // függőleges mozgás lefelé
        if (deltaRow < 0 && deltaColumn == 0) {
            Function<Position, Position> backward = x -> x.backwards(movePiece.getColor());
            if (!possibleInThatWay(actualPosition, movePiece, move, backward)) {
                return false;
            }
        }

        // keresztbe mozgás felfelé jobbra
        if (deltaRow > 0 && deltaColumn > 0) {
            Function<Position, Position> rightDiagonal = x -> x.rightDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, rightDiagonal)) {
                return false;
            }
        }

        // keresztbe mozgás lefelé jobbra
        if (deltaRow < 0 && deltaColumn < 0) {
            Function<Position, Position> rightDiagonal = x -> x.rightDiagonal(movePiece.getColor(), Direction.BACKWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, rightDiagonal)) {
                return false;
            }
        }

        // keresztbe mozgás felfelé balra
        if (deltaRow > 0 && deltaColumn < 0) {
            Function<Position, Position> leftDiagonal = x -> x.leftDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, leftDiagonal)) {
                return false;
            }
        }

        // keresztbe mozgás lefelé balra
        if (deltaRow < 0 && deltaColumn > 0) {
            Function<Position, Position> leftDiagonal = x -> x.leftDiagonal(movePiece.getColor(), Direction.BACKWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, leftDiagonal)) {
                return false;
            }
        }

        return true;
    }
}
