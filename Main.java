//package org.pleasval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Main {
    private static final JFrame mainFrame = new JFrame("Checkers");

    public static void endGame(int teamWon) {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));

        if (teamWon == 0) {
            JOptionPane.showMessageDialog(null, "Red team won.");
        } else {
            JOptionPane.showMessageDialog(null, "Blue team won.");
        }
    }

    public static void main(String[] args) {
        int gameMode = JOptionPane.showConfirmDialog(null, "Would you like to play against AI?", "Checkers", JOptionPane.YES_NO_OPTION);
        boolean useAI = gameMode == 0;

        Board gameBoard = new Board(useAI);
        BoardRenderer renderer = gameBoard.getBoardRenderer();
        int slotSize = renderer.getSLOT_SIZE();
        int boxSize = renderer.getBoxWidth();

        mainFrame.add(renderer);
        mainFrame.setSize(new Dimension((slotSize * 8) + boxSize + 15, (slotSize * 8) + 39));
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        gameBoard.setupBoard();
    }
}