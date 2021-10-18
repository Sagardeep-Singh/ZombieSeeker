package com.cmpt276.model;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    private final boolean hasMine;
    private boolean isScanned;
    private boolean isMineRevealed;
    private List<TileScanObserver> tileScanObservers = new ArrayList<>();
    private List<MineRevealObserver> mineRevealObservers = new ArrayList<>();

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

    public boolean isMineHidden() {

        return !isMineRevealed;
    }

    public void markAsScanned() {

        this.isScanned = true;
        this.notifyTileScanObservers();
    }

    public void markAsRevealed() {

        isMineRevealed = true;
        this.notifyMineRevealObservers();
    }

    public void registerScanObserver(TileScanObserver obs) {

        tileScanObservers.add(obs);
    }

    public void registerRevealObserver(MineRevealObserver obs) {

        mineRevealObservers.add(obs);
    }

    public void unregisterScanObserver(TileScanObserver obs) {

        tileScanObservers.remove(obs);
    }

    public void unregisterRevealObserver(MineRevealObserver obs) {

        mineRevealObservers.remove(obs);
    }

    private void notifyTileScanObservers() {
        for (TileScanObserver obs : tileScanObservers) {
            obs.notifyTileScanned();
        }
    }

    private void notifyMineRevealObservers() {
        for (MineRevealObserver obs : mineRevealObservers) {
            obs.notifyMineRevealed();
        }
    }

    public interface TileScanObserver {
        void notifyTileScanned();
    }

    public interface MineRevealObserver {
        void notifyMineRevealed();
    }
}
