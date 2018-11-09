package com.tournafond.raphael.spinit.controller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.injections.Injection;
import com.tournafond.raphael.spinit.injections.ViewModelFactory;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.utils.ItemClickSupport;
import com.tournafond.raphael.spinit.view.ListeViewModel;
import com.tournafond.raphael.spinit.view.adapter.EditListeAdapter;

import java.util.List;


public class EditActivity extends AppCompatActivity implements EditListeAdapter.Listener {

    // FOR DESIGN
    private ImageButton mBtnReturn;
    private RecyclerView mRecyclerView;

    // FOR DATA
    private ListeViewModel mListeViewModel;
    private EditListeAdapter mEditListeAdapter;

    public static final String BUNDLE_LISTE = "BUNDLE_LISTE";

    public static final int EDIT_LISTE_REQUEST_CODE = 21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Make to run your application only in portrait mode*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit);

        mBtnReturn = findViewById(R.id.btnReturn);

        //Animation
        final Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);

        // recuperation de la recycler view
        mRecyclerView = findViewById(R.id.fragment_main_recycler_view);

        this.configureRecyclerView();
        this.configureViewModel();

        this.getListes();

        // clic sur le bouton retour
        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(zoom);
                finish();
            }
        });
    }

    // ACTIONS

    // Clic sur le bouton favoris
    @Override
    public void onClickFavButton(int position) {
        Liste liste = this.mEditListeAdapter.getListe(position);
        int type = liste.getType();
        if (type == Liste.FAVORI) {
            liste.setType(Liste.NORMAL);
        } else if (type == Liste.NORMAL) {
            liste.setType(Liste.FAVORI);
        }
        // reaffichage
        this.mListeViewModel.updateListe(liste);
    }

    //DATA
    /*Configure view model*/
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mListeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListeViewModel.class);
        this.mListeViewModel.init();
    }

    /*Get all listes*/
    private void getListes() {
        this.mListeViewModel.getListes().observe(this, this::updateListesList);
    }

    /*Delete a liste*/
    public void deleteListe(Liste liste) {
        this.mListeViewModel.deleteListe(liste.getId());
    }

    /*Update a liste*/
    private void updateListe(Liste liste) {
        this.mListeViewModel.updateListe(liste);
    }

    /*Load liste in main activity*/
    public void chargeListe(Liste liste) {
        Intent mainactivity = new Intent();
        mainactivity.putExtra(BUNDLE_LISTE, liste);
        setResult(RESULT_OK, mainactivity);
        finish();
    }

    /* Accès à la modifiaction d'une liste */
    public void modifierListe(Liste liste) {
        Intent editListActivity = new Intent(EditActivity.this, EditListActivity.class);
        editListActivity.putExtra("Liste", liste);
        startActivityForResult(editListActivity, EDIT_LISTE_REQUEST_CODE);
    }

    // UI
    /* Configure Recycler View*/
    private void configureRecyclerView(){
        this.mEditListeAdapter = new EditListeAdapter(this);
        this.mRecyclerView.setAdapter(this.mEditListeAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(mRecyclerView, R.layout.recycler_cell)
                .setOnItemClickListener((recyclerView1, position, v) -> this.updateListe(this.mEditListeAdapter.getListe(position)));
    }

    /*Update listes list*/
    private void updateListesList(List<Liste> listes) {
        this.mEditListeAdapter.updateData(listes);
    }

    /* Lors de la récupération suite à la modification */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (EDIT_LISTE_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // update de la liste
            getListes();
        }
    }

}
