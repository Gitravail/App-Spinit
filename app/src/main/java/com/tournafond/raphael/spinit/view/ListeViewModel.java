package com.tournafond.raphael.spinit.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.repositories.ListeDataRepository;
import com.tournafond.raphael.spinit.repositories.UserDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ListeViewModel extends ViewModel {
    // REPOSITORIES
    private final ListeDataRepository listeDataSource;
    private final UserDataRepository userDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<User> currentUser;

    public ListeViewModel(ListeDataRepository listeDataSource, UserDataRepository userDataSource, Executor executor) {
        this.listeDataSource = listeDataSource;
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    public void init() {
        if (this.currentUser != null) {
            return;
        }
        currentUser = userDataSource.getUser();
    }

    // -------------
    // FOR USER
    // -------------

    public LiveData<User> getUser() { return this.currentUser;  }

    // -------------
    // FOR ITEM
    // -------------

    public LiveData<List<Liste>> getListes() {
        return listeDataSource.getListes();
    }

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
        executor.execute(() -> {
            listeDataSource.updateListe(liste);
        });
    }
}
