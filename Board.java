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
    private AIPiece lastJumpPiece;

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
        if (blueTeamCaptured == 12) {
            Main.endGame(0);
        }

        this.blueTeamCaptured = blueTeamCaptured;
    }

    public void setRedTeamCaptured(int redTeamCaptured) {
        if (redTeamCaptured == 12) {
            Main.endGame(1);
        }

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

    public void setLastJumpPiece(AIPiece lastJumpPiece) {
        this.lastJumpPiece = lastJumpPiece;
    }

    public AIPiece getLastJumpPiece() {
        return this.lastJumpPiece;
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

        if (piece == null) return new ArrayList<>();

        ArrayList<Move> positions = new ArrayList<>();

        //front facing group
        Piece piece1 = this.getPieceAt(x + 1, y + 1);
        Piece piece2 = this.getPieceAt(x + 2, y + 2);
        Piece piece3 = this.getPieceAt(x - 1, y + 1);
        Piece piece4 = this.getPieceAt(x - 2, y + 2);

        //back facing group
        Piece piece5 = this.getPieceAt(x - 1, y - 1); // piece 1
        Piece piece6 = this.getPieceAt(x - 2, y - 2); // piece 2
        Piece piece7 = this.getPieceAt(x + 1, y - 1); // piece 3
        Piece piece8 = this.getPieceAt(x + 2, y - 2); // piece 4

        if (piece.isKing()) {
            //king move logic

            boolean rightSidePositioning = this.isValidPosition(x + 1, y + 1) && this.isValidPosition(x + 2, y + 2);

            if ((piece1 != null && piece2 == null) && piece1.getColor() != piece.getColor() && rightSidePositioning) {
                positions.add(new Move(x, y, x + 1, y + 1, x + 2, y + 2, true));
            }

            //group 2 left side
            boolean leftSidePositioning = this.isValidPosition(x - 1, y + 1) && this.isValidPosition(x - 2, y + 2);

            if ((piece3 != null && piece4 == null) && piece3.getColor() != piece.getColor() && leftSidePositioning) {
                positions.add(new Move(x, y, x - 1, y + 1, x - 2, y + 2, true));
            }

            boolean rightSidePositioning2 = this.isValidPosition(x - 1, y - 1) && this.isValidPosition(x - 2, y - 2);

            if ((piece5 != null && piece6 == null) && piece5.getColor() != piece.getColor() && rightSidePositioning2) {
                positions.add(new Move(x, y, x - 1, y - 1, x - 2, y - 2, true));
            }

            //group 2 left side
            boolean leftSidePositioning2 = this.isValidPosition(x + 1, y - 1) && this.isValidPosition(x + 2, y - 2);

            if ((piece7 != null && piece8 == null) && piece7.getColor() != piece.getColor() && leftSidePositioning2) {
                positions.add(new Move(x, y, x + 1, y - 1, x + 2, y - 2, true));
            }
        } else {
            if (piece.getColor() == 0) {
                //group 1 right side
                boolean rightSidePositioning = this.isValidPosition(x + 1, y + 1) && this.isValidPosition(x + 2, y + 2);

                if ((piece1 != null && piece2 == null) && piece1.getColor() != piece.getColor() && rightSidePositioning) {
                    positions.add(new Move(x, y, x + 1, y + 1, x + 2, y + 2, true));
                }

                //group 2 left side
                boolean leftSidePositioning = this.isValidPosition(x - 1, y + 1) && this.isValidPosition(x - 2, y + 2);

                if ((piece3 != null && piece4 == null) && piece3.getColor() != piece.getColor() && leftSidePositioning) {
                    positions.add(new Move(x, y, x - 1, y + 1, x - 2, y + 2, true));
                }
            } else {
                //group 1 right side
                boolean rightSidePositioning = this.isValidPosition(x - 1, y - 1) && this.isValidPosition(x - 2, y - 2);

                if ((piece5 != null && piece6 == null) && piece5.getColor() != piece.getColor() && rightSidePositioning) {
                    positions.add(new Move(x, y, x - 1, y - 1, x - 2, y - 2, true));
                }

                //group 2 left side
                boolean leftSidePositioning = this.isValidPosition(x + 1, y - 1) && this.isValidPosition(x + 2, y - 2);

                if ((piece7 != null && piece8 == null) && piece7.getColor() != piece.getColor() && leftSidePositioning) {
                    positions.add(new Move(x, y, x + 1, y - 1, x + 2, y - 2, true));
                }
            }
        }

        return positions;
    }

    public ArrayList<Move> calculatePossibleMoves(int x, int y) {
        Piece piece = this.getPieceAt(x, y);

        if (piece == null) {
            return new ArrayList<>();
        }

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

    public ArrayList<Move> getValidMoves(int x, int y) {
        ArrayList<Move> moves = new ArrayList<>();

        moves.addAll(this.calculatePossibleMoves(x, y));
        moves.addAll(this.calculatePossibleJumps(x, y));

        ArrayList<Move> validMoves = new ArrayList<>();

        boolean foundJumps = false;
        for (Move move1 : moves) { // force jump
            if (move1.canCapturePiece()) {
                validMoves.add(move1);

                foundJumps = true;
            }
        }

        if (!foundJumps) validMoves.addAll(moves);
        return validMoves;
    }

    private void invokeAI() {
        ArrayList<AIPiece> pieces = new ArrayList<>();

        if (this.lastJumpPiece == null) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    Piece piece = this.getPieceAt(x, y);

                    if (piece != null && piece.getColor() == 1) {
                        pieces.add(new AIPiece(x, y, piece.getColor()));
                    }
                }
            }
        } else {
            pieces.add(this.lastJumpPiece);
        }

        ArrayList<Move> moves = new ArrayList<>();
        for (AIPiece aiPiece : pieces) {
            moves.addAll(this.getValidMoves(aiPiece.getX(), aiPiece.getY()));
        }
        Collections.shuffle(moves);

        if (moves.size() == 0 && this.lastJumpPiece != null) {
            this.setLastJumpPiece(null);
            this.switchTurn();
            System.out.println("No double jump available.");

            return;
        }

        Move move = moves.get(0);
        for (Move move1 : moves) {
            if (move1.canCapturePiece()) {
                move = move1;
            }
        }

        if (move != null && !move.canCapturePiece() && this.lastJumpPiece != null) {
            this.setLastJumpPiece(null);
            this.switchTurn();
            System.out.println("No double jump available.");

            return;
        }


        AIPiece piece = new AIPiece(move.getX3(), move.getY3(), 1);

        this.setPieceAt(move.getX(), move.getY(), null);
        this.setPieceAt(move.getX3(), move.getY3(), piece); 

        if (move.canCapturePiece()) {
            this.setPieceAt(move.getX2(), move.getY2(), null);

            if (this.turn == 0) {
                this.setBlueTeamCaptured(this.blueTeamCaptured + 1);
            } else {
                this.setRedTeamCaptured(this.redTeamCaptured + 1);
            }

            this.setLastJumpPiece(piece);
            this.invokeAI(); // gets another turn
        } else {
            this.setLastJumpPiece(null);
            this.switchTurn();
        }
    }

    public void setPieceAt(int x, int y, Piece piece) {
        if (piece != null && piece.getColor() == 0 && y == 7) {
            piece.setKing(true);
        } else if (piece != null && piece.getColor() == 1 && y == 0) {
            piece.setKing(true);
        }

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