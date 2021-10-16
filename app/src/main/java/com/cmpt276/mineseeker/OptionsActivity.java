package com.cmpt276.mineseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        createBoardDimensionRadioButtons();
        createNumMinesRadioButtons();
    }

    private void createBoardDimensionRadioButtons() {
        RadioGroup group = findViewById(R.id.radio_group_board_size);
        int []rowOptions = getResources().getIntArray(R.array.board_row_options);
        int []colOptions = getResources().getIntArray(R.array.board_col_options);
        for(int i = 0; i < rowOptions.length; i++){
           String boardDimens = rowOptions[i] + " by " + colOptions[i];
            RadioButton dimenButton = new RadioButton(this);
            setButtonGraphics(dimenButton, boardDimens);
            group.addView(dimenButton);
        }
    }

    private void createNumMinesRadioButtons() {
        RadioGroup group = findViewById(R.id.num_mines_radio_group);
        int []numMinesOptions = getResources().getIntArray(R.array.num_mines_options);
        for(int i = 0; i < numMinesOptions.length; i++){
            RadioButton numMinesButton = new RadioButton(this);
            this.setButtonGraphics(numMinesButton, numMinesOptions[i] + "" );
            group.addView(numMinesButton);
        }
    }

    private void setButtonGraphics(RadioButton rb, String text){
        rb.setText(text);
        rb.setTextColor(Color.WHITE);
        rb.setTextSize(24);
        rb.setButtonTintList(ColorStateList.valueOf(Color.GREEN));

    }
}