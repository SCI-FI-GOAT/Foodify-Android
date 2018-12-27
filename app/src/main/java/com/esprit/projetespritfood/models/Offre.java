package com.esprit.projetespritfood.models;

public class Offre {
    String idOffre, dateoffre, description, taux, id_food, _image;

    public Offre() {
    }

    public Offre(String idOffre, String dateoffre, String description, String taux, String id_food) {
        this.idOffre = idOffre;
        this.dateoffre = dateoffre;
        this.description = description;
        this.taux = taux;
        this.id_food = id_food;
    }

    public Offre(String idOffre, String dateoffre, String description, String taux, String id_food, String _image) {
        this.idOffre = idOffre;
        this.dateoffre = dateoffre;
        this.description = description;
        this.taux = taux;
        this.id_food = id_food;
        this._image = _image;
    }

    public String getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(String idOffre) {
        this.idOffre = idOffre;
    }

    public String getDateoffre() {
        return dateoffre;
    }

    public void setDateoffre(String dateoffre) {
        this.dateoffre = dateoffre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public String getId_food() {
        return id_food;
    }

    public void setId_food(String id_food) {
        this.id_food = id_food;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }
}
