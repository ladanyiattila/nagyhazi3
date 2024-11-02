package pieces;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import pieces.*;

public abstract class Piece extends Component {
    protected PieceColor color; 
    protected Position position;
    protected String imageName;

    public List<Position> getEveryMove() { return null; }

    public Position getPosition() {
        return position;
    }

    public String getImageName() {
        return imageName;
    }
}
