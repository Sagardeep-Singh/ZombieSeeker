package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.mineseeker.databinding.ActivityMainBinding;
import com.cmpt276.model.Options;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static final String SHARED_OPTIONS_TAG = "SHARED OPTIONS";
    public static final String SHARED_GAME_TAG = "SHARED GAME";
    public static final String OPTIONS_TAG = "OPTIONS";
    public static final String GAME_TAG = "GAME";

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public static Gson getGson() {
        return new GsonBuilder().create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpContinueGameButton();
        setUpPlayGameButton();
        setUpOptionsButton();
        setUpHelpButton();

    }

    private void setUpContinueGameButton() {
        String gameString = getGameString(this);
        if (gameString != null) {
            Button continueGameButton = findViewById(R.id.continue_game_button);
            continueGameButton.setVisibility(View.VISIBLE);
            continueGameButton.setOnClickListener(view -> {
                Intent i = PlayGameActivity.getIntentForIncompleteGame(MainActivity.this);
                startActivity(i);
            });
        }
    }

    public static String getGameString(Context context) {
        return context.getSharedPreferences(MainActivity.SHARED_GAME_TAG, MODE_PRIVATE)
                .getString(MainActivity.GAME_TAG, null);
    }

    private void setUpPlayGameButton() {
        Button playGameButton = findViewById(R.id.play_game_button);
        playGameButton.setOnClickListener(view -> {
            Intent i = PlayGameActivity.getIntentForNewGame(MainActivity.this);
            startActivity(i);
        });

    }

    private void setUpOptionsButton(){
        String optionsString = this.getSharedPreferences(MainActivity.SHARED_OPTIONS_TAG, MODE_PRIVATE)
                .getString(MainActivity.OPTIONS_TAG, null);

        if (optionsString != null) {
            Options.setInstance(MainActivity.getGson().fromJson(optionsString, Options.class));
        }
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