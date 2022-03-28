//package org.pleasval;

public class Move {
    private final boolean canMove;
    private boolean capturePiece;

    private final int x;
    private final int y;

    private int x2 = -1; // taking piece
    private int y2 = -1;

    private final int x3; // final position
    private final int y3;

    public Move(int x, int y, int x3, int y3, boolean canMove) {
        this.x = x;
        this.y = y;
        this.x3 = x3;
        this.y3 = y3;
        this.canMove = canMove;
    }

    public Move(int x, int y, int x2, int y2, int x3, int y3, boolean canMove) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.canMove = canMove;
        this.capturePiece = true;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY2() {
        return this.y2;
    }

    public int getX3() {
        return this.x3;
    }

    public int getY3() {
        return this.y3;
    }

    public boolean canMove() {
        return this.canMove;
    }

    public boolean canCapturePiece() {
        return capturePiece;
    }
}
