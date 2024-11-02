package chess;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.*;

import pieces.*;

public class Board {
    private JPanel boardPanel;
    private static String[] columns = { "", "A", "B", "C", "D", "E", "F", "G", "H" };
    private List<Piece> actualPosition;

    public Board() {
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(750, 750));
        boardPanel.setBackground(Color.red);

        String[][] rows = new String[9][9];
        for (int row = 0; row < 8; row++) {
            String[] oneRow = new String[9];
            oneRow[0] = Integer.toString(row + 1);

            for (int j = 1; j < 9; j++) {
                oneRow[j] = "";
            }

            rows[row] = oneRow;
        }

        rows[8] = columns;

        JTable boardTable = new JTable(rows, columns);

        actualPosition = getStartingPosition();

        boardTable.setRowHeight(50);

        for (int i = 1; i < 9; i++)
            boardTable.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());

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

    static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (row == 8) {
                Position pos = new Position(column, row + 1);
                JLabel label = new JLabel(pos.columnToString().toUpperCase());
                label.setOpaque(true);
                label.setForeground(Color.black);
                label.setBackground(Color.white);
                return label;
            }

            JLabel label = new JLabel();
            label.setIcon(new ImageIcon("src/main/resources/black_pawn.png"));

            if (column > 0 && row > 1) {
                if ((row + column) % 2 == 0) {
                    label.setBackground(Color.black);
                } else {
                    label.setBackground(Color.white);
                }
            }

            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            }

            return label;
        }
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }
}
