import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

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

        System.out.println(whiteMovementList.size());

        assertTrue(hasPosition(whiteMovementList, new Position("e", 3))); // 1 előre
        assertTrue(hasPosition(whiteMovementList, new Position("e", 4))); // 2 előre
        assertTrue(hasPosition(whiteMovementList, new Position("f", 3))); // jobbra átlósan 1
        assertTrue(hasPosition(whiteMovementList, new Position("d", 3))); // balra átlósan 1

        List<Position> blackMovementList = blackPawn.getEveryMove();

        assertTrue(hasPosition(blackMovementList, new Position("e", 6))); // 1 előre
        assertTrue(hasPosition(blackMovementList, new Position("e", 5))); // 2 előre
        assertTrue(hasPosition(blackMovementList, new Position("d", 6))); // jobbra átlósan 1
        assertTrue(hasPosition(blackMovementList, new Position("f", 6))); // balra átlósan 1
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
