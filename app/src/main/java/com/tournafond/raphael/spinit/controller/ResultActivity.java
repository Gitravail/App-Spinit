package com.tournafond.raphael.spinit.controller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.injections.Injection;
import com.tournafond.raphael.spinit.injections.ViewModelFactory;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.view.ListeViewModel;
import com.tournafond.raphael.spinit.view.holder.ListDualItem;
import com.tournafond.raphael.spinit.view.adapter.ResultListeAdapter;

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
            resultatAction = "doit " + resultatAction + ".";
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
        ResultListeAdapter dtva = new ResultListeAdapter(this, R.layout.dual_item, listDualItemArrayList);
        mList.setAdapter(dtva);
        // ***

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
                v.startAnimation(zoom);
                sauvegarderListe();
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
                    liste.setTitre(text);
                    liste.setType(Liste.NORMAL);
                    liste.setAction(listeAction);
                    liste.setParticipant(listeParticipant);
                    insererListe(liste);
                    Toast.makeText(ResultActivity.this, "La liste a bien été enregistré", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResultActivity.this, "Veuillez entrer un titre valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    private void insererListe(Liste liste) {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        ListeViewModel mListeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListeViewModel.class);
        mListeViewModel.createListe(liste);
    }
}
