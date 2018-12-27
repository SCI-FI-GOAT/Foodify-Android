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

import com.esprit.projetespritfood.models.MenuCarte;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuAdapter extends ArrayAdapter<MenuCarte> {
    Context context;
    List<MenuCarte> menu;
    int resources;

    public MenuAdapter(Context context, int resource, List<MenuCarte> objects) {
        super(context, resource, objects);
        this.context = context;
        this.menu = objects;
        this.resources = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);

        TextView txt_lib = (TextView) convertView.findViewById(R.id.item_menu_libelle);
        txt_lib.setText(menu.get(position).getLibelle());

        ImageView img = (ImageView) convertView.findViewById(R.id.item_menu_img);
        Picasso.with(context).load(menu.get(position).getImg2()).into(img);


        return convertView;
    }
}