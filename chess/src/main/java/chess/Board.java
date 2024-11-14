package chess;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.io.File;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseEvent;

import java.util.*;

import pieces.*;

public class Board {
    private JPanel boardPanel;
    private static String[] columns = { "", "A", "B", "C", "D", "E", "F", "G", "H" };
    private static List<Piece> actualPosition;
    private static DefaultTableModel tableModel;
    private static JTable boardTable;

    public Board() {
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(675, 750));
        boardPanel.setBackground(Color.red);

        String[][] rows = new String[9][9];
        tableModel = new DefaultTableModel(rows, columns);
        boardTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        actualPosition = getStartingPosition();

        boardTable.setRowHeight(75);

        boardTable.setCellSelectionEnabled(true);

        boardTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boardTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (int i = 0; i < 9; i++) {
            boardTable.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());
        }

        boardTable.addMouseListener(new ClickedOnTable());        

        boardPanel.add(boardTable);
    }


    private static List<Piece> getStartingPosition() {
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

    class ClickedOnTable extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = boardTable.rowAtPoint(e.getPoint());
            int column = boardTable.columnAtPoint(e.getPoint());

            if (row >= 0 && column >= 0) {                
                Position clickedPosition = new Position(column, 8 - row);
                Piece pieceThere = getPieceInActualPosition(clickedPosition);
                
                if (pieceThere != null) {
                    List<Position> allMoves = pieceThere.getEveryMove();
                    clickedPiece = pieceThere;

                    ListIterator<Position> iter = allMoves.listIterator();

                    while (iter.hasNext()) {
                        Position current = iter.next();
                        
                        if (!Rules.isMovePossible(actualPosition, pieceThere, current)) {
                            iter.remove();
                        }
                    }

                    possibleMoves = allMoves;
                } else if (isInPossibleMove(clickedPosition)) {
                    for (Piece piece : actualPosition) {
                        if (clickedPiece.equals(piece)) {
                            piece.setPosition(new Position(clickedPosition.getColumn(), clickedPosition.getRow()));
                        }
                    }


                    clickedPiece.setPosition(clickedPosition);
                    possibleMoves = null;
                } else {
                    possibleMoves = null;
                    clickedPiece = null;
                }

                boardTable.repaint();
            }
        }
    }


    static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 0 && row == 8) {
                return new JLabel();
            }

            if (column == 0) {
                JLabel label = new JLabel(Integer.toString(8 - row));
                label.setOpaque(true);
                label.setForeground(Color.black);
                label.setBackground(Color.white);
                return label;
            }


            if (row == 8) {
                Position pos = new Position(column, row + 1);
                JLabel label = new JLabel(pos.columnToString().toUpperCase());
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
