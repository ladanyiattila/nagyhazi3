import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import pieces.Pawn;
import pieces.PieceColor;
import pieces.Position;

class PawnTest {
    private Pawn whitePawn;
    private Pawn blackPawn;

    @BeforeEach
    public void setUp() {
        whitePawn = new Pawn(PieceColor.WHITE, new Position("e", 2));
        blackPawn = new Pawn(PieceColor.BLACK, new Position("e", 7));
    }

    @Test
    void movementTest() {
        List<Position> whiteMovementList = whitePawn.getEveryMove();
        List<Position> expectedList = new ArrayList<>(List.of(
            new Position("e", 3),
            new Position("e", 4),
            new Position("f", 3),
            new Position("d", 3)
        ));

        assertTrue(equalLists(expectedList, whiteMovementList));

        List<Position> blackMovementList = blackPawn.getEveryMove();
        expectedList = new ArrayList<>(List.of(
            new Position("e", 6),
            new Position("e", 5),
            new Position("d", 6),
            new Position("f", 6)
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
