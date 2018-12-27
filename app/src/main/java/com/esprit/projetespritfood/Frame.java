package com.esprit.projetespritfood;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Frame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Log.d("State connection ", "CONNECTED");
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(Frame.this);
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }


        if (!RecyclerAdapter.libelle.equals("")) {
            RecyclerAdapter.libelle = "";
            getFragmentManager().beginTransaction().replace(R.id.container, new FoodDetailFragment()).commit();
        } else {

            FoodDetailFragment fd = new FoodDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("libelle", getIntent().getStringExtra("libelle"));
            bundle.putString("img", getIntent().getStringExtra("img"));
            bundle.putString("description", getIntent().getStringExtra("description"));
            bundle.putFloat("prix", Float.parseFloat(getIntent().getStringExtra("prix")));
            bundle.putFloat("prixRemise", Float.parseFloat(getIntent().getStringExtra("prixRemise")));
            fd.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.container, fd).commit();
        }
    }
}
