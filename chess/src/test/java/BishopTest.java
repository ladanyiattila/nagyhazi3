import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import pieces.Bishop;
import pieces.PieceColor;
import pieces.Position;

public class BishopTest {
    private Bishop whiteBishop;
    private Bishop blackBishop;

    @BeforeEach
    public void setUp() {
        whiteBishop = new Bishop(PieceColor.WHITE, new Position("c", 1));
        blackBishop = new Bishop(PieceColor.BLACK, new Position("d", 5));
    }

    @Test
    void movementTest() {
        System.out.println("asdasdads");

        List<Position> whiteMovementList = whiteBishop.getEveryMove();
        List<Position> expectedList = new ArrayList<>(List.of(
                new Position("d", 2),
                new Position("e", 3),
                new Position("f", 4),
                new Position("g", 5),
                new Position("h", 6),
                new Position("b", 2),
                new Position("a", 3)
        ));

        assertTrue(equalLists(expectedList, whiteMovementList));

        List<Position> blackMovementList = blackBishop.getEveryMove();
        expectedList = new ArrayList<>(List.of(
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
