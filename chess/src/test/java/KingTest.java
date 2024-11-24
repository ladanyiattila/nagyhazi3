import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import pieces.King;
import pieces.PieceColor;
import pieces.Position;
import pieces.Rook;

public class KingTest {
    private King whiteKing;
    private King blackKing;

    @BeforeEach
    public void setUp() {
        whiteKing = new King(PieceColor.WHITE, new Position("e", 1));
        blackKing = new King(PieceColor.BLACK, new Position("e", 5));
    }

    @Test
    void movementTest() {
        List<Position> whiteMovementList = whiteKing.getEveryMove();
        List<Position> expectedList = new ArrayList<>(List.of(
            new Position("d", 1),
            new Position("d", 2),
            new Position("e", 2),
            new Position("f", 2),
            new Position("f", 1),
            new Position("g", 1),
            new Position("c", 1)
        ));

        assertTrue(equalLists(expectedList, whiteMovementList));

        List<Position> blackMovementList = blackKing.getEveryMove();
        expectedList = new ArrayList<>(List.of(
                new Position("d", 6),
                new Position("e", 6),
                new Position("f", 6),
                new Position("d", 5),
                new Position("f", 5),
                new Position("d", 4),
                new Position("e", 4),
                new Position("f", 4)
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
