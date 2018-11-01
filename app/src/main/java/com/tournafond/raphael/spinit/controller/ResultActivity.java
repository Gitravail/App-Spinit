package com.tournafond.raphael.spinit.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.holder.ListDualItem;
import com.tournafond.raphael.spinit.model.adapter.DualTextViewAdapter;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ImageButton mBtnReturn;
    private ImageButton mBtnSave;

    private TextView mTextParticipant;
    private TextView mTextAction;
    private TextView mTextListParticipant;

    private ListView mList;

    private ArrayList<String> listeAction;
    private ArrayList<String> listeParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mBtnReturn = findViewById(R.id.btnReturn);
        mBtnSave = findViewById(R.id.btnSave);

        mTextParticipant = findViewById(R.id.textParticipant);
        mTextAction = findViewById(R.id.textAction);
        mTextListParticipant = findViewById(R.id.textListParticipant);
        mTextListParticipant.setVisibility(View.INVISIBLE);

        mList = findViewById(R.id.list);

        // Ajout des animations
        final Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);
        final Animation text_zoom = AnimationUtils.loadAnimation(this, R.anim.text_zoom);

        listeAction = getIntent().getStringArrayListExtra("listeAction");
        listeParticipant = getIntent().getStringArrayListExtra("listeParticipant");

        String resultatAction;
        String resultatParticipant;

        Bundle b = getIntent().getExtras();
        if (b != null) {
            resultatAction = (String) b.get("resultatAction");
            resultatParticipant = (String) b.get("resultatParticipant");
        } else {
            resultatAction = "";
            resultatParticipant = "";
        }
        if (resultatParticipant == null) {
            resultatParticipant = "";
        }
        if (!resultatParticipant.equals("")) {
            resultatAction = resultatAction.substring(0,1).toLowerCase() + resultatAction.substring(1);
            resultatAction = "dois " + resultatAction + ".";
            if (resultatParticipant.length() > 20) {
                resultatParticipant = resultatParticipant.substring(0,1).toUpperCase() + resultatParticipant.substring(1, 20);
            } else {
                resultatParticipant = resultatParticipant.substring(0,1).toUpperCase() + resultatParticipant.substring(1);
            }
            mTextListParticipant.setVisibility(View.VISIBLE);
        } else {
            resultatAction = resultatAction.substring(0,1).toUpperCase() + resultatAction.substring(1);
            mTextListParticipant.setVisibility(View.INVISIBLE);
        }
        mTextParticipant.setText(resultatParticipant);
        mTextAction.setText(resultatAction);
        mTextAction.startAnimation(text_zoom);
        mTextParticipant.startAnimation(text_zoom);

        // Gestion de la liste des possibilités ***
        ArrayList<ListDualItem> listDualItemArrayList = new ArrayList<>();
        listDualItemArrayList = creeListDualItem();
        DualTextViewAdapter dtva = new DualTextViewAdapter(this, R.layout.dual_item, listDualItemArrayList);
        mList.setAdapter(dtva);
        // ***

        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                finish();
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                //sauvegardeListe();
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

    private ArrayList<ListDualItem> creeListDualItem() {
        ArrayList<ListDualItem> listDualItemArrayList = new ArrayList<>();
        int tailleAction = listeAction.size();
        int tailleParticipant = listeParticipant.size();
        int max = tailleAction;
        if (max < tailleParticipant) {
            max = tailleParticipant;
        }
        ListDualItem ldi;
        String action;
        String participant;
        for (int i = 0; i < max; i++) {
            action = "";
            participant = "";
            if (i < tailleAction) {
                action = listeAction.get(i);
            }
            if (i < tailleParticipant) {
                participant = listeParticipant.get(i);
            }
            ldi = new ListDualItem(action, participant);
            listDualItemArrayList.add(ldi);
        }
        return listDualItemArrayList;
    }
}
