package com.tournafond.raphael.spinit.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tournafond.raphael.spinit.model.OnSwipeListener;
import com.tournafond.raphael.spinit.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomActivity extends AppCompatActivity {

    private ImageButton mBtnReturn;
    private ImageView mSlot;
    private TextView mText1;
    private TextView mText2;
    private TextView mText3;
    private String saveText1;
    private long duration;
    Random r = new Random();
    ArrayList<String> listeMots;

    final long START_DURATION = 20;
    final long STEP_DURATION = 5;
    final long END_DURATION = 120;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_random);

        mBtnReturn = findViewById(R.id.btnReturn);
        mSlot = findViewById(R.id.slot);
        mText1 = findViewById(R.id.text_1);
        mText2 = findViewById(R.id.text_2);
        mText3 = findViewById(R.id.text_3);

        listeMots = getIntent().getStringArrayListExtra("liste");

        Collections.shuffle(listeMots);

        mText1.setText(listeMots.get(0));
        mText2.setText(listeMots.get(1));
        if (listeMots.size() > 2) {
            mText3.setText(listeMots.get(2));
        } else {
            mText3.setText(listeMots.get(0));
        }

        mSlot.setOnTouchListener(new OnSwipeListener(this) {
            public void onSwipeBottom() {

            }
        });

        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 1000);


    }

    // Gestion du mode immersif ********************************************************************

    // Gestion du focus
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // si prise de focus
        if (hasFocus) {
            hideSystemUI();
        }
    }

    // Fonction permettant d'activer le mode immersif **********************************************

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // *********************************************************************************************


    private void startAnimation() {
        TranslateAnimation spin = new TranslateAnimation(0,0,-80,80);
        spin.setRepeatCount(Animation.INFINITE);
        spin.setRepeatMode(Animation.RESTART);
        duration = START_DURATION;
        spin.setDuration(duration);
        mText1.startAnimation(spin);
        mText2.startAnimation(spin);
        mText3.startAnimation(spin);

        spin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent resultActivity = new Intent(RandomActivity.this, ResultActivity.class);
                        resultActivity.putStringArrayListExtra("liste", listeMots);
                        resultActivity.putExtra("resultat", mText2.getText());
                        startActivity(resultActivity);
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Choix d'un indice aleatoire de la liste
                int alea = (r.nextInt((listeMots.size())));
                // Selection de l'element tire au sort
                mText1.setText(listeMots.get(alea));
                mText3.setText(mText2.getText());
                mText2.setText(saveText1);
                saveText1 = (String) mText1.getText();
                duration = animation.getDuration()+STEP_DURATION;
                animation.setDuration(duration);
                if (duration == 2*END_DURATION) {
                    animation.cancel();
                }
                if (duration >= END_DURATION) {
                    duration = 2*END_DURATION;
                }
            }
        });
    }
}
