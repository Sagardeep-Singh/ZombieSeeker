package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.model.Game;
import com.cmpt276.model.Options;
import com.google.gson.Gson;

public class OptionsActivity extends AppCompatActivity {

    public static final String SHARED_OPTIONS_TAG = "SHARED OPTIONS";
    public static final String SHARED_GAME_TAG = "SHARED GAME";
    public static final String OPTIONS_TAG = "OPTIONS";
    public static final String GAME_TAG = "GAME";

    private Options options;

    public static Intent getIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    public static void saveGame(Context context, Game game) {
        context.getSharedPreferences(OptionsActivity.SHARED_GAME_TAG, MODE_PRIVATE)
                .edit()
                .putString(
                        OptionsActivity.GAME_TAG,
                        OptionsActivity.getGson().toJson(game)
                ).apply();
    }

    public static Game getSavedGame(Context context) {
        String gameString = context.getSharedPreferences(
                OptionsActivity.SHARED_GAME_TAG,
                MODE_PRIVATE
        ).getString(OptionsActivity.GAME_TAG, null);

        Game game = null;
        if (gameString != null) {
            game = OptionsActivity.getGson().fromJson(gameString, Game.class);
        }

        return game;
    }

    public static void deleteSavedGame(Context context) {
        context.getSharedPreferences(
                OptionsActivity.SHARED_GAME_TAG,
                MODE_PRIVATE
        ).edit().clear().apply();
    }

    public static void saveOptions(Context context, Options options) {
        context.getSharedPreferences(
                OptionsActivity.SHARED_OPTIONS_TAG,
                MODE_PRIVATE
        ).edit().putString(
                OptionsActivity.OPTIONS_TAG,
                OptionsActivity.getGson().toJson(options)
        ).apply();
    }

    public static Options getSavedOptions(Context context) {
        String optionsString = context.getSharedPreferences(
                OptionsActivity.SHARED_OPTIONS_TAG,
                MODE_PRIVATE
        ).getString(OptionsActivity.OPTIONS_TAG, null);

        Options options = null;
        if (optionsString != null) {
            options = OptionsActivity.getGson().fromJson(optionsString, Options.class);
        }

        return options;
    }

    private static Gson getGson() {
        return new Gson();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        options = Options.getInstance();

        createBoardDimensionRadioButtons();
        createNumMinesRadioButtons();
        handleSaveButton();
        handleEraseTimesPlayedButton();
    }

    private void createBoardDimensionRadioButtons() {
        RadioGroup group = findViewById(R.id.radio_group_board_size);

        int[] rowOptions = getResources().getIntArray(R.array.board_row_options);
        int[] colOptions = getResources().getIntArray(R.array.board_col_options);

        for (int i = 0; i < rowOptions.length; i++) {
            DimenButton dimenButton = new DimenButton(this, rowOptions[i], colOptions[i]);
            setButtonGraphics(dimenButton, dimenButton.getRow() + " x " + dimenButton.getCol());
            group.addView(dimenButton);
            if ((dimenButton.getRow() == this.options.getNumRows())
                    && (dimenButton.getCol() == this.options.getNumCols())) {
                dimenButton.setChecked(true);
            }
        }
    }

    private void createNumMinesRadioButtons() {
        RadioGroup group = findViewById(R.id.num_mines_radio_group);

        int[] numMinesOptions = getResources().getIntArray(R.array.num_mines_options);

        for (int numMinesOption : numMinesOptions) {
            RadioButton numMinesButton = new RadioButton(this);
            this.setButtonGraphics(numMinesButton, numMinesOption + "");
            group.addView(numMinesButton);
            if (Integer.parseInt(numMinesButton.getText().toString()) == this.options.getNumMines()) {
                numMinesButton.setChecked(true);
            }
        }
    }

    private void handleSaveButton() {
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(view -> {
            OptionsActivity.deleteSavedGame(this);
            updateOptions();
            finish();
        });
    }

    private void handleEraseTimesPlayedButton() {
        Button eraseButton = findViewById(R.id.erase_button);

        eraseButton.setOnClickListener(view -> {
            options.eraseCurrentTimesPlayed();
            OptionsActivity.deleteSavedGame(this);
            Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateOptions() {
        RadioGroup dimenGroup = findViewById(R.id.radio_group_board_size);
        RadioGroup mineGroup = findViewById(R.id.num_mines_radio_group);
        DimenButton db = findViewById(dimenGroup.getCheckedRadioButtonId());
        RadioButton rb = findViewById(mineGroup.getCheckedRadioButtonId());

        options.setNumRows(db.getRow());
        options.setNumCols(db.getCol());
        options.setNumMines(Integer.parseInt(rb.getText().toString()));

        OptionsActivity.saveOptions(this, this.options);
    }

    private void setButtonGraphics(RadioButton rb, String text) {
        rb.setText(text);
        rb.setTextColor(Color.WHITE);
        rb.setTextSize(24);
        rb.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
    }


    private static class DimenButton extends androidx.appcompat.widget.AppCompatRadioButton {
        private final int row;
        private final int col;

        DimenButton(Context context, int row, int col) {
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