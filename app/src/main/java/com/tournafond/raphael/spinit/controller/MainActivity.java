package com.tournafond.raphael.spinit.controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.ColorInt;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import com.tournafond.raphael.spinit.model.ListItem;
import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.model.adapter.ChoixAdapterHolder;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Creation des elements en lien avec le layout
    private DrawerLayout mDrawer; // Menu gauche
    private ImageButton mBtnHam; // Bouton d'ouverture du drawer
    private ImageView mLogo; // logo de l'application
    private ImageButton mBtnAdd; // bouton d'ajout d'un element
    private Button mBtnStart; //
    private ImageButton mFav;
    private Switch mSwitch;

    private ListView mList;
    private ChoixAdapterHolder choixAdapter;

    private User utilisateur = new User();


    // Lors du lancement de l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_main);


        // Ajout des animations
        final Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);

        // Lien avec les element du layout avec leur identifiant
        mDrawer = findViewById(R.id.drawer);
        mBtnHam = findViewById(R.id.btnHam);
        mLogo = findViewById(R.id.logo);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnStart = findViewById(R.id.btnStart);
        mSwitch = findViewById(R.id.switchPO);

        mList = findViewById(R.id.itemList);
        mList.setItemsCanFocus(true);
        choixAdapter = new ChoixAdapterHolder(this, utilisateur);
        mList.setAdapter(choixAdapter);


        if (mSwitch.isChecked()) {
            mBtnStart.setText(R.string.participants);
        } else {
            mBtnStart.setText(R.string.launch_msg);
        }

        // Ajout d'un evenement de clic sur le bouton d'ajout
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                choixAdapter.addNewItem();
                // focus sur le dernier element de la liste
                mList.setSelection(choixAdapter.getCount()-1);
            }
        });

        // Ajout d'un evenement de clic sur le bouton principal
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gereChoix();
            }
        });

        // Ajout d'un evenement de clic pour le menu hamburger
        mBtnHam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.START);
            }
        });

        // Ajout d'un evenement de clic sur le logo
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    utilisateur.setTypeElement(User.PERSONNE);
                    mBtnStart.setEnabled(false);
                    mBtnStart.setTextColor(getResources().getColor(R.color.textWhiteTransparent));
                    mBtnStart.setBackgroundColor(getResources().getColor(R.color.colorAccentTransparent));
                    mSwitch.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    utilisateur.setTypeElement(User.OPTION);
                    mBtnStart.setEnabled(true);
                    mBtnStart.setTextColor(getResources().getColor(R.color.textWhite));
                    mBtnStart.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mSwitch.setTextColor(getResources().getColor(R.color.textWhite));
                }
            }
        });
    }

    // Gestion du choix ****************************************************************************

    private void gereChoix() {
        ArrayList<String> listeMots = new ArrayList<>();
        ListItem li;
        for (int i = 0; i < choixAdapter.getCount(); i++) {
            li = (ListItem) choixAdapter.getItem(i);
            String text = li.text.trim().toUpperCase();
            if (!(text.equals(""))) {
                listeMots.add(text);
            }
        }

        int tailleListe = listeMots.size();
        boolean listeIncomplete = (tailleListe <= 1);

        if (listeIncomplete) {
            // Affichage d'un message informatif
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Veuillez entrer au moins deux choix");
            dlgAlert.setTitle("Info");
            // add a button
            dlgAlert.setPositiveButton("OK", null);
            // create and show the alert dialog
            AlertDialog dialog = dlgAlert.create();
            dialog.show();
        } else {
            Intent wheelActivity = new Intent(MainActivity.this, RandomActivity.class);
            wheelActivity.putStringArrayListExtra("liste", listeMots);
            startActivity(wheelActivity);
        }
    }
    // *********************************************************************************************
}
