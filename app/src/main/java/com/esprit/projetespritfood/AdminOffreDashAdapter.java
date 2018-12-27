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

import com.esprit.projetespritfood.models.Offre;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminOffreDashAdapter extends ArrayAdapter<Offre> {

    Context context;
    List<Offre> offres;
    int resources;

    public AdminOffreDashAdapter(@NonNull Context context, int resource, @NonNull List<Offre> objects) {
        super(context, resource, objects);
        this.context = context;
        this.offres = objects;
        this.resources = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);

        TextView txt_id = (TextView) convertView.findViewById(R.id.item_offre_id);
        TextView txt_libelle = (TextView) convertView.findViewById(R.id.item_offre_libelle);
        TextView txt_description = (TextView) convertView.findViewById(R.id.item_offre_description);
        TextView txt_date = (TextView) convertView.findViewById(R.id.item_offre_date);
        TextView txt_taux = (TextView) convertView.findViewById(R.id.item_offre_taux);
        ImageView img = (ImageView) convertView.findViewById(R.id.item_offre_image);

        txt_id.setText(offres.get(position).getIdOffre());
        txt_libelle.setText(offres.get(position).getId_food());
        txt_description.setText(offres.get(position).getDescription());
        txt_date.setText(offres.get(position).getDateoffre());
        txt_taux.setText(offres.get(position).getTaux());

        // get the url from the data you passed to the `Map`
        String url = offres.get(position).get_image();
        // do Picasso
        Picasso.with(context).load(url).fit().into(img);

        return convertView;
    }

}
