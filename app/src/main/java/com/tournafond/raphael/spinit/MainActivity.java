package com.tournafond.raphael.spinit;

import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tournafond.raphael.spinit.adapter.ChoixAdapter;

import java.util.Random;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Creation des elements en lien avec le layout
    private DrawerLayout mDrawer;
    private ImageButton mBtnHam;
    private ImageView mLogo;
    private ListView mList;
    private FloatingActionButton mBtnAdd;
    private Button mBtnStart;

    // Gestion de la liste dynamique ***************************************************************

    // Variable contenant les elements de la liste
    private ArrayList<String> arrayList = new ArrayList<String>();
    // Variable permettant de faire le lien avec la vue
    private ArrayAdapter<String> arrayAdapter;
    // Correspond au nombre d'elements dans la liste
    private int nbOfItems = 0;

    // Constantes
    private static final int DEFAUT_ITEM_NB = 3;

    // *********************************************************************************************

    // Lors du lancement de l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lien avec les element du layout avec leur identifiant
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mBtnHam = (ImageButton) findViewById(R.id.btnHam);
        mLogo = (ImageView) findViewById(R.id.logo);
        mList = (ListView) findViewById(R.id.itemList);
        mBtnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        mBtnStart = (Button) findViewById(R.id.btnStart);



        // Gestion de la liste dynamique ***********************************************************
        arrayAdapter = new ChoixAdapter(this,
                arrayList);
        mList.setAdapter(arrayAdapter);

        // Initialisation de la liste avec DEFAUT_ITEM_NB elements
        listInit();

        // Ajout d'un evenement de clic sur le bouton d'ajout
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // incrementation du nombre d'elements
                nbOfItems++;
                arrayList.add("");
                // lien avec la vue
                arrayAdapter.notifyDataSetChanged();
                // focus sur le dernier element de la liste
                mList.setSelection(arrayAdapter.getCount()-1);
            }
        });

        // Ajout d'un evenement de clic sur le bouton principal
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialisation d'un nombre aleatoire
                Random r = new Random();
                // Choix d'un indice aleatoire de la liste
                int alea = (r.nextInt((arrayAdapter.getCount())));
                // Affichage de l'element tire au sort
                mBtnStart.setText(arrayList.get(alea));
                //setContentView(R.layout.activity_wheel);
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

    // Procedure permettant d'initialiser la liste
    private void listInit() {
        // Ajout du nombre d'element par defaut a la liste
        for (int i = 0; i < DEFAUT_ITEM_NB; i++) {
            nbOfItems++;
            arrayList.add("Element" + nbOfItems);
        }
        // lien avec la vue
        arrayAdapter.notifyDataSetChanged();
        // focus sur le dernier element de la liste
        mList.setSelection(arrayAdapter.getCount()-1);
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
