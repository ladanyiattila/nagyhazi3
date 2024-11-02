package main.java.chess;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import javax.swing.table.DefaultTableCellRenderer;

import pieces.Position;

public class Board {
    private JPanel boardPanel;

    public Board() {
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(750, 750));
        boardPanel.setBackground(Color.red);

        String[] columns = {"", "A", "B", "C", "D", "E", "F", "G", "H"};
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

        boardTable.setRowHeight(50);

        for (int i = 1; i < 9; i++)
            boardTable.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());
        
        boardPanel.add(boardTable);
    }

    static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
                System.out.println(column + " " + row);
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
