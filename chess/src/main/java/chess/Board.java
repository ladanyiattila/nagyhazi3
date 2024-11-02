package chess;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.util.*;

import pieces.*;

public class Board {
    private JPanel boardPanel;
    private static String[] columns = { "", "A", "B", "C", "D", "E", "F", "G", "H" };
    private static List<Piece> actualPosition;
    private static DefaultTableModel tableModel;

    public Board() {
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(675, 750));
        boardPanel.setBackground(Color.red);

        String[][] rows = new String[9][9];
        tableModel = new DefaultTableModel(rows, columns);
        JTable boardTable = new JTable(tableModel);

        actualPosition = getStartingPosition();

        boardTable.setRowHeight(75);

        boardTable.setCellSelectionEnabled(true);

        boardTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boardTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (int i = 0; i < 9; i++) {
            boardTable.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());
        }

        

        boardPanel.add(boardTable);
    }

    private static void paintCellBlue(int row, int column) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.blue);
        tableModel.setValueAt(label, row, column);
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

    static class CellRenderer extends DefaultTableCellRenderer {
        private List<Position> possibleMoves;

        private static Piece getPieceInActualPosition(Position position) {
            for (Piece piece : actualPosition) {
                System.out.println(piece.getPosition());
                if (piece.getPosition().equals(position)) {
                    return piece;
                }
            }

            return null;
        }

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
                for (Position position : possibleMoves) {
                    if (position != null && cellPosition.equals(position)) {
                        paintCellBlue(position.getRow(), position.getColumn());
                    }
                }
            }
            

            label.setOpaque(true);

            if (isSelected) {
                Position clickedPosition = new Position(column, 8 - row);
                System.out.println("selected: " + column + " " + (8 - row) + " pos:" + clickedPosition.toString());

                Piece pieceThere = getPieceInActualPosition(clickedPosition);

                System.out.println("possible moves:");
                if (pieceThere != null) {
                    possibleMoves = pieceThere.getEveryMove();
                }
            }

            return label;
        }
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }
}
