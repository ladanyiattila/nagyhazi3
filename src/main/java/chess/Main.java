package chess;

import javax.swing.*;
import pieces.*;
import chess.Program;

public class Main {
    public static void main(String[] args) {
        Program.startProgram();

        Queen r = new Queen(PieceColor.WHITE, new Position("d", 1));
        r.getEveryMove();
    }
}