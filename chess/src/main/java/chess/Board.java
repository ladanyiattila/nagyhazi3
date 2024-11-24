package chess;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseEvent;

import java.util.*;

import pieces.*;

/**
 * A játék táblájának megvalósításáért felelős osztály
 */
public class Board {
    private static JPanel boardPanel;
    private static final String[] columns = { "", "A", "B", "C", "D", "E", "F", "G", "H" };
    private static List<Piece> actualPosition;
    private static JTable boardTable;
    private static PieceColor nextMoveColor;
    private static JFrame endOfGame;
    private static String movesListed;
    private static int numberOfMoves;
    private static JTextArea movesTextArea;
    private static JFrame promotionFrame;
    private static List<Position> possibleMoves;
    private static Piece clickedPiece;

    private DefaultTableModel tableModel;
    private Piece lastMovePiece;
    private Position lastMovePosition;

    /**
     * Board konstruktor
     * 
     * Paraméterként egy beolvasott játékállás tulajdonságait (állás, lépések
     * leírva,
     * lépések száma, következő lépést végző játékos) és a lépéseket megjelenítő
     * JTextArea-t kapja meg.
     * 
     * @param startingPos
     * @param movesListed
     * @param numberOfMoves
     * @param movesTextArea
     * @param nextMoveColor
     */
    public Board(List<Piece> startingPos, String movesListed, int numberOfMoves, JTextArea movesTextArea,
            PieceColor nextMoveColor) {
        // fehér kezd alapértelmezetten
        if (nextMoveColor != null) {
            this.nextMoveColor = nextMoveColor;
        } else {
            this.nextMoveColor = PieceColor.WHITE;
        }

        // ne lehessen többször megnyitni az ablakot
        endOfGame = null;
        promotionFrame = null;

        // a felállás beállítása
        if (startingPos == null) {
            actualPosition = getStartingPosition();
        } else {
            actualPosition = startingPos;
        }

        // beolvasás esetén a lépések, lépések száma
        this.movesListed = movesListed;
        this.numberOfMoves = numberOfMoves;

        // a lépéseket megjelenítő JTextArea-hoz hozzáférhessen, hogy annak tartalmát
        // frissíthesse
        this.movesTextArea = movesTextArea;

        // alapértelmezetten null
        possibleMoves = null;
        clickedPiece = null;

        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(675, 750));

        String[][] rows = new String[9][9];
        tableModel = new DefaultTableModel(rows, columns);
        boardTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        boardTable.setRowHeight(75);
        boardTable.setCellSelectionEnabled(true);
        boardTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boardTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // a cellák megjelenítéséhez CellRenderer
        for (int i = 0; i < 9; i++) {
            CellRenderer renderer = new CellRenderer();
            boardTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // a táblára való kattintáskor a lépések lekezelése
        boardTable.addMouseListener(new ClickedOnTable());

        boardPanel.add(boardTable);
    }

