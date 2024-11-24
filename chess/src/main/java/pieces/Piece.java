package pieces;

import java.util.List;

/**
 * Absztrakt alaposztály, a különböző típusú bábuk ebből vannak
 * leszármaztatva.
 */
public abstract class Piece {
    protected PieceColor color; 
    protected Position position;
    protected String imageName;
    protected PieceType type;
    protected boolean inStartingPosition;

    /**
     * A Piece alaposztályon nem értelmezett ennek a függvénynek a
     * használata, ezt felüldefiniálják a leszármazott osztályok.
     * 
     * @return
     */
    public List<Position> getEveryMove() { 
        return null; 
    }

    
    /** 
     * position változó getter függvénye
     * 
     * @return Position
     */
    public Position getPosition() {
        return position;
    }

    
    /** 
     * position változó setter függvénye
     * 
     * @param p
     */
    public void setPosition(Position p) {
        position = p;
    }

    
    /** 
     * imageName változó getter függvénye
     * 
     * @return String
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * color változó getter függvénye
     * 
     * @return
     */
    public PieceColor getColor() {
        return color;
    }

    /**
     * toString() hibakereséshez
     */
    public String toString() {
        return color + " " + position;
    }

    /**
     * type változó getter függvénye
     * 
     * @return
     */
    public PieceType getType() {
        return type;
    }

    /**
     * inStartingPosition "setter" függvénye
     */
    public void pieceHasMoved() {
        inStartingPosition = false;
    }

    /**
     * Igaz, ha a bábu már nem a kezdő pozíciójában van
     * 
     * @return
     */
    public boolean hasPieceMoved() {
        return !inStartingPosition;
    }
}
