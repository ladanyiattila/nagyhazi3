package chess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

import pieces.*;

/**
 * Játékállások beolvasásáért és lépések PGN formátummá alakításáért felelős
 * osztály
 */
public class PGN_Formatter {
    /**
     * Privát konstruktor elrejteni a publikusat
     */
    private PGN_Formatter() {
    }

    private static List<Piece> loadedPosition;

    /**
     * Visszatér a betöltött pozícióban szereplő paraméterként kapott adott helyen,
     * adott színű
     * és adott típusú bábuval: ha nincs ilyen, akkor null-al tér vissza
     * 
     * @param color
     * @param type
     * @param pos
     * @return Piece
     */
    private static Piece getPiece(PieceColor color, PieceType type, Position pos) {
        for (Piece piece : loadedPosition) {
            if (piece.getColor() == color && piece.getType() == type && piece.getPosition().equals(pos)) {
                return piece;
            }
        }

        return null;
    }

    /**
     * A paraméterként kapott betűt PieceType-á alakítja és visszatér vele
     * 
     * @param letter
     * @return PieceType
     */
    private static PieceType getPieceTypeByLetter(String letter) {
        if (letter.equals(letter.toLowerCase())) {
            return PieceType.PAWN;
        }

        // szótár inicializálása statikus inicializáló blokkal
        Map<String, PieceType> map = new HashMap<>() {
            {
                put("Q", PieceType.QUEEN);
                put("K", PieceType.KING);
                put("B", PieceType.BISHOP);
                put("R", PieceType.ROOK);
                put("N", PieceType.KNIGHT);
            }
        };

        return map.get(letter);
    }

    /**
     * A paraméterként kapott típusnak visszatér a kezdőbetűjével
     * 
     * @param type
     * @return String
     */
    private static String getLetterByPieceType(PieceType type) {
        if (type == PieceType.PAWN) {
            return "";
        }

        // inicializálás statikus blokkal
        Map<PieceType, String> map = new HashMap<>() {
            {
                put(PieceType.QUEEN, "Q");
                put(PieceType.KING, "K");
                put(PieceType.BISHOP, "B");
                put(PieceType.ROOK, "R");
                put(PieceType.KNIGHT, "N");
            }
        };

        return map.get(type);
    }

    /**
     * Visszatér a paraméterként kapott színű és típusú (javarészt tisztek)
     * listájával, akik a betöltött állásban helyezkednek el.
     * 
     * @param color
     * @param type
     * @return
     */
    private static List<Piece> getOfficers(PieceColor color, PieceType type) {
        List<Piece> list = new ArrayList<>();

        for (Piece piece : loadedPosition) {
            if (piece.getColor() == color && piece.getType() == type) {
                list.add(piece);
            }
        }

        return list;
    }

    /**
     * Visszatér a paraméterként kapott bábu összes lehetséges lépéseinek
     * listájával.
     * 
     * @param piece
     * @return
     */
    private static List<Position> getPossibleMoves(Piece piece) {
        List<Position> positions = new ArrayList<>();

        for (Position pos : piece.getEveryMove()) {
            if (Rules.isMovePossible(loadedPosition, piece, pos, piece.getColor(), true, true, null, null)) {
                positions.add(pos);
            }
        }

        return positions;
    }

    /**
     * Igaz, ha a paraméterként kapott lista rendelkezik a szintén paraméterként
     * kapott lépéssel.
     * Egyébként hamis.
     * 
     * @param list
     * @param move
     * @return
     */
    private static boolean containsMove(List<Position> list, Position move) {
        for (Position pos : list) {
            if (pos.equals(move)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Törli a betöltött állásból a paraméterként kapott mezőn elhelyezkedű bábut.
     * Mivel egy mezőn csak egy bábu helyezkedhet el, így fontos, hogy pl. ütésnél
     * először ezt a függvényt kell meghívni, majd mozgatni azt a bábut, aki üt,
     * különben mindkét bábu törlődik.
     * 
     * @param pos
     */
    private static void deletePiece(Position pos) {
        ListIterator<Piece> iter = loadedPosition.listIterator();

        while (iter.hasNext()) {
            Piece current = iter.next();

            if (current.getPosition().equals(pos)) {
                iter.remove();
            }
        }
    }

    /**
     * Beolvassa a paraméterként kapott fájlnévű .txt fájlból a lépéseket és
     * formázva őket
     * visszatér egy String-gel, amely ezeket a lépéseket tartalmazza. Ha nem
     * létezik ilyen
     * nevű szövegfájl, akkor FileNotFoundException-t dob.
     * 
     * @param textFile
     * @return
     * @throws FileNotFoundException
     */
    public static String getMovesListed(String textFile) throws FileNotFoundException {
        File readFile = null;
        String ret = "";

        if (!textFile.contains(".txt")) {
            readFile = new File("saved_games/" + textFile + ".txt");
        } else {
            readFile = new File("saved_games/" + textFile);
        }

        Scanner scanner = new Scanner(readFile);

        String[] moves = scanner.nextLine().split(" ");

        scanner.close();

        int i = 1;

        for (String move : moves) {
            if (i % 2 == 1) {
                ret += move + " ";
            } else {
                ret += move + "\n";
            }

            i++;
        }

        return ret;
    }

    /**
     * Igaz, ha a paraméterként kapott mezőn van bábu a betöltött állásban.
     * Egyébként hamis.
     * 
     * @param pos
     * @return
     */
    private static boolean isThereAPiece(Position pos) {
        for (Piece piece : loadedPosition) {
            if (piece.getPosition().equals(pos)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Hozzáad a jelenlegi álláshoz egy úgy tisztet a paraméterként színnel és adott
     * mezőre.
     * A tiszt "típusát" szintén paraméterként kapja meg.
     * 
     * @param typeString
     * @param nextColor
     * @param column
     * @param row
     */
    private static void addOfficer(String typeString, PieceColor nextColor, String column, int row) {
        switch (getPieceTypeByLetter(typeString)) {
            case QUEEN:
                loadedPosition.add(new Queen(nextColor, new Position(column, row)));
                break;

            case ROOK:
                loadedPosition.add(new Rook(nextColor, new Position(column, row)));
                break;

            case KNIGHT:
                loadedPosition.add(new Knight(nextColor, new Position(column, row)));
                break;

            case BISHOP:
                loadedPosition.add(new Bishop(nextColor, new Position(column, row)));
                break;

            default:
                break;
        }
    }

    /**
     * Átalakulás lépés beolvasása esetén módosítja a betöltött állást a
     * szabályoknak megfelelően.
     * 
     * @param actualMove
     * @param nextColor
     */
    private static void promotion(String actualMove, PieceColor nextColor) {
        // gyalog törlése az eggyel előtti sorból

        String[] splitMove = actualMove.split("\\=");
        actualMove = splitMove[0];
        String typeString = splitMove[1];

        // axb8=Q pl.
        if (actualMove.contains("x")) {
            String pawnColumn = Character.toString(actualMove.charAt(0));

            String column = Character.toString(actualMove.charAt(2));
            int row = Character.getNumericValue(actualMove.charAt(3));

            // ütött bábu törlése
            ListIterator<Piece> iterator = loadedPosition.listIterator();

            while (iterator.hasNext()) {
                Piece current = iterator.next();

                if (current.getPosition().equals(new Position(column, row))) {
                    iterator.remove();
                }
            }

            // gyalog törlése
            iterator = loadedPosition.listIterator();
            Position pawnPos = new Position(pawnColumn, nextColor == PieceColor.WHITE ? row - 1 : row + 1);

            while (iterator.hasNext()) {
                Piece current = iterator.next();

                if (current.getPosition().equals(pawnPos) && current.getType() == PieceType.PAWN) {
                    iterator.remove();
                }
            }

            addOfficer(typeString, nextColor, column, row);
        } else {
            String column = Character.toString(actualMove.charAt(0));
            int row = Character.getNumericValue(actualMove.charAt(1));

            // gyalog törlése
            ListIterator<Piece> iterator = loadedPosition.listIterator();
            Position pawnPos = new Position(column, nextColor == PieceColor.WHITE ? row - 1 : row + 1);

            while (iterator.hasNext()) {
                Piece current = iterator.next();

                if (current.getPosition().equals(pawnPos) && current.getType() == PieceType.PAWN) {
                    iterator.remove();
                }
            }

            addOfficer(typeString, nextColor, column, row);
        }
    }

    /**
     * Megvalósítja a rövidsáncolás lépését.
     * 
     * @param nextColor
     */
    private static void shortCastling(PieceColor nextColor) {
        int row = 8;

        if (nextColor == PieceColor.WHITE) {
            row = 1;
        }

        Piece king = getPiece(nextColor, PieceType.KING, new Position("e", row));
        Piece rook = getPiece(nextColor, PieceType.ROOK, new Position("h", row));

        king.setPosition(new Position("g", row));
        king.pieceHasMoved();
        rook.setPosition(new Position("f", row));
        rook.pieceHasMoved();
    }

    /**
     * Megvalósítja a hosszúsáncolás lépését
     * 
     * @param nextColor
     */
    private static void longCastling(PieceColor nextColor) {
        int row = 8;

        if (nextColor == PieceColor.WHITE) {
            row = 1;
        }

        Piece king = getPiece(nextColor, PieceType.KING, new Position("e", row));
        Piece rook = getPiece(nextColor, PieceType.ROOK, new Position("a", row));

        king.setPosition(new Position("c", row));
        king.pieceHasMoved();
        rook.setPosition(new Position("d", row));
        rook.pieceHasMoved();
    }

    /**
     * A beolvasott lépésben szereplő ütést valósítja meg és módosítja
     * ennek megfelelően az állást.
     * 
     * @param actualMove
     * @param nextColor
     */
    private static void pieceTakenMove(String actualMove, PieceColor nextColor) {
        PieceType movePieceType = getPieceTypeByLetter(Character.toString(actualMove.charAt(0)));

        if (movePieceType == PieceType.PAWN) {
            String column = Character.toString(actualMove.charAt(2));
            int row = Character.getNumericValue(actualMove.charAt(3));

            Piece movePiece = getPiece(nextColor, movePieceType,
                    new Position(Character.toString(actualMove.charAt(0)),
                            nextColor == PieceColor.WHITE ? row - 1 : row + 1));

            if (isThereAPiece(new Position(column, row))) {
                deletePiece(new Position(column, row));
            } else {
                deletePiece(new Position(column, row + (nextColor == PieceColor.WHITE ? -1 : 1)));
            }

            movePiece.setPosition(new Position(column, row));
            movePiece.pieceHasMoved();
        } else {
            List<Piece> officers = getOfficers(nextColor, movePieceType);

            // egyértelmű a lépés, hogy melyik tiszt lép oda
            if (actualMove.length() == 4) {
                String column = Character.toString(actualMove.charAt(2));
                int row = Character.getNumericValue(actualMove.charAt(3));

                if (officers.size() == 1) {
                    deletePiece(new Position(column, row));

                    officers.get(0).setPosition(new Position(column, row));
                    officers.get(0).pieceHasMoved();
                } else {
                    deletePiece(new Position(column, row));

                    if (containsMove(getPossibleMoves(officers.get(0)), new Position(column, row))) {
                        officers.get(0).setPosition(new Position(column, row));
                        officers.get(0).pieceHasMoved();
                    } else {
                        officers.get(1).setPosition(new Position(column, row));
                        officers.get(1).pieceHasMoved();
                    }
                }
            } else {
                Character specified = actualMove.charAt(1);
                String column = Character.toString(actualMove.charAt(3));
                int row = Character.getNumericValue(actualMove.charAt(4));

                deletePiece(new Position(column, row));

                // sor van megadva
                if (Character.isDigit(specified)) {
                    if (officers.get(0).getPosition().getRow() == Character.getNumericValue(specified)) {
                        officers.get(0).setPosition(new Position(column, row));
                        officers.get(0).pieceHasMoved();
                    } else {
                        officers.get(1).setPosition(new Position(column, row));
                        officers.get(1).pieceHasMoved();
                    }
                } else { // oszlop esetén
                    if (officers.get(0).getPosition().columnToString().equals(Character.toString(specified))) {
                        officers.get(0).setPosition(new Position(column, row));
                        officers.get(0).pieceHasMoved();
                    } else {
                        officers.get(1).setPosition(new Position(column, row));
                        officers.get(1).pieceHasMoved();
                    }
                }
            }
        }
    }

    /**
     * A beolvasott lépést, melyben nem szerepel ütés, valósítja meg
     * és módosítja az állást.
     * 
     * @param actualMove
     * @param nextColor
     */
    private static void regularMove(String actualMove, PieceColor nextColor) {
        PieceType movePieceType = getPieceTypeByLetter(Character.toString(actualMove.charAt(0)));

        if (movePieceType == PieceType.PAWN) {
            String column = Character.toString(actualMove.charAt(0));
            int row = Character.getNumericValue(actualMove.charAt(1));

            Piece movePiece = getPiece(nextColor, movePieceType,
                    new Position(column, nextColor == PieceColor.WHITE ? row - 1 : row + 1));

            // két lépés esetén
            if (movePiece == null) {
                movePiece = getPiece(nextColor, movePieceType,
                        new Position(column, nextColor == PieceColor.WHITE ? row - 2 : row + 2));
            }

            movePiece.setPosition(new Position(column, row));
            movePiece.pieceHasMoved();
        } else {
            List<Piece> officers = getOfficers(nextColor, movePieceType);

            // egyértelmű a lépés, hogy melyik tiszt lép oda
            if (actualMove.length() == 3) {
                String column = Character.toString(actualMove.charAt(1));
                int row = Character.getNumericValue(actualMove.charAt(2));

                if (officers.size() == 1) {
                    officers.get(0).setPosition(new Position(column, row));
                    officers.get(0).pieceHasMoved();
                } else {
                    if (containsMove(getPossibleMoves(officers.get(0)), new Position(column, row))) {
                        officers.get(0).setPosition(new Position(column, row));
                        officers.get(0).pieceHasMoved();
                    } else {
                        officers.get(1).setPosition(new Position(column, row));
                        officers.get(1).pieceHasMoved();
                    }
                }
            } else {
                Character specified = actualMove.charAt(1);
                String column = Character.toString(actualMove.charAt(2));
                int row = Character.getNumericValue(actualMove.charAt(3));

                // sor van megadva
                if (Character.isDigit(specified)) {
                    if (officers.get(0).getPosition().getRow() == Character.getNumericValue(specified)) {
                        officers.get(0).setPosition(new Position(column, row));
                        officers.get(0).pieceHasMoved();
                    } else {
                        officers.get(1).setPosition(new Position(column, row));
                        officers.get(1).pieceHasMoved();
                    }
                } else { // oszlop esetén
                    if (officers.get(0).getPosition().columnToString().equals(Character.toString(specified))) {
                        officers.get(0).setPosition(new Position(column, row));
                        officers.get(0).pieceHasMoved();
                    } else {
                        officers.get(1).setPosition(new Position(column, row));
                        officers.get(1).pieceHasMoved();
                    }
                }
            }
        }
    }

    /**
     * Beolvassa a paraméterként kapott szövegfájl tartalmát és azokat egy
     * játékállássá (azaz bábuk listájává) alakítja. Ha nem létezik ilyen fájl,
     * akkor
     * FileNotFoundException-t dob.
     * 
     * @param textFile
     * @return
     * @throws FileNotFoundException
     */
    public static List<Piece> readFromTextFile(String textFile) throws FileNotFoundException {
        File readFile = null;

        if (!textFile.contains(".txt")) {
            readFile = new File("saved_games/" + textFile + ".txt");
        } else {
            readFile = new File("saved_games/" + textFile);
        }

        if (!readFile.exists()) {
            return null;
        }

        loadedPosition = Board.getStartingPosition();
        Scanner scanner = new Scanner(readFile);
        String[] moves = scanner.nextLine().split(" ");

        scanner.close();

        for (String move : moves) {
            PieceColor nextColor = PieceColor.BLACK;
            String actualMove = move;

            if (move.contains(".")) {
                nextColor = PieceColor.WHITE;
                String[] splitMove = move.split("\\.");
                actualMove = splitMove[1];
            }

            if (actualMove.contains("+")) {
                actualMove = actualMove.split("\\+")[0];
            }

            // átalakulás
            if (actualMove.contains("=")) {
                promotion(actualMove, nextColor);
                continue;
            }

            // rövidsánc
            if (actualMove.equals("O-O")) {
                shortCastling(nextColor);
                continue;
            }

            // hosszúsánc
            if (actualMove.equals("O-O-O")) {
                longCastling(nextColor);
                continue;
            }

            // történt-e ütés
            if (actualMove.contains("x")) {
                pieceTakenMove(actualMove, nextColor);
            } else {
                regularMove(actualMove, nextColor);
            }
        }

        return loadedPosition;
    }

    /**
     * Igaz, ha a paraméterként kapott bábu lehetséges lépései között szerepel az
     * adott állásban
     * a szintén paraméterként kapott lépés. Egyébként hamis.
     * 
     * @param piece
     * @param move
     * @param actualPosition
     * @return
     */
    private static boolean isInPossibleMoves(Piece piece, Position move, List<Piece> actualPosition) {
        List<Position> possibleMoves = piece.getEveryMove();
        ListIterator<Position> iter = possibleMoves.listIterator();

        while (iter.hasNext()) {
            Position current = iter.next();

            if (!Rules.isMovePossible(actualPosition, piece, current, piece.getColor(), true, true, null, null)) {
                iter.remove();
            }
        }

        for (Position pos : possibleMoves) {
            if (pos.equals(move)) {
                return true;
            }
        }

        return false;
    }

    /**
     * A paraméterként kapott lépést (bábu és mező) alakítja PGN formátum szerinti
     * lépésre és visszatér a lépést tartalmazó String-gel.
     * 
     * @param piece
     * @param movedTo
     * @param wasPieceTaken
     * @param actualPosition
     * @return
     */
    public static String getMoveFormatted(Piece piece, Position movedTo, boolean wasPieceTaken,
            List<Piece> actualPosition) {
        loadedPosition = actualPosition;
        String move = "";

        if (piece.getType() == PieceType.KING && (piece.getPosition().equals(new Position("e", 1))
                || piece.getPosition().equals(new Position("e", 8)))) {
            if (movedTo.equals(new Position("g", 1)) || movedTo.equals(new Position("g", 8))) {
                return "O-O";
            } else if (movedTo.equals(new Position("c", 1)) || movedTo.equals(new Position("c", 8))) {
                return "O-O-O";
            }
        }

        if (piece.getType() == PieceType.PAWN) {
            if (wasPieceTaken) {
                move += piece.getPosition().columnToString() + "x" + movedTo;
            } else {
                move += movedTo;
            }
        } else {
            List<Piece> officers = getOfficers(piece.getColor(), piece.getType());

            // első betű a tiszt típusa
            move += getLetterByPieceType(piece.getType());

            if (officers.size() == 2 && isInPossibleMoves(officers.get(0), movedTo, actualPosition)
                    && isInPossibleMoves(officers.get(1), movedTo, actualPosition)) {
                // ha mindkét tiszt odaléphet, akkor nem egyértelmű
                if (officers.get(0).getPosition().getRow() == officers.get(1).getPosition().getRow()) {
                    move += piece.getPosition().columnToString();
                } else if (officers.get(0).getPosition().getColumn() == officers.get(1).getPosition().getColumn()) {
                    move += piece.getPosition().getRow();
                } else {
                    move += piece.getPosition().columnToString();
                }
            }

            if (wasPieceTaken) {
                move += "x";
            }

            move += movedTo.toString();
        }

        return move;
    }

    /**
     * Elmenti a paraméterként kapott listát egy .txt fájlba.
     * A fájl neve a mentés időpontja "yyyy-MM-dd HH-mm-ss" formátumban.
     * Esetleges fájlkezelési hiba esetén IOException-t dob.
     * 
     * @param moveList
     * @throws IOException
     */
    public static void saveGame(String moveList) throws IOException {
        LocalDateTime date = LocalDateTime.now();

        BufferedWriter file = new BufferedWriter(new FileWriter(
                "saved_games/" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")) + ".txt"));

        for (char c : moveList.toCharArray()) {
            if (c != '\n') {
                file.write(c);
            } else {
                file.write(" ");
            }
        }

        file.close();
    }

    /**
     * Visszatér a paraméterként kapott fájlnevű fájl lépéseinek számával.
     * Egy lépést a fehér lépése (n.lépés) határoz meg, ahol n a lépés sorszáma.
     * Fájl megnyitása esetén fellépő hiba esetén FileNotFoundException-t dob.
     * 
     * @param textFile
     * @return
     * @throws FileNotFoundException
     */
    public static int getNumberOfMoves(String textFile) throws FileNotFoundException {
        File readFile = null;

        if (!textFile.contains(".txt")) {
            readFile = new File("saved_games/" + textFile + ".txt");
        } else {
            readFile = new File("saved_games/" + textFile);
        }

        Scanner scanner = new Scanner(readFile);

        String line = scanner.nextLine();
        int numberOfNewLine = 0;

        for (char c : line.toCharArray()) {
            if (c == '.') {
                numberOfNewLine++;
            }
        }

        scanner.close();

        return numberOfNewLine + 1;
    }

    /**
     * Visszatér a paraméterként kapott fájlnevű fájl lépései alapján a következő
     * oldal színével. Fájl megnyitása esetén fellépő hiba esetén
     * FileNotFoundException-t dob.
     * 
     * @param textFile
     * @return
     * @throws FileNotFoundException
     */
    public static PieceColor getNextMoveColor(String textFile) throws FileNotFoundException {
        File readFile = null;

        if (!textFile.contains(".txt")) {
            readFile = new File("saved_games/" + textFile + ".txt");
        } else {
            readFile = new File("saved_games/" + textFile);
        }

        Scanner scanner = new Scanner(readFile);
        String[] moves = scanner.nextLine().split(" ");

        scanner.close();

        if (moves[moves.length - 1].contains(".")) {
            return PieceColor.BLACK;
        }

        return PieceColor.WHITE;
    }
}
