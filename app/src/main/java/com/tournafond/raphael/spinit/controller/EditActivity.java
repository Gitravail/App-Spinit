package com.tournafond.raphael.spinit.controller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.injections.Injection;
import com.tournafond.raphael.spinit.injections.ViewModelFactory;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.utils.ItemClickSupport;
import com.tournafond.raphael.spinit.view.ListeViewModel;
import com.tournafond.raphael.spinit.view.adapter.EditListeAdapter;

import java.util.List;

import butterknife.BindView;


public class EditActivity extends AppCompatActivity implements EditListeAdapter.Listener {

    // FOR DESIGN
    private RecyclerView mRecyclerView;

    //FOR DATA
    private ListeViewModel mListeViewModel;
    private EditListeAdapter mEditListeAdapter;

    private List<Liste> listes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Make to run your application only in portrait mode*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit);

        mRecyclerView = findViewById(R.id.fragment_main_recycler_view);

        this.configureRecyclerView();
        this.configureViewModel();

        this.getListes();
    }

    // ACTIONS

    @Override
    public void onClickFavButton(int position) {
        Liste liste = this.mEditListeAdapter.getListe(position);
        int type = liste.getType();
        if (type == Liste.FAVORI) {
            liste.setType(Liste.NORMAL);
        } else if (type == Liste.NORMAL) {
            liste.setType(Liste.FAVORI);
        }
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

    /*Create a new liste*/
    private void createListe() {

    }

    /*Delete a liste*/
    private void deleteListe(Liste liste) {
        this.mListeViewModel.deleteListe(liste.getId());
    }

    /*Update a liste*/
    private void updateListe(Liste liste) {
        this.mListeViewModel.updateListe(liste);
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





}
