package com.tournafond.raphael.spinit.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.database.dao.ListeDao;

@Database(entities = {Liste.class}, version = 2, exportSchema = false)
public abstract class SpinItDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile SpinItDatabase INSTANCE;

    // --- DAO ---
    public abstract ListeDao listeDao();

    // --- INSTANCE ---
    public static SpinItDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SpinItDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SpinItDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ---

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }
        };
    }
}
