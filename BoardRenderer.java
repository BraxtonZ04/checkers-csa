//package org.pleasval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BoardRenderer extends JPanel {
    private final int SLOT_SIZE = 75;
    private final int PIECE_PADDING = 10;
    private final Board board;
    private Piece selectedPiece;
    private int selectedPieceX;
    private int selectedPieceY;
    private ArrayList<Move> possibleMoves;

    public BoardRenderer(Board board) {
        this.board = board;
        this.createRepaintTimer();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                processClick(e);
            }
        });
    }

    private void createRepaintTimer() {
        final Timer timer = new Timer(15, null);

        timer.addActionListener(e -> {
            if (!this.isVisible()) {
                timer.stop();
            } else {
                this.repaint();
            }
        });

        timer.start();
    }

    public int[] calculatePositionAtMouse(int x, int y) {
        int colX = x / SLOT_SIZE;
        int rowY = y / SLOT_SIZE;

        return new int[]{colX, rowY};
    }

    public Piece calculatePieceAtMouse(int x, int y) {
        int colX = x / SLOT_SIZE;
        int rowY = y / SLOT_SIZE;

        this.selectedPieceX = colX;
        this.selectedPieceY = rowY;//TODO

        System.out.println(colX + " " + rowY);

        return this.board.getPieceAt(colX, rowY);
    }

    public void processClick(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();

        System.out.println("Mouse X: " + x + " Mouse Y: " + y);

        Piece piece = calculatePieceAtMouse(x, y);

        boolean selectPiece = piece != null && this.board.getTurn() == piece.getColor();
        if (selectPiece) {
            this.selectedPiece = piece;
        }

        if (this.selectedPiece != null && piece == null) { // selected empty square
            this.possibleMoves = this.board.calculatePossibleMoves(this.selectedPieceX, this.selectedPieceY);

            for (Move move : possibleMoves) {
                if (move.getX() == x && move.getY() == y && move.canMove()){
                    this.board.setPieceAt(this.selectedPieceX, this.selectedPieceY, null);

                    int[] coordinates = this.calculatePositionAtMouse(x, y);
                    this.board.setPieceAt(coordinates[0], coordinates[1], this.selectedPiece);
                }
            }
        }

        //this.board.setPieceAt(5, 5, new Piece(0));
    }

    @Override
    public void paintComponent(Graphics g) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }

                if (this.selectedPiece != null) {
                    this.possibleMoves = this.board.calculatePossibleMoves(this.selectedPieceX, this.selectedPieceY);

                    for (Move move : possibleMoves) {
                        if (move.getX() == x && move.getY() == y && move.canMove()){
                            g.setColor(Color.GREEN);
                        }
                    }
                }
                g.fillRect(x * SLOT_SIZE, y * SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);

                Piece piece = this.board.getPieceAt(x, y);
                if (piece != null) {
                    if (piece == this.selectedPiece) {
                        g.setColor(Color.YELLOW);
                    } else if (piece.getColor() == 0) {
                        g.setColor(Color.RED);
                    } else if (piece.getColor() == 1) {
                        g.setColor(Color.BLUE);
                    }

                    g.fillOval(x * SLOT_SIZE + (PIECE_PADDING / 2), y * SLOT_SIZE + (PIECE_PADDING / 2), SLOT_SIZE - PIECE_PADDING, SLOT_SIZE - PIECE_PADDING);
                }


                if ((x + y) % 2 == 0) { // opposite color for text
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }

                int slot = x + (y * 8);
                double cellTextX = ((x + 1) * SLOT_SIZE) - (SLOT_SIZE / 2.0);
                double cellTextY = ((y + 1) * SLOT_SIZE) - (SLOT_SIZE / 2.0);

                g.drawString(String.valueOf(slot), (int) cellTextX, (int) cellTextY);
            }
        }
    }
}
