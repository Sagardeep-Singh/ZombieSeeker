package com.cmpt276.model;

public class Tile {

    private boolean isMine;
    private int rowIndex;
    private int colIndex;
    private boolean isScanned;

    public Tile(){
        this.isScanned = false;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public boolean getIsScanned() {
        return isScanned;
    }

    public void setIsScanned(){
        this.isScanned = true;
    }


}
