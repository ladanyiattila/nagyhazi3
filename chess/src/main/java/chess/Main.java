package chess;

import javax.swing.*;
import java.awt.*;
import pieces.*;

public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame();

        Pawn p1 = new Pawn(PieceColor.BLACK);

        Panel pan = new Panel();
        pan.add(p1);
        //p1.paint(pan);

        f.setSize(500, 500);
        f.setVisible(true);
    }
}