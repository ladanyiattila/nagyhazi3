import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import chess.Board;
import chess.PGN_Formatter;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.PieceColor;
import pieces.Position;
import pieces.Queen;
import pieces.Rook;

public class PGN_FormatterTest {
    @Test
    void getMovesListedTest() {
        String s = null;

        try {
            s = PGN_Formatter.getMovesListed("test1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals("1.e4 e5\n2.Bc4 Nf6\n", s);
    }

    @Test
    void getMovesListedTestLong() {
        String s = null;

        try {
            s = PGN_Formatter.getMovesListed("test2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        String expected = "1.e4 e5\n2.Nf3 Nc6\n3.Bb5 a6\n4.Ba4 Nf6\n5.O-O Be7\n6.Re1 b5\n7.Bb3 d6\n8.c3 O-O\n9.h3 Nb8\n10.d4 Nbd7\n11.c4 c6\n12.cxb5 axb5\n13.Nc3 Bb7\n14.Bg5 b4\n15.Nb1 h6\n16.Bh4 c5\n17.dxe5 Nxe4\n18.Bxe7 Qxe7\n19.exd6 Qf6\n20.Nbd2 Nxd6\n21.Nc4 Nxc4\n22.Bxc4 Nb6\n23.Ne5 Rae8\n24.Bxf7+ Rxf7\n25.Nxf7 Rxe1+\n26.Qxe1 Kxf7\n27.Qe3 Qg5\n28.Qxg5 hxg5\n29.b3 Ke6\n30.a3 Kd6\n31.axb4 cxb4\n32.Ra5 Nd5\n33.f3 Bc8\n34.Kf2 Bf5\n35.Ra7 g6\n36.Ra6+ Kc5\n37.Ke1 Nf4\n38.g3 Nxh3\n39.Kd2 Kb5\n40.Rd6 Kc5\n41.Ra6 Nf2\n42.g4 Bd3\n43.Re6 ";
        assertEquals(expected, s);
    }

    @Test
    void getMoveFormattedTest() {
        List<Piece> actualPosition = Board.getStartingPosition();

        Piece eWhitePawn = getPiece(actualPosition, new Position("e", 2));
        String move = PGN_Formatter.getMoveFormatted(eWhitePawn, new Position("e", 4), false, actualPosition);
        assertEquals("e4", move);

        Piece eBlackKnight = getPiece(actualPosition, new Position("g", 8));
        move = PGN_Formatter.getMoveFormatted(eBlackKnight, new Position("f", 6), false, actualPosition);
        assertEquals("Nf6", move);

        // ütés gyaloggal
        Piece dBlackPawn = getPiece(actualPosition, new Position("d", 7));
        eWhitePawn.setPosition(new Position("e", 4));
        dBlackPawn.setPosition(new Position("d", 5));

        move = PGN_Formatter.getMoveFormatted(eWhitePawn, new Position("d", 5), true, actualPosition);
        assertEquals("exd5", move);

        // ütés tiszttel: a lépés az szabálytalan, a tesztelés célja pusztán a helyes
        // PGN formátum ellenőrzése
        Piece blackQueen = getPiece(actualPosition, new Position("d", 8));
        move = PGN_Formatter.getMoveFormatted(blackQueen, new Position("h", 2), true, actualPosition);
        assertEquals("Qxh2", move);
    }

    @Test 
    void getNumberOfMovesTest() {
        int num = -1;

        try {
            num = PGN_Formatter.getNumberOfMoves("test1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        // 2 lépés történt, de a 3. következik
        assertEquals(3, num);
    }

    @Test
    void getNumberOfMovesTestLong() {
        int num = -1;

        try {
            num = PGN_Formatter.getNumberOfMoves("test2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(44, num);
    }
    
    @Test
    void getNextMoveColorTest() {
        PieceColor color = null;

        try {
            color = PGN_Formatter.getNextMoveColor("test1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(PieceColor.WHITE, color);
    }
    
    @Test
    void readFromTextFileTest() {
        List<Piece> loaded = null;

        try {
            loaded = PGN_Formatter.readFromTextFile("test2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        List<Piece> expected = new ArrayList<>(List.of(
            new Pawn(PieceColor.BLACK, new Position("g", 6)),
            new Pawn(PieceColor.BLACK, new Position("g", 5)),
            new Pawn(PieceColor.BLACK, new Position("b", 4)),
            new Knight(PieceColor.BLACK, new Position("f", 2)),
            new Bishop(PieceColor.BLACK, new Position("d", 3)),
            new King(PieceColor.BLACK, new Position("c", 5)),
            new Pawn(PieceColor.WHITE, new Position("b", 3)),
            new Pawn(PieceColor.WHITE, new Position("f", 3)),
            new Pawn(PieceColor.WHITE, new Position("g", 4)),
            new Rook(PieceColor.WHITE, new Position("e", 6)),
            new King(PieceColor.WHITE, new Position("d", 2))
        ));

        assertTrue(areTwoPositionsEqual(expected, loaded));
    }

    @Test
    void readFromTextFilePromotionTest() {
        List<Piece> loaded = null;

        try {
            loaded = PGN_Formatter.readFromTextFile("test5");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        List<Piece> expected = new ArrayList<>(List.of(
            new Pawn(PieceColor.BLACK, new Position("b", 7)),
            new Pawn(PieceColor.BLACK, new Position("c", 7)),
            new Pawn(PieceColor.BLACK, new Position("d", 7)),
            new Pawn(PieceColor.BLACK, new Position("e", 7)),
            new Pawn(PieceColor.BLACK, new Position("f", 7)),
            new Pawn(PieceColor.BLACK, new Position("g", 7)),
            new Bishop(PieceColor.BLACK, new Position("c", 8)),
            new Bishop(PieceColor.BLACK, new Position("f", 8)),
            new Knight(PieceColor.BLACK, new Position("g", 8)),
            new Rook(PieceColor.BLACK, new Position("h", 7)),
            new Queen(PieceColor.BLACK, new Position("d", 8)),
            new King(PieceColor.BLACK, new Position("d", 8)),

            new Pawn(PieceColor.WHITE, new Position("b", 2)),
            new Pawn(PieceColor.WHITE, new Position("c", 2)),
            new Pawn(PieceColor.WHITE, new Position("d", 4)),
            new Pawn(PieceColor.WHITE, new Position("e", 2)),
            new Pawn(PieceColor.WHITE, new Position("f", 2)),
            new Pawn(PieceColor.WHITE, new Position("g", 2)),
            new Pawn(PieceColor.WHITE, new Position("h", 2)),
            new Rook(PieceColor.WHITE, new Position("a", 1)),
            new Rook(PieceColor.WHITE, new Position("h", 1)),
            new Knight(PieceColor.WHITE, new Position("b", 1)),
            new Knight(PieceColor.WHITE, new Position("g", 1)),
            new Bishop(PieceColor.WHITE, new Position("c", 1)),
            new Bishop(PieceColor.WHITE, new Position("f", 1)),
            new King(PieceColor.WHITE, new Position("e", 1)),

            new Rook(PieceColor.WHITE, new Position("b", 8))
        ));

        assertTrue(areTwoPositionsEqual(expected, loaded));
    }
    
    private static boolean areTwoPositionsEqual(List<Piece> first, List<Piece> second) {
        if (first.size() != second.size()) {
            return false;
        }

        for (Piece piece : first) {
            Piece getFromSecond = getPiece(second, piece.getPosition());

            if (getFromSecond == null) {
                return false;
            }
        }


        return true;
    }

    private static Piece getPiece(List<Piece> actualPosition, Position pos) {
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(pos)) {
                return piece;
            }
        }

        return null;
    }
}
