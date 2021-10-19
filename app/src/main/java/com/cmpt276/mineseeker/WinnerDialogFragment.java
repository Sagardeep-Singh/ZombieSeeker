package com.cmpt276.mineseeker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class WinnerDialogFragment extends DialogFragment {

    int scanCount;
    public WinnerDialogFragment(int scanCount){
        this.scanCount = scanCount;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View v = this.getLayoutInflater().inflate(R.layout.dialog_game_end, null);

        TextView tv = v.findViewById(R.id.tvWinningMessage);

        tv.setText(String.format(
                Locale.ENGLISH,
                getString(R.string.winning_message),
                this.scanCount));

        DialogInterface.OnClickListener listener = (dialogInterface, which) -> {
            if (which == DialogInterface.BUTTON_NEUTRAL) {
                Activity activity = getActivity();

                if(activity != null){
                    activity.finish();
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNeutralButton("OK", listener)
                .create();
    }
}