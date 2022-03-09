//package org.pleasval;

public class Move {
    private final boolean canMove;
    private final int x;
    private final int y;

    public Move(int x, int y, boolean canMove) {
        this.x = x;
        this.y = y;
        this.canMove = canMove;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean canMove() {
        return this.canMove;
    }
}
