package pieces;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import pieces.*;

public abstract class Piece extends Component {
    protected PieceColor color; 
    protected Position position;

    protected List<Position> getEveryMove() { return null; }
}
