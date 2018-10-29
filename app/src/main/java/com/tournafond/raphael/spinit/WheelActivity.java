package com.tournafond.raphael.spinit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class WheelActivity extends AppCompatActivity {

    private ImageButton mBtnReturn;
    private TextView mText1;
    private TextView mText2;
    private TextView mText3;
    private String saveText1;
    private long duration = 800;

    final long START_DURATION = 300;
    final long STEP_DURATION = 200;
    final long END_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_wheel);

        mBtnReturn = (ImageButton) findViewById(R.id.btnReturn);
        mText1 = (TextView) findViewById(R.id.text_1);
        mText2 = (TextView) findViewById(R.id.text_2);
        mText3 = (TextView) findViewById(R.id.text_3);

        String choisi = getIntent().getStringExtra("choisi");
        ArrayList<String> listeMots = (ArrayList<String>) getIntent().getStringArrayListExtra("liste");

        System.out.println(listeMots);
        System.out.println("Fin");

        Collections.shuffle(listeMots);

        mText1.setText(listeMots.get(0));
        mText2.setText(listeMots.get(1));
        if (listeMots.size() > 2) {
            mText3.setText(listeMots.get(2));
        } else {
            mText3.setText(listeMots.get(0));
        }

        saveText1 = (String) mText1.getText();

        TranslateAnimation spin = new TranslateAnimation(0,0,-200,200);
        spin.setRepeatCount(Animation.INFINITE);
        spin.setRepeatMode(Animation.REVERSE);
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

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mText1.setText(mText3.getText());
                mText3.setText(mText2.getText());
                mText2.setText(saveText1);
                saveText1 = (String) mText1.getText();
                duration = animation.getDuration()+STEP_DURATION;
                animation.setDuration(duration);
                if (duration >= END_DURATION) {
                    animation.cancel();
                }
            }
        });

        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

    // Gestion sortie du clavier *******************************************************************

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
}
