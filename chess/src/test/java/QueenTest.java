import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import pieces.Queen;
import pieces.Rook;
import pieces.PieceColor;
import pieces.Position;

public class QueenTest {
    private Queen whiteQueen;
    private Queen blackQueen;

    @BeforeEach
    public void setUp() {
        whiteQueen = new Queen(PieceColor.WHITE, new Position("d", 1));
        blackQueen = new Queen(PieceColor.BLACK, new Position("d", 5));
    }

    @Test
    public void movementTest() {
        List<Position> whiteMovementList = whiteQueen.getEveryMove();
        List<Position> expectedList = new ArrayList<>(List.of(
                new Position("d", 2),
                new Position("d", 3),
                new Position("d", 4),
                new Position("d", 5),
                new Position("d", 6),
                new Position("d", 7),
                new Position("d", 8),
                new Position("a", 1),
                new Position("b", 1),
                new Position("c", 1),
                new Position("e", 1),
                new Position("f", 1),
                new Position("g", 1),
                new Position("h", 1),
                new Position("e", 2),
                new Position("f", 3),
                new Position("g", 4),
                new Position("h", 5),
                new Position("c", 2),
                new Position("b", 3),
                new Position("a", 4)
        ));

        assertTrue(equalLists(expectedList, whiteMovementList));

        List<Position> blackMovementList = blackQueen.getEveryMove();
        expectedList = new ArrayList<>(List.of(
                new Position("d", 8),
                new Position("d", 7),
                new Position("d", 6),
                new Position("d", 4),
                new Position("d", 3),
                new Position("d", 2),
                new Position("d", 1),
                new Position("a", 5),
                new Position("b", 5),
                new Position("c", 5),
                new Position("e", 5),
                new Position("f", 5),
                new Position("g", 5),
                new Position("h", 5),
                new Position("c", 6),
                new Position("b", 7),
                new Position("a", 8),
                new Position("e", 6),
                new Position("f", 7),
                new Position("g", 8),
                new Position("c", 4),
                new Position("b", 3),
                new Position("a", 2),
                new Position("e", 4),
                new Position("f", 3),
                new Position("g", 2),
                new Position("h", 1)
        ));

        assertTrue(equalLists(expectedList, blackMovementList));
    }

    public boolean equalLists(List<Position> expected, List<Position> got) {
        if (expected.size() != got.size()) {
            return false;
        }

        for (Position position : got) {
            if (!hasPosition(expected, position)) {
                return false;
            }
        }

        return true;
    }

    public boolean hasPosition(List<Position> list, Position position) {
        for (Position p : list) {
            if (p.equals(position)) {
                return true;
            }
        }

        return false;
    }
}
