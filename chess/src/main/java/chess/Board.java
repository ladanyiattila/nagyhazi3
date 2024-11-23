package chess;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseEvent;

import java.util.*;

import pieces.*;

public class Board {
    private static JPanel boardPanel;
    private static String[] columns = { "", "A", "B", "C", "D", "E", "F", "G", "H" };
    private static List<Piece> actualPosition;
    private static DefaultTableModel tableModel;
    private static JTable boardTable;
    private static PieceColor nextMoveColor;
    private static JFrame endOfGame;
    private static String movesListed;
    private static int numberOfMoves;
    private static JTextArea movesTextArea;
    private static JFrame promotionFrame;

    static {
        // fehér kezd alapértelmezetten
        nextMoveColor = PieceColor.WHITE;

        // ne lehessen többször megnyitni az ablakot
        endOfGame = null;
        promotionFrame = null;
    }

    public Board(List<Piece> startingPos, String movesListed, int numberOfMoves, JTextArea movesTextArea, PieceColor nextMoveColor) {
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

        this.movesListed = movesListed;
        this.numberOfMoves = numberOfMoves;
        this.movesTextArea = movesTextArea;
        
        if (nextMoveColor != null) {
            this.nextMoveColor = nextMoveColor;
        }

        if (startingPos == null) {
            actualPosition = getStartingPosition();
        } else {
            actualPosition = startingPos;
        }

        boardTable.setRowHeight(75);

        boardTable.setCellSelectionEnabled(true);

        boardTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boardTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (int i = 0; i < 9; i++) {
            CellRenderer renderer = new CellRenderer();
            boardTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        boardTable.addMouseListener(new ClickedOnTable());        

        boardPanel.add(boardTable);
    }


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

        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(PieceColor.WHITE, new Position(columns[i + 1].toLowerCase(), 2)));
        }

        return pieces;
    }


    private static List<Position> possibleMoves;
    private static Piece clickedPiece;

    private static Piece getPieceInActualPosition(Position position) {
        for (Piece piece : actualPosition) {
            if (piece.getPosition().equals(position)) {
                return piece;
            }
        }

        return null;
    }

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
                JLabel winner = new JLabel("The winner is: " + (nextMoveColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE));
                winner.setFont(new Font("Arial", 0, 20));
                textPanel.add(winner);
            }

        
            JButton button = new JButton("Back to main menu");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boardPanel.setVisible(false);
                    endOfGame.setVisible(false);
                    Program.startProgram();
                }
            });

            endOfGame.add(textPanel, BorderLayout.NORTH);
            endOfGame.add(button, BorderLayout.SOUTH);
            endOfGame.setVisible(true);
        }
    }

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

    public static String getMovesListedVariable() {
        return movesListed;
    }

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
        queenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualPosition.add(new Queen(clickedPiece.getColor(), clickedPosition));

                movesListed += (nextMoveColor == PieceColor.BLACK ? numberOfMoves + "." : "") + PGN_Formatter.getMoveFormatted(clickedPiece, clickedPosition, wasPieceTaken, actualPosition) + "=Q" 
                + (nextMoveColor == PieceColor.BLACK ? " " : "\n");

                if (nextMoveColor == PieceColor.WHITE) {
                    numberOfMoves++;
                }

                movesTextArea.repaint();
                movesTextArea.setText(movesListed);

                promotionFrame.setVisible(false);
                boardTable.repaint();
            }
        });
        queenButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(queenButton);


        JButton rookButton = new JButton();
        if (clickedPiece.getColor() == PieceColor.BLACK) {
            rookButton.setIcon(new ImageIcon("piece_images/black_rook.png"));
        } else {
            rookButton.setIcon(new ImageIcon("piece_images/white_rook.png"));
        }
        rookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualPosition.add(new Rook(clickedPiece.getColor(), clickedPosition));

                movesListed += (nextMoveColor == PieceColor.BLACK ? numberOfMoves + "." : "")
                        + PGN_Formatter.getMoveFormatted(clickedPiece, clickedPosition, wasPieceTaken, actualPosition)
                        + "=R"
                        + (nextMoveColor == PieceColor.BLACK ? " " : "\n");

                if (nextMoveColor == PieceColor.WHITE) {
                    numberOfMoves++;
                }

                movesTextArea.repaint();
                movesTextArea.setText(movesListed);

                promotionFrame.setVisible(false);
                boardTable.repaint();
            }
        });
        rookButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(rookButton);

        JButton knightButton = new JButton();
        if (clickedPiece.getColor() == PieceColor.BLACK) {
            knightButton.setIcon(new ImageIcon("piece_images/black_knight.png"));
        } else {
            knightButton.setIcon(new ImageIcon("piece_images/white_knight.png"));
        }
        knightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualPosition.add(new Knight(clickedPiece.getColor(), clickedPosition));

                movesListed += (nextMoveColor == PieceColor.BLACK ? numberOfMoves + "." : "")
                        + PGN_Formatter.getMoveFormatted(clickedPiece, clickedPosition, wasPieceTaken, actualPosition)
                        + "=N"
                        + (nextMoveColor == PieceColor.BLACK ? " " : "\n");

                if (nextMoveColor == PieceColor.WHITE) {
                    numberOfMoves++;
                }

                movesTextArea.repaint();
                movesTextArea.setText(movesListed);

                promotionFrame.setVisible(false);
                boardTable.repaint();
            }
        });
        knightButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(knightButton);

        JButton bishopButton = new JButton();
        if (clickedPiece.getColor() == PieceColor.BLACK) {
            bishopButton.setIcon(new ImageIcon("piece_images/black_bishop.png"));
        } else {
            bishopButton.setIcon(new ImageIcon("piece_images/white_bishop.png"));
        }
        bishopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualPosition.add(new Bishop(clickedPiece.getColor(), clickedPosition));

                movesListed += (nextMoveColor == PieceColor.BLACK ? numberOfMoves + "." : "")
                        + PGN_Formatter.getMoveFormatted(clickedPiece, clickedPosition, wasPieceTaken, actualPosition)
                        + "=B"
                        + (nextMoveColor == PieceColor.BLACK ? " " : "\n");

                if (nextMoveColor == PieceColor.WHITE) {
                    numberOfMoves++;
                }

                movesTextArea.repaint();
                movesTextArea.setText(movesListed);

                promotionFrame.setVisible(false);
                boardTable.repaint();
            }
        });
        bishopButton.setPreferredSize(new Dimension(80, 80));
        buttonPanel.add(bishopButton);

        promotionFrame.add(textPanel, BorderLayout.NORTH);
        promotionFrame.add(buttonPanel, BorderLayout.SOUTH);
        promotionFrame.setVisible(true);
    }

    class ClickedOnTable extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = boardTable.rowAtPoint(e.getPoint());
            int column = boardTable.columnAtPoint(e.getPoint());

            if (row >= 0 && column >= 0) {                
                Position clickedPosition = new Position(column, 8 - row);
                Piece pieceThere = getPieceInActualPosition(clickedPosition);

                if (pieceThere != null && pieceThere.getColor() == nextMoveColor) {
                    List<Position> allMoves = pieceThere.getEveryMove();
                    clickedPiece = pieceThere;

                    ListIterator<Position> iter = allMoves.listIterator();

                    while (iter.hasNext()) {
                        Position current = iter.next();
                        
                        if (!Rules.isMovePossible(actualPosition, pieceThere, current, nextMoveColor, true, true)) {
                            iter.remove();
                        }
                    }

                    possibleMoves = allMoves;
                } else if (isInPossibleMove(clickedPosition)) {
                    ListIterator<Piece> iter = actualPosition.listIterator();
                    boolean wasPieceTaken = false;

                    while (iter.hasNext()) {
                        Piece current = iter.next();

                        if (current.getPosition().equals(clickedPosition)) {
                            iter.remove();
                            wasPieceTaken = true;
                            break;
                        }
                    }

                    Position movedTo = new Position(clickedPosition.getColumn(), clickedPosition.getRow());

                    boolean wasTherePromotion = false;

                    // átalakulás
                    if (clickedPiece.getType() == PieceType.PAWN
                            && (clickedPosition.getRow() == 1 || clickedPosition.getRow() == 8)) {
                        ListIterator<Piece> iterator = actualPosition.listIterator();


                        while (iterator.hasNext()) {
                            Piece current = iterator.next();

                            if (current.getPosition().equals(clickedPiece.getPosition())) {
                                iterator.remove();
                                break;
                            }
                        }

                        System.out.println("promotion");
                        System.out.println(clickedPiece.getPosition());
                        Board.promotionWindow(clickedPosition, wasPieceTaken);
                        boardTable.repaint();
                        wasTherePromotion = true;
                    }
                
                    if (!wasTherePromotion) {
                        addMove(PGN_Formatter.getMoveFormatted(clickedPiece, movedTo, wasPieceTaken, actualPosition));
                    }

                    for (Piece piece : actualPosition) {
                        if (clickedPiece.equals(piece)) {
                            piece.setPosition(movedTo);
                        }
                    }

                    if (nextMoveColor == PieceColor.WHITE) {
                        nextMoveColor = PieceColor.BLACK;
                    } else {
                        nextMoveColor = PieceColor.WHITE;
                    }

                    if (!wasTherePromotion) {
                        clickedPiece.setPosition(clickedPosition);
                        clickedPiece.pieceHasMoved();
                    }

                    // sáncolás
                    if (clickedPiece.getType() == PieceType.KING) {
                        if (clickedPiece.getColor() == PieceColor.WHITE) {
                            // rövid
                            if (movedTo.equals(new Position("g", 1))) {
                                for (Piece piece : actualPosition) {
                                    if (piece.getType() == PieceType.ROOK && piece.getPosition().equals(new Position("h", 1))) {
                                        piece.setPosition(new Position("f", 1));
                                        piece.pieceHasMoved();
                                    }
                                }
                            }

                            // hosszú
                            if (movedTo.equals(new Position("c", 1))) {
                                for (Piece piece : actualPosition) {
                                    if (piece.getType() == PieceType.ROOK
                                            && piece.getPosition().equals(new Position("a", 1))) {
                                        piece.setPosition(new Position("d", 1));
                                        piece.pieceHasMoved();
                                    }
                                }
                            }   
                        } else {
                            // rövid
                            if (movedTo.equals(new Position("g", 8))) {
                                for (Piece piece : actualPosition) {
                                    if (piece.getType() == PieceType.ROOK
                                            && piece.getPosition().equals(new Position("h", 8))) {
                                        piece.setPosition(new Position("f", 8));
                                        piece.pieceHasMoved();
                                    }
                                }
                            }

                            // hosszú
                            if (movedTo.equals(new Position("c", 8))) {
                                for (Piece piece : actualPosition) {
                                    if (piece.getType() == PieceType.ROOK
                                            && piece.getPosition().equals(new Position("a", 8))) {
                                        piece.setPosition(new Position("d", 8));
                                        piece.pieceHasMoved();
                                    }
                                }
                            }
                        }
                    }

                    

                    possibleMoves = null;
                } else {
                    possibleMoves = null;
                    clickedPiece = null;
                }

                boardTable.repaint();
            }

            checkBoardAfterEveryMove();
        }
    }


    static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 0 && row == 8) {
                return new JLabel();
            }

            if (column == 0) {
                JLabel label = new JLabel("        " + Integer.toString(8 - row));
                label.setOpaque(true);
                label.setForeground(Color.black);
                label.setBackground(Color.white);
                return label;
            }


            if (row == 8) {
                Position pos = new Position(column, row + 1);
                JLabel label = new JLabel("        " + pos.columnToString().toUpperCase());
                label.setOpaque(true);
                label.setForeground(Color.black);
                label.setBackground(Color.white);
                return label;
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

            label.setOpaque(true);

            return label;
        }
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }
}
