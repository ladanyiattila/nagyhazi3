package chess;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

// Források: 
/*
 * https://stackoverflow.com/questions/15694107/how-to-layout-multiple-panels-on-a-jframe-java
 * https://www.javatpoint.com/java-jbutton
 */

public class Program {
    private static JFrame mainFrame;

    public static void startProgram() {
        mainFrame = new JFrame();

        mainFrame.setSize(500, 500);
        mainFrame.setResizable(false);
        mainFrame.setTitle("Chess");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // a főcím létrehozása
        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(500, 50));
        titlePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel();
        titleLabel.setText("Chess");
        titleLabel.setFont(new Font("Arial", 0, 50));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.TOP);
        titleLabel.setForeground(Color.black);

        titlePanel.add(titleLabel);

        // az alcím hozzáadása
        JPanel authorPanel = new JPanel();
        authorPanel.setSize(new Dimension(500, 20));
        authorPanel.setLayout(new BorderLayout());

        JLabel authorLabel = new JLabel();
        authorLabel.setText("Made by: Attila Ladányi");
        authorLabel.setFont(new Font("Arial", 0, 20));
        authorLabel.setHorizontalAlignment(JLabel.CENTER);
        authorLabel.setVerticalAlignment(JLabel.TOP);
        authorLabel.setForeground(Color.black);

        authorPanel.add(authorLabel);

        // nyomógombok
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(150, 75));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel buttonsJPanel = new JPanel();
        buttonsJPanel.setPreferredSize(new Dimension(150, 200));
        buttonsJPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton startGameButton = new JButton("Start");
        startGameButton.setPreferredSize(new Dimension(150, 25));
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                titlePanel.setVisible(false);
                bottomPanel.setVisible(false);
                startGame();
            }
        });

        JButton readTextFileButton = new JButton("Load game");
        readTextFileButton.setPreferredSize(new Dimension(150, 25));
        readTextFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                titlePanel.setVisible(false);
                bottomPanel.setVisible(false);
                readTextFile();
            }
        });

        JButton exitGameButton = new JButton("Exit");
        exitGameButton.setPreferredSize(new Dimension(150, 25));
        exitGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                return;
            }
        });

        buttonsJPanel.add(startGameButton);
        buttonsJPanel.add(readTextFileButton);
        buttonsJPanel.add(exitGameButton);

        bottomPanel.add(buttonsJPanel);
 
        mainFrame.add(titlePanel, BorderLayout.NORTH);
        mainFrame.add(authorPanel, BorderLayout.CENTER);

        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    private static void startGame() {
        // TODO
    }

    private static void readTextFile() {
        // TODO
        ;
    }
}
