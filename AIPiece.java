//package org.pleasval;

public class AIPiece extends Piece {
    private final int x;
    private final int y;

    public AIPiece(int x, int y, int color) {
        super(color);

        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
