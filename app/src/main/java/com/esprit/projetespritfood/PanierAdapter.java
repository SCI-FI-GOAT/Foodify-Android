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

import com.esprit.projetespritfood.models.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PanierAdapter extends ArrayAdapter<Food> {
    Context context;
    List<Food> food;
    int resources;

    public PanierAdapter(Context context, int resource, List<Food> objects) {
        super(context, resource, objects);
        this.context = context;
        this.food = objects;
        this.resources = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);

        TextView txt_lib = (TextView) convertView.findViewById(R.id.item_panier_libelle);
        txt_lib.setText(food.get(position).getLibelle());

        TextView txt_prix = (TextView) convertView.findViewById(R.id.item_panier_prix);
        txt_prix.setText(String.valueOf(food.get(position).getPrix()));

        TextView txt_qte = (TextView) convertView.findViewById(R.id.item_panier_quantite);
        txt_qte.setText(String.valueOf(food.get(position).getQuantite()));

        ImageView img = (ImageView) convertView.findViewById(R.id.item_panier_img);
        Picasso.with(context).load(food.get(position).getImg2()).into(img);


        return convertView;
    }
}
