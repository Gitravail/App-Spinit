package com.tournafond.raphael.spinit.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import java.util.ArrayList;

@Entity(foreignKeys = @ForeignKey(entity = Liste.class,
        parentColumns = "id",
        childColumns = "listeParDefaut"))
public class User {

    private Liste listeParDefaut; // liste par defaut au lancement de l'app
    private int typeElement; // type courant (ajout d'options ou de personnes)

    public final static int ACTION = 0;
    public final static int PARTICIPANT = 1;
    public static final int OPTION = 2;

    // Constructors ***

    public User() {
        this.listeParDefaut = new Liste();
        this.typeElement = ACTION;
    }

    public User(ArrayList<Liste> listesEnregistrees, Liste listeParDefaut, int typeElement) {
        this.listeParDefaut = listeParDefaut;
        this.typeElement = typeElement;
    }

    // ***


    // Get/Set ***

    public Liste getListeParDefaut() {
        return listeParDefaut;
    }

    public void setListeParDefaut(Liste listeParDefaut) {
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
