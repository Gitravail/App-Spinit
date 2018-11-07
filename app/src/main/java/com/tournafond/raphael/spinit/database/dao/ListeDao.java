package com.tournafond.raphael.spinit.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tournafond.raphael.spinit.model.Liste;

import java.util.List;

@Dao
public interface ListeDao {


        @Query("SELECT * FROM Liste")
        LiveData<List<Liste>> getListes();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long insertListe(Liste liste);

        @Update
        int updateListe(Liste liste);

        @Query("DELETE FROM Liste WHERE id = :listId")
        int deleteListe(long listId);
}
