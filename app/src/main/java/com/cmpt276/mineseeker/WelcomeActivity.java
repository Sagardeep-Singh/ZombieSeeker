package com.cmpt276.mineseeker;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {

    public static final int WELCOME_SCREEN_DURATION = 4000;
    public static final int COUNTER_DOWN_INTERVAL = 1000;
    public static final String TAG = "WELCOME_SCREEN";

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupWelcomeScreenTimer();
        setupSkipButton();
    }

    private void setupSkipButton() {
        Button btnSkip = findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(view -> {
            if (timer != null) {
                timer.cancel();
            }
            startMainActivity();
        });
    }

    private void setupWelcomeScreenTimer() {
        timer = new CountDownTimer(WELCOME_SCREEN_DURATION, COUNTER_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                TextView tvCounter = findViewById(R.id.tvCounter);
                long remainingDelay = (l / COUNTER_DOWN_INTERVAL);
                tvCounter.setText(String.format(Locale.ENGLISH, "%d", remainingDelay));
            }

            @Override
            public void onFinish() {

                startMainActivity();
            }
        };

        timer.start();
    }

    private void startMainActivity() {
        Intent intent = MainActivity.getIntent(WelcomeActivity.this);
        startActivity(intent);
        finish();
    }
}