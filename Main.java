//package org.pleasval;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final JFrame mainFrame = new JFrame("Checkers");
    private static final Board gameBoard = new Board(true);

    public static void main(String[] args) {
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