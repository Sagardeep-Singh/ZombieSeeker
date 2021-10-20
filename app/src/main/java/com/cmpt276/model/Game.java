package com.cmpt276.model;

import java.util.ArrayList;

/**
 * Class to represent zombie seeker game
 */
public class Game implements Tile.TileScanObserver, Tile.MineRevealObserver {

    private final int numRows;
    private final int numCols;
    private final Tile[][] tiles;
    private final ArrayList<Integer> mineIndices;
    private int scannedTiles = 0;
    private int revealedMines = 0;

    public Game(int numRows, int numCols, int numMines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.tiles = new Tile[numRows][numCols];
        this.mineIndices = new ArrayList<>();
        this.setupMineIndices(numMines);
        this.setupTiles();
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

    private void setupTiles() {

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                boolean isMine = this.mineIndices.contains((i * numCols) + j);
                Tile tile = new Tile(isMine);
                this.tiles[i][j] = tile;
            }
        }

        this.registerObservers();
    }

    public int getHiddenMineCountRowCol(int row, int col) {
        int mineCount = 0;

        for (int index : mineIndices) {
            int mineRow = index / this.numCols;
            int mineCol = index % this.numCols;
            Tile tile = this.tiles[mineRow][mineCol];
            if ((mineRow == row || mineCol == col) && tile.isMineHidden()) {
                mineCount++;
            }
        }

        return mineCount;
    }

    public int getMineCount() {

        return this.mineIndices.size();
    }

    public int getRevealedMineCount() {

        return revealedMines;
    }

    public int getScannedTileCount() {

        return scannedTiles;
    }

    public Tile getTile(int row, int col) {

        return this.tiles[row][col];
    }

    @Override
    public void notifyTileScanned() {

        this.scannedTiles++;
    }

    @Override
    public void notifyMineRevealed() {

        this.revealedMines++;
    }

    public void registerObservers() {
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                tile.registerScanObserver(this);

                if(tile.hasMine()){
                    tile.registerRevealObserver(this);
                }
            }
        }
    }

    public void unRegisterObservers() {
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                tile.unregisterScanObserver(this);

                if(tile.hasMine()){
                    tile.unregisterRevealObserver(this);
                }
            }
        }
    }
}
