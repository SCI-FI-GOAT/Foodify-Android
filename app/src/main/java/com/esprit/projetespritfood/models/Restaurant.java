package com.esprit.projetespritfood.models;

public class Restaurant {

    public String id, ville, adresse, latitude, longitude, _image;

    public Restaurant() {

    }

    public Restaurant(String ville, String adresse, String latitude, String longitude, String _image) {
        this.ville = ville;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this._image = _image;
    }
}
