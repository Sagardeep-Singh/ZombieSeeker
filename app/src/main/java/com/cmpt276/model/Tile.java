package com.cmpt276.model;

public class Tile {

    private final boolean hasMine;
    private boolean isClicked;

    public Tile(boolean hasMine) {
        this.isClicked = false;
        this.hasMine = hasMine;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public boolean hasBeenClicked() {
        return isClicked;
    }

    public void markAsClicked() {
        this.isClicked = true;
    }


}
