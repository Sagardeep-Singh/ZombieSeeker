package com.cmpt276.mineseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {
    public static Intent getIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

    }
}