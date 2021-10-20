package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    public static final String CONTINUE_GAME_TAG = "CONTINUE GAME";

    public static Intent getIntentForNewGame(Context context) {
        Intent intent = new Intent(context, PlayGameActivity.class);
        intent.putExtra(CONTINUE_GAME_TAG, false);
        return intent;
    }

    public static Intent getIntentForIncompleteGame(Context context) {
        Intent intent = new Intent(context, PlayGameActivity.class);
        intent.putExtra(CONTINUE_GAME_TAG, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        options = Options.getInstance();

        setupGame();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupUpdateButtonTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveGameToPreferences();
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
        TextView tvGameCount = this.findViewById(R.id.tvGameCount);

        tvGameCount.setText(
                String.format(
                        getString(R.string.game_count_text),
                        this.options.getConfigRunCount()
                )
        );


    }

    private void setupUpdateButtonTimer() {
        CountDownTimer timer = new CountDownTimer(300, 300) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                showScannedTiles();
            }
        };
        timer.start();
    }
    private void setupGame() {

        if(this.getIntent().getBooleanExtra(CONTINUE_GAME_TAG, false)){
            String gameString = MainActivity.getGameString(this);
            this.game = MainActivity.getGson().fromJson(gameString, Game.class);
            this.game.registerObservers();
        }else{
            this.game = new Game(options.getNumRows(), options.getNumCols(), options.getNumMines());
            options.increaseConfigRunCount();
            OptionsActivity.saveOptionsToPreferences(this, options);
        }

        //Get the number of rows, columns and mines from options
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
                btn.setTextSize(24);

                btn.setOnClickListener(view -> handleButtonClick((TileButton) view));

                tableRow.addView(btn);
                this.buttons[row][col] = btn;
            }
            tblTiles.addView(tableRow);
        }
    }

    private void showScannedTiles() {
        for (int i = 0; i < game.getNumRows(); i++) {
            for (int j = 0; j < game.getNumCols(); j++) {
                Tile currentTile = this.game.getTile(i,j);
                TileButton btn = this.buttons[i][j];
                if(!currentTile.isMineHidden()){
                    lockButtonSizes();
                    setButtonBackground(btn, R.drawable.zombie);
                }
                if(currentTile.isScanned()){
                    int count = this.game.getHiddenMineCountRowCol(i, j);
                    btn.setText(String.format(Locale.ENGLISH, "%d", count));
                }
            }
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

    private void saveGameToPreferences() {
        this.game.unRegisterObservers();
        this.getSharedPreferences(MainActivity.SHARED_GAME_TAG, MODE_PRIVATE)
                .edit()
                .putString(
                        MainActivity.GAME_TAG,
                        MainActivity.getGson().toJson(this.game)
                ).apply();
    }
}