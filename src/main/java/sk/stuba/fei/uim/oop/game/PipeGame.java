package sk.stuba.fei.uim.oop.game;

import sk.stuba.fei.uim.oop.board.GameBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PipeGame extends JFrame implements KeyListener {
    private int currentLevel = 1;
    private JLabel levelLabel;
    private GameBoard gameBoard;

    public PipeGame() {
        initComponents();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }
    private void initComponents() {
        setTitle("Pipe Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameBoard = new GameBoard(this);
        add(gameBoard, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

        levelLabel = new JLabel("level: " + currentLevel);
        controlPanel.add(levelLabel);

        JComboBox<String> sizeSelector = new JComboBox<>(new String[]{"8x8", "10x10", "12x12"});
        sizeSelector.addActionListener(e -> {
            String selectedSize = (String) sizeSelector.getSelectedItem();
            int newSize = Integer.parseInt(selectedSize.split("x")[0]);
            gameBoard.setBoardSize(newSize);
            levelLabel.setText("level: " + currentLevel);
            this.requestFocus();

        });
        controlPanel.add(sizeSelector);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            gameBoard.resetGame();
            this.requestFocus();
        });
        controlPanel.add(resetButton);

        JButton checkButton = new JButton("Check path");
        checkButton.addActionListener(e -> {
            gameBoard.checkPath();
            this.requestFocus();
        });
        controlPanel.add(checkButton);

        add(controlPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateLevelLabel(int newLevel) {
        currentLevel = newLevel;
        levelLabel.setText("level: " + currentLevel);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 27:
                System.exit(0);
                break;
            case 82:
                gameBoard.resetGame();
                break;
            case 10:
                gameBoard.checkPath();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
