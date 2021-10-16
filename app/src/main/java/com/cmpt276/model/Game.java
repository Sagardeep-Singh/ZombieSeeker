package com.cmpt276.model;

import java.util.ArrayList;

public class Game {

    private final int numRows;
    private final int numCols;
    private final Tile[][] tiles;
    private final ArrayList<Integer> mineIndices;

    public Game(int numRows, int numCols, int numMines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.tiles = new Tile[numRows][numCols];
        this.mineIndices = new ArrayList<>();

        this.setupMineIndices(numMines);
        this.setupTiles(numMines);
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    private void setupMineIndices(int numMines) {
        for (int i = 0; this.mineIndices.size() < numMines; i++) {
            int index = (int) (Math.random() * (numRows * numCols));
            if (!this.mineIndices.contains(index)) {
                this.mineIndices.add(index);
            }
        }
    }

    private void setupTiles(int numMines) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.tiles[i][j] = new Tile(this.mineIndices.contains((i * numCols) + j));
            }
        }
    }

    public int getHiddenMineCountRowCol(int row, int col) {
        int mineCount = 0;

        for (int index : mineIndices) {
            int mineRow = index / this.numCols;
            int mineCol = index % this.numCols;
            if ((mineRow == row || mineCol == col) && !this.tiles[mineRow][mineCol].isMineRevealed()) {
                mineCount++;
            }
        }

        return mineCount;
    }

    public int getMineCount() {
        return this.mineIndices.size();
    }

    public int getRevealedMineCount() {
        int mineCount = 0;

        for (int index : mineIndices) {
            int mineRow = index / this.numCols;
            int mineCol = index % this.numCols;
            if (this.tiles[mineRow][mineCol].isMineRevealed()) {
                mineCount++;
            }
        }

        return mineCount;
    }

    public int getScannedMineCount() {
        int mineCount = 0;

        for (Tile[] row : this.tiles) {
            for (Tile tile : row) {
                if(tile.isScanned()){
                    mineCount++;
                }
            }
        }

        return mineCount;
    }

    public Tile getTile(int row, int col) {
        return this.tiles[row][col];
    }


}
