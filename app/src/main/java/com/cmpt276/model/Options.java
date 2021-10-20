package com.cmpt276.model;

import java.util.HashMap;
import java.util.Locale;

/**
 * Class to hold game wide options to be used by the game class and UI
 */
public class Options {
    public static final int DEFAULT_VALUE = 0;

    private final int DEFAULT_ROWS = 4;
    private final int DEFAULT_COLS = 6;
    private final int DEFAULT_MINES = 6;

    private int numRows;
    private int numCols;
    private int numMines;


    private static Options instance;
    private final HashMap<String, Integer> configMap = new HashMap<>();

    public static Options getInstance(){
        if(instance == null){
             instance = new Options();
        }
        return instance;
    }

    public static void setInstance(Options instance) {
        Options.instance = instance;
    }

    private Options(){
        numRows = DEFAULT_ROWS;
        numCols = DEFAULT_COLS;
        numMines = DEFAULT_MINES;
    }

    public Integer getCurrentConfigRunCount(){
        String key = this.createCurrentKeyString();
        return configMap.getOrDefault(key, DEFAULT_VALUE);
    }

    public void increaseCurrentConfigRunCount(){
        String key = this.createCurrentKeyString();
        configMap.put(key, getCurrentConfigRunCount() + 1);
    }

    public String createCurrentKeyString(){
        return String.format(Locale.ENGLISH,"%d_%d_%d",this.getNumRows(),this.getNumCols(),this.getNumMines());
    }

    public void eraseCurrentTimesPlayed(){
        configMap.remove(createCurrentKeyString());
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
}
