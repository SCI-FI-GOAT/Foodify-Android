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

import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Utilisateur;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminClientAdapter extends ArrayAdapter<Utilisateur> {

    Context context;
    List<Utilisateur> utilisateurs;
    int resources;

    public AdminClientAdapter(@NonNull Context context, int resource, @NonNull List<Utilisateur> objects) {
        super(context, resource, objects);
        this.context = context;
        this.utilisateurs = objects;
        this.resources = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);

        TextView txt_nom = (TextView) convertView.findViewById(R.id.item_client_nom);
        TextView txt_prenom = (TextView) convertView.findViewById(R.id.item_client_prenom);
        TextView txt_email = (TextView) convertView.findViewById(R.id.item_client_email);
        TextView txt_username = (TextView) convertView.findViewById(R.id.item_client_username);
        TextView txt_tel = (TextView) convertView.findViewById(R.id.item_client_tel);
        ImageView img = (ImageView) convertView.findViewById(R.id.item_client_img);

        txt_nom.setText(utilisateurs.get(position).getNom());
        txt_prenom.setText(utilisateurs.get(position).getPrenom());
        txt_email.setText(utilisateurs.get(position).getEmail());
        txt_username.setText(utilisateurs.get(position).getUsername());
        txt_tel.setText(utilisateurs.get(position).getTel());

        // get the url from the data you passed to the `Map`
        String url = utilisateurs.get(position).getImage();
        // do Picasso
        Picasso.with(context).load(IP.images + url).fit().into(img);
        return convertView;
    }

}
