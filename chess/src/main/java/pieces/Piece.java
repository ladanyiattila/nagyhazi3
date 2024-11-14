package pieces;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import pieces.*;

public abstract class Piece extends Component {
    protected PieceColor color; 
    transient protected Position position;
    protected String imageName;

    public List<Position> getEveryMove() { return null; }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position p) {
        position = p;
    }

    public String getImageName() {
        return imageName;
    }

    public PieceColor getColor() {
        return color;
    }

    public String toString() {
        return color + " " + position;
    }
}
