package com.esprit.projetespritfood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminDashboardTopFragment extends Fragment {

    CardView card_commade, card_offre, card_restau, card_users;
    TextView txt_v_users, txt_v_commandes, txt_v_offres, txt_v_restau;
    String v_users, v_commandes, v_offres, v_restau;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard_top, container, false);

        txt_v_users = view.findViewById(R.id.v_users);
        txt_v_commandes = view.findViewById(R.id.v_commandes);
        txt_v_offres = view.findViewById(R.id.v_offres);
        txt_v_restau = view.findViewById(R.id.v_restaurants);

        card_commade = view.findViewById(R.id.card_view1);
        card_offre = view.findViewById(R.id.card_view2);
        card_restau = view.findViewById(R.id.card_view3);
        card_users = view.findViewById(R.id.card_view4);

        card_commade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminLigneCommandeFragment()).commit();
            }
        });

        card_offre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminOffreFragment()).commit();
            }
        });

        card_restau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminRestaurantFragment()).commit();
            }
        });

        card_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminClientFragment()).commit();
            }
        });


        // Récupération des menus
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "DashTopAdmin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<HashMap<String, String>> liste = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        v_users = o.getString("users");
                        v_commandes = o.getString("commandes");
                        v_offres = o.getString("offres");
                        v_restau = o.getString("restaurants");

                    }

                    txt_v_commandes.setText(v_commandes);
                    txt_v_users.setText(v_users);
                    txt_v_offres.setText(v_offres);
                    txt_v_restau.setText(v_restau);

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
        return view;
    }
}
