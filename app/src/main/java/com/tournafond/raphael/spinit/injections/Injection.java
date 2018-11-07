package com.tournafond.raphael.spinit.injections;

import android.content.Context;

import com.tournafond.raphael.spinit.database.SpinItDatabase;
import com.tournafond.raphael.spinit.repositories.ListeDataRepository;
import com.tournafond.raphael.spinit.repositories.UserDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static ListeDataRepository provideListeDataSource(Context context) {
        SpinItDatabase database = SpinItDatabase.getInstance(context);
        return new ListeDataRepository(database.listeDao());
    }

    public static UserDataRepository provideUserDataSource(Context context) {
        SpinItDatabase database = SpinItDatabase.getInstance(context);
        return new UserDataRepository(database.userDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ListeDataRepository dataSourceListe = provideListeDataSource(context);
        UserDataRepository dataSourceUser = provideUserDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceListe, dataSourceUser, executor);
    }
}
