package com.cmpt276.model;

public class Tile {

    private final boolean hasMine;
    private boolean isScanned;
    private boolean isMineRevealed;

    public Tile(boolean hasMine) {
        this.isScanned = false;
        this.isMineRevealed = false;
        this.hasMine = hasMine;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public boolean isScanned() {
        return isScanned;
    }

    public boolean isMineRevealed() {
        return isMineRevealed;
    }

    public void markAsScanned() {
        this.isScanned = true;
    }

    public void markAsRevealed() {
        isMineRevealed = true;
    }
}
