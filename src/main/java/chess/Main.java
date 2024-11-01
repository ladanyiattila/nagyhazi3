package chess;

import javax.swing.*;
import pieces.*;
import chess.Program;

public class Main {
    public static void main(String[] args) {
        Program.startProgram();

        Rook r = new Rook(PieceColor.WHITE, new Position("e", 4));
        r.getEveryMove();
    }
}