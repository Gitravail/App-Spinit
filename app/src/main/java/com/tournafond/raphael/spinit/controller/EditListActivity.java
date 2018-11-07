package com.tournafond.raphael.spinit.controller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.injections.Injection;
import com.tournafond.raphael.spinit.injections.ViewModelFactory;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.view.ListeViewModel;
import com.tournafond.raphael.spinit.view.adapter.MainListeAdapter;

import java.util.ArrayList;

public class EditListActivity extends AppCompatActivity {

    // Creation des elements en lien avec le layout
    private ImageButton mBtnSave; // Bouton de sauvegarde de la liste
    private ImageButton mBtnAdd; // bouton d'ajout d'un element
    private ImageButton mBtnDelete; // bouton de suppression d'une liste
    private ImageButton mBtnReturn; // bouton de retour

    private LinearLayout mLayoutAP; // layout contenant le menu action/participant
    private Button mBtnAction; // bouton pour le choix d'edition des actions
    private Button mBtnParticipant; // bouton pour le choix d'edition des participants
    private int mActionParticipant;

    private ListView mList;
    private MainListeAdapter mMainListeAdapter;

    private ArrayList<String> listeAction = new ArrayList<>();
    private ArrayList<String> listeParticipant = new ArrayList<>();

    private User utilisateur = new User();
    private Liste liste;
    public static final int ACTION = 0;
    public static final int PARTICPANT = 1;
    public static final int OPTION = 2;

    private static final int EDIT_LIST_ACTIVITY_REQUEST_CODE = 20;


    // Lors du lancement de l'app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_edit_list);


        // Ajout des animations
        final Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);

        // Lien avec les element du layout avec leur identifiant
        mBtnSave = findViewById(R.id.btnSave);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnDelete = findViewById(R.id.btnDelete);
        mBtnReturn = findViewById(R.id.btnReturn);

        mLayoutAP = findViewById(R.id.layoutAP);
        mBtnAction = findViewById(R.id.btnAction);
        mBtnParticipant = findViewById(R.id.btnParticipant);
        mActionParticipant = ACTION; // valeur par defaut

        mList = findViewById(R.id.itemList);
        mList.setItemsCanFocus(true);
        mMainListeAdapter = new MainListeAdapter(this, utilisateur, MainListeAdapter.ACTION);
        mList.setAdapter(mMainListeAdapter);

        mLayoutAP.setVisibility(LinearLayout.VISIBLE);
        liste = (Liste) getIntent().getSerializableExtra("Liste");
        setOption();

        listeAction = liste.getAction();
        listeParticipant = liste.getParticipant();
        mMainListeAdapter = new MainListeAdapter(getApplicationContext(), utilisateur.getPrefixe(), listeAction);
        mList.setAdapter(mMainListeAdapter);

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

        // Ajout d'un evenement de clic sur le bouton de suppression
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                if (!mMainListeAdapter.getList().isEmpty()) {
                    dialogOuiNon("Voulez vous vraiment supprimer le contenu de cette liste ?");
                }
            }
        });

        // Ajout d'un evenement de clic pour le retour
        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                finish();
            }
        });

        // Ajout d'un evenement de clic pour la sauvegarde
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listeValide()) {
                    v.startAnimation(zoom);
                    gereSauvegarde();
                }
            }
        });
    }

    // Gestion du choix ****************************************************************************

    private boolean listeValide() {
        if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
            listeAction = mMainListeAdapter.getList();
        } else {
            listeParticipant = mMainListeAdapter.getList();
        }
        int tailleListeAction = listeAction.size();
        int tailleListeParticipant = listeParticipant.size();

        if (tailleListeParticipant > 0) {
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

    private void gereSauvegarde() {
        if (listeValide()) {
            sauvegarderListe();
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

    DialogInterface.OnClickListener changerTitre = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    sauvegarderListeTitre();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    if (mActionParticipant == ACTION || mActionParticipant == OPTION) {
                        listeAction = mMainListeAdapter.getList();
                    } else {
                        listeParticipant = mMainListeAdapter.getList();
                    }
                    liste.setAction(listeAction);
                    liste.setParticipant(listeParticipant);
                    updateListe(liste);
                    Toast.makeText(EditListActivity.this, "La liste a bien été enregistré", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void sauvegarderListe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(liste.getTitre()).setMessage("Voulez-vous modifier le titre ?").setPositiveButton("Oui", changerTitre)
                .setNegativeButton("Non", changerTitre).show();
    }

    private void sauvegarderListeTitre() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
        alert.setTitle("Nouveau titre");

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
                    liste.setAction(listeAction);
                    liste.setParticipant(listeParticipant);
                    updateListe(liste);
                    Toast.makeText(EditListActivity.this, "La liste a bien été enregistré", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditListActivity.this, "Veuillez entrer un titre valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    private void updateListe(Liste liste) {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        ListeViewModel mListeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListeViewModel.class);
        System.out.println(liste);
        mListeViewModel.updateListe(liste);
    }
}
