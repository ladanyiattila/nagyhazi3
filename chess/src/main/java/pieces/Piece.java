package pieces;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import pieces.*;

public abstract class Piece extends Component {
    protected PieceColor color; 
    transient protected Position position;
    protected String imageName;
    protected PieceType type;
    protected boolean inStartingPosition;

    public List<Position> getEveryMove() { return null; }

    
    /** 
     * @return Position
     */
    public Position getPosition() {
        return position;
    }

    
    /** 
     * @param p
     */
    public void setPosition(Position p) {
        position = p;
    }

    
    /** 
     * @return String
     */
    public String getImageName() {
        return imageName;
    }

    public PieceColor getColor() {
        return color;
    }

    public String toString() {
        return color + " " + position;
    }

    public PieceType getType() {
        return type;
    }

    public void pieceHasMoved() {
        inStartingPosition = false;
    }

    public boolean hasPieceMoved() {
        return !inStartingPosition;
    }
}
