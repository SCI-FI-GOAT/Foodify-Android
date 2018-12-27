package com.esprit.projetespritfood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.projetespritfood.models.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<Food> mData;

    public RecyclerViewAdapter(Context mContext, List<Food> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_carte_cardview, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.txt_menucarte_lib.setText(mData.get(i).libelle);
        //myViewHolder.imgv_menucarte_img.setImageResource(mData.get(i).img);

        Picasso.with(mContext).load(mData.get(i).img2).into(myViewHolder.imgv_menucarte_img);

        myViewHolder.txt_menucarte_prix.setText("Prix : " + mData.get(i).prixRemise + " dt");

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    FoodDetailFragment fd = new FoodDetailFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("libelle",mData.get(i).libelle);
//                    bundle.putInt("img",mData.get(i).img);
//                    bundle.putString("description",mData.get(i).descrip);
//                    bundle.putFloat("prix",mData.get(i).prix);
//                    fd.setArguments(bundle);

                Intent intent = new Intent(mContext, Frame.class);
                intent.putExtra("libelle", mData.get(i).libelle);
                intent.putExtra("img", String.valueOf(mData.get(i).img2));
                intent.putExtra("description", mData.get(i).descrip);
                intent.putExtra("prix", String.valueOf(mData.get(i).prix));
                intent.putExtra("prixRemise", String.valueOf(mData.get(i).prixRemise));
                mContext.startActivity(intent);

                //android.app.Fragment.getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container,fd).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_menucarte_lib;
        TextView txt_menucarte_prix;
        ImageView imgv_menucarte_img;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_menucarte_lib = (TextView) itemView.findViewById(R.id.item_menu_catre_libelle);
            imgv_menucarte_img = (ImageView) itemView.findViewById(R.id.item_menu_carte_img);
            txt_menucarte_prix = (TextView) itemView.findViewById(R.id.item_menu_catre_prix);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}

