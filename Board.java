//package org.pleasval;

import java.util.ArrayList;
import java.util.Collections;

public class Board {
    private final Piece[][] board;
    private final BoardRenderer boardRenderer;
    private int turn = 0; // 0 - blue 1 - red
    private int blueTeamCaptured = 0;
    private int redTeamCaptured = 0;
    private final boolean ai; // ai is always second player so player 1 (red)

    public Board(boolean ai) {
        this.ai = ai;
        this.board = new Piece[8][8];
        this.boardRenderer = new BoardRenderer(this);
    }

    public void setupBoard() {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 != 0) {
                    this.board[y][x] = new Piece(0);
                }
            }
        }

        for (int y = 5; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 != 0) {
                    this.board[y][x] = new Piece(1);
                }
            }
        }
    }

    public void setBlueTeamCaptured(int blueTeamCaptured) {
        this.blueTeamCaptured = blueTeamCaptured;
    }

    public void setRedTeamCaptured(int redTeamCaptured) {
        this.redTeamCaptured = redTeamCaptured;
    }

    public int getCapturedPieces(int team) {
        if (team == 0) {
            return this.blueTeamCaptured;
        } else {
            return this.redTeamCaptured;
        }
    }

    public int getBlueTeamCaptured() {
        return this.blueTeamCaptured;
    }

    public int getRedTeamCaptured() {
        return this.redTeamCaptured;
    }

    public void switchTurn() {
        if (this.turn == 0) {
            this.turn = 1;

            if (this.ai) {
                this.invokeAI();
            }
        } else {
            this.turn = 0;
        }
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

            if (piece.getColor() == 0) {
                //group 1 right side
                Piece piece1 = this.getPieceAt(x + 1, y + 1);
                Piece piece2 = this.getPieceAt(x + 2, y + 2);
                boolean rightSidePositioning = this.isValidPosition(x + 1, y + 1) && this.isValidPosition(x + 2, y + 2);

                if ((piece1 != null && piece2 == null) && piece1.getColor() != piece.getColor() && rightSidePositioning) {
                    positions.add(new Move(x, y, x + 1, y + 1, x + 2, y + 2, true));
                }

                //group 2 left side
                Piece piece3 = this.getPieceAt(x - 1, y + 1);
                Piece piece4 = this.getPieceAt(x - 2, y + 2);
                boolean leftSidePositioning = this.isValidPosition(x - 1, y + 1) && this.isValidPosition(x - 2, y + 2);

                if ((piece3 != null && piece4 == null) && piece3.getColor() != piece.getColor() && leftSidePositioning) {
                    positions.add(new Move(x, y, x - 1, y + 1, x - 2, y + 2, true));
                }
            } else {
                //group 1 right side
                Piece piece1 = this.getPieceAt(x - 1, y - 1);
                Piece piece2 = this.getPieceAt(x - 2, y - 2);
                boolean rightSidePositioning = this.isValidPosition(x - 1, y - 1) && this.isValidPosition(x - 2, y - 2);

                if ((piece1 != null && piece2 == null) && piece1.getColor() != piece.getColor() && rightSidePositioning) {
                    positions.add(new Move(x, y, x - 1, y - 1, x - 2, y - 2, true));
                }

                //group 2 left side
                Piece piece3 = this.getPieceAt(x + 1, y - 1);
                Piece piece4 = this.getPieceAt(x + 2, y - 2);
                boolean leftSidePositioning = this.isValidPosition(x + 1, y - 1) && this.isValidPosition(x + 2, y - 2);

                if ((piece3 != null && piece4 == null) && piece3.getColor() != piece.getColor() && leftSidePositioning) {
                    positions.add(new Move(x, y, x + 1, y - 1, x + 2, y - 2, true));
                }
            }

            return positions;
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
                    if (this.canMoveTo(piece, x, y, x2, y2)) {
                        positions.add(new Move(x, y, x2, y2, true));
                    }
                }
            }

            return positions;
        }
    }

    public void invokeAI() {
        ArrayList<AIPiece> pieces = new ArrayList<>();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = this.getPieceAt(x, y);

                if (piece != null && piece.getColor() == 1) {
                    pieces.add(new AIPiece(x, y, piece.getColor()));
                }
            }
        }

        ArrayList<Move> moves = new ArrayList<>();
        for (AIPiece aiPiece : pieces) {
            moves.addAll(this.calculatePossibleMoves(aiPiece.getX(), aiPiece.getY()));
            moves.addAll(this.calculatePossibleJumps(aiPiece.getX(), aiPiece.getY()));
        }
        Collections.shuffle(moves); // randomize and select the first

        Move move = moves.get(0);
        if (move == null) {
            System.exit(0); //TODO means we lost or something went wrong
        }
        
        this.setPieceAt(move.getX(), move.getY(), null);
        this.setPieceAt(move.getX3(), move.getY3(), new Piece(1)); //TODO

        if (move.canCapturePiece()) {
            this.setPieceAt(move.getX2(), move.getY2(), null);

            if (this.turn == 0) {
                this.setBlueTeamCaptured(this.blueTeamCaptured + 1);
            } else {
                this.setRedTeamCaptured(this.redTeamCaptured + 1);
            }

            this.invokeAI(); // gets another turn
        } else {
            this.switchTurn();
        }
    }

    public void setPieceAt(int x, int y, Piece piece) {
        this.board[y][x] = piece;
    }

    private boolean isValidPosition(int x, int y) {
        if (x > 7 || y > 7) return false;
        if (x < 0 || y < 0) return false;

        return true;
    }

    public Piece getPieceAt(int x, int y) {
        if (!this.isValidPosition(x, y)) return null;

        return this.board[y][x];
    }

    public BoardRenderer getBoardRenderer() {
        return boardRenderer;
    }
}
