package pieces;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;


import java.io.*;

import pieces.Piece;
import pieces.PieceColor;

public class Pawn extends Piece {
    public Pawn(PieceColor c) {
        this.color = c;
    }

    public void paint(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        
        if (this.color.equals(PieceColor.BLACK)) {
            this.pieceImage = t.getImage("chess/src/main/resources/black_pawn.png");
        } else {
            // this.pieceImage = ImageIO.read(new File("resources/white_pawn.svg"));
        }

        g.drawImage(this.pieceImage, 0, 0, null);
    }
}
