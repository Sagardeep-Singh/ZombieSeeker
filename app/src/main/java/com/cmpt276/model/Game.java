package com.cmpt276.model;

import java.util.ArrayList;

public class Game {

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    private final int numRows;
    private final int numCols;
    private final Tile[][] tiles;

    public Game(int numRows, int numCols, int numMines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.tiles = new Tile[numRows][numCols];
        this.setupTiles(numMines);
    }

    private ArrayList<Integer> getMineIndices(int numMines) {
        ArrayList<Integer> mineIndices = new ArrayList<>();
        for (int i = 0; mineIndices.size() < numMines; i++) {
            int index = (int) (Math.random() * (numRows * numCols));
            if (!mineIndices.contains(index)) {
                mineIndices.add(index);
            }
        }

        return mineIndices;
    }

    private void setupTiles(int numMines) {
        ArrayList<Integer> mineIndices = getMineIndices(numMines);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.tiles[i][j] = new Tile(mineIndices.contains((i * numCols) + j));
            }
        }
    }

    public int getHiddenMineCountRowCol(int row, int col) {
        int mineCount = 0;

        for (int i = 0; i < numRows; i++) {
            Tile tile = this.tiles[i][col];
            mineCount += tile.hasMine() && !tile.isMineRevealed() ? 1 : 0;
        }

        for (int i = 0; i < numCols; i++) {
            Tile tile = this.tiles[row][i];
            mineCount += tile.hasMine() && !tile.isMineRevealed() ? 1 : 0;
        }

        return mineCount;
    }

    public Tile getTile(int row,int col){
        return this.tiles[row][col];
    }


}
