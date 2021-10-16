package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.model.Game;
import com.cmpt276.model.Tile;

import java.util.Locale;

public class PlayGameActivity extends AppCompatActivity {

    public static final int NUM_ROWS = 5;
    public static final int NUM_COLS = 7;
    public static final int NUM_MINES = 8;
    private TileButton[][] buttons;
    private Game game;

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayGameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        setupGame();
    }

    private void setupGame() {

        //Get the number of rows, columns and mines from options
        this.game = new Game(NUM_ROWS, NUM_COLS, NUM_MINES);
        this.buttons = new TileButton[NUM_ROWS][NUM_COLS];
        TableLayout tblTiles = findViewById(R.id.tblTiles);

        for (int row = 0; row < NUM_ROWS; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            for (int col = 0; col < NUM_COLS; col++) {
                TileButton btn = new TileButton(this, row, col);
                btn.setLayoutParams(new TableRow.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT,
                        1.0f));

                btn.setOnClickListener(view -> handleButtonClick((TileButton) view));

                tableRow.addView(btn);
                this.buttons[row][col] = btn;
            }
            tblTiles.addView(tableRow);
        }
    }


    private void handleButtonClick(TileButton tileButton) {
        Tile tile = this.game.getTile(tileButton.getRow(), tileButton.getCol());

        if (tile.isScanned()) {
            return;
        }

        if (tile.hasMine() && !tile.isMineRevealed()) {

            tileButton.setBackgroundColor(getColor(R.color.purple_200));
            tile.markAsRevealed();

            updateMineCountInRowColForScannedMines(tileButton);

            return;
        }
        int count = this.game.getHiddenMineCountRowCol(tileButton.getRow(), tileButton.getCol());
        tile.markAsScanned();
        tileButton.setText(String.format(Locale.ENGLISH, "%d", count));
    }

    private void updateMineCountInRowColForScannedMines(TileButton tileButton) {
        for (int row = 0; row < this.game.getNumRows(); row++) {
            Tile tile = this.game.getTile(row, tileButton.getCol());
            if (tile.isScanned()) {
                decreaseButtonMineCount(row, tileButton.getCol());
            }
        }

        for (int col = 0; col < this.game.getNumCols(); col++) {
            Tile tile = this.game.getTile(tileButton.getRow(), col);
            if (tile.isScanned()) {
                decreaseButtonMineCount(tileButton.getRow(), col);
            }
        }
    }

    private void decreaseButtonMineCount(int row, int col) {
        TileButton button = this.buttons[row][col];
        int currentCount = Integer.parseInt(button.getText().toString());
        currentCount--;
        button.setText(String.format(Locale.ENGLISH, "%d", currentCount));
    }

    private class TileButton extends androidx.appcompat.widget.AppCompatButton {
        private int row;
        private int col;

        TileButton(Context context, int row, int col) {
            super(context);
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

}