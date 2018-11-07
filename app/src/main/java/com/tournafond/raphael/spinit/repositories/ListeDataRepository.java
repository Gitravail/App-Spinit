package com.tournafond.raphael.spinit.repositories;

import android.arch.lifecycle.LiveData;

import com.tournafond.raphael.spinit.database.dao.ListeDao;
import com.tournafond.raphael.spinit.model.Liste;

import java.util.List;

public class ListeDataRepository {
    private final ListeDao listeDao;

    public ListeDataRepository(ListeDao listeDao) { this.listeDao = listeDao; }

    // --- GET ---

    public LiveData<List<Liste>> getListes(){ return this.listeDao.getListes(); }

    // --- CREATE ---

    public void createListe(Liste liste){ listeDao.insertListe(liste); }

    // --- DELETE ---
    public void deleteListe(long listeId){ listeDao.deleteListe(listeId); }

    // --- UPDATE ---
    public void updateListe(Liste liste){ listeDao.updateListe(liste); }
}
