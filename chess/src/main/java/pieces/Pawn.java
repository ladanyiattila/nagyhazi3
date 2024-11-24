package pieces;

import java.util.ArrayList;
import java.util.List;

/**
 * A gyalog bábut megvalósító osztály.
 */
public class Pawn extends Piece {
    private boolean movedTwoAtStart;

    /**
     * Pawn konstruktor.
     * Létrehoz egy gyalogot az adott színnel és pozícióval.
     * 
     * @param c
     * @param p
     */
    public Pawn(PieceColor c, Position p) {
        this.color = c;
        this.position = p;

        if (c == PieceColor.WHITE) {
            this.imageName = "white_pawn.png";
        } else {
            this.imageName = "black_pawn.png";
        }

        type = PieceType.PAWN;
        inStartingPosition = true;
        movedTwoAtStart = false;
    }

    /**
     * Lehetséges lépések:
     * - 1 lépés előre
     * - 2 lépés előre
     * - 1 lépés átlósan jobbra
     * - 1 lépés átlósan balra
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
            positions.add(move);
        }

        // kezdő sorból 2 lépés is lehetséges
        if ((color == PieceColor.WHITE && position.getRow() == 2) 
        || (color == PieceColor.BLACK && position.getRow() == 7)) {
            positions.add(position.forward(color).forward(color));
        }

        move = position.rightDiagonal(color, Direction.FORWARD);

        if (move != null) {
            positions.add(move);
        }

        move = position.leftDiagonal(color, Direction.FORWARD);

        if (move != null) {
            positions.add(move);
        }

        return positions;
    }

    
    /**
     * movedTwoAtStart getter függvénye: en passant lépés vizsgálatánál
     * használatos
     * 
     * @return boolean
     */
    public boolean getMovedTwoAtStart() {
        return movedTwoAtStart;
    }

    /**
     * movedTwoAtStart "setter" függvénye: ha kettőt lép a gyalog
     * a kezdő pozícióból, akkor movedTwoAtStart igaz lesz;
     * en passant lépés vizsgálatánál használatos
     */
    public void setTwoMove() {
        movedTwoAtStart = true;
    }
}
