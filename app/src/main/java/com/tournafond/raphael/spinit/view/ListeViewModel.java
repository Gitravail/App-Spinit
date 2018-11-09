package com.tournafond.raphael.spinit.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.repositories.ListeDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

// view model de la recyclerview des lsites
public class ListeViewModel extends ViewModel {
    // REPOSITORIES
    private final ListeDataRepository listeDataSource;
    // Afin de faciliter l'execution en arriere plan
    private final Executor executor;


    public ListeViewModel(ListeDataRepository listeDataSource, Executor executor) {
        this.listeDataSource = listeDataSource;
        this.executor = executor;
    }

    public void init() {
    }

    // -------------
    // FOR ITEM
    // -------------

    public LiveData<List<Liste>> getListes() {
        return listeDataSource.getListes();
    }

    // L'execution asynchrone permet d'eviter les ralentissements lors des requetes
    // utile lorsque l'on charge un grand volume de donnees
    public void createListe(Liste liste) {
        executor.execute(() -> {
            listeDataSource.createListe(liste);
        });
    }

    public void deleteListe(long listeId) {
        executor.execute(() -> {
            listeDataSource.deleteListe(listeId);
        });
    }

    public void updateListe(Liste liste) {
        System.out.println(liste);
        executor.execute(() -> {
            listeDataSource.updateListe(liste);
        });
    }
}
