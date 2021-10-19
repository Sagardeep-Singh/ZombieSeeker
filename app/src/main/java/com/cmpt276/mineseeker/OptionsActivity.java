package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.model.Options;

public class OptionsActivity extends AppCompatActivity {

    private Options options;

    public static Intent getIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        options = Options.getInstance();

        createBoardDimensionRadioButtons();
        createNumMinesRadioButtons();
        handleSaveButton();
    }

    private void handleSaveButton() {
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> {
            updateOptions();
            finish();
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
        saveOptionsToPreferences();
    }

    private void createBoardDimensionRadioButtons() {
        RadioGroup group = findViewById(R.id.radio_group_board_size);
        int []rowOptions = getResources().getIntArray(R.array.board_row_options);
        int []colOptions = getResources().getIntArray(R.array.board_col_options);
        for(int i = 0; i < rowOptions.length; i++){
            DimenButton dimenButton = new DimenButton(this, rowOptions[i], colOptions[i]);
            setButtonGraphics(dimenButton, dimenButton.getRow() + " by " + dimenButton.getCol());
            group.addView(dimenButton);
            if((dimenButton.getRow() == this.options.getNumRows())
                    && (dimenButton.getCol() == this.options.getNumCols())){
                dimenButton.setChecked(true);
            }
        }
    }

    private void createNumMinesRadioButtons() {
        RadioGroup group = findViewById(R.id.num_mines_radio_group);
        int []numMinesOptions = getResources().getIntArray(R.array.num_mines_options);
        for (int numMinesOption : numMinesOptions) {
            RadioButton numMinesButton = new RadioButton(this);
            this.setButtonGraphics(numMinesButton, numMinesOption + "");
            group.addView(numMinesButton);
            if (Integer.parseInt(numMinesButton.getText().toString()) == this.options.getNumMines()) {
                numMinesButton.setChecked(true);
            }
        }
    }

    private void setButtonGraphics(RadioButton rb, String text){
        rb.setText(text);
        rb.setTextColor(Color.WHITE);
        rb.setTextSize(24);
        rb.setButtonTintList(ColorStateList.valueOf(Color.GREEN));

    }

    private void saveOptionsToPreferences() {
        this.getSharedPreferences(MainActivity.SHARED_PREFERENCES_TAG, MODE_PRIVATE)
                .edit()
                .putString(
                        MainActivity.OPTIONS_TAG,
                        MainActivity.getGson().toJson(this.options)
                ).apply();
    }

    public class DimenButton extends androidx.appcompat.widget.AppCompatRadioButton{
        private int row;
        private int col;

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