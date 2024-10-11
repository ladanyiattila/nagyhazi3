package pieces;

import java.awt.*;
import java.awt.image.BufferedImage;

import pieces.PieceColor;

public abstract class Piece extends Component {
    protected PieceColor color; 
    protected BufferedImage pieceImage;
}
