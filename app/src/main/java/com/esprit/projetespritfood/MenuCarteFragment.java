package com.esprit.projetespritfood;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.esprit.projetespritfood.MenuFragment.selectedtlib;

public class MenuCarteFragment extends Fragment {
    RecyclerView myrv;
    ArrayList<Food> list_food;
    ArrayList<Food> maliste;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu_carte, container, false);

        getActivity().setTitle("Carte " + selectedtlib);

        if (!isNetworkConnected()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        myrv = (RecyclerView) root.findViewById(R.id.recyclerview_carte);


        //Récupération du webservice
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeFood.php?libelleFood=" + selectedtlib, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list_food = new ArrayList<>();
                list_food.clear();
                maliste = new ArrayList<>();
                maliste.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        //list_food.add(new Food(o.getString("libelleFood"),R.drawable.menu_pizza,o.getString("descriptionFood"),Float.parseFloat(o.getString("prixFood"))));
                        //list_food.add(new Food(o.getString("libelleFood"),IP.images+o.getString("imageFood"),o.getString("descriptionFood"),Float.parseFloat(o.getString("prixFood"))));
                        list_food.add(new Food(
                                o.getString("libelleFood"),
                                IP.images + o.getString("imageFood"),
                                o.getString("descriptionFood"),
                                Float.parseFloat(o.getString("prixFood")),
                                Float.parseFloat(o.getString("prixRemise"))
                        ));

                    }

                    for (Food f : list_food) {
                        maliste.add(f);
                    }

                    RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getActivity(), maliste);
                    myrv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    myrv.setAdapter(myAdapter);

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

        container.setFocusableInTouchMode(true);
        container.requestFocus();
        container.setOnKeyListener(new ViewGroup.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();
                    return true;
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


