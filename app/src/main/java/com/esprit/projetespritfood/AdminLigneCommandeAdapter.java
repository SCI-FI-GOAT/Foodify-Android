package com.esprit.projetespritfood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.esprit.projetespritfood.models.CommandeLigne;

import java.util.List;

public class AdminLigneCommandeAdapter extends ArrayAdapter<CommandeLigne> {

    Context context;
    List<CommandeLigne> lignes;
    int resources;

    public AdminLigneCommandeAdapter(@NonNull Context context, int resource, @NonNull List<CommandeLigne> objects) {
        super(context, resource, objects);
        this.context = context;
        this.lignes = objects;
        this.resources = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);

        TextView txt_id = (TextView) convertView.findViewById(R.id.item_ligne_commande_id);
        TextView txt_user = (TextView) convertView.findViewById(R.id.item_ligne_commande_user);
        TextView txt_date = (TextView) convertView.findViewById(R.id.item_ligne_commande_date);
        TextView txt_etat = (TextView) convertView.findViewById(R.id.item_ligne_commande_etat);

        txt_id.setText(lignes.get(position).getIdLc());
        txt_user.setText(lignes.get(position).getUsername());
        txt_date.setText(lignes.get(position).getDate());
        txt_etat.setText(lignes.get(position).getEtat());
        if (lignes.get(position).getEtat().equals("En cours")) {
            txt_etat.setTextColor(convertView.getResources().getColor(R.color.colorOrange));
            //txt_etat.setBackgroundColor(convertView.getResources().getColor(R.color.colorOrange));
        }
        if (lignes.get(position).getEtat().equals("Traitee")) {
            txt_etat.setTextColor(convertView.getResources().getColor(R.color.colorGreen));
            //txt_etat.setBackgroundColor(convertView.getResources().getColor(R.color.colorGreen));

        }
        if (lignes.get(position).getEtat().equals("Annulee")) {
            txt_etat.setTextColor(convertView.getResources().getColor(R.color.colorRed));
            //txt_etat.setBackgroundColor(convertView.getResources().getColor(R.color.colorRed));
        }

        return convertView;
    }

}
