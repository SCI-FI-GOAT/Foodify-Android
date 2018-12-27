package com.esprit.projetespritfood;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.DatabaseHelper;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.config.JSONParser;
import com.esprit.projetespritfood.models.MenuCarte;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FoodDetailFragment extends Fragment {
    ImageView imageView;
    TextView txt_libelle, txt_description, txt_prix, txt_prix_remise, txt_qte;
    Button btn_ajouter, btn_plus, btn_moins;
    int q;
    float p;
    String lib, img;
    public static ArrayList<MenuCarte> panier_liste = new ArrayList<>();

    String s = null;

    DatabaseHelper myDB;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_detail, container, false);

        if (HomeFragment.selected == "") {
            getActivity().setTitle("Details " + getArguments().getString("libelle"));
        } else {
            getActivity().setTitle("Details " + HomeFragment.selected);
        }

        if (!isNetworkConnected()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        txt_libelle = (TextView) root.findViewById(R.id.txt_food_detail_libelle);
        txt_description = (TextView) root.findViewById(R.id.txt_food_detail_description);
        txt_prix = (TextView) root.findViewById(R.id.txt_food_detail_prix);
        txt_prix_remise = (TextView) root.findViewById(R.id.txt_food_detail_prix_remise);

        btn_ajouter = (Button) root.findViewById(R.id.btn_ajouter_panier);
        imageView = (ImageView) root.findViewById(R.id.img_food_detail);

        txt_qte = (TextView) root.findViewById(R.id.txt_quantite);

        btn_plus = (Button) root.findViewById(R.id.btn_quantite_plus);
        btn_moins = (Button) root.findViewById(R.id.btn_quantite_moins);

        txt_prix_remise.setVisibility(View.GONE);

        myDB = new DatabaseHelper(getActivity());

        if (HomeFragment.selected == "") {
            //Récupération du fragment précédent

            //imageView.setImageResource(getArguments().getInt("img"));
            img = getArguments().getString("img");
            Picasso.with(getActivity()).load(img).into(imageView);

            float _prix = getArguments().getFloat("prix");
            float _remise = getArguments().getFloat("prixRemise");
            txt_libelle.setText(getArguments().getString("libelle"));
            txt_description.setText(getArguments().getString("description"));
            txt_prix.setText("Prix : " + _prix + " DT");
            txt_prix_remise.setText("Offre : " + _remise + " DT");

            if (_prix == _remise) {
                txt_prix_remise.setVisibility(View.GONE);
            } else {
                txt_prix.getPaint().setStrikeThruText(true);
                txt_prix.setTextColor(Color.parseColor("#544847"));
                txt_prix_remise.setVisibility(View.VISIBLE);
            }


            //p = getArguments().getFloat("prix");
            p = _remise;
        } else {
            HomeFragment.index = 1;
            JSONParser j = new JSONParser("InfoFood.php?libelleFood=" + HomeFragment.selected);
            j.execute();

            try {
                s = j.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                    IP.localhost + "InfoFood.php?libelleFood=" + HomeFragment.selected, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array = new JSONArray(s);
                        JSONObject o = array.getJSONObject(0);

                        float _prix = Float.parseFloat(o.getString("prixFood"));
                        float _remise = Float.parseFloat(o.getString("prixRemise"));


//                        imageView.setImageResource(R.drawable.menu_pizza);
                        img = IP.images + o.getString("imageFood");
                        Picasso.with(getActivity()).load(img).into(imageView);

                        txt_libelle.setText(o.getString("libelleFood"));
                        lib = o.getString("libelleFood");
                        txt_description.setText(o.getString("descriptionFood"));
                        txt_prix.setText("Prix : " + _prix + " DT");
                        txt_prix_remise.setText("Offre : " + _remise + " DT");


                        if (_prix == _remise) {
                            txt_prix_remise.setVisibility(View.GONE);
                        } else {
                            txt_prix.getPaint().setStrikeThruText(true);
                            txt_prix.setTextColor(Color.parseColor("#544847"));
                            txt_prix_remise.setVisibility(View.VISIBLE);
                        }

                        //p = Float.parseFloat(o.getString("prixFood"));
                        p = _remise;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Erreur ... ", Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjReq);


            HomeFragment.selected = "";
        }
        lib = txt_libelle.getText().toString();

        //Quantité + / -
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                q = Integer.parseInt(txt_qte.getText().toString());
                q++;
                txt_qte.setText(String.valueOf(q));
            }
        });

        btn_moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                q = Integer.parseInt(txt_qte.getText().toString());
                if (q > 1)
                    q--;
                txt_qte.setText(String.valueOf(q));
            }
        });


        //Passer la commande vers la liste statique "panier_liste"
        btn_ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (q == 0)
                    q = 1;
//                MenuCarte m = new MenuCarte(lib,R.drawable.menu_pizza,q,p);
//                panier_liste.add(m);

                //INSERT BD

                Cursor res = myDB.getAllData();

                boolean exist = false;
                if (res.getCount() > 0) {
                    while (res.moveToNext()) {
                        if (res.getString(1).equals(lib)) {
                            exist = true;
                            if (exist == true) {
                                boolean isUpdate = myDB.updateData(res.getString(0), Integer.parseInt(res.getString(2)) + q);
                                if (isUpdate == true) {
                                    Toast.makeText(getActivity(), "Ajouté au panier", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                }
                if (exist == false) {
                    boolean isInserted1 = myDB.insertData(lib, q, img, p);
                    if (isInserted1 == true) {
                        Toast.makeText(getActivity(), "Ajouté au panier", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });


        container.setFocusableInTouchMode(true);
        container.requestFocus();
        container.setOnKeyListener(new ViewGroup.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (HomeFragment.index == 1) {
                        HomeFragment.index = 0;
//                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        return true;
                    }


                }
                return false;
            }

        });


        return root;
    }


    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return mNetworkInfo != null;

        } catch (NullPointerException e) {
            return false;

        }
    }

}
