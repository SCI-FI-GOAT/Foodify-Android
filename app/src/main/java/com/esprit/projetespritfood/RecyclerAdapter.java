package com.esprit.projetespritfood;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.projetespritfood.models.Food;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Backend Developer on 9/10/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public static String libelle = "";

    Context mContext;
    private final ArrayList<Food> data;

    public RecyclerAdapter(ArrayList<Food> data, Context mContext) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(data.get(position).libelle);
        Picasso.with(mContext).load(data.get(position).img2).into(holder.icon);


/*
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                libelle = data.get(position).getLibelle();
                libelle=libelle.substring(0,libelle.indexOf("\n"));

                HomeFragment.selected=libelle;
                Intent intent = new Intent(mContext, Frame.class);
                mContext.startActivity(intent);
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_dash);
            icon = (ImageView) itemView.findViewById(R.id.img_dash);
        }
    }
}