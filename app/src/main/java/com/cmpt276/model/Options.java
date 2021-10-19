package com.cmpt276.model;

public class Options {
    private final int DEFAULT_ROWS = 4;
    private final int DEFAULT_COLS = 6;
    private final int DEFAULT_MINES = 6;
    private int numRows = DEFAULT_ROWS;
    private int numCols = DEFAULT_COLS;
    private int numMines = DEFAULT_MINES;
    private int timesPlayed;
    private static Options instance;

    public static Options getInstance(){
        if(instance == null){
             instance = new Options();
        }
        return instance;
    }

    public static void setInstance(Options instance) {
        Options.instance = instance;
    }


    public Options(){
    }

    public void setNumRows(int numRows){
        this.numRows = numRows;
    }

    public void setNumCols(int numCols){
        this.numCols = numCols;
    }

    public void setNumMines(int numMines){
        this.numMines = numMines;
    }

    public int getNumRows(){
        return this.numRows;
    }

    public int getNumCols(){
        return this.numCols;
    }

    public int getNumMines(){
        return this.numMines;
    }

    public int getTimesPlayed(){
        return this.timesPlayed;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }
}
