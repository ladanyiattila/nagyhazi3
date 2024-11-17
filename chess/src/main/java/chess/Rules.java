package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.math.*;

import pieces.*;

public class Rules {
    private static boolean possibleInThatWay(List<Piece> actualPosition, Piece movePiece, Position move, Function<Position, Position> function, boolean checkForChecksAfterMove) {
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

        Piece king = null;
        for (Piece piece : actualPosition) {
            if (piece.getType() == PieceType.KING && piece.getColor() == movePiece.getColor()) {
                king = piece;
            }
        }

        if (checkForChecksAfterMove && isKingInCheckAfterMove(actualPosition, movePiece, move, king)) {
            return false;
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

    private static boolean isKingInCheckAfterMove(List<Piece> actualPosition, Piece movePiece, Position move, Piece king) {
        List<Piece> actualPositionClone = new ArrayList<>();

        for (Piece piece : actualPosition) {
            actualPositionClone.add(piece);
        }
        
        Position temp = movePiece.getPosition();

        // történik-e ütés
        Piece tempPiece = null;

        ListIterator<Piece> iter = actualPositionClone.listIterator();

        while (iter.hasNext()) {
            Piece current = iter.next();

            if (current.getPosition().equals(move)) {
                tempPiece = current;
                iter.remove();
                break;
            }
        }

        movePiece.setPosition(move);

        if (!isKingInCheck(actualPositionClone, king, false)) {
            movePiece.setPosition(temp);

            if (tempPiece != null) {
                actualPositionClone.add(tempPiece);
            }

            return false;
        }

        if (tempPiece != null) {
            actualPositionClone.add(tempPiece);
        }

        movePiece.setPosition(temp);

        return true;
    }

    private static boolean isKingInCheck(List<Piece> actualPosition, Piece king, boolean checkForChecksAfterMove) {
        for (Piece piece : actualPosition) {
            // király nem tud sakkot adni
            if (piece.getType() != PieceType.KING && piece.getColor() != king.getColor()) {
                List<Position> positions = piece.getEveryMove();

                ListIterator<Position> iter = positions.listIterator();

                while (iter.hasNext()) {
                    Position current = iter.next();

                    // gyalog lépésénél az előrét nem kell figyelembe venni
                    if (piece.getType() == PieceType.PAWN && current.getColumn() == piece.getPosition().getColumn()) {
                        iter.remove();
                        continue;
                    }
                        
                    if (!isMovePossible(actualPosition, piece, current, piece.getColor(), false, false)) {
                        iter.remove();
                    }
                }

                for (Position pos : positions) {
                    if (pos.equals(king.getPosition())){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isMovePossible(List<Piece> actualPosition, Piece movePiece, Position move, PieceColor nextMoveColor, boolean checkForChecks, boolean checkForChecksAfterMove) {
        // ugyanolyan színű bábut ne lehessen "ütni"
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(move) && movePiece.getColor() == piece.getColor()) {
                return false;
            }
        }

        Piece king = null;
        if (checkForChecks) {
            for (Piece piece : actualPosition) {
                if (piece.getType() == PieceType.KING && piece.getColor() == nextMoveColor) {
                    king = piece;
                }
            }

            if (isKingInCheck(actualPosition, king, false)) {
                if (isMovePossible(actualPosition, movePiece, move, nextMoveColor, false, false)) {
                    return !isKingInCheckAfterMove(actualPosition, movePiece, move, king);
                }
            }
        }

        
        // király ne léphessen sakkba
        if (movePiece.getType() == PieceType.KING && checkForChecksAfterMove) {
            ArrayList<Position> opponentEveryMove = new ArrayList<>();

            for (Piece piece : actualPosition) {
                if (piece.getColor() != movePiece.getColor()) {
                    for (Position pos : piece.getEveryMove()) {
                        if (piece.getType() != PieceType.KING && !(piece.getType() == PieceType.PAWN
                                && pos.getColumn() == piece.getPosition().getColumn()) && isMovePossible(actualPosition, piece, pos, piece.getColor(), false, true)) {
                            opponentEveryMove.add(pos);
                        }
                    }
                }
            }

            for (Position pos : opponentEveryMove) {
                if (pos.equals(move)) {
                    return false;
                }
            }
        }

        // király mellé ne lehessen belépni királlyal
        if (movePiece.getType() == PieceType.KING) {
            Piece enemyKing = null;
            for (Piece piece : actualPosition) {
                if (piece.getType() == PieceType.KING && piece.getColor() != movePiece.getColor()) {
                    enemyKing = piece;
                }
            }

            List<Position> enemyKingMoves = enemyKing.getEveryMove();

            for (Position pos : enemyKingMoves) {
                if (pos.equals(move)) {
                    return false;
                }
            }
        }


        // "lólépés"
        if (movePiece.getType() == PieceType.KNIGHT) {
            king = null;
            for (Piece piece : actualPosition) {
                if (piece.getType() == PieceType.KING && piece.getColor() == movePiece.getColor()) {
                    king = piece;
                }
            }

            return !(checkForChecksAfterMove && isKingInCheckAfterMove(actualPosition, movePiece, move, king));
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
            if (!possibleInThatWay(actualPosition, movePiece, move, right, checkForChecks)) {
                return false;
            }
        }

        // vízszintes mozgás balra
        if (deltaRow == 0 && deltaColumn < 0) {
            Function<Position, Position> left = x -> x.left(movePiece.getColor());        
            if (!possibleInThatWay(actualPosition, movePiece, move, left, checkForChecks)) {
                return false;
            } 
        }

        // függőleges mozgás felfelé
        if (deltaRow > 0 && deltaColumn == 0) {
            Function<Position, Position> forward = x -> x.forward(movePiece.getColor());

            // előre ne lehessen ütni gyaloggal
            if (movePiece.getType() == PieceType.PAWN) {
                for (Piece piece : actualPosition) {
                    if (piece.getPosition().equals(move)) {
                        return false;
                    }
                }
            }

            if (!possibleInThatWay(actualPosition, movePiece, move, forward, checkForChecks)) {
                return false;
            }
        }

        // függőleges mozgás lefelé
        if (deltaRow < 0 && deltaColumn == 0) {
            Function<Position, Position> backward = x -> x.backwards(movePiece.getColor());
            if (!possibleInThatWay(actualPosition, movePiece, move, backward, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás felfelé jobbra
        if (deltaRow > 0 && deltaColumn > 0) {
            Function<Position, Position> rightDiagonal = x -> x.rightDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, rightDiagonal, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás lefelé jobbra
        if (deltaRow < 0 && deltaColumn < 0) {
            Function<Position, Position> rightDiagonal = x -> x.rightDiagonal(movePiece.getColor(), Direction.BACKWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, rightDiagonal, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás felfelé balra
        if (deltaRow > 0 && deltaColumn < 0) {
            Function<Position, Position> leftDiagonal = x -> x.leftDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, leftDiagonal, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás lefelé balra
        if (deltaRow < 0 && deltaColumn > 0) {
            Function<Position, Position> leftDiagonal = x -> x.leftDiagonal(movePiece.getColor(), Direction.BACKWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, leftDiagonal, checkForChecks)) {
                return false;
            }
        }

        return true;
    }

    public static String getEndOfGame(List<Piece> actualPosition, PieceColor color) {
        ArrayList<Position> everyMove = new ArrayList<>();

        Piece king = null;
        for (Piece piece : actualPosition) {
            if (piece.getColor() == color) {
                for (Position pos : piece.getEveryMove()) {
                    if (isMovePossible(actualPosition, piece, pos, color, true, true)) {
                        everyMove.add(pos);
                    }
                }

                if (piece.getType() == PieceType.KING) {
                    king = piece;
                }
            }
        }

        // ha nincs több szabályos lépés és sakkban van a király, akkor matt
        if (everyMove.isEmpty() && isKingInCheck(actualPosition, king, true)) {
            return "CHECKMATE";
        } else if (everyMove.isEmpty() && !isKingInCheck(actualPosition, king, true)) {
            // ha nincs több szabályos lépés, de nincs sakkban a király, akkor patt
            return "STALEMATE";
        }

        return "NOT_FINISHED";

    }
}
