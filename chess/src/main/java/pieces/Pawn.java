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

    @Override
    public void paint(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        
        String filename;

        if (this.color.equals(PieceColor.BLACK)) {
            filename = "black_pawn.png";
        } else {
            filename = "white_pawn.png";
        }

        this.pieceImage = t.getImage("chess/src/main/resources/" + filename);

        g.drawImage(this.pieceImage, 0, 0, null);
    }
}
