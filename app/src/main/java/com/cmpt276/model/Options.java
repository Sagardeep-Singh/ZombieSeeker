package com.cmpt276.model;

import java.util.HashMap;
import java.util.Locale;

public class Options {
    public static final int DEFAULT_VALUE = 0;

    private final int DEFAULT_ROWS = 4;
    private final int DEFAULT_COLS = 6;
    private final int DEFAULT_MINES = 6;
    private int numRows = DEFAULT_ROWS;
    private int numCols = DEFAULT_COLS;
    private int numMines = DEFAULT_MINES;

    private static Options instance;
    private final HashMap<String, Integer> configMap = new HashMap<>();

    public static Options getInstance(){
        if(instance == null){
             instance = new Options();
        }
        return instance;
    }

    public Integer getConfigRunCount(){
        String key = this.createKeyString();
        return configMap.getOrDefault(key, DEFAULT_VALUE);
    }

    public void increaseConfigRunCount(){
        String key = this.createKeyString();
        configMap.put(key, getConfigRunCount() + 1);
    }

    public String createKeyString(){
        return String.format(Locale.ENGLISH,"%d_%d_%d",this.getNumRows(),this.getNumCols(),this.getNumMines());
    }

    public void eraseTimesPlayed(){
        configMap.remove(createKeyString());
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
}
