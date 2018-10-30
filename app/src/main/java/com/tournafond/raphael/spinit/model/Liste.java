package com.tournafond.raphael.spinit.model;

import java.util.ArrayList;

public class Liste {
    private int type;
    private String titre;
    private ArrayList<String> action;
    private ArrayList<String> participant;

    final static int VIDE = -1;
    final static int FAVORI = 0;
    final static int NORMAL = 1;
    final static int BONUS = 2;

    public Liste() {
        this.type = VIDE;
        this.titre = "";
        this.action = new ArrayList<>();;
        this.participant = new ArrayList<>();;
    }

    public Liste(int type, String titre, ArrayList<String> action, ArrayList<String> participant) {
        this.type = type;
        this.titre = titre;
        this.action = action;
        this.participant = participant;
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
