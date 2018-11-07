package com.tournafond.raphael.spinit.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.database.dao.ListeDao;
import com.tournafond.raphael.spinit.database.dao.UserDao;

import java.util.ArrayList;

@Database(entities = {Liste.class, User.class}, version = 1, exportSchema = false)
public abstract class SpinItDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile SpinItDatabase INSTANCE;

    // --- DAO ---
    public abstract ListeDao listeDao();
    public abstract UserDao userDao();

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

                ContentValues contentValues = new ContentValues();
                contentValues.put("type", Liste.NORMAL);
                contentValues.put("titre", "Liste de test");
                ArrayList<String> actionList = new ArrayList<>();
                actionList.add("Chanter allumer le feu");
                actionList.add("Exploser une porte");
                actionList.add("Dire bonjour à 22h");
                actionList.add("Aller chez Valérie Damido et dire que sa maison est moche");
                actionList.add("Regarder Rrrrrr! deux fois d'affilé");
                String actionJSON = new Gson().toJson(actionList);
                contentValues.put("action", actionJSON);
                ArrayList<String> participantList = new ArrayList<>();
                participantList.add("Ambroise");
                participantList.add("Paul");
                participantList.add("Raphaël");
                String participantJSON = new Gson().toJson(participantList);
                contentValues.put("action", participantJSON);

                db.insert("Liste", OnConflictStrategy.IGNORE, contentValues);

                contentValues.clear();
                contentValues = new ContentValues();
                contentValues.put("listeParDefaut", 1);
                contentValues.put("typeElement", User.ACTION);

                db.insert("User", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }
}
