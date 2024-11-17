package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pieces.*;

public class PGN_Formatter {
    private PGN_Formatter() {}

    private static List<Piece> loadedPosition;

    private static Piece getPiece(PieceColor color, PieceType type, Position pos) {
        for (Piece piece : loadedPosition) {
            if (piece.getColor() == color && piece.getType() == type && piece.getPosition().equals(pos)) {
                return piece;
            }
        }

        return null;
    }

    private static PieceType getPieceTypeByLetter(String letter) {
        if (letter.equals(letter.toLowerCase())) {
            return PieceType.PAWN;
        }

        Map<String, PieceType> map = new HashMap<>() {{
            put("Q", PieceType.QUEEN);
            put("K", PieceType.KING);
            put("B", PieceType.BISHOP);
            put("R", PieceType.ROOK);
            put("N", PieceType.KNIGHT);
        }};

        return map.get(letter);
    }

    private static List<Piece> getOfficers(PieceColor color, PieceType type) {
        List<Piece> list = new ArrayList<>();

        for (Piece piece : loadedPosition) {
            if (piece.getColor() == color && piece.getType() == type) {
                list.add(piece);
            }
        }

        return list;
    }

    private static List<Position> getPossibleMoves(Piece piece) {
        List<Position> positions = new ArrayList<>();

        for (Position pos : piece.getEveryMove()) {
            if (Rules.isMovePossible(loadedPosition, piece, pos, piece.getColor(), true, true)) {
                positions.add(pos);
            }
        }

        return positions;
    }

    private static boolean containsMove(List<Position> list, Position move) {
        for (Position pos : list) {
            if (pos.equals(move)) {
                return true;
            }
        }

        return false;
    }

    private static void deletePiece(Position pos) {
        ListIterator<Piece> iter = loadedPosition.listIterator();

        while (iter.hasNext()) {
            Piece current = iter.next();

            if (current.getPosition().equals(pos)) {
                iter.remove();
            }
        }
    }

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


    public static List<Piece> readFromTextFile(String textFile) throws InvalidPGNFormatException, FileNotFoundException {
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

            // sáncolás
            if (actualMove.equals("O-O")) {
                int row = 8;

                if (nextColor == PieceColor.WHITE) {
                    row = 1; 
                }

                Piece king = getPiece(nextColor, PieceType.KING, new Position("e", row));
                Piece rook = getPiece(nextColor, PieceType.ROOK, new Position("h", row));

                king.setPosition(new Position("g", row));
                rook.setPosition(new Position("f", row));
                continue;
            }


            if (actualMove.equals("O-O-O")) {
                int row = 8;

                if (nextColor == PieceColor.WHITE) {
                    row = 1;
                }

                Piece king = getPiece(nextColor, PieceType.KING, new Position("e", row));
                Piece rook = getPiece(nextColor, PieceType.ROOK, new Position("a", row));

                king.setPosition(new Position("c", row));
                rook.setPosition(new Position("d", row));
                continue;
            }

            // történt-e ütés
            if (actualMove.contains("x")) {
                PieceType movePieceType = getPieceTypeByLetter(Character.toString(actualMove.charAt(0)));

                if (movePieceType == PieceType.PAWN) {
                    String column = Character.toString(actualMove.charAt(2));
                    int row = Character.getNumericValue(actualMove.charAt(3));



                    Piece movePiece = getPiece(nextColor, movePieceType,
                            new Position(Character.toString(actualMove.charAt(0)), nextColor == PieceColor.WHITE ? row - 1 : row + 1));

                    deletePiece(new Position(column, row));

                    movePiece.setPosition(new Position(column, row));
                } else {
                    List<Piece> officers = getOfficers(nextColor, movePieceType);

                    // egyértelmű a lépés, hogy melyik tiszt lép oda
                    if (actualMove.length() == 4) {
                        String column = Character.toString(actualMove.charAt(2));
                        int row = Character.getNumericValue(actualMove.charAt(3));

                        if (officers.size() == 1) {
                            deletePiece(new Position(column, row));

                            officers.get(0).setPosition(new Position(column, row));
                        } else {
                            deletePiece(new Position(column, row));

                            if (containsMove(getPossibleMoves(officers.get(0)), new Position(column, row))) {
                                officers.get(0).setPosition(new Position(column, row));
                            } else {
                                officers.get(1).setPosition(new Position(column, row));
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
                            } else {
                                officers.get(1).setPosition(new Position(column, row));
                            }
                        } else { // oszlop esetén
                            if (officers.get(0).getPosition().columnToString().equals(Character.toString(specified))) {
                                officers.get(0).setPosition(new Position(column, row));
                            } else {
                                officers.get(1).setPosition(new Position(column, row));
                            }
                        }
                    }
                }
            } else {
                PieceType movePieceType = getPieceTypeByLetter(Character.toString(actualMove.charAt(0)));

                if (movePieceType == PieceType.PAWN) {
                    String column = Character.toString(actualMove.charAt(0));
                    int row = Character.getNumericValue(actualMove.charAt(1));

                    Piece movePiece = getPiece(nextColor, movePieceType, new Position(column, nextColor == PieceColor.WHITE ? row - 1 : row + 1));

                    // két lépés esetén
                    if (movePiece == null) {
                        movePiece = getPiece(nextColor, movePieceType, new Position(column, nextColor == PieceColor.WHITE ? row - 2 : row + 2));
                    }

                    movePiece.setPosition(new Position(column, row));
                } else {
                    List<Piece> officers = getOfficers(nextColor, movePieceType);

                    // egyértelmű a lépés, hogy melyik tiszt lép oda
                    if (actualMove.length() == 3) {
                        String column = Character.toString(actualMove.charAt(1));
                        int row = Character.getNumericValue(actualMove.charAt(2));

                        if (officers.size() == 1) {
                            officers.get(0).setPosition(new Position(column, row));
                        } else {
                            if (containsMove(getPossibleMoves(officers.get(0)), new Position(column, row))) {
                                officers.get(0).setPosition(new Position(column, row));
                            } else {
                                officers.get(1).setPosition(new Position(column, row));
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
                            } else {
                                officers.get(1).setPosition(new Position(column, row));
                            }
                        } else { // oszlop esetén
                            if (officers.get(0).getPosition().columnToString().equals(Character.toString(specified))) {
                                officers.get(0).setPosition(new Position(column, row));
                            } else {
                                officers.get(1).setPosition(new Position(column, row));
                            }
                        }
                    }
                }
            }
        }

        return loadedPosition;
    }
}
