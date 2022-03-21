//package org.pleasval;

import java.util.ArrayList;

public class Board {
    private final Piece[][] board;
    private final BoardRenderer boardRenderer;
    private int turn = 0; // 0 - blue 1 - red

    public Board() {
        this.board = new Piece[8][8];
        this.boardRenderer = new BoardRenderer(this);
    }

    public void setupBoard() {
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                this.board[y][x] = new Piece(0);
            }
        }

        for (int y = 6; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                this.board[y][x] = new Piece(1);
            }
        }
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return this.turn;
    }

    public boolean canMoveTo(Piece piece, int fromX, int fromY, int toX, int toY) {
        Piece occupying = this.getPieceAt(toX, toY);

        if (occupying != null) {
            return false;
        }

        if (piece.getColor() == 0 && !piece.isKing()) {
            int distanceX = fromX - toX;
            int distanceY = fromY - toY;

            System.out.println(distanceX + "  " + distanceY);

            return (distanceX == -1 && distanceY == -1) || (distanceX == 1 && distanceY == -1);
        } else if (piece.getColor() == 1 && !piece.isKing()) {
            int distanceX = fromX - toX;
            int distanceY = fromY - toY;

            return (distanceX == 1 && distanceY == 1) || (distanceX == -1 && distanceY == 1);
        } else {
            int distanceX = Math.abs(fromX - toX);
            int distanceY = Math.abs(fromY - toY);

            return distanceX == 1 && distanceY == 1;
        }
    }

    public ArrayList<Move> calculatePossibleJumps(int x, int y) {
        Piece piece = this.getPieceAt(x, y);

        if (piece.isKing()) {
            //king move logic
            return null;
        } else {
            ArrayList<Move> positions = new ArrayList<>();

            for (int y2 = 0; y2 < 8; y2++) {
                for (int x2 = 0; x2 < 8; x2++) {
                    positions.add(new Move(x2, y2, this.canMoveTo(piece, x, y, x2, y2)));
                }
            }

            return positions;
            //normal move logic
        }
    }

    public ArrayList<Move> calculatePossibleMoves(int x, int y) {
        Piece piece = this.getPieceAt(x, y);

        if (piece == null) {
            return new ArrayList<>();
        }

        if (piece.isKing()) {
            //king move logic
            return null;
        } else {
            ArrayList<Move> positions = new ArrayList<>();

            for (int y2 = 0; y2 < 8; y2++) {
                for (int x2 = 0; x2 < 8; x2++) {
                    positions.add(new Move(x2, y2, this.canMoveTo(piece, x, y, x2, y2)));
                }
            }

            return positions;
            //normal move logic
        }
    }

    public void setPieceAt(int x, int y, Piece piece) {
        this.board[y][x] = piece;
    }

    public Piece getPieceAt(int x, int y) {
        return this.board[y][x];
    }

    public Piece[][] getBoard() {
        return board;
    }

    public BoardRenderer getBoardRenderer() {
        return boardRenderer;
    }
}
