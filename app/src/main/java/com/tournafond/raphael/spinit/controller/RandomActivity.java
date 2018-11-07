package com.tournafond.raphael.spinit.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tournafond.raphael.spinit.listener.OnSwipeListener;
import com.tournafond.raphael.spinit.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomActivity extends AppCompatActivity {

    private ImageButton mBtnReturn;
    private ImageButton mBtnPasser;

    private TextView mTextCeluiQui;
    private TextView mTextAction;

    private ImageView mSlot;
    private TextView mText1;
    private TextView mText2;
    private TextView mText3;

    private String saveText1;
    private long duration;
    private Random r = new Random();

    private ArrayList<String> listeEnCours;
    private ArrayList<String> listeAction;
    private ArrayList<String> listeParticipant;

    private boolean doisJouerAction;
    private boolean doisJouerParticipant;
    private boolean sauvegardeAction;
    private boolean sauvegardeParticipant;
    private String resultatAction;
    private String resultatParticipant;

    public static final long START_DURATION = 20;
    public static final long STEP_DURATION = 5;
    public static final long END_DURATION = 120;
    public static final int START_DELAY = 1000;
    public static final int BETWEEN_DELAY = 2000;

    private boolean mEnableTouchEvent;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_random);

        mBtnReturn = findViewById(R.id.btnReturn);
        mBtnPasser = findViewById(R.id.btnPasser);

        mTextCeluiQui = findViewById(R.id.textCeluiQui);
        mTextAction = findViewById(R.id.textAction);

        mSlot = findViewById(R.id.slot);
        mText1 = findViewById(R.id.text_1);
        mText2 = findViewById(R.id.text_2);
        mText3 = findViewById(R.id.text_3);

        mEnableTouchEvent = true;

        // Ajout des animations
        final Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);

        listeAction = getIntent().getStringArrayListExtra("listeAction");
        listeParticipant = getIntent().getStringArrayListExtra("listeParticipant");

        Collections.shuffle(listeAction);
        Collections.shuffle(listeParticipant);

        doisJouerParticipant = !listeParticipant.isEmpty();
        mTextCeluiQui.setVisibility(View.INVISIBLE);
        mTextAction.setVisibility(View.INVISIBLE);
        doisJouerAction = true;

        mSlot.setOnTouchListener(new OnSwipeListener(this) {
            public void onSwipeBottom() {

            }
        });

        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                finish();
            }
        });

        mBtnPasser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                // Choix d'un indice aleatoire de la liste
                int aleaAction = (r.nextInt((listeAction.size())));
                resultatAction = listeAction.get(aleaAction);
                if (listeParticipant.size() > 0) {
                    int aleaParticipant = (r.nextInt((listeParticipant.size())));
                    resultatParticipant = listeParticipant.get(aleaParticipant);
                } else {
                    resultatParticipant = "";
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent resultActivity = new Intent(RandomActivity.this, ResultActivity.class);
                        resultActivity.putStringArrayListExtra("listeAction", listeAction);
                        resultActivity.putStringArrayListExtra("listeParticipant", listeParticipant);
                        resultActivity.putExtra("resultatAction", resultatAction);
                        resultActivity.putExtra("resultatParticipant", resultatParticipant);
                        startActivity(resultActivity);
                        finish();
                    }
                }, 500);
            }
        });

        initialiseChampsEtAnime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Animation text_zoom = AnimationUtils.loadAnimation(this, R.anim.text_zoom);
        mBtnPasser.startAnimation(text_zoom);
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
                mEnableTouchEvent = false;

                if (sauvegardeAction) {
                    resultatAction = (String) mText2.getText();
                    sauvegardeAction = false;
                    if (doisJouerParticipant) {
                        mTextCeluiQui.setVisibility(View.VISIBLE);
                        mTextAction.setText(resultatAction);
                        mTextAction.setVisibility(View.VISIBLE);
                    }
                }
                if (sauvegardeParticipant) {
                    resultatParticipant = (String) mText2.getText();
                    sauvegardeParticipant = false;
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialiseChampsEtAnime();
                    }
                }, BETWEEN_DELAY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Choix d'un indice aleatoire de la liste
                int alea = (r.nextInt((listeEnCours.size())));
                // Selection de l'element tire au sort
                mText1.setText(listeEnCours.get(alea));
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

    private void initialiseChampsEtAnime() {
        if (doisJouerAction) {
            doisJouerAction = false;
            int tailleListeAction = listeAction.size();
            boolean plusDUn = tailleListeAction > 1;
            if (plusDUn) {
                mText1.setText(listeAction.get(0));
                mText2.setText(listeAction.get(1));
                if (tailleListeAction > 2) {
                    mText3.setText(listeAction.get(2));
                }
                sauvegardeAction = true;
                sauvegardeParticipant = false;
                listeEnCours = listeAction;
                lanceAnimationDelay(START_DELAY);
            } else {
                sauvegardeAction = true;
                sauvegardeParticipant = false;
                resultatAction = listeAction.get(0);
                sauvegardeAction = false;
                if (doisJouerParticipant) {
                    mTextCeluiQui.setVisibility(View.VISIBLE);
                    mTextAction.setText(resultatAction);
                    mTextAction.setVisibility(View.VISIBLE);
                }
                initialiseChampsEtAnime();
            }
            mEnableTouchEvent = true;
        } else if (doisJouerParticipant) {
            doisJouerParticipant = false;
            int tailleListeParticipant = listeParticipant.size();
            if (tailleListeParticipant > 0) {
                mText1.setText(listeParticipant.get(0));
                if (tailleListeParticipant > 1) {
                    mText2.setText(listeParticipant.get(1));
                    if (tailleListeParticipant > 2) {
                        mText3.setText(listeParticipant.get(2));
                    }
                }
            }
            sauvegardeAction = false;
            sauvegardeParticipant = true;
            listeEnCours = listeParticipant;
            mEnableTouchEvent = true;
            lanceAnimationDelay(0);
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent resultActivity = new Intent(RandomActivity.this, ResultActivity.class);
                    resultActivity.putStringArrayListExtra("listeAction", listeAction);
                    resultActivity.putStringArrayListExtra("listeParticipant", listeParticipant);
                    resultActivity.putExtra("resultatAction", resultatAction);
                    resultActivity.putExtra("resultatParticipant", resultatParticipant);
                    startActivity(resultActivity);
                    finish();
                }
            }, 1000);
        }
    }

    private void lanceAnimationDelay(int delay) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, delay);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvent && super.dispatchTouchEvent(ev);
    }
}
