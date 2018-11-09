package com.tournafond.raphael.spinit.controller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.injections.Injection;
import com.tournafond.raphael.spinit.injections.ViewModelFactory;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.view.ListeViewModel;
import com.tournafond.raphael.spinit.view.adapter.MainListeAdapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Creation des elements en lien avec le layout
    private DrawerLayout mDrawer; // Menu gauche
    private ImageButton mBtnHam; // Bouton d'ouverture du drawer
    private ImageButton mBtnSave; // Bouton de sauvegarde de la liste
    private ImageView mLogo; // logo de l'application
    private ImageButton mBtnAdd; // bouton d'ajout d'un element
    private ImageButton mBtnDelete; // bouton de suppression d'une liste
    private Button mBtnStart; // bouton de lancement du tirage au sort
    private Switch mSwitch; // choix d'ajouter des participants (deux tirages)

    private LinearLayout mBtnEdit; // Layout clicable pour editer les listes
    private LinearLayout mLayoutAP; // layout contenant le menu action/participant
    private Button mBtnAction; // bouton pour le choix d'edition des actions
    private Button mBtnParticipant; // bouton pour le choix d'edition des participants
    private int mActionParticipant;

    private ListView mList;
    private MainListeAdapter mMainListeAdapter;

    private User utilisateur = new User();
    private ArrayList<String> listeAction = new ArrayList<>();
    private ArrayList<String> listeParticipant = new ArrayList<>();

    public static final int ACTION = 0;
    public static final int PARTICPANT = 1;
    public static final int OPTION = 2;

    private static final int EDIT_ACTIVITY_REQUEST_CODE = 20;


    // Lors du lancement de l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_main);
        setNavigationViewListener();


        // Ajout des animations
        final Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);

        // Lien avec les element du layout avec leur identifiant
        mDrawer = findViewById(R.id.drawer);
        mBtnHam = findViewById(R.id.btnHam);
        mBtnSave = findViewById(R.id.btnSave);
        mLogo = findViewById(R.id.logo);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnDelete = findViewById(R.id.btnDelete);
        mBtnStart = findViewById(R.id.btnStart);
        mSwitch = findViewById(R.id.switchPO);

        mBtnEdit = findViewById(R.id.btnEdit);
        mLayoutAP = findViewById(R.id.layoutAP);
        mBtnAction = findViewById(R.id.btnAction);
        mBtnParticipant = findViewById(R.id.btnParticipant);
        mActionParticipant = ACTION; // valeur par defaut

        mList = findViewById(R.id.itemList);
        mList.setItemsCanFocus(true);
        mMainListeAdapter = new MainListeAdapter(this, utilisateur, MainListeAdapter.ACTION);
        mList.setAdapter(mMainListeAdapter);

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
                mMainListeAdapter.addNewItem();
                // focus sur le dernier element de la liste
                mList.setSelection(mMainListeAdapter.getCount()-1);
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                if (!mMainListeAdapter.getList().isEmpty()) {
                    dialogOuiNon("Voulez vous vraiment supprimer le contenu de cette liste ?");
                }
            }
        });

        // Ajout d'un evenement de clic sur le bouton principal
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
                    listeAction = mMainListeAdapter.getList();
                } else {
                    listeParticipant = mMainListeAdapter.getList();
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
                    mMainListeAdapter = new MainListeAdapter(MainActivity.this, utilisateur.getPrefixe(), listeAction);
                } else {
                    mMainListeAdapter = new MainListeAdapter(MainActivity.this, utilisateur.getPrefixe(), listeParticipant);
                }
                mList.setAdapter(mMainListeAdapter);
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
                getBonusList();
            }
        });

        // Ajout d'un evenement de clic pour la sauvegarde
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listeValide()) {
                    v.startAnimation(zoom);
                    sauvegarderListe();
                }
            }
        });

        // Ajout d'un evenement de clic pour le switch
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

        // Ajout d'un evenement de clic pour edit
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(editActivity, EDIT_ACTIVITY_REQUEST_CODE);
                //close navigation drawer
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    // Gestion du choix ****************************************************************************

    // Test validite de la liste
    private boolean listeValide() {
        if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
            listeAction = mMainListeAdapter.getList();
        } else {
            listeParticipant = mMainListeAdapter.getList();
        }
        int tailleListeAction = listeAction.size();
        int tailleListeParticipant = listeParticipant.size();

        if (mSwitch.isChecked()) {
            if (tailleListeAction >= 1) {
                if (tailleListeParticipant >= 2) {
                    return true;
                } else {
                    afficheDialog("Veuillez entrer au moins 2 participants en mode défi");
                }
            } else {
                afficheDialog("Veuillez entrer au moins 1 action en mode défi");
            }
        } else {
            if (tailleListeAction >= 2 ) {
                return true;
            } else {
                afficheDialog("Veuillez entrer au moins 2 options en mode normal");
            }
        }
        return false;
    }

    private void gereChoix() {
        // si liste valide lance la roulette
        if (listeValide()) {
            lanceResult();
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
            listeAction = mMainListeAdapter.getList();
            mMainListeAdapter = new MainListeAdapter(this, utilisateur.getPrefixe(), listeAction);
        } else if (mActionParticipant == PARTICPANT) {
            listeParticipant = mMainListeAdapter.getList();
            mMainListeAdapter = new MainListeAdapter(this, utilisateur.getPrefixe(), listeAction);
        }
        mActionParticipant = ACTION;
        mMainListeAdapter.setPrefixe(utilisateur.getPrefixe());
        mList.setAdapter(mMainListeAdapter);
    }

    private void setParticipant() {
        mBtnAction.setBackgroundColor(getResources().getColor(R.color.colorAccentTransparent));
        mBtnParticipant.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mBtnAction.setTextColor(getResources().getColor(R.color.textWhiteTransparent));
        mBtnParticipant.setTextColor(getResources().getColor(R.color.textWhite));
        mActionParticipant = PARTICPANT;
        utilisateur.setTypeElement(User.PARTICIPANT);
        listeAction = mMainListeAdapter.getList();
        mMainListeAdapter = new MainListeAdapter(this, utilisateur.getPrefixe(), listeParticipant);
        mMainListeAdapter.setPrefixe(utilisateur.getPrefixe());
        mList.setAdapter(mMainListeAdapter);
    }

    private void setOption() {
        utilisateur.setTypeElement(User.OPTION);
        if (mActionParticipant == ACTION) {
            listeAction = mMainListeAdapter.getList();
            mMainListeAdapter = new MainListeAdapter(this, utilisateur.getPrefixe(), listeAction);
        } else if (mActionParticipant == PARTICPANT) {
            listeParticipant = mMainListeAdapter.getList();
            mMainListeAdapter = new MainListeAdapter(this, utilisateur.getPrefixe(), listeAction);
        }
        mActionParticipant = OPTION;
        mMainListeAdapter.setPrefixe(utilisateur.getPrefixe());
        mList.setAdapter(mMainListeAdapter);
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
                        mMainListeAdapter = new MainListeAdapter(getApplicationContext(), utilisateur.getPrefixe(), listeAction);
                        mList.setAdapter(mMainListeAdapter);
                    } else {
                        listeParticipant.clear();
                        mMainListeAdapter = new MainListeAdapter(getApplicationContext(), utilisateur.getPrefixe(), listeParticipant);
                        mList.setAdapter(mMainListeAdapter);
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


    // *********************************************************************************************

    // lance la roulette
    private void lanceResult() {
        Intent wheelActivity = new Intent(MainActivity.this, RandomActivity.class);
        wheelActivity.putStringArrayListExtra("listeAction", listeAction);
        if (!mSwitch.isChecked()) {
            listeParticipant = new ArrayList<>();
        }
        wheelActivity.putStringArrayListExtra("listeParticipant", listeParticipant);
        startActivity(wheelActivity);
    }

    // debut d'implementation du drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

        }
        //close navigation drawer
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // sauvegarde de la liste
    private void sauvegarderListe() {
        Liste liste = new Liste();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
        alert.setTitle("Titre de la nouvelle liste");

        alert.setView(edittext);

        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String text = edittext.getText().toString();
                if (!text.equals(""))  {
                    if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
                        listeAction = mMainListeAdapter.getList();
                    } else {
                        listeParticipant = mMainListeAdapter.getList();
                    }
                    liste.setTitre(text);
                    liste.setType(Liste.NORMAL);
                    liste.setAction(listeAction);
                    liste.setParticipant(listeParticipant);
                    insererListe(liste);
                    Toast.makeText(MainActivity.this, "La liste a bien été enregistré", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un titre valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    // insertion de la nouvelle liste
    private void insererListe(Liste liste) {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        ListeViewModel mListeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListeViewModel.class);
        mListeViewModel.createListe(liste);
    }


    // une fois la liste charge depuis la liste de listes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (EDIT_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            Liste liste = (Liste) data.getSerializableExtra(EditActivity.BUNDLE_LISTE);
            listeAction = liste.getAction();
            listeParticipant = liste.getParticipant();
            if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
                mMainListeAdapter = new MainListeAdapter(getApplicationContext(), utilisateur.getPrefixe(), listeAction);
                mList.setAdapter(mMainListeAdapter);
            } else {
                mMainListeAdapter = new MainListeAdapter(getApplicationContext(), utilisateur.getPrefixe(), listeParticipant);
                mList.setAdapter(mMainListeAdapter);
            }
        }
    }

    // permet de recuperer les liste bonus (juste en demo, elles sont vides)
    private void getBonusList() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        ListeViewModel mListeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListeViewModel.class);
        Liste liste = new Liste();
        liste.setTitre("Foot");
        liste.setType(Liste.BONUS);
        mListeViewModel.createListe(liste);
        liste = new Liste();
        liste.setTitre("Bonus avec un long nom, très long");
        liste.setType(Liste.BONUS);
        mListeViewModel.createListe(liste);
        liste = new Liste();
        liste.setTitre("Drôle");
        liste.setType(Liste.BONUS);
        mListeViewModel.createListe(liste);
    }
}
