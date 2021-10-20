package com.cmpt276.mineseeker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayGameActivity extends AppCompatActivity {

    public static final int BUTTON_ANIMATION_DURATION = 200;
    public static final float BUTTON_ANIMATION_STRENGTH = 1f;
    public static final String BUTTON_ANIMATION_PROPERTY1 = "scaleX";
    public static final long BUTTON_ANIMATION_DELAY_MULTIPLIER = 100L;
    public static final String CONTINUE_GAME_TAG = "CONTINUE GAME";

    private TileButton[][] buttons;
    private Game game;
    private Options options;
    private AnimatorSet buttonAnimatorSet = new AnimatorSet();

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

        this.game.unRegisterObservers();
        OptionsActivity.saveGame(this, this.game);
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
                        this.options.getCurrentConfigRunCount()
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

        if (this.getIntent().getBooleanExtra(CONTINUE_GAME_TAG, false)) {
            this.game = OptionsActivity.getSavedGame(this);
            this.game.registerObservers();
        } else {
            this.game = new Game(options.getNumRows(), options.getNumCols(), options.getNumMines());
            options.increaseCurrentConfigRunCount();
            OptionsActivity.saveOptions(this, options);
        }

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
                btn.setTypeface(Typeface.DEFAULT_BOLD);
                btn.setTextSize(30);

                btn.setOnClickListener(view -> handleButtonClick((TileButton) view));
                btn.setHapticFeedbackEnabled(true);

                tableRow.addView(btn);
                this.buttons[row][col] = btn;
            }
            tblTiles.addView(tableRow);
        }
    }

    private void showScannedTiles() {
        for (int i = 0; i < game.getNumRows(); i++) {
            for (int j = 0; j < game.getNumCols(); j++) {
                Tile currentTile = this.game.getTile(i, j);
                TileButton btn = this.buttons[i][j];
                if (!currentTile.isMineHidden()) {
                    lockButtonSizes();
                    setButtonBackground(btn, R.drawable.zombie);
                }
                if (currentTile.isScanned()) {
                    int count = this.game.getHiddenMineCountRowCol(i, j);
                    btn.setText(String.format(Locale.ENGLISH, "%d", count));
                }
            }
        }
    }


    private void handleButtonClick(TileButton tileButton) {
        Tile tile = this.game.getTile(tileButton.getRow(), tileButton.getCol());

        if (tile.isScanned()) {
            return;
        }


        if (tile.hasMine() && tile.isMineHidden()) {

            tile.markAsRevealed();
            lockButtonSizes();
            setButtonBackground(tileButton, R.drawable.zombie);
            updateMineCountInRowColForScannedMines(tileButton);

            if (this.game.getMineCount() == this.game.getRevealedMineCount()) {
                handleWinningCondition();
            }
        }else{
            int count = this.game.getHiddenMineCountRowCol(tileButton.getRow(), tileButton.getCol());
            tileButton.setText(String.format(Locale.ENGLISH, "%d", count));
            tile.markAsScanned();
        }

        animateRowColOnScan(tileButton.getRow(), tileButton.getCol());
        updateUI();
    }

    private void handleWinningCondition() {
        FragmentManager fm = this.getSupportFragmentManager();
        DialogFragment fragment = new WinnerDialogFragment(this.game.getScannedTileCount());
        fragment.show(fm, "Winner Dialog");
    }

    private void animateRowColOnScan(int rowIndex, int colIndex) {

        if (buttonAnimatorSet.isRunning()) {
            buttonAnimatorSet.end();
        }

        for (int row = 0; row < this.game.getNumRows(); row++) {
            if (row == rowIndex) {
                continue;
            }
            TileButton button = this.buttons[row][colIndex];
            buttonAnimatorSet.play(getAnimatorSet(button, row));
        }

        for (int col = 0; col < this.game.getNumCols(); col++) {
            if (col == colIndex) {
                continue;
            }
            TileButton button = this.buttons[rowIndex][col];
            buttonAnimatorSet.play(getAnimatorSet(button, col));
        }

        buttonAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                buttonAnimatorSet = new AnimatorSet();
            }
        });
        buttonAnimatorSet.start();
    }

    private void updateMineCountInRowColForScannedMines(TileButton tileButton) {

        for (int row = 0; row < this.game.getNumRows(); row++) {
            Tile tile = this.game.getTile(row, tileButton.getCol());
            TileButton button = this.buttons[row][tileButton.getCol()];
            if (tile.isScanned()) {
                decreaseButtonMineCount(button);
            }
        }

        for (int col = 0; col < this.game.getNumCols(); col++) {
            Tile tile = this.game.getTile(tileButton.getRow(), col);
            TileButton button = this.buttons[tileButton.getRow()][col];
            if (tile.isScanned()) {
                decreaseButtonMineCount(button);
            }
        }
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

    private AnimatorSet getAnimatorSet(TileButton button, int i) {

        AnimatorSet set = new AnimatorSet();

        List<Animator> animatorList = new ArrayList<>();
        animatorList.add(
                ObjectAnimator.ofFloat(
                        button,
                        BUTTON_ANIMATION_PROPERTY1,
                        -BUTTON_ANIMATION_STRENGTH
                ).setDuration(BUTTON_ANIMATION_DURATION)
        );
        animatorList.add(
                ObjectAnimator.ofFloat(
                        button,
                        BUTTON_ANIMATION_PROPERTY1,
                        BUTTON_ANIMATION_STRENGTH
                ).setDuration(BUTTON_ANIMATION_DURATION)
        );
        set.playSequentially(animatorList);
        set.setStartDelay(i * BUTTON_ANIMATION_DELAY_MULTIPLIER);

        return set;
    }

    private void decreaseButtonMineCount(TileButton button) {
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