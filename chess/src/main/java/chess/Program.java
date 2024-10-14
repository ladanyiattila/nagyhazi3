package chess;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Program {
    public static void startProgram() {
        JFrame mainFrame = new JFrame();

        mainFrame.setSize(500, 500);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setTitle("Chess");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel bottomPanel = new JPanel();
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);



        JLabel titleLabel = new JLabel();
        titleLabel.setText("Chess");
        titleLabel.setFont(new Font("Arial", 0, 50));

        JLabel authorLabel = new JLabel();
        authorLabel.setText("Made by: Attila Ladányi");
        authorLabel.setFont(new Font("Arial", 0, 20));



        JButton startGameButton = new JButton("Start");
        startGameButton.setAlignmentY(400);
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        topPanel.add(titleLabel);
        topPanel.add(authorLabel);

        bottomPanel.add(startGameButton);
        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);
        mainFrame.add(mainPanel);

        mainFrame.setVisible(true);
    }

    private static void startGame() {
        JFrame mainFrame = new JFrame();

        mainFrame.setSize(2500, 2500);
        mainFrame.setVisible(true);
    }
}
