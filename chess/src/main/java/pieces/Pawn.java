package pieces;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import pieces.*;

public class Pawn extends Piece {
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
    }

    /*
     * Lehetséges lépések:
     * - 1 lépés előre
     * - 2 lépés előre
     * - 1 lépés átlósan jobbra
     * - 1 lépés átlósan balra
     */
    @Override
    public List<Position> getEveryMove() {
        List<Position> positions = new ArrayList<>();

        Position move = position.forward(color);

        if (move != null) {
            positions.add(move);
        }

        // kezdő sorból 2 lépés is lehetséges
        if ((color == PieceColor.WHITE && position.row == 2) 
        || (color == PieceColor.BLACK && position.row == 7)) {
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

    @Override
    public void paint(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        
        String filename;

        if (this.color.equals(PieceColor.BLACK)) {
            filename = "black_pawn.png";
        } else {
            filename = "white_pawn.png";
        }

        g.drawImage(t.getImage("chess/src/main/resources/" + filename), 0, 0, null);
    }
}
