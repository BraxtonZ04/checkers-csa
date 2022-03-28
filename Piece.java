//package org.pleasval;

public class Piece {
    private boolean isKing;
    private final int color; // 0 - white 1 - black

    public Piece(int color) {
        this.color = color;
        this.isKing = false;
    }

    public void setKing(boolean king) {
        this.isKing = king;
    }

    public boolean isKing() {
        return this.isKing;
    }

    public int getColor() {
        return this.color;
    }
}
