package com.cmpt276.mineseeker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {

    public static final int WELCOME_SCREEN_DURATION = 4000;
    public static final int COUNTER_DOWN_INTERVAL = 1000;
    public static final String TAG = "WELCOME_SCREEN";
    public static final int DURATION = 2000;
    public static final float X_TRANSLATION = 2500f;
    AnimatorSet set;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupAnimation();
        setupSkipButton();
    }

    private void setupAnimation() {
        set = new AnimatorSet();
        List<Animator> animatorList = new ArrayList<>();

        TextView textView = this.findViewById(R.id.tvWelcomeMessage);
        animatorList.add(
                ObjectAnimator.ofArgb(
                        textView,
                        "textColor",
                        getColor(R.color.green_text)
                ).setDuration(DURATION));

        ImageView image = this.findViewById(R.id.imgZombie);
        ObjectAnimator animator =ObjectAnimator.ofFloat(
                image,
                "translationX",
                X_TRANSLATION
        ).setDuration(DURATION);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.i(TAG, "onAnimationEnd: ");
                setupWelcomeScreenTimer();
            }
        });

        animatorList.add(animator);

        set.playSequentially(animatorList);


        set.start();
    }

    private void setupSkipButton() {
        Button btnSkip = findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(view -> {
            if (timer != null) {
                timer.cancel();
            }
            if (set != null) {
                set.cancel();
            }
            Log.i(TAG, "SKIPPED");

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