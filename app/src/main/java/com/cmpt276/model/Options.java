package com.cmpt276.model;

public class Options {
    private int numRows;
    private int numCols;
    private int numMines;
    private int timesPlayed;

    Options(int numRows, int numCols, int numMines, int timesPlayed){
        this.numRows = numRows;
        this.numCols = numCols;
        this.timesPlayed = timesPlayed;
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
