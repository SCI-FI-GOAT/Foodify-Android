package com.esprit.projetespritfood;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.MenuCarte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuFragment extends Fragment {

    ListView listview;
    public static String selectedtlib;
    ArrayList<MenuCarte> menu_liste;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        getActivity().setTitle("Menu");


        if (!isNetworkConnected()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }


        listview = (ListView) root.findViewById(R.id.listView_menu);


        //Récupération des menus
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "LibelleCatFood.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                menu_liste = new ArrayList<>();
                try {

                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        //menu_liste.add(new MenuCarte(o.getString("libelleCategorie"),R.drawable.menu_pizza));
                        menu_liste.add(new MenuCarte(o.getString("libelleCategorie"), IP.images + o.getString("imageCategorie")));
                    }

                    ArrayList<HashMap<String, String>> liste = new ArrayList<>();
                    for (MenuCarte mc : menu_liste) {
                        HashMap<String, String> hmap = new HashMap<>();
                        hmap.put("libelle", mc.libelle);
                        //hmap.put("img", Integer.toString(mc.img));
                        hmap.put("img", mc.img2);
                        liste.add(hmap);
                    }

                    //ListAdapter adapter = new SimpleAdapter(getActivity(),liste,R.layout.item_liste_menu,new String[]{"libelle","img"},new int[]{R.id.item_menu_libelle,R.id.item_menu_img});
                    // ListAdapter adapter = new SimpleAdapter(getActivity(),liste,R.layout.item_liste_menu,new String[]{"libelle","img"},new int[]{R.id.item_menu_libelle,R.id.item_menu_img});
                    // listview.setAdapter(adapter);
                    MenuAdapter adapter = null;
                    adapter = new MenuAdapter(getActivity(), R.layout.item_liste_menu, menu_liste);
                    listview.setAdapter(adapter);


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

        //Récupération de l'item sélectionné

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedtlib = menu_liste.get(position).libelle;
                getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new MenuCarteFragment()).commit();
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

