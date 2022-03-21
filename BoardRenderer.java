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

        System.out.println(colX + " " + rowY);

        return this.board.getPieceAt(colX, rowY);
    }

    public void processClick(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();

        System.out.println("Mouse X: " + x + " Mouse Y: " + y);

        Piece piece = calculatePieceAtMouse(x, y);
        int[] coordinates = this.calculatePositionAtMouse(x, y);

        boolean selectPiece = piece != null && this.board.getTurn() == piece.getColor();
        if (selectPiece) {
            this.selectedPiece = piece;
            this.selectedPieceX = coordinates[0];
            this.selectedPieceY = coordinates[1];//TODO
        }

        if (this.selectedPiece != null && piece == null) { // selected empty square
            this.possibleMoves = this.board.calculatePossibleMoves(this.selectedPieceX, this.selectedPieceY);
            System.out.println(this.selectedPieceX + " " + this.selectedPieceY);

            for (Move move : possibleMoves) {
                if (move.getX() == coordinates[0] && move.getY() == coordinates[1] && move.canMove()){
                    System.out.println("tried to move");

                    this.board.setPieceAt(this.selectedPieceX, this.selectedPieceY, null);

                    this.board.setPieceAt(coordinates[0], coordinates[1], this.selectedPiece);
                }
            }

            this.selectedPiece = null;
            this.selectedPieceY = 0;
            this.selectedPieceX = 0;
        }

        //this.board.setPieceAt(5, 5, new Piece(0));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    graphics2D.setColor(Color.WHITE);
                } else {
                    graphics2D.setColor(Color.BLACK);
                }

                if (this.selectedPiece != null) {
                    this.possibleMoves = this.board.calculatePossibleMoves(this.selectedPieceX, this.selectedPieceY);

                    for (Move move : possibleMoves) {
                        if (move.getX() == x && move.getY() == y && move.canMove()){
                            graphics2D.setColor(Color.GREEN);
                        }
                    }
                }
                graphics2D.fillRect(x * SLOT_SIZE, y * SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);

                Piece piece = this.board.getPieceAt(x, y);
                if (piece != null) {
                    if (piece == this.selectedPiece) {
                        graphics2D.setColor(Color.YELLOW);
                    } else if (piece.getColor() == 0) {
                        graphics2D.setColor(Color.RED);
                    } else if (piece.getColor() == 1) {
                        graphics2D.setColor(Color.BLUE);
                    }

                    graphics2D.fillOval(x * SLOT_SIZE + (PIECE_PADDING / 2), y * SLOT_SIZE + (PIECE_PADDING / 2), SLOT_SIZE - PIECE_PADDING, SLOT_SIZE - PIECE_PADDING);
                }


                if ((x + y) % 2 == 0) { // opposite color for text
                    graphics2D.setColor(Color.BLACK);
                } else {
                    graphics2D.setColor(Color.WHITE);
                }

                int slot = x + (y * 8);
                double cellTextX = ((x + 1) * SLOT_SIZE) - (SLOT_SIZE / 2.0);
                double cellTextY = ((y + 1) * SLOT_SIZE) - (SLOT_SIZE / 2.0);

                graphics2D.drawString(String.valueOf(slot), (int) cellTextX, (int) cellTextY);
            }
        }
    }
}
