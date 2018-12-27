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

import com.esprit.projetespritfood.models.Commande;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommandeUserAdapter extends ArrayAdapter<Commande> {

    Context context;
    List<Commande> commandes;
    int resources;

    public CommandeUserAdapter(@NonNull Context context, int resource, @NonNull List<Commande> objects) {
        super(context, resource, objects);
        this.context = context;
        this.commandes = objects;
        this.resources = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);

        TextView txt_libelle = (TextView) convertView.findViewById(R.id.item_commande_user_libelle);
        TextView txt_quantite = (TextView) convertView.findViewById(R.id.item_commande_user_quantite);
        TextView txt_adresse = (TextView) convertView.findViewById(R.id.item_commande_user_adresse);
        TextView txt_date = (TextView) convertView.findViewById(R.id.item_commande_user_date);
        TextView txt_etat = (TextView) convertView.findViewById(R.id.item_commande_user_etat);
        ImageView img = (ImageView) convertView.findViewById(R.id.item_commande_user_img);

        txt_libelle.setText(commandes.get(position).getLibelleC());
        txt_quantite.setText(commandes.get(position).getQuantitec());
        txt_adresse.setText(commandes.get(position).getAdresseC());
        txt_date.setText(commandes.get(position).getDateC());
        txt_etat.setText(commandes.get(position).getEtatC());

        // get the url from the data you passed to the `Map`
        String url = commandes.get(position).getImageC();
        // do Picasso
        Picasso.with(context).load(url).fit().into(img);

        return convertView;
    }

}


