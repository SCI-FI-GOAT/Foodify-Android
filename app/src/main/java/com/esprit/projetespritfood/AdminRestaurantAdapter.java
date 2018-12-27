package com.esprit.projetespritfood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.projetespritfood.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminRestaurantAdapter extends ArrayAdapter<Restaurant> {

    Context context;
    List<Restaurant> restaurants;
    int resources;

    public AdminRestaurantAdapter(@NonNull Context context, int resource, @NonNull List<Restaurant> objects) {
        super(context, resource, objects);
        this.context = context;
        this.restaurants = objects;
        this.resources = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);

        TextView txt_ville = (TextView) convertView.findViewById(R.id.item_restau_libelle);
        TextView txt_adresse = (TextView) convertView.findViewById(R.id.item_restau_adresse);
        ImageView img = (ImageView) convertView.findViewById(R.id.item_restau_img);

        txt_ville.setText(restaurants.get(position).ville);
        txt_adresse.setText(restaurants.get(position).adresse);

        // get the url from the data you passed to the `Map`
        String url = restaurants.get(position)._image;
        // do Picasso
        Picasso.with(context).load(url).fit().into(img);

        return convertView;
    }

}
