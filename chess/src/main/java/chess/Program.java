package chess;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import chess.*;
import pieces.*;

public class Program {
    private Program() {}

    private static JFrame mainFrame;

    static {
        mainFrame = null;
    }

    public static void startProgram() {
        if (mainFrame == null) {
            mainFrame = new JFrame();
        }

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
                authorPanel.setVisible(false);
                bottomPanel.setVisible(false);
                startGame(null, "", 1, null);
            }
        });

        JButton readTextFileButton = new JButton("Load game");
        readTextFileButton.setPreferredSize(new Dimension(150, 25));
        readTextFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                titlePanel.setVisible(false);
                bottomPanel.setVisible(false);
                authorPanel.setVisible(false);
                readTextFile();
            }
        });

        JButton exitGameButton = new JButton("Exit");
        exitGameButton.setPreferredSize(new Dimension(150, 25));
        exitGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                System.exit(0);
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

    private static void startGame(List<Piece> startingPos, String movesListed, int numberOfMoves, PieceColor nextMoveColor) {
        mainFrame.setSize(1000, 750);

        JTextArea movesText = new JTextArea();
        movesText.setSize(new Dimension(300, 400));
        movesText.enableInputMethods(false);
        movesText.setEditable(false);
        movesText.setLineWrap(true);
        movesText.setWrapStyleWord(true);
        SwingUtilities.invokeLater(() -> movesText.setText(movesListed));

        // a doboz tartalma görgethető legyen
        JScrollPane movesScroll = new JScrollPane(movesText);
        movesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        movesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        Board board = new Board(startingPos, movesListed, numberOfMoves, movesText, nextMoveColor);

        JPanel bottomJPanel = new JPanel();
        bottomJPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        JButton backToMainMenu = new JButton("Back to main menu");
        backToMainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.getBoardPanel().setVisible(false);
                Program.startProgram();
            }
        });
        bottomJPanel.add(backToMainMenu);

        JButton saveGame = new JButton("Save game");
        saveGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PGN_Formatter.saveGame(movesText.getText());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                board.getBoardPanel().setVisible(false);
                Program.startProgram();
            }
        });
        bottomJPanel.add(saveGame);


        mainFrame.add(board.getBoardPanel(), BorderLayout.WEST);
        mainFrame.add(movesScroll, BorderLayout.EAST);
        mainFrame.add(bottomJPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private static void readTextFile() {
        JPanel textJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        textJPanel.setPreferredSize(new Dimension(500, 100));

        JLabel titleLabel = new JLabel();
        titleLabel.setText("Load game");
        titleLabel.setFont(new Font("Arial", 0, 30));
        titleLabel.setForeground(Color.black);
        textJPanel.add(titleLabel);

        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("Enter the .txt file's name which contains the game you would like to load.");
        descriptionLabel.setFont(new Font("Arial", 0, 14));
        descriptionLabel.setForeground(Color.black);
        textJPanel.add(descriptionLabel);

        JLabel noteLabel = new JLabel("(Note: the file must be in the saved_games folder)");
        noteLabel.setFont(new Font("Arial", 0, 12));
        noteLabel.setForeground(Color.gray);
        textJPanel.add(noteLabel);

        JPanel entryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel backToMenuPanel = new JPanel();

        JButton backToMenuButton = new JButton("Back to main menu");
        backToMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textJPanel.setVisible(false);
                entryPanel.setVisible(false);
                backToMenuPanel.setVisible(false);
                startProgram();
            }
        });
        backToMenuPanel.add(backToMenuButton);

        JTextField textField = new JTextField(30);
        entryPanel.add(textField);

        JButton button = new JButton("LOAD");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Piece> list = PGN_Formatter.readFromTextFile(textField.getText());

                    textJPanel.setVisible(false);
                    entryPanel.setVisible(false);
                    backToMenuPanel.setVisible(false);

                    if (list.isEmpty()) {
                        startGame(null, PGN_Formatter.getMovesListed(textField.getText()), 1, null);
                    } else {
                        System.out.println("lepesszam: " + PGN_Formatter.getNumberOfMoves(textField.getText()));
                        System.out.println("kov szin: " + PGN_Formatter.getNextMoveColor(textField.getText()));
                        startGame(list, PGN_Formatter.getMovesListed(textField.getText()), PGN_Formatter.getNumberOfMoves(textField.getText()),
                        PGN_Formatter.getNextMoveColor(textField.getText()));
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    errorWhenLoadingGame();
                }
            }
        });
        entryPanel.add(button);

        mainFrame.add(textJPanel, BorderLayout.NORTH);
        mainFrame.add(entryPanel, BorderLayout.CENTER);
        mainFrame.add(backToMenuPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    private static void errorWhenLoadingGame() {
        JFrame errorFrame = new JFrame("Error");
        errorFrame.setSize(400, 200);
        errorFrame.setResizable(false);
        errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        errorFrame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Error when loading game:");
        label.setFont(new Font("Arial", 0, 14));
        label.setForeground(Color.red);

        JLabel label2 = new JLabel("file not found in saved_games directory");
        label2.setFont(new Font("Arial", 0, 14));
        label2.setForeground(Color.red);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 100));
        panel.add(label);
        panel.add(label2);

        JButton backToMainMenu = new JButton("Back to main menu");
        backToMainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                backToMainMenu.setVisible(false);
                errorFrame.setVisible(false);
                Program.startProgram();
            }
        });
        
        errorFrame.add(panel, BorderLayout.NORTH);
        errorFrame.add(backToMainMenu, BorderLayout.SOUTH);

        errorFrame.setVisible(true);
    }
}
