package com.tournafond.raphael.spinit.injections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.tournafond.raphael.spinit.view.ListeViewModel;
import com.tournafond.raphael.spinit.repositories.ListeDataRepository;

import java.util.concurrent.Executor;

// permet de deleguer la creation du ViewModel
// cela nous permet de regrouper la creation des ViewModels si la creation d'un nouveau nous venait a l'esprit
// pour le menu drawer par exemple
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ListeDataRepository listeDataSource;
    private final Executor executor;

    public ViewModelFactory(ListeDataRepository listeDataSource, Executor executor) {
        this.listeDataSource = listeDataSource;
        this.executor = executor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListeViewModel.class)) {
            return (T) new ListeViewModel(listeDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
