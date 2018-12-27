package com.esprit.projetespritfood.models;

public class Commande {
    String idC;
    String libelleC;
    String quantitec;
    String dateC;
    String adresseC;
    String etatC;
    String usernamec;
    String imageC;

    public Commande(String idC, String libelleC, String quantitec, String dateC, String adresseC, String etatC, String usernamec) {
        this.idC = idC;
        this.libelleC = libelleC;
        this.quantitec = quantitec;
        this.dateC = dateC;
        this.adresseC = adresseC;
        this.etatC = etatC;
        this.usernamec = usernamec;
    }

    public Commande(String idC, String libelleC, String quantitec, String dateC, String adresseC, String etatC, String usernamec, String imageC) {
        this.libelleC = libelleC;
        this.quantitec = quantitec;
        this.dateC = dateC;
        this.adresseC = adresseC;
        this.etatC = etatC;
        this.usernamec = usernamec;
        this.imageC = imageC;
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getLibelleC() {
        return libelleC;
    }

    public void setLibelleC(String libelleC) {
        this.libelleC = libelleC;
    }

    public String getQuantitec() {
        return quantitec;
    }

    public void setQuantitec(String quantitec) {
        this.quantitec = quantitec;
    }

    public String getDateC() {
        return dateC;
    }

    public void setDateC(String dateC) {
        this.dateC = dateC;
    }

    public String getAdresseC() {
        return adresseC;
    }

    public void setAdresseC(String adresseC) {
        this.adresseC = adresseC;
    }

    public String getUsernamec() {
        return usernamec;
    }

    public void setUsernamec(String usernamec) {
        this.usernamec = usernamec;
    }

    public String getEtatC() {
        return etatC;
    }

    public void setEtatC(String etatC) {
        this.etatC = etatC;
    }

    public String getImageC() {
        return imageC;
    }

    public void setImageC(String etatC) {
        this.imageC = imageC;
    }


}
