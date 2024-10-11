package pieces;

import java.awt.*;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import pieces.Piece;
import pieces.PieceColor;

public class Pawn extends Piece {
    public Pawn(PieceColor c) {
        this.color = c;

        try {
            if (this.color.equals(PieceColor.BLACK)) {
                this.pieceImage = ImageIO.read(new File("resources/black_pawn.svg"));
            } else {
                this.pieceImage = ImageIO.read(new File("resources/white_pawn.svg"));
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(this.pieceImage, 0, 0, null);
    }
}
