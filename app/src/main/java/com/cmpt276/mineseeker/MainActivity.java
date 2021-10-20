package com.cmpt276.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.mineseeker.databinding.ActivityMainBinding;
import com.cmpt276.model.Game;
import com.cmpt276.model.Options;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Options options = OptionsActivity.getSavedOptions(this);
        if(options != null){
            Options.setInstance(options);
        }

        setUpPlayGameButton();
        setUpOptionsButton();
        setUpHelpButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpContinueGameButton();

    }

    private void setUpContinueGameButton() {
        Button continueGameButton = findViewById(R.id.continue_game_button);

        Game game = OptionsActivity.getSavedGame(this);
        if (game != null) {
            continueGameButton.setVisibility(View.VISIBLE);
            continueGameButton.setOnClickListener(view -> {
                Intent i = PlayGameActivity.getIntentForIncompleteGame(MainActivity.this);
                startActivity(i);
            });
        }else{
            continueGameButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpPlayGameButton() {
        Button playGameButton = findViewById(R.id.play_game_button);
        playGameButton.setOnClickListener(view -> {
            Intent i = PlayGameActivity.getIntentForNewGame(MainActivity.this);
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