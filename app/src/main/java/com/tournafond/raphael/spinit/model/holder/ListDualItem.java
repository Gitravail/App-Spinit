package com.tournafond.raphael.spinit.model.holder;

public class ListDualItem {
    private String action;
    private String participant;

    public ListDualItem(String action, String participant) {
        this.action = action;
        this.participant = participant;
    }

    public String getAction() {return action;}

    public String getParticipant() {return participant;}
}
