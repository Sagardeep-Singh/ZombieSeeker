package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.cmpt276.model.Game;
import com.cmpt276.model.Options;
import com.cmpt276.model.Tile;

import java.util.Locale;

public class PlayGameActivity extends AppCompatActivity {

    private TileButton[][] buttons;
    private Game game;
    private Options options;

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayGameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        options = Options.getInstance();

        setupGame();
        updateUI();
    }

    private void updateUI() {
        TextView tvScanCount = this.findViewById(R.id.tvScanCount);

        tvScanCount.setText(
                String.format(
                        getString(R.string.game_activity_scan_count),
                        this.game.getScannedTileCount()
                )
        );

        TextView tvMineCount = this.findViewById(R.id.tvMineCount);

        tvMineCount.setText(
                String.format(
                        getString(R.string.game_activity_mine_count),
                        this.game.getRevealedMineCount(),
                        this.game.getMineCount()
                )
        );
    }

    private void setupGame() {

        //Get the number of rows, columns and mines from options
        this.game = new Game(options.getNumRows(), options.getNumCols(), options.getNumMines());
        this.buttons = new TileButton[game.getNumRows()][game.getNumCols()];
        TableLayout tblTiles = findViewById(R.id.tblTiles);

        for (int row = 0; row < game.getNumRows(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            for (int col = 0; col < game.getNumCols(); col++) {
                TileButton btn = new TileButton(this, row, col);
                btn.setLayoutParams(new TableRow.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT,
                        1.0f));
                btn.setBackgroundResource(R.drawable.grave);
                btn.setTextColor(getColor(R.color.white));
                btn.setOnClickListener(view -> handleButtonClick((TileButton) view));

                tableRow.addView(btn);
                this.buttons[row][col] = btn;
            }
            tblTiles.addView(tableRow);
        }
    }


    private void handleButtonClick(TileButton tileButton) {
        Tile tile = this.game.getTile(tileButton.getRow(), tileButton.getCol());

        String string = null;
        if (tile.isScanned()) {
            return;
        }

        if (tile.hasMine() && tile.isMineHidden()) {

            lockButtonSizes();
            setButtonBackground(tileButton, R.drawable.zombie);

            tile.markAsRevealed();

            updateMineCountInRowColForScannedMines(tileButton);

            if (this.game.getMineCount() == this.game.getRevealedMineCount()) {
                FragmentManager fm = this.getSupportFragmentManager();
                DialogFragment fragment = new WinnerDialogFragment(this.game.getScannedTileCount());
                fragment.show(fm, "Winner Dialog");
            }

            updateUI();
            return;
        }

        int count = this.game.getHiddenMineCountRowCol(tileButton.getRow(), tileButton.getCol());
        tileButton.setText(String.format(Locale.ENGLISH, "%d", count));
        tile.markAsScanned();
        updateUI();

    }

    private void lockButtonSizes() {
        for (int i = 0; i < game.getNumRows(); i++) {
            for (int j = 0; j < game.getNumCols(); j++) {
                TileButton btn = this.buttons[i][j];
                int height = btn.getHeight();
                int width = btn.getWidth();

                btn.setMaxHeight(height);
                btn.setMinHeight(height);

                btn.setMaxWidth(width);
                btn.setMinWidth(width);
            }

        }
    }

    private void setButtonBackground(TileButton tileButton, int image) {
        Resources resources = getResources();
        Bitmap originalBitmap = BitmapFactory.decodeResource(resources, image);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, tileButton.getWidth(), tileButton.getHeight(), true);
        tileButton.setBackground(new BitmapDrawable(resources, resizedBitmap));
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

    private static class TileButton extends androidx.appcompat.widget.AppCompatButton {
        private final int row;
        private final int col;

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