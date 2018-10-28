package com.tournafond.raphael.spinit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import com.tournafond.raphael.spinit.adapter.ChoixAdapterHolder;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Creation des elements en lien avec le layout
    private DrawerLayout mDrawer;
    private ImageButton mBtnHam;
    private ImageView mLogo;
    private ImageButton mBtnAdd;
    private Button mBtnStart;
    private ImageButton mFav;


    // Gestion de la liste dynamique ***************************************************************                    // ***

    private ListView mList;
    private ChoixAdapterHolder choixAdapter;

    // *********************************************************************************************

    // Lors du lancement de l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_main);

        // Lien avec les element du layout avec leur identifiant
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mBtnHam = (ImageButton) findViewById(R.id.btnHam);
        mLogo = (ImageView) findViewById(R.id.logo);
        mBtnAdd = (ImageButton) findViewById(R.id.btnAdd);
        mBtnStart = (Button) findViewById(R.id.btnStart);

        mList = (ListView) findViewById(R.id.itemList);                                                                   // ***
        mList.setItemsCanFocus(true);
        choixAdapter = new ChoixAdapterHolder(this);
        mList.setAdapter(choixAdapter);


        // Ajout d'un evenement de clic sur le bouton d'ajout
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    // Gestion du choix ****************************************************************************

    private void gereChoix() {
        ArrayList<String> listeMots = new ArrayList<String>();
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
            // Initialisation d'un nombre aleatoire
            Random r = new Random();
            // Choix d'un indice aleatoire de la liste
            int alea = (r.nextInt((tailleListe)));
            // Selection de l'element tire au sort
            String element = listeMots.get(alea);
            // Affichage de l'element tire au sort
            mBtnStart.setText(element);
            Intent wheelActivity = new Intent(MainActivity.this, WheelActivity.class);
            wheelActivity.putExtra("chosen",element);
            startActivity(wheelActivity);
        }

    }
    // *********************************************************************************************
}
