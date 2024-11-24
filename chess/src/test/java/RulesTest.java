import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import chess.Board;
import chess.PGN_Formatter;
import chess.Rules;
import pieces.Piece;
import pieces.PieceColor;
import pieces.Position;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RulesTest {
    @Test
    void isMovePossibleTest() {
        List<Piece> loaded = null;

        try {
            loaded = PGN_Formatter.readFromTextFile("test2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        Piece blackKnight = getPiece(loaded, new Position("f", 2));

        // ütheti a gyalogot
        assertTrue(Rules.isMovePossible(loaded, blackKnight, new Position("g", 4), PieceColor.BLACK, true, true, null,
                null));
        // fekete futót nem ütheti
        assertFalse(Rules.isMovePossible(loaded, blackKnight, new Position("d", 3), PieceColor.BLACK, true, true, null,
                null));


        // a betöltött játékban NEM a fehér következik, pusztán a lehetséges lépések vizsgálata történik
        Piece whiteKing = getPiece(loaded, new Position("d", 2));

        // c1-re léphet
        assertTrue(Rules.isMovePossible(loaded, whiteKing, new Position("c", 1), PieceColor.WHITE, true, true, null, null));
        // c2-re nem léphet
        assertFalse(Rules.isMovePossible(loaded, whiteKing, new Position("c", 2), PieceColor.WHITE, true, true, null,
                null));
        // c3-ra nem léphet
        assertFalse(Rules.isMovePossible(loaded, whiteKing, new Position("c", 3), PieceColor.WHITE, true, true, null,
                null));
    }

    @Test
    void isMovePossibleCastlingTest() {
        List<Piece> loaded = null;

        try {
            loaded = PGN_Formatter.readFromTextFile("test3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        Piece whiteKing = getPiece(loaded, new Position("e", 1));

        assertTrue(Rules.isMovePossible(loaded, whiteKing, new Position("g", 1), PieceColor.WHITE, true, true, null,
                null));

        loaded = null;

        try {
            loaded = PGN_Formatter.readFromTextFile("test4");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        Piece blackKing = getPiece(loaded, new Position("e", 8));

        assertTrue(Rules.isMovePossible(loaded, blackKing, new Position("c", 8), PieceColor.BLACK, true, true, null,
                null));
    }

    @Test
    void getEndOfGameTest() {
        List<Piece> loaded = null;

        try {
            loaded = PGN_Formatter.readFromTextFile("test2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        Rules.getEndOfGame(loaded, PieceColor.BLACK);
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
