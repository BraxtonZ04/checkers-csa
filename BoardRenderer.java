//package org.pleasval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BoardRenderer extends JPanel {
    private final int SLOT_SIZE = 75;
    private final int boxWidth = 250;

    private final Board board;
    private Piece selectedPiece;
    private int selectedPieceX;
    private int selectedPieceY;

    public BoardRenderer(Board board) {
        this.board = board;
        this.createRepaintTimer();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                processClick(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                processMove(e);
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

    private int[] calculatePositionAtMouse(int x, int y) {
        int colX = x / SLOT_SIZE;
        int rowY = y / SLOT_SIZE;

        return new int[]{colX, rowY};
    }

    private Piece calculatePieceAtMouse(int x, int y) {
        int colX = x / SLOT_SIZE;
        int rowY = y / SLOT_SIZE;

        return this.board.getPieceAt(colX, rowY);
    }

    private void processMove(MouseEvent mouseEvent) {

    }

    private void processClick(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();

        if (x > (this.getSLOT_SIZE() * 8) || y > (this.getSLOT_SIZE() * 8)) {
            System.out.println("Tried clicking outside of board.");

            return;
        }

        System.out.println("Mouse X: " + x + " Mouse Y: " + y);

        Piece piece = calculatePieceAtMouse(x, y);
        int[] coordinates = this.calculatePositionAtMouse(x, y);

        boolean selectPiece = piece != null && this.board.getTurn() == piece.getColor();
        if (selectPiece) {
            this.selectedPiece = piece;
            this.selectedPieceX = coordinates[0];
            this.selectedPieceY = coordinates[1]; // TODO
        }

        if (this.selectedPiece != null && piece == null) { // selected empty square
            ArrayList<Move> moves = this.board.calculatePossibleMoves(this.selectedPieceX, this.selectedPieceY);
            ArrayList<Move> jumps = this.board.calculatePossibleJumps(this.selectedPieceX, this.selectedPieceY);
            moves.addAll(jumps);

            for (Move move : moves) {
                if (move.getX3() == coordinates[0] && move.getY3() == coordinates[1] && move.canMove()){
                    this.board.setPieceAt(this.selectedPieceX, this.selectedPieceY, null);
                    this.board.setPieceAt(coordinates[0], coordinates[1], this.selectedPiece);

                    if (move.canCapturePiece()) {
                        this.board.setPieceAt(move.getX2(), move.getY2(), null);

                        if (this.board.getTurn() == 0) {
                            this.board.setBlueTeamCaptured(this.board.getBlueTeamCaptured() + 1);
                        } else {
                            this.board.setRedTeamCaptured(this.board.getRedTeamCaptured() + 1);
                        }
                    } else {
                        this.board.switchTurn();
                    }

                    this.selectedPiece = null;
                    this.selectedPieceY = 0;
                    this.selectedPieceX = 0;

                    return;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int PIECE_PADDING = 10;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    graphics2D.setColor(Color.WHITE);
                } else {
                    graphics2D.setColor(Color.BLACK);
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

                if (this.selectedPiece != null) {
                    ArrayList<Move> jumps = this.board.calculatePossibleJumps(this.selectedPieceX, this.selectedPieceY);
                    ArrayList<Move> moves = this.board.calculatePossibleMoves(this.selectedPieceX, this.selectedPieceY);
                    moves.addAll(jumps);

                    for (Move move : moves) {
                        if (!move.canCapturePiece() && move.getX3() == x && move.getY3() == y && move.canMove()) { // not a jump move
                            graphics2D.setColor(Color.DARK_GRAY);
                        }

                        if (move.canCapturePiece() && move.getX2() == x && move.getY2() == y && move.canMove()) {
                            graphics2D.setColor(Color.BLACK);
                        }

                        if (move.canCapturePiece() && move.getX3() == x && move.getY3() == y && move.canMove()) {
                            graphics2D.setColor(Color.DARK_GRAY);
                        }

                        graphics2D.fillOval(x * SLOT_SIZE + (SLOT_SIZE / 2) - 10, y * SLOT_SIZE + (SLOT_SIZE / 2) - 10, 20, 20);
                    }

                }
            }
        }

        int boardSizeOffset = SLOT_SIZE * 8;

        // menu background
        graphics2D.setColor(Color.LIGHT_GRAY);
        graphics2D.fillRect(boardSizeOffset, 0, boxWidth, boardSizeOffset);

        //turn text
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("Arial", Font.BOLD, 20));
        graphics2D.drawString("Player " + (this.board.getTurn() + 1) + "'s turn!", boardSizeOffset + 10, 30);

        //boxes
        int boxHeight = 55;
        int boxPadding = 10;
        int boxTopOffset = 50;

        for (int i = 0; i < 2; i++) {
            if (this.board.getTurn() == i) {
                graphics2D.setColor(Color.DARK_GRAY);
            } else {
                graphics2D.setColor(Color.GRAY);
            }

            int boxX = boardSizeOffset + boxPadding;
            int boxY = boxTopOffset + (i * boxHeight) + (i * boxPadding);

            graphics2D.fillRoundRect(boxX, boxY, 250 - (boxPadding * 2), boxHeight, 10, 10);
            graphics2D.setColor(Color.BLACK);

            // player name drawing
            graphics2D.drawString("Player " + (i + 1), boxX + 5, boxY + 20);

            int capturedSize = (int) ((boxWidth - (4 * 12) - (boxPadding * 2)) / 12d);
            for (int i2 = 0; i2 < this.board.getCapturedPieces(i); i2++) {
                if (i == 0) {
                    graphics2D.setColor(Color.BLUE);
                } else {
                    graphics2D.setColor(Color.RED);
                }

                graphics2D.fillOval(boxX + (capturedSize * i2) + (4 * (i2 + 1)), boxY + 30, capturedSize, capturedSize);
            }
        }
    }

    public int getSLOT_SIZE() {
        return this.SLOT_SIZE;
    }

    public int getBoxWidth() {
        return this.boxWidth;
    }
}
