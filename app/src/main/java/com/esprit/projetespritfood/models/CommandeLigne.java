package com.esprit.projetespritfood.models;

public class CommandeLigne {
    private String idLc, username, date, etat;

    public CommandeLigne() {
    }

    public CommandeLigne(String idLc, String username, String date, String etat) {
        this.idLc = idLc;
        this.username = username;
        this.date = date;
        this.etat = etat;
    }

    public String getIdLc() {
        return idLc;
    }

    public void setIdLc(String idLc) {
        this.idLc = idLc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
