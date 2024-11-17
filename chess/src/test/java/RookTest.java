import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import pieces.Rook;
import pieces.PieceColor;
import pieces.Position;

public class RookTest {
    private Rook whiteRook;
    private Rook blackRook;

    @BeforeEach
    public void setUp() {
        whiteRook = new Rook(PieceColor.WHITE, new Position("e", 4));
        blackRook = new Rook(PieceColor.BLACK, new Position("a", 8));
    }

    @Test
    void movementTest() {
        List<Position> whiteMovementList = whiteRook.getEveryMove();
        List<Position> expectedList = new ArrayList<>(List.of(
            new Position("e", 3),
            new Position("e", 2),
            new Position("e", 1),
            new Position("d", 4),
            new Position("c", 4),
            new Position("b", 4),
            new Position("a", 4),
            new Position("f", 4),
            new Position("g", 4),
            new Position("h", 4),
            new Position("e", 5),
            new Position("e", 6),
            new Position("e", 7),
            new Position("e", 8)
        ));

        assertTrue(equalLists(expectedList, whiteMovementList));

        List<Position> blackMovementList = blackRook.getEveryMove();
        expectedList = new ArrayList<>(List.of(
                new Position("a", 7),
                new Position("a", 6),
                new Position("a", 5),
                new Position("a", 4),
                new Position("a", 3),
                new Position("a", 2),
                new Position("a", 1),
                new Position("b", 8),
                new Position("c", 8),
                new Position("d", 8),
                new Position("e", 8),
                new Position("f", 8),
                new Position("g", 8),
                new Position("h", 8)
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
