package sk.stuba.fei.uim.oop;

import sk.stuba.fei.uim.oop.game.PipeGame;

import javax.swing.*;

public class Assignment2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PipeGame::new);
    }
}
