package com.cmpt276.mineseeker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.cmpt276.mineseeker.databinding.ActivityMainBinding;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpPlayGameButton();
        setUpOptionsButton();
        setUpHelpButton();
    }

    private void setUpPlayGameButton() {
        Button playGameButton = findViewById(R.id.play_game_button);
        playGameButton.setOnClickListener(view -> {
            Intent i = PlayGameActivity.getIntent(MainActivity.this);
            startActivity(i);
        });

    }

    private void setUpOptionsButton(){
        Button optionsButton = findViewById(R.id.options_button);
        optionsButton.setOnClickListener(view -> {
            Intent i = OptionsActivity.getIntent(MainActivity.this);
            startActivity(i);
        });
    }

    private void setUpHelpButton(){
        Button helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(view -> {
            Intent i = HelpActivity.getIntent(MainActivity.this);
            startActivity(i);
        });

    }


}