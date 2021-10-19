package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.mineseeker.databinding.ActivityMainBinding;
import com.cmpt276.model.Options;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static final String SHARED_PREFERENCES_TAG = "SHARED OPTIONS";
    public static final String OPTIONS_TAG = "OPTIONS";
    private Options options;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public static Gson getGson() {
        return new GsonBuilder().create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String optionsString = this.getSharedPreferences(MainActivity.SHARED_PREFERENCES_TAG, MODE_PRIVATE)
                .getString(MainActivity.OPTIONS_TAG, null);

        if (optionsString != null) {
            Options.setInstance(MainActivity.getGson().fromJson(optionsString, Options.class));
        }

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