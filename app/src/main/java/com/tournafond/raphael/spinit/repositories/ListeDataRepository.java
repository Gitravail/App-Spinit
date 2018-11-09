package com.tournafond.raphael.spinit.repositories;

import android.arch.lifecycle.LiveData;

import com.tournafond.raphael.spinit.database.dao.ListeDao;
import com.tournafond.raphael.spinit.model.Liste;

import java.util.List;


// Le but du repository est d'isoler la source de données (DAO) du ViewModel
// afin que ce dernier ne manipule pas directement la source de données
public class ListeDataRepository {
    private final ListeDao listeDao;

    public ListeDataRepository(ListeDao listeDao) { this.listeDao = listeDao; }

    // --- GET ---

    // LiveData, type qui permet "d'observer" plus facilement les changements
    // tout en respectant le cycle de vie de notre application
    public LiveData<List<Liste>> getListes(){ return this.listeDao.getListes(); }

    // --- CREATE ---

    public void createListe(Liste liste){ listeDao.insertListe(liste); }

    // --- DELETE ---
    public void deleteListe(long listeId){ listeDao.deleteListe(listeId); }

    // --- UPDATE ---
    public void updateListe(Liste liste){ listeDao.updateListe(liste); }
}
