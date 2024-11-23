import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import pieces.Knight;
import pieces.PieceColor;
import pieces.Position;
import pieces.Rook;

public class KnightTest {
    private Knight whiteKnight;
    private Knight blackKnight;

    @BeforeEach
    public void setUp() {
        whiteKnight = new Knight(PieceColor.WHITE, new Position("b", 1));
        blackKnight = new Knight(PieceColor.BLACK, new Position("f", 6));
    }

    @Test
    void movementTest() {
        List<Position> whiteMovementList = whiteKnight.getEveryMove();
        List<Position> expectedList = new ArrayList<>(List.of(
            new Position("a", 3),
            new Position("c", 3),
            new Position("d", 2)
        ));

        assertTrue(equalLists(expectedList, whiteMovementList));

        List<Position> blackMovementList = blackKnight.getEveryMove();
                expectedList = new ArrayList<>(List.of(
                new Position("g", 8),
                new Position("h", 7),
                new Position("h", 5),
                new Position("g", 4),
                new Position("e", 4),
                new Position("d", 5),
                new Position("d", 7),
                new Position("e", 8)
        ));

        assertTrue(equalLists(expectedList, blackMovementList));
    }

    
    /** 
     * @param expected
     * @param got
     * @return boolean
     */
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

    
    /** 
     * @param list
     * @param position
     * @return boolean
     */
    public boolean hasPosition(List<Position> list, Position position) {
        for (Position p : list) {
            if (p.equals(position)) {
                return true;
            }
        }

        return false;
    }
}
