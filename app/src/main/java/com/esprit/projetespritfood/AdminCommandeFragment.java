package com.esprit.projetespritfood;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Commande;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.esprit.projetespritfood.AdminLigneCommandeFragment.idLigneCommande;

public class AdminCommandeFragment extends Fragment {
    ListView listView;
    ArrayList<Commande> commande_list;
    Button btn_valider, btn_annuler;
    boolean action = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_commande, container, false);

        listView = view.findViewById(R.id.listView_commande_admin);
        btn_annuler = view.findViewById(R.id.btn_annuler_commande);
        btn_valider = view.findViewById(R.id.btn_valider_commande);

        // Remplissage de la liste des commande
        // Récupération du webservice
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeSousCommandeAdmin.php?id_commandeP=" + idLigneCommande, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                commande_list = new ArrayList<>();
                commande_list.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        commande_list.add(new Commande(
                                "" + o.getString("id_commande"),
                                "" + o.getString("libelle_commande"),
                                "Quantité : " + o.getString("quantite"),
                                "Date : " + o.getString("date"),
                                "Adresse : " + o.getString("adresse"),
                                "",
                                "User : " + o.getString("username"),
                                "" + IP.images + o.getString("image")
                        ));
                    }

                    if (commande_list.size() == 0) {
                        Toast.makeText(getActivity(), "listeVide", Toast.LENGTH_LONG).show();
                    }

                    AdminCommandeAdapter adapter = new AdminCommandeAdapter(getActivity(), R.layout.item_commande_admin, commande_list);
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


        // On Click sur le boutton Valider
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Traitemt de la commande");
                alert.setMessage("Voulez vous confirmer la commande ?");
                alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "TraiterCommande.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity(), "Erreur ...", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("etat", "Traitee");
                                map.put("id_commande", idLigneCommande);
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(sendData);

                        btn_annuler.setVisibility(View.GONE);
                        btn_valider.setVisibility(View.GONE);

                    }
                });
                alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });


        // On Click sur le boutton Annuler
        btn_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Traitemt de la commande");
                alert.setMessage("Voulez vous Annuler la commande ?");
                alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "TraiterCommande.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity(), "Erreur ...", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("etat", "Annulee");
                                map.put("id_commande", idLigneCommande);
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(sendData);

                        btn_annuler.setVisibility(View.GONE);
                        btn_valider.setVisibility(View.GONE);
                    }
                });
                alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
            }
        });
        return view;
    }
}
