package com.tournafond.raphael.spinit.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.model.adapter.ChoixAdapterHolder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Creation des elements en lien avec le layout
    private DrawerLayout mDrawer; // Menu gauche
    private ImageButton mBtnHam; // Bouton d'ouverture du drawer
    private ImageView mLogo; // logo de l'application
    private ImageButton mBtnAdd; // bouton d'ajout d'un element
    private ImageButton mBtnDelete; // bouton de suppression d'une liste
    private Button mBtnStart; // bouton de lancement du tirage au sort
    private Switch mSwitch; // choix d'ajouter des participants (deux tirages)

    private LinearLayout mLayoutAP; // layout contenant le menu action/participant
    private Button mBtnAction; // bouton pour le choix d'edition des actions
    private Button mBtnParticipant; // bouton pour le choix d'edition des participants
    private int mActionParticipant;

    private ListView mList;
    private ChoixAdapterHolder choixAdapter;

    private User utilisateur = new User();
    private ArrayList<String> listeAction = new ArrayList<>();
    private ArrayList<String> listeParticipant = new ArrayList<>();

    public static final int ACTION = 0;
    public static final int PARTICPANT = 1;
    public static final int OPTION = 2;


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
        mBtnDelete = findViewById(R.id.btnDelete);
        mBtnStart = findViewById(R.id.btnStart);
        mSwitch = findViewById(R.id.switchPO);

        mLayoutAP = findViewById(R.id.layoutAP);
        mBtnAction = findViewById(R.id.btnAction);
        mBtnParticipant = findViewById(R.id.btnParticipant);
        mActionParticipant = ACTION; // valeur par defaut

        mList = findViewById(R.id.itemList);
        mList.setItemsCanFocus(true);
        choixAdapter = new ChoixAdapterHolder(this, utilisateur, ChoixAdapterHolder.ACTION);
        mList.setAdapter(choixAdapter);

        mLayoutAP.setVisibility(LinearLayout.GONE);
        setOption();

        // Ajout d'un evenement de clic sur le bouton d'edition des actions
        mBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionParticipant != ACTION) {
                    setAction();
                }
            }
        });

        // Ajout d'un evenement de clic sur le bouton d'edition des participants
        mBtnParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionParticipant != PARTICPANT) {
                    setParticipant();
                }
            }
        });

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

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                if (!choixAdapter.getList().isEmpty()) {
                    dialogOuiNon("Voulez vous vraiment supprimer le contenu de cette liste ?");
                }
            }
        });

        // Ajout d'un evenement de clic sur le bouton principal
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
                    listeAction = choixAdapter.getList();
                } else {
                    listeParticipant = choixAdapter.getList();
                }
                System.out.println(listeAction);
                Set<String> set = new LinkedHashSet<>(listeAction);
                listeAction.clear();
                listeAction.addAll(set);
                System.out.println(listeAction);
                System.out.println(listeParticipant);
                set = new LinkedHashSet<>(listeParticipant);
                listeParticipant.clear();
                listeParticipant.addAll(set);
                System.out.println(listeParticipant);
                if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
                    choixAdapter = new ChoixAdapterHolder(MainActivity.this, utilisateur.getPrefixe(), listeAction);
                } else {
                    choixAdapter = new ChoixAdapterHolder(MainActivity.this, utilisateur.getPrefixe(), listeParticipant);
                }
                mList.setAdapter(choixAdapter);
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
                    mLayoutAP.setVisibility(LinearLayout.VISIBLE);
                    if (mActionParticipant != ACTION) {
                        setAction();
                    }
                } else {
                    mLayoutAP.setVisibility(LinearLayout.GONE);
                    if (mActionParticipant != OPTION) {
                        setOption();
                    }
                }
            }
        });
    }

    // Gestion du choix ****************************************************************************

    private void gereChoix() {
        if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
            listeAction = choixAdapter.getList();
        } else {
            listeParticipant = choixAdapter.getList();
        }
        int tailleListeAction = listeAction.size();
        int tailleListeParticipant = listeParticipant.size();

        if (mSwitch.isChecked()) {
            if (tailleListeAction >= 1) {
                if (tailleListeParticipant >= 2) {
                    lanceResult();
                } else {
                    afficheDialog("Veuillez entrer au moins 2 participants en mode défi");
                }
            } else {
                afficheDialog("Veuillez entrer au moins 1 action en mode défi");
            }
        } else {
            if (tailleListeAction >= 2 ) {
                lanceResult();
            } else {
                afficheDialog("Veuillez entrer au moins 2 options en mode normal");
            }
        }
    }
    // *********************************************************************************************

    // Gestion des deux listes *********************************************************************

    private void setAction() {
        mBtnParticipant.setBackgroundColor(getResources().getColor(R.color.colorAccentTransparent));
        mBtnAction.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mBtnParticipant.setTextColor(getResources().getColor(R.color.textWhiteTransparent));
        mBtnAction.setTextColor(getResources().getColor(R.color.textWhite));
        utilisateur.setTypeElement(User.ACTION);
        if (mActionParticipant == OPTION) {
            listeAction = choixAdapter.getList();
            choixAdapter = new ChoixAdapterHolder(this, utilisateur.getPrefixe(), listeAction);
        } else if (mActionParticipant == PARTICPANT) {
            listeParticipant = choixAdapter.getList();
            choixAdapter = new ChoixAdapterHolder(this, utilisateur.getPrefixe(), listeAction);
        }
        mActionParticipant = ACTION;
        choixAdapter.setPrefixe(utilisateur.getPrefixe());
        mList.setAdapter(choixAdapter);
    }

    private void setParticipant() {
        mBtnAction.setBackgroundColor(getResources().getColor(R.color.colorAccentTransparent));
        mBtnParticipant.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mBtnAction.setTextColor(getResources().getColor(R.color.textWhiteTransparent));
        mBtnParticipant.setTextColor(getResources().getColor(R.color.textWhite));
        mActionParticipant = PARTICPANT;
        utilisateur.setTypeElement(User.PARTICIPANT);
        listeAction = choixAdapter.getList();
        choixAdapter = new ChoixAdapterHolder(this, utilisateur.getPrefixe(), listeParticipant);
        choixAdapter.setPrefixe(utilisateur.getPrefixe());
        mList.setAdapter(choixAdapter);
    }

    private void setOption() {
        utilisateur.setTypeElement(User.OPTION);
        if (mActionParticipant == ACTION) {
            listeAction = choixAdapter.getList();
            choixAdapter = new ChoixAdapterHolder(this, utilisateur.getPrefixe(), listeAction);
        } else if (mActionParticipant == PARTICPANT) {
            listeParticipant = choixAdapter.getList();
            choixAdapter = new ChoixAdapterHolder(this, utilisateur.getPrefixe(), listeAction);
        }
        mActionParticipant = OPTION;
        choixAdapter.setPrefixe(utilisateur.getPrefixe());
        mList.setAdapter(choixAdapter);
    }

    private void afficheDialog(String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Info");
        // add a button
        dlgAlert.setPositiveButton("OK", null);
        // create and show the alert dialog
        AlertDialog dialog = dlgAlert.create();
        dialog.show();
    }

    DialogInterface.OnClickListener deleteYesNo = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
                        listeAction.clear();
                        choixAdapter = new ChoixAdapterHolder(getApplicationContext(), utilisateur.getPrefixe(), listeAction);
                        mList.setAdapter(choixAdapter);
                    } else {
                        listeParticipant.clear();
                        choixAdapter = new ChoixAdapterHolder(getApplicationContext(), utilisateur.getPrefixe(), listeParticipant);
                        mList.setAdapter(choixAdapter);
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private void dialogOuiNon(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton("Oui", deleteYesNo)
                .setNegativeButton("Non", deleteYesNo).show();
    }

    private void lanceResult() {
        Intent wheelActivity = new Intent(MainActivity.this, RandomActivity.class);
        wheelActivity.putStringArrayListExtra("listeAction", listeAction);
        if (!mSwitch.isChecked()) {
            listeParticipant = new ArrayList<>();
        }
        wheelActivity.putStringArrayListExtra("listeParticipant", listeParticipant);
        startActivity(wheelActivity);
    }
}
