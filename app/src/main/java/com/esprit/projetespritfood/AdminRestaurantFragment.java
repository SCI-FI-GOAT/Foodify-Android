package com.esprit.projetespritfood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminRestaurantFragment extends Fragment {
    ListView listView;
    FloatingActionButton fb_add;
    TextView txt_restau_vide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_restaurant, container, false);

        listView = view.findViewById(R.id.listView_restaurant_admin);
        txt_restau_vide = view.findViewById(R.id.txt_admin_liste_restau_vide);
        txt_restau_vide.setVisibility(View.GONE);

        fb_add = view.findViewById(R.id.fb_add_restau);
        fb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminRestaurantAjoutFragment()).commit();
            }
        });

        //Récupération des menus
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "Restaurants.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Restaurant> liste = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        liste.add(new Restaurant("EspritFood " + o.getString("villeRestau"),
                                "Adresse : " + o.getString("adresseRestau"),
                                "" + o.getString("cordonneeX"),
                                "" + o.getString("cordonneeY"),
                                IP.images + o.getString("imageRestau")));
                        System.out.println(IP.images + o.getString("imageRestau"));
                    }
                    if (liste.size() == 0) {
                        txt_restau_vide.setVisibility(View.VISIBLE);
                    }

                    AdminRestaurantAdapter adapter = new AdminRestaurantAdapter(getActivity(), R.layout.item_liste_restaurant_admin, liste);
                    listView.setAdapter(adapter);

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
