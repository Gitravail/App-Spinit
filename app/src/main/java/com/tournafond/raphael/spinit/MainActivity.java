package com.tournafond.raphael.spinit;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.Random;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
    private static final String PREFIXE = "Element";

    // *********************************************************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mode immersif **************************************************

        View decorView = getWindow().getDecorView();
        int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        // ****************************************************************************************/
        setContentView(R.layout.activity_main);

        mLogo = (ImageView) findViewById(R.id.logo);
        mList = (ListView) findViewById(R.id.itemList);
        mBtnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        mBtnStart = (Button) findViewById(R.id.btnStart);

        // Gestion de la liste dynamique
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                arrayList);
        mList.setAdapter(arrayAdapter);

        listInit();

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // incrementation du nombre d'elements
                nbOfItems++;
                // equivaut a arrayList.add("item"); et ensuite arrayAdapter.notifyDataSetChanged();
                arrayAdapter.add(PREFIXE + " " + nbOfItems);
                // focus sur le dernier element de la liste
                mList.setSelection(arrayAdapter.getCount()-1);
            }
        });

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int alea = (r.nextInt((arrayAdapter.getCount())));
                mBtnStart.setText(arrayList.get(alea));
            }
        });



    }

    private void listInit() {
        // Ajout du nombre d'element par defaut a la liste
        for (int i = 0; i < DEFAUT_ITEM_NB; i++) {
            nbOfItems++;
            arrayList.add(PREFIXE + " " + nbOfItems);
        }
        // lien avec la vue
        arrayAdapter.notifyDataSetChanged();
        // focus sur le dernier element de la liste
        mList.setSelection(arrayAdapter.getCount()-1);
    }
}
