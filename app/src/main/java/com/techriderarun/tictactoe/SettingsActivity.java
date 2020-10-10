//        Copyright 2020 Techrider Arun
//        Don't change anything else which is not written in
//        read.me else you will kill the source code.
//        If you are publishing this app then give
//        credit to me else don't publish.



package com.techriderarun.tictactoe;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";

    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;

    private int revealX;
    private static final String PREFS_NAME = "vibration";
    private static final String PREF_VIBRATION = "TicVib";
    private int revealY;
    private boolean Vibration;
    private boolean isChecked;
    private String[] Randomfirst;
    private SharedPreferences sharedPreferences;
    private TextView dif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Vibration = preferences.getBoolean(PREF_VIBRATION, true);

        setContentView(R.layout.activity_settings);
        Window w = getWindow(); // in Activity's onCreate() for instance
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Intent intent = getIntent();

        Switch swit = findViewById(R.id.swith2);
        swit.setChecked(Vibration);
        swit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vibration) {
                    isChecked = false;
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_VIBRATION, isChecked);
                    editor.apply();
                } else {
                    isChecked = true;

                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_VIBRATION, isChecked);
                    editor.apply();
                }
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        dif = findViewById(R.id.dif);
        dif.setText(getDiffLabel(sharedPreferences.getInt("key", 0)));

        RelativeLayout r3 = findViewById(R.id.r3);
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty();
            }
        });

        RelativeLayout r6 = findViewById(R.id.r6);
        r6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating();
            }
        });

        RelativeLayout r7 = findViewById(R.id.r7);
        r7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbacks();
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void difficulty() {
        String[] choices = new String[] {" Easy", " Medium", " Hard", " Impossible", " Random"};

        int selected = -1;

        switch (sharedPreferences.getInt("key", 0)) {
            case 1:
                selected = 0;
                break;
            case 2:
                selected = 1;
                break;
            case 3:
                selected = 2;
                break;
            case 4:
                selected = 3;
                break;
            case 5:
                selected = 4;
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Difficulty")
                .setPositiveButton("Great!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dif.setText(getDiffLabel(sharedPreferences.getInt("key", 0)));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setSingleChoiceItems(choices, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        switch (which) {
                            case 0:
                                editor.putInt("key", 1);
                                break;
                            case 1:
                                editor.putInt("key", 2);
                                break;
                            case 2:
                                editor.putInt("key", 3);
                                break;
                            case 3:
                                editor.putInt("key", 4);
                                break;
                            case 4:
                                editor.putInt("key", 5);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + which);
                        }
                        editor.apply();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void rating() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    private void feedbacks() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {getString(R.string.feedback)};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Tic Tac Toe Feedbacks");
        intent.putExtra(Intent.EXTRA_CC, getString(R.string.feedback));
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }

    private String getDiffLabel(int level) {
        String label = "";
        switch (level) {
            case 1:
                label = "Easy";
                break;
            case 2:
                label = "Medium";
                break;
            case 3:
                label = "Hard";
                break;
            case 4:
                label = "Impossible";
                break;
            case 5:
                label = "Random";
                break;
        }
        return label;
    }

}
