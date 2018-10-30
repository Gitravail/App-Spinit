package com.tournafond.raphael.spinit.model;

import java.util.ArrayList;

public class User {

    private ArrayList<Liste> listesEnregistrees; // listes enregistres par l'utilisateur
    private Liste listeCourante; // liste en cours
    private Liste listeParDefaut; // liste par defaut au lancement de l'app
    private int typeElement; // type courant (ajout d'options ou de personnes)

    public final static int PERSONNE = 0;
    public final static int OPTION = 1;

    // Constructors ***

    public User() {
        this.listesEnregistrees = new ArrayList<Liste>();
        this.listeCourante = new Liste();
        this.listeParDefaut = listeCourante;
        this.typeElement = OPTION;
    }

    public User(ArrayList<Liste> listesEnregistrees, Liste listeCourante, Liste listeParDefaut, int typeElement) {
        this.listesEnregistrees = listesEnregistrees;
        this.listeCourante = listeCourante;
        this.listeParDefaut = listeParDefaut;
        this.typeElement = typeElement;
    }

    // ***


    // Get/Set ***

    public ArrayList<Liste> getListesEnregistrees() {
        return listesEnregistrees;
    }

    public void setListesEnregistrees(ArrayList<Liste> listesEnregistrees) {
        this.listesEnregistrees = listesEnregistrees;
    }

    public Liste getListeCourante() {
        return listeCourante;
    }

    public void setListeCourante(Liste listeCourante) {
        this.listeCourante = listeCourante;
    }

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

    // additional get
    public ArrayList<Liste> getFavoris() {
        ArrayList<Liste> res = new ArrayList<Liste>();
        Liste courante;
        int taille = this.listesEnregistrees.size();
        for (int i = 0; i < taille; i++) {
            courante = listesEnregistrees.get(i);
            if (courante.estFavori()) {
                res.add(courante);
            }
        }
        return res;
    }

    public ArrayList<Liste> getNormal() {
        ArrayList<Liste> res = new ArrayList<Liste>();
        Liste courante;
        int taille = this.listesEnregistrees.size();
        for (int i = 0; i < taille; i++) {
            courante = listesEnregistrees.get(i);
            if (courante.estNormal()) {
                res.add(courante);
            }
        }
        return res;
    }

    public ArrayList<Liste> getBonus() {
        ArrayList<Liste> res = new ArrayList<Liste>();
        Liste courante;
        int taille = this.listesEnregistrees.size();
        for (int i = 0; i < taille; i++) {
            courante = listesEnregistrees.get(i);
            if (courante.estBonus()) {
                res.add(courante);
            }
        }
        return res;
    }

    public String getPrefixe() {
        String res;
        switch (typeElement) {
            case OPTION:
                res = "Action";
                break;
            case PERSONNE:
                res = "Participant";
                break;
            default:
                res = "Choix";
        }
        return res;
    }

    // ***


}
