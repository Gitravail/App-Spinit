package com.tournafond.raphael.spinit.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class User {

    @PrimaryKey
    private long listeParDefaut; // liste par defaut au lancement de l'app
    private int typeElement; // type courant (ajout d'options ou de personnes)

    public final static long PAS_DE_LISTE_PAR_DEFAUT = -1;

    public final static int ACTION = 0;
    public final static int PARTICIPANT = 1;
    public static final int OPTION = 2;

    // Constructors ***

    public User() {
        this.listeParDefaut = PAS_DE_LISTE_PAR_DEFAUT;
        this.typeElement = ACTION;
    }

    @Ignore
    public User(int listeParDefaut, int typeElement) {
        this.listeParDefaut = listeParDefaut;
        this.typeElement = typeElement;
    }

    // ***


    // Get/Set ***

    public long getListeParDefaut() {
        return listeParDefaut;
    }

    public void setListeParDefaut(long listeParDefaut) {
        this.listeParDefaut = listeParDefaut;
    }

    public int getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(int typeElement) {
        this.typeElement = typeElement;
    }

    public String getPrefixe() {
        String res;
        switch (getTypeElement()) {
            case ACTION:
                res = "Action";
                break;
            case PARTICIPANT:
                res = "Participant";
                break;
            case OPTION:
                res = "Option";
                break;
            default:
                res = "Choix";
        }
        return res;
    }

    // ***


}
