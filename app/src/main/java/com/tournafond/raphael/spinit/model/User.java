package com.tournafond.raphael.spinit.model;


// Utilisateur (sauvegarde de preferences et etat courant)
public class User {
    private int typeElement; // type courant (ajout d'options ou de personnes)

    public final static int ACTION = 0;
    public final static int PARTICIPANT = 1;
    public static final int OPTION = 2;

    // Constructors ***

    public User() {
        this.typeElement = ACTION;
    }

    public User(int typeElement) {
        this.typeElement = typeElement;
    }

    // ***


    // Get/Set ***

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
