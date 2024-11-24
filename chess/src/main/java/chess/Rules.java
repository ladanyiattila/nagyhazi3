package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;

import pieces.*;

/**
 * A szabályrendszer megvalósításáért felelős osztály.
 */
public class Rules {
    /**
     * Privát konstruktor a publikus elrejtése céljából
     */
    private Rules() {}

    /**
     * Ellenőrzi, hogy az adott irányba nincs-e "útban" másik bábu
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @param function
     * @param checkForChecksAfterMove
     * @return boolean
     */
    private static boolean possibleInThatWay(List<Piece> actualPosition, Piece movePiece, Position move,
            UnaryOperator<Position> function, boolean checkForChecksAfterMove) {
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

        return !(checkForChecksAfterMove && isKingInCheckAfterMove(actualPosition, movePiece, move, king));
    }

    /**
     * Igaz a visszatérési értéke, ha az adott állásban olyan bábu helyezkedik el a
     * paraméterként kapott mezőn, ami színe nem a paraméterként kapott szín.
     * 
     * @param actualPosition
     * @param position
     * @param color
     * @return boolean
     */
    private static boolean isAPieceThere(List<Piece> actualPosition, Position position, PieceColor color) {
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(position) && piece.getColor() != color) {
                return true;
            }
        }

        return false;
    }

    /**
     * Igaz, ha a lépés elvégzése után a király sakkban marad.
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @param king
     * @return boolean
     */
    private static boolean isKingInCheckAfterMove(List<Piece> actualPosition, Piece movePiece, Position move,
            Piece king) {
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

        if (!isKingInCheck(actualPositionClone, king)) {
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

    /**
     * Igaz, ha a király az adott állásban sakkban van.
     * 
     * @param actualPosition
     * @param king
     * @param checkForChecksAfterMove
     * @return
     */
    private static boolean isKingInCheck(List<Piece> actualPosition, Piece king) {
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

                    if (!isMovePossible(actualPosition, piece, current, piece.getColor(), false, false, null, null)) {
                        iter.remove();
                    }
                }

                for (Position pos : positions) {
                    if (pos.equals(king.getPosition())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Ellenőrzi, hogy az adott állásban a lépés elhárítja-e a kapott sakkot.
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @param nextMoveColor
     * @param lastMovePiece
     * @param lastMovePosition
     * @return
     */
    private static boolean checkForChecksOnTheBoard(List<Piece> actualPosition, Piece movePiece, Position move,
            PieceColor nextMoveColor, Piece lastMovePiece, Position lastMovePosition) {
        Piece king = null;
        for (Piece piece : actualPosition) {
            if (piece.getType() == PieceType.KING && piece.getColor() == nextMoveColor) {
                king = piece;
            }
        }

        if (isKingInCheck(actualPosition, king)
                && isMovePossible(actualPosition, movePiece, move, nextMoveColor, false, false, lastMovePiece,
                        lastMovePosition)) {
            return !isKingInCheckAfterMove(actualPosition, movePiece, move, king);
        }

        return true;
    }

    /**
     * Ellenőrzi, hogy a király sakkba lép-e: ha igen, akkor nem lehetséges a lépés
     * és hamis
     * a visszatérési érték. Egyébként igaz.
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @param lastMovePiece
     * @param lastMovePosition
     * @return
     */
    private static boolean kingMovesIntoCheck(List<Piece> actualPosition, Piece movePiece, Position move,
            Piece lastMovePiece, Position lastMovePosition) {
        ArrayList<Position> opponentEveryMove = new ArrayList<>();

        for (Piece piece : actualPosition) {
            if (piece.getColor() != movePiece.getColor()) {
                for (Position pos : piece.getEveryMove()) {
                    if (piece.getType() != PieceType.KING && !(piece.getType() == PieceType.PAWN
                            && pos.getColumn() == piece.getPosition().getColumn())
                            && isMovePossible(actualPosition, piece, pos, piece.getColor(), false, true, lastMovePiece,
                                    lastMovePosition)) {
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

        return true;
    }

    /**
     * Ha a király az ellenfél királya mellé nem léphet. Ha ez lenne a lépés,
     * akkor a visszatérési érték hamis. Egyébként igaz.
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @return
     */
    private static boolean kingMovesNextToKing(List<Piece> actualPosition, Piece movePiece, Position move) {
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

        return true;
    }

    /**
     * Igaz, ha a rövidsánc lehetséges
     * 
     * @param actualPosition
     * @param movePiece
     * @param lastMovePiece
     * @param lastMovePosition
     * @return
     */
    private static boolean shortCastlingPossible(List<Piece> actualPosition, Piece movePiece, Piece lastMovePiece, Position lastMovePosition) {
        int row = movePiece.getColor() == PieceColor.WHITE ? 1 : 8;
        int enemyRow = movePiece.getColor() == PieceColor.WHITE ? 8 : 1;
        PieceColor moveColor = movePiece.getColor();
        PieceColor enemyColor = movePiece.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        
        List<Position> allEnemyMoves = new ArrayList<>();

        for (Piece piece : actualPosition) {
            if (piece.getColor() == enemyColor) {
                List<Position> pieceMoveList = piece.getEveryMove();

                for (Position position : pieceMoveList) {
                    if (piece.getType() == PieceType.KING && (position.equals(new Position("g", enemyRow))
                            || position.equals(new Position("c", enemyRow)))) {
                        continue;
                    }

                    if (isMovePossible(actualPosition, piece, position, piece.getColor(), false, false,
                            lastMovePiece, lastMovePosition)) {
                        allEnemyMoves.add(position);
                    }
                }
            }
        }

        if (isKingInCheck(actualPosition, movePiece)) {
            return false;
        }

        // nem tud egyik ellenfél bábu oda lépni
        for (Position position : allEnemyMoves) {
            if (position.equals(new Position("f", row)) || position.equals(new Position("g", row))) {
                return false;
            }
        }

        // ha a király vagy a bástya megmozdult, akkor nem lehet sáncolni
        if (movePiece.hasPieceMoved()) {
            return false;
        }

        for (Piece piece : actualPosition) {
            if (piece.getColor() == moveColor && piece.getType() == PieceType.ROOK
                    && piece.getPosition().equals(new Position("h", row))) {
                if (piece.hasPieceMoved()) {
                    return false;
                }
            }
        }

        return true;
    }
    
    /**
     * Igaz, ha a hosszúsánc lehetséges
     * 
     * @param actualPosition
     * @param movePiece
     * @param lastMovePiece
     * @param lastMovePosition
     * @return
     */
    private static boolean longCastlingPossible(List<Piece> actualPosition, Piece movePiece, Piece lastMovePiece,
            Position lastMovePosition) {
        int row = movePiece.getColor() == PieceColor.WHITE ? 1 : 8;
        int enemyRow = movePiece.getColor() == PieceColor.WHITE ? 8 : 1;
        PieceColor moveColor = movePiece.getColor();
        PieceColor enemyColor = movePiece.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        List<Position> allEnemyMoves = new ArrayList<>();

        for (Piece piece : actualPosition) {
            if (piece.getColor() == enemyColor) {
                List<Position> pieceMoveList = piece.getEveryMove();

                for (Position position : pieceMoveList) {
                    if (piece.getType() == PieceType.KING && (position.equals(new Position("g", enemyRow))
                            || position.equals(new Position("c", enemyRow)))) {
                        continue;
                    }

                    if (isMovePossible(actualPosition, piece, position, piece.getColor(), false, false,
                            lastMovePiece, lastMovePosition)) {
                        allEnemyMoves.add(position);
                    }
                }
            }
        }

        if (isKingInCheck(actualPosition, movePiece)) {
            return false;
        }

        // nem tud egyik ellenfél bábu oda lépni
        for (Position position : allEnemyMoves) {
            if (position.equals(new Position("d", row)) || position.equals(new Position("c", row))
                    || position.equals(new Position("b", row))) {
                return false;
            }
        }

        // ha a király vagy a bástya megmozdult, akkor nem lehet sáncolni
        if (movePiece.hasPieceMoved()) {
            return false;
        }

        for (Piece piece : actualPosition) {
            if (piece.getColor() == moveColor && piece.getType() == PieceType.ROOK
                    && piece.getPosition().equals(new Position("a", row))) {
                if (piece.hasPieceMoved()) {
                    return false;
                }
            }
        }

        return true;
    }
    
    /**
     * Igaz, ha a sáncolás lehetséges
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @param lastMovePiece
     * @param lastMovePosition
     * @return
     */
    private static boolean isCastlingPossible(List<Piece> actualPosition, Piece movePiece, Position move,
            Piece lastMovePiece, Position lastMovePosition) {
        int row = movePiece.getColor() == PieceColor.WHITE ? 1 : 8;

        // rövidsánc
        if (move.equals(new Position("g", row))) {
            return shortCastlingPossible(actualPosition, movePiece, lastMovePiece, lastMovePosition);
        } else if (move.equals(new Position("c", row))) { // hosszúsánc
            return longCastlingPossible(actualPosition, movePiece, lastMovePiece, lastMovePosition);
        }

        return true;
    }

    /**
     * A szabályrendszer fő függvénye, szerepe, hogy eldöntse egy lépésről, hogy az szabályos-e.
     * Ha szabályos, akkor a visszatérési értéke igaz
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @param nextMoveColor
     * @param checkForChecks
     * @param checkForChecksAfterMove
     * @param lastMovePiece
     * @param lastMovePosition
     * @return
     */
    public static boolean isMovePossible(List<Piece> actualPosition, Piece movePiece, Position move,
            PieceColor nextMoveColor, boolean checkForChecks, boolean checkForChecksAfterMove, Piece lastMovePiece,
            Position lastMovePosition) {
        // ugyanolyan színű bábut ne lehessen "ütni"
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(move) && movePiece.getColor() == piece.getColor()) {
                return false;
            }
        }

        // ha a király sakkban van a lépés után is, akkor nem lehetséges a lépés
        Piece king = null;
        if (checkForChecks && !checkForChecksOnTheBoard(actualPosition, movePiece, move, nextMoveColor, lastMovePiece,
                lastMovePosition)) {
            return false;
        }

        // király ne léphessen sakkba
        if (movePiece.getType() == PieceType.KING && checkForChecksAfterMove
                && !kingMovesIntoCheck(actualPosition, movePiece, move, lastMovePiece, lastMovePosition)) {
            return false;
        }

        // király mellé ne lehessen belépni királlyal
        if (movePiece.getType() == PieceType.KING && kingMovesNextToKing(actualPosition, movePiece, move)) {
            return false;
        }

        // sáncolás
        if (movePiece.getType() == PieceType.KING && (move.equals(new Position("g", 1))
                || move.equals(new Position("c", 1))) || move.equals(new Position("g", 8))
                || move.equals(new Position("c", 8))) {
            return isCastlingPossible(actualPosition, movePiece, move, lastMovePiece, lastMovePosition);
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
            if (lastMovePiece != null && lastMovePiece.getType() == PieceType.PAWN
                    && lastMovePosition.getRow() == movePiece
                            .getPosition().getRow()
                    && ((Pawn) lastMovePiece).getMovedTwoAtStart()
                    && lastMovePosition.getColumn() - movePiece.getPosition().getColumn() == 1
                            * (movePiece.getColor() == PieceColor.WHITE ? 1 : -1)) {
                return true;
            }

            if (diagonalPos != null && diagonalPos.equals(move)) {
                return isAPieceThere(actualPosition, diagonalPos, movePiece.getColor());
            }

            diagonalPos = movePiece.getPosition().leftDiagonal(movePiece.getColor(), Direction.FORWARD);

            if (lastMovePiece != null
                    && lastMovePiece.getType() == PieceType.PAWN
                    && lastMovePosition.getRow() == movePiece.getPosition().getRow()
                    && ((Pawn) lastMovePiece).getMovedTwoAtStart()
                    && lastMovePosition.getColumn() - movePiece.getPosition().getColumn() == -1
                            * (movePiece.getColor() == PieceColor.WHITE ? 1 : -1)) {
                return true;
            }

            if (diagonalPos != null && diagonalPos.equals(move)) {
                return isAPieceThere(actualPosition, diagonalPos, movePiece.getColor());
            }
        }

        return checkForEveryWay(actualPosition, movePiece, move, checkForChecks);
    }

    /**
     * Ellenőrzi, hogy a lépés lehetséges-e a mező irányába:
     * ha például átlósan lép egy futó, akkor amíg eljut a lépés irányába,
     * nem lehet az "útjában" más bábu, mivel nem tud "átugrani" bábukat.
     * 
     * @param actualPosition
     * @param movePiece
     * @param move
     * @param checkForChecks
     * @return
     */
    private static boolean checkForEveryWay(List<Piece> actualPosition, Piece movePiece, Position move,
            boolean checkForChecks) {
        int deltaRow = move.getRow() - movePiece.getPosition().getRow();
        int deltaColumn = move.getColumn() - movePiece.getPosition().getColumn();

        if (movePiece.getColor() == PieceColor.BLACK) {
            deltaRow *= -1;
            deltaColumn *= -1;
        }

        // vízszintes mozgás jobbra
        if (deltaRow == 0 && deltaColumn > 0) {
            UnaryOperator<Position> right = x -> x.right(movePiece.getColor());
            if (!possibleInThatWay(actualPosition, movePiece, move, right, checkForChecks)) {
                return false;
            }
        }

        // vízszintes mozgás balra
        if (deltaRow == 0 && deltaColumn < 0) {
            UnaryOperator<Position> left = x -> x.left(movePiece.getColor());
            if (!possibleInThatWay(actualPosition, movePiece, move, left, checkForChecks)) {
                return false;
            }
        }

        // függőleges mozgás felfelé
        if (deltaRow > 0 && deltaColumn == 0) {
            UnaryOperator<Position> forward = x -> x.forward(movePiece.getColor());

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
            UnaryOperator<Position> backward = x -> x.backwards(movePiece.getColor());
            if (!possibleInThatWay(actualPosition, movePiece, move, backward, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás felfelé jobbra
        if (deltaRow > 0 && deltaColumn > 0) {
            UnaryOperator<Position> rightDiagonal = x -> x.rightDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, rightDiagonal, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás lefelé jobbra
        if (deltaRow < 0 && deltaColumn < 0) {
            UnaryOperator<Position> rightDiagonal = x -> x.rightDiagonal(movePiece.getColor(), Direction.BACKWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, rightDiagonal, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás felfelé balra
        if (deltaRow > 0 && deltaColumn < 0) {
            UnaryOperator<Position> leftDiagonal = x -> x.leftDiagonal(movePiece.getColor(), Direction.FORWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, leftDiagonal, checkForChecks)) {
                return false;
            }
        }

        // keresztbe mozgás lefelé balra
        if (deltaRow < 0 && deltaColumn > 0) {
            UnaryOperator<Position> leftDiagonal = x -> x.leftDiagonal(movePiece.getColor(), Direction.BACKWARD);
            if (!possibleInThatWay(actualPosition, movePiece, move, leftDiagonal, checkForChecks)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ellenőrzi, hogy véget ért már-e a játék, visszatérési értéke:
     * - "CHECKMATE" (matt), ha a király sakkban van és már nincs több lehetséges
     * lépése
     * - "STALEMATE" (patt), ha a király nincs sakkban de már nincs több lehetséges
     * lépése
     * - "NOT_FINISHED" (folyamatban), minden egyéb esetben
     * 
     * @param actualPosition
     * @param color
     * @return
     */
    public static String getEndOfGame(List<Piece> actualPosition, PieceColor color) {
        ArrayList<Position> everyMove = new ArrayList<>();

        Piece king = null;
        for (Piece piece : actualPosition) {
            if (piece.getColor() == color) {
                for (Position pos : piece.getEveryMove()) {
                    if (isMovePossible(actualPosition, piece, pos, color, true, true, null, null)) {
                        everyMove.add(pos);
                    }
                }

                if (piece.getType() == PieceType.KING) {
                    king = piece;
                }
            }
        }

        // ha nincs több szabályos lépés és sakkban van a király, akkor matt
        if (everyMove.isEmpty() && isKingInCheck(actualPosition, king)) {
            return "CHECKMATE";
        } else if (everyMove.isEmpty() && !isKingInCheck(actualPosition, king)) {
            // ha nincs több szabályos lépés, de nincs sakkban a király, akkor patt
            return "STALEMATE";
        }

        return "NOT_FINISHED";
    }
}
