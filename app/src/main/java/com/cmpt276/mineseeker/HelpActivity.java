package com.cmpt276.mineseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    public static Intent getIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setupHelpLinks();
    }

    private void setupHelpLinks() {
        LinearLayout layout = findViewById(R.id.layoutHelp);

        String[] links = getResources().getStringArray(R.array.citations);

        for (String link:links) {
            TextView tvMedal = new TextView(this);
            tvMedal.setText(Html.fromHtml(link,Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
            tvMedal.setLinksClickable(true);
            tvMedal.setTextColor(getColor(R.color.white));
            layout.addView(tvMedal);
        }

    }


}