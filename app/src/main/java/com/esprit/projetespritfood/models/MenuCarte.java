package com.esprit.projetespritfood.models;

// declaration d'une classe MenuCarte pour instancier statiquement les champs du Menu
public class MenuCarte {
    public String libelle;
    public int img;
    public int quantite;
    public float prix;

    public String img2;

    public MenuCarte() {

    }

    public MenuCarte(String libelle, int img) {
        this.libelle = libelle;
        this.img = img;
    }

    public MenuCarte(String libelle, String img2) {
        this.libelle = libelle;
        this.img2 = img2;
    }


    public MenuCarte(String libelle, int img, int quantite, float prix) {
        this.libelle = libelle;
        this.img = img;
        this.quantite = quantite;
        this.prix = prix;
    }

    public MenuCarte(String libelle, String img2, int quantite, float prix) {
        this.libelle = libelle;
        this.img2 = img2;
        this.quantite = quantite;
        this.prix = prix;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }
}