    /**
     * Az alapfelállás bábuinak listáját hozza létre és tér vissza a listával
     * 
     * @return List<Piece>
     */
    public static List<Piece> getStartingPosition() {
        List<Piece> pieces = new ArrayList<>();

        // sötét bábuk
        pieces.add(new Rook(PieceColor.BLACK, new Position("a", 8)));
        pieces.add(new Rook(PieceColor.BLACK, new Position("h", 8)));
        pieces.add(new Knight(PieceColor.BLACK, new Position("b", 8)));
        pieces.add(new Knight(PieceColor.BLACK, new Position("g", 8)));
        pieces.add(new Bishop(PieceColor.BLACK, new Position("c", 8)));
        pieces.add(new Bishop(PieceColor.BLACK, new Position("f", 8)));
        pieces.add(new Queen(PieceColor.BLACK, new Position("d", 8)));
        pieces.add(new King(PieceColor.BLACK, new Position("e", 8)));

        // 8 gyalog
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(PieceColor.BLACK, new Position(columns[i + 1].toLowerCase(), 7)));
        }

        // világos bábuk
        pieces.add(new Rook(PieceColor.WHITE, new Position("a", 1)));
        pieces.add(new Rook(PieceColor.WHITE, new Position("h", 1)));
        pieces.add(new Knight(PieceColor.WHITE, new Position("b", 1)));
        pieces.add(new Knight(PieceColor.WHITE, new Position("g", 1)));
        pieces.add(new Bishop(PieceColor.WHITE, new Position("c", 1)));
        pieces.add(new Bishop(PieceColor.WHITE, new Position("f", 1)));
        pieces.add(new Queen(PieceColor.WHITE, new Position("d", 1)));
        pieces.add(new King(PieceColor.WHITE, new Position("e", 1)));

        // 8 gyalog
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(PieceColor.WHITE, new Position(columns[i + 1].toLowerCase(), 2)));
        }

        return pieces;
    }

    /**
     * Visszaadja a paraméterként kapott mezőn elhelyezkedő bábut;
     * ha nincs ilyen, akkor null a visszatérési érték
     * 
     * @param position
     * @return Piece
     */
    private static Piece getPieceInActualPosition(Position position) {
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(position)) {
                return piece;
            }
        }

        return null;
    }

    /**
     * Igaz, ha a paraméterként kapott mező szerepel a lehetséges lépések között.
     * Egyébként hamis (ha null a mező, akkor is).
     * 
     * @param pos
     * @return boolean
     */
    private static boolean isInPossibleMove(Position pos) {
        if (possibleMoves == null) {
            return false;
        }

        for (Position position : possibleMoves) {
            if (position.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lekérdezi az aktuális állásra, hogy vége van-e már a játéknak:
     * - ha nem, akkor nem történik semmi
     * - ha igen, akkor megjeleníti az ablakot, melyen a játék
     * eredménye látható és vissza lehet térni a főmenübe
     */
    private static void checkBoardAfterEveryMove() {
        String state = Rules.getEndOfGame(actualPosition, nextMoveColor);

        if (!state.equals("NOT_FINISHED")) {
            if (endOfGame == null) {
                endOfGame = new JFrame();
            }
            endOfGame.setSize(400, 200);
            endOfGame.setResizable(false);
            endOfGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            endOfGame.setTitle("Chess");
            endOfGame.setLayout(new BorderLayout());

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            textPanel.setPreferredSize(new Dimension(400, 300));

            JLabel label = new JLabel();
            String text = "End of game by " + state;
            label.setText(text);
            label.setFont(new Font("Arial", 0, 20));
            textPanel.add(label);

            if (state.equals("CHECKMATE")) {
                JLabel winner = new JLabel(
                        "The winner is: " + (nextMoveColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE));
                winner.setFont(new Font("Arial", 0, 20));
                textPanel.add(winner);
            }

            JButton button = new JButton("Back to main menu");
            button.addActionListener((ActionEvent ae) -> {
                boardPanel.setVisible(false);
                endOfGame.setVisible(false);
                Program.startProgram();
            });

            endOfGame.add(textPanel, BorderLayout.NORTH);
            endOfGame.add(button, BorderLayout.SOUTH);
            endOfGame.setVisible(true);
        }
    }

    /**
     * Hozzáadja a lépések szövegéhez a paraméterként kapott lépést, majd ezt
     * a JTextArea-n is frissíti
     * 
     * @param move
     */
    private static void addMove(String move) {
        if (nextMoveColor == PieceColor.WHITE) {
            movesListed += numberOfMoves + "." + move + " ";
        } else {
            movesListed += move + "\n";
            numberOfMoves++;
        }

        movesTextArea.repaint();
        movesTextArea.setText(movesListed);
    }

    /**
     * movesListed paraméter getter függvénye
     * 
     * @return String
     */
    public static String getMovesListedVariable() {
        return movesListed;
    }

    /**
     * Frissíti az előléptetés után a PGN lépéseket
     * 
     * @param clickedPosition
     * @param wasPieceTaken
     * @param pieceLetter
     */
    private static void updateMovesText(Position clickedPosition, boolean wasPieceTaken, String pieceLetter) {
        movesListed += (nextMoveColor == PieceColor.BLACK ? numberOfMoves + "." : "")
                + PGN_Formatter.getMoveFormatted(clickedPiece, clickedPosition, wasPieceTaken, actualPosition) + "="
                + pieceLetter
                + (nextMoveColor == PieceColor.BLACK ? " " : "\n");

        if (nextMoveColor == PieceColor.WHITE) {
            numberOfMoves++;
        }

        movesTextArea.repaint();
        movesTextArea.setText(movesListed);

        promotionFrame.setVisible(false);
        boardTable.repaint();
    }

    /**
     * Átalakuláskor megjeleníti az ablakot, ahonnan választhat a játékos bábut,
     * illetve hozzáadja az álláshoz az új bábut
     * 
     * @param clickedPosition
     * @param wasPieceTaken
     */
    private static void promotionWindow(Position clickedPosition, boolean wasPieceTaken) {
        if (promotionFrame == null) {
            promotionFrame = new JFrame();
        }

        promotionFrame.setSize(400, 200);
        promotionFrame.setResizable(false);
        promotionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        promotionFrame.setTitle("Promotion");
        promotionFrame.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        textPanel.setPreferredSize(new Dimension(400, 50));

        JLabel label = new JLabel("Choose a piece: ");
        label.setFont(new Font("Arial", 0, 20));
        textPanel.add(label);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setPreferredSize(new Dimension(400, 100));

        JButton queenButton = new JButton();
        if (clickedPiece.getColor() == PieceColor.BLACK) {
            queenButton.setIcon(new ImageIcon("piece_images/black_queen.png"));
        } else {
            queenButton.setIcon(new ImageIcon("piece_images/white_queen.png"));
        }
        queenButton.addActionListener((ActionEvent e) -> {
            actualPosition.add(new Queen(clickedPiece.getColor(), clickedPosition));
            updateMovesText(clickedPosition, wasPieceTaken, "Q");
        });
        queenButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(queenButton);

        JButton rookButton = new JButton();
        if (clickedPiece.getColor() == PieceColor.BLACK) {
            rookButton.setIcon(new ImageIcon("piece_images/black_rook.png"));
        } else {
            rookButton.setIcon(new ImageIcon("piece_images/white_rook.png"));
        }
        rookButton.addActionListener((ActionEvent e) -> {
            actualPosition.add(new Rook(clickedPiece.getColor(), clickedPosition));
            updateMovesText(clickedPosition, wasPieceTaken, "R");
        });
        rookButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(rookButton);

        JButton knightButton = new JButton();
        if (clickedPiece.getColor() == PieceColor.BLACK) {
            knightButton.setIcon(new ImageIcon("piece_images/black_knight.png"));
        } else {
            knightButton.setIcon(new ImageIcon("piece_images/white_knight.png"));
        }
        knightButton.addActionListener((ActionEvent e) -> {
            actualPosition.add(new Knight(clickedPiece.getColor(), clickedPosition));
            updateMovesText(clickedPosition, wasPieceTaken, "N");
        });
        knightButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(knightButton);

        JButton bishopButton = new JButton();
        if (clickedPiece.getColor() == PieceColor.BLACK) {
            bishopButton.setIcon(new ImageIcon("piece_images/black_bishop.png"));
        } else {
            bishopButton.setIcon(new ImageIcon("piece_images/white_bishop.png"));
        }
        bishopButton.addActionListener((ActionEvent e) -> {
            actualPosition.add(new Bishop(clickedPiece.getColor(), clickedPosition));
            updateMovesText(clickedPosition, wasPieceTaken, "B");
        });
        bishopButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(bishopButton);

        promotionFrame.add(textPanel, BorderLayout.NORTH);
        promotionFrame.add(buttonPanel, BorderLayout.SOUTH);
        promotionFrame.setVisible(true);
    }

    /**
     * A tábla JPanel attribútumának getter függvénye
     * 
     * @return
     */
    public JPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * A táblára történő kattintások kezeléséért felelős belső osztály:
     * - lekérdezi a lehetséges lépéseket
     * - lépés esetén frissíti az aktuális állást
     */
    class ClickedOnTable extends MouseAdapter {
        private Piece pieceThere;
        private Position clickedPosition;
        private boolean wasPieceTaken;
        private boolean wasTherePromotion;
        private Position movedTo;

        /**
         * A kattintás egy bábura vonatkozik és az nem ütés
         */
        private void clickedOnPiece() {
            List<Position> allMoves = pieceThere.getEveryMove();
            clickedPiece = pieceThere;

            ListIterator<Position> iter = allMoves.listIterator();

            while (iter.hasNext()) {
                Position current = iter.next();

                if (!Rules.isMovePossible(actualPosition, pieceThere, current, nextMoveColor, true, true, lastMovePiece,
                        lastMovePosition)) {
                    iter.remove();
                }
            }

            possibleMoves = allMoves;
        }

        /**
         * Átalakulás esetén
         */
        private void promotion() {
            ListIterator<Piece> iterator = actualPosition.listIterator();

            while (iterator.hasNext()) {
                Piece current = iterator.next();

                if (current.getPosition().equals(clickedPiece.getPosition())) {
                    iterator.remove();
                    break;
                }
            }

            Board.promotionWindow(clickedPosition, wasPieceTaken);
            boardTable.repaint();
            wasTherePromotion = true;
        }

        /**
         * En passant esetén
         */
        private void enPassant() {
            ListIterator<Piece> iterator = actualPosition.listIterator();

            Position pawnPos = new Position(clickedPosition.getColumn(),
                    clickedPosition.getRow() + (clickedPiece.getColor() == PieceColor.WHITE ? -1 : 1));

            while (iterator.hasNext()) {
                Piece current = iterator.next();

                if (current.getPosition().equals(pawnPos)) {
                    iterator.remove();
                }
            }

            addMove(clickedPiece.getPosition().columnToString() + "x" + clickedPosition.columnToString()
                    + clickedPosition.getRow());
        }

        /**
         * Sáncolás esetén
         */
        private void castling() {
            int row = clickedPiece.getColor() == PieceColor.WHITE ? 1 : 8;

            // rövid
            if (movedTo.equals(new Position("g", row))) {
                for (Piece piece : actualPosition) {
                    if (piece.getType() == PieceType.ROOK
                            && piece.getPosition().equals(new Position("h", row))) {
                        piece.setPosition(new Position("f", row));
                        piece.pieceHasMoved();
                        lastMovePiece = clickedPiece;
                    }
                }
            }

            // hosszú
            if (movedTo.equals(new Position("c", row))) {
                for (Piece piece : actualPosition) {
                    if (piece.getType() == PieceType.ROOK
                            && piece.getPosition().equals(new Position("a", row))) {
                        piece.setPosition(new Position("d", row));
                        piece.pieceHasMoved();
                        lastMovePiece = clickedPiece;
                    }
                }
            }
        }

        /**
         * Egy bábu lépésének megvalósítása
         */
        private void executeMove() {
            ListIterator<Piece> iter = actualPosition.listIterator();
            wasPieceTaken = false;

            // ütés esetén a bábu törlése
            while (iter.hasNext()) {
                Piece current = iter.next();

                if (current.getPosition().equals(clickedPosition)) {
                    iter.remove();
                    wasPieceTaken = true;
                    break;
                }
            }

            movedTo = new Position(clickedPosition.getColumn(), clickedPosition.getRow());
            wasTherePromotion = false;

            // átalakulás
            if (clickedPiece.getType() == PieceType.PAWN
                    && (clickedPosition.getRow() == 1 || clickedPosition.getRow() == 8)) {
                promotion();
            }

            // en passant esetén a gyalog törlése
            if (clickedPiece.getType() == PieceType.PAWN && getPieceInActualPosition(clickedPosition) == null
                    && (clickedPosition.getRow() == 3 || clickedPosition.getRow() == 6)
                    && clickedPiece.getPosition().getColumn() != clickedPosition.getColumn()) {
                enPassant();
            } else if (!wasTherePromotion) {
                addMove(PGN_Formatter.getMoveFormatted(clickedPiece, movedTo, wasPieceTaken, actualPosition));
            }

            // gyalog két lépés esetén a változó bebillentése igazra
            if (clickedPiece.getType() == PieceType.PAWN
                    && Math.abs(clickedPiece.getPosition().getRow() - clickedPosition.getRow()) == 2) {
                ((Pawn) clickedPiece).setTwoMove();
            }

            // a bábu "mozgatása" a lépés helyére
            for (Piece piece : actualPosition) {
                if (clickedPiece.equals(piece)) {
                    piece.setPosition(movedTo);
                }
            }

            // következő játékos jön
            if (nextMoveColor == PieceColor.WHITE) {
                nextMoveColor = PieceColor.BLACK;
            } else {
                nextMoveColor = PieceColor.WHITE;
            }

            // előléptetés esetén már ezek a parancsok megvoltak
            if (!wasTherePromotion) {
                clickedPiece.setPosition(clickedPosition);
                lastMovePiece = clickedPiece;
                lastMovePosition = clickedPosition;
                clickedPiece.pieceHasMoved();
            }

            // sáncolás
            if (clickedPiece.getType() == PieceType.KING) {
                castling();
            }

            possibleMoves = null;
        }

        /**
         * Kattintás esetén felelős a bábu lépéseinek megjelenítéséért, lépések
         * lebonyolításáért
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = boardTable.rowAtPoint(e.getPoint());
            int column = boardTable.columnAtPoint(e.getPoint());

            if (row >= 0 && column >= 0) {
                clickedPosition = new Position(column, 8 - row);
                pieceThere = getPieceInActualPosition(clickedPosition);

                if (pieceThere != null && pieceThere.getColor() == nextMoveColor) {
                    clickedOnPiece();
                } else if (isInPossibleMove(clickedPosition)) {
                    executeMove();
                } else {
                    // ha nem lépés vagy nem bábu, akkor null a két paraméter és nem történik semmi
                    possibleMoves = null;
                    clickedPiece = null;
                }

                boardTable.repaint();
            }

            // véget ért már-e a játék
            checkBoardAfterEveryMove();
        }
    }

    /**
     * A JTable celláinak megjelenítéséért felelő belső osztály.
     * Megjeleníti a bábukat, a lehetséges lépéseket, felrajzolja a
     * mezők háttereit és az oszlopok/sorok nevét
     */
    static class CellRenderer extends DefaultTableCellRenderer {
        /**
         * A sorok neveinek megjelenítéséért felel
         * 
         * @param row
         * @return
         */
        private static JLabel getRowLabel(int row) {
            JLabel label = new JLabel("        " + Integer.toString(8 - row));
            label.setOpaque(true);
            label.setForeground(Color.black);
            label.setBackground(Color.white);
            return label;
        }

        /**
         * Az oszlopok neveinek megjelenítéséért felel
         * 
         * @param column
         * @param row
         * @return
         */
        private static JLabel getColumnLabel(int column, int row) {
            Position pos = new Position(column, row + 1);
            JLabel label = new JLabel("        " + pos.columnToString().toUpperCase());
            label.setOpaque(true);
            label.setForeground(Color.black);
            label.setBackground(Color.white);
            return label;
        }

        /**
         * A lehetséges lépéseket megjeleníti:
         * - ütés esetén pirossal
         * - sima lépés esetén kékkel
         * 
         * @param label
         * @param column
         * @param row
         */
        private static void drawMoves(JLabel label, int column, int row) {
            for (Position p : possibleMoves) {
                Position there = new Position(column, 8 - row);

                if (p.equals(there)) {
                    label.setBackground(Color.blue);

                    for (Piece piece : actualPosition) {
                        if (piece.getPosition().equals(there)) {
                            label.setBackground(Color.red);
                        }
                    }
                }
            }
        }

        /**
         * A tábla celláinak kirajzolásáért felelős függvény felüldefiniálása
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (column == 0 && row == 8) {
                return new JLabel();
            }

            if (column == 0) {
                return getRowLabel(row);
            }

            if (row == 8) {
                return getColumnLabel(column, row);
            }

            JLabel label = new JLabel();
            Position cellPosition = new Position(column, 8 - row);
            for (Piece piece : actualPosition) {
                if (piece.getPosition().equals(cellPosition)) {
                    label.setIcon(new ImageIcon("piece_images/" + piece.getImageName()));
                }
            }

            if ((row + column) % 2 == 0) {
                label.setBackground(new Color(128, 128, 128));
            } else {
                label.setBackground(Color.white);
            }

            if (possibleMoves != null) {
                drawMoves(label, column, row);
            }

            label.setOpaque(true);

            return label;
        }
    }
}
