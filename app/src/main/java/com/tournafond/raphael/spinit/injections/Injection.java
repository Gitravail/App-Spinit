package com.tournafond.raphael.spinit.injections;

import android.content.Context;

import com.tournafond.raphael.spinit.database.SpinItDatabase;
import com.tournafond.raphael.spinit.repositories.ListeDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Injection des dependances (complexe)
public class Injection {

    public static ListeDataRepository provideListeDataSource(Context context) {
        SpinItDatabase database = SpinItDatabase.getInstance(context);
        return new ListeDataRepository(database.listeDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ListeDataRepository dataSourceListe = provideListeDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceListe, executor);
    }
}
