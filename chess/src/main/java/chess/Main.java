package chess;

import javax.swing.*;
import pieces.*;

public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame();

        Pawn p1 = new Pawn(PieceColor.BLACK);
        
        f.add(p1);

        f.setSize(500, 500);
        f.setVisible(true);
    }
}