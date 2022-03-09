//package org.pleasval;

import javax.swing.*;

public class Main {
    private static final JFrame mainFrame = new JFrame("Checkers");
    private static final Board gameBoard = new Board();

    public static void main(String[] args) {
        mainFrame.setSize(650, 650);

        mainFrame.setLocationByPlatform(true);
        gameBoard.setupBoard();
        mainFrame.add(gameBoard.getBoardRenderer());

        mainFrame.setVisible(true);
    }
}