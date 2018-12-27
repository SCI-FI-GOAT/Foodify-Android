package com.esprit.projetespritfood.models;

// creation d'une classe food pour fraire des instances stqtiquement !!
public class Food {
    public String libelle;
    public int img;
    public String descrip;
    public float prix;
    public float prixRemise;

    public int quantite;
    public String img2;
    public int id;

    public Food() {
    }

    public Food(int id, String libelle, String img2, int quantite, float prix) {
        this.id = id;
        this.libelle = libelle;
        this.img2 = img2;
        this.quantite = quantite;
        this.prix = prix;
    }

    public Food(String libelle, String img2, String descrip, float prix) {
        this.libelle = libelle;

        this.img2 = img2;
        this.descrip = descrip;
        this.prix = prix;
    }

    public Food(String libelle, String img2, String descrip, float prix, float prixRemise) {
        this.libelle = libelle;
        this.img2 = img2;
        this.descrip = descrip;
        this.prix = prix;
        this.prixRemise = prixRemise;
    }

    public Food(String libelle, String img2) {
        this.libelle = libelle;

        this.img2 = img2;
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

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
