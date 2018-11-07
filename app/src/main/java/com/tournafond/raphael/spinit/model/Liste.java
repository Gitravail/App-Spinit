package com.tournafond.raphael.spinit.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.tournafond.raphael.spinit.database.converter.ListConverter;

import java.util.ArrayList;

@Entity
public class Liste {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int type;
    private String titre;
    @TypeConverters(ListConverter.class)
    private ArrayList<String> action;
    @TypeConverters(ListConverter.class)
    private ArrayList<String> participant;

    public final static int VIDE = -1;
    public final static int FAVORI = 0;
    public final static int NORMAL = 1;
    public final static int BONUS = 2;

    public Liste() {
        this.type = VIDE;
        this.titre = "";
        this.action = new ArrayList<>();;
        this.participant = new ArrayList<>();;
    }

    @Ignore
    public Liste(int type, String titre, ArrayList<String> action, ArrayList<String> participant) {
        this.type = type;
        this.titre = titre;
        this.action = action;
        this.participant = participant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public ArrayList<String> getAction() {
        return action;
    }

    public void setAction(ArrayList<String> action) {
        this.action = action;
    }

    public ArrayList<String> getParticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<String> participant) {
        this.participant = participant;
    }

    public boolean estVide() {
        return this.getType() == VIDE;
    }

    public boolean estFavori() {
        return this.getType() == FAVORI;
    }

    public boolean estNormal() {
        return this.getType() == NORMAL;
    }

    public boolean estBonus() {
        return this.getType() == BONUS;
    }
}
