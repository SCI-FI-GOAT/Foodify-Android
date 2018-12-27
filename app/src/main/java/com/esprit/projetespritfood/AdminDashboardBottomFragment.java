package com.esprit.projetespritfood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.esprit.projetespritfood.models.CommandeLigne;
import com.esprit.projetespritfood.models.Offre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminDashboardBottomFragment extends Fragment {

    //Declaration dash commande
    TextView txt_encours, txt_traitees, txt_annulees;

    //Declaration parie ligne commande
    ListView list_ligne;
    TextView txtpluscommande;
    TextView txt_commande_vide;
    TextView txt_ID, txt_Date, txt_User, txt_Etat;

    //Declaration parie offre
    ListView list_offre;
    TextView txtplusoffre, txt_offre_vide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard_bottom, container, false);

        /*
         *** Traitement de la Dashboard Commandes
         */

        txt_annulees = view.findViewById(R.id.txt_commandes_annulee);
        txt_traitees = view.findViewById(R.id.txt_commandes_traitees);
        txt_encours = view.findViewById(R.id.txt_commandes_encours);

        // Repmlissage de la dashboard des commandes
        //Récupération
        final StringRequest jsonObjReqC = new StringRequest(Request.Method.GET,
                IP.localhost + "CommandeDash.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<HashMap<String, String>> liste = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        txt_encours.setText(o.getString("EnCours"));
                        txt_traitees.setText(o.getString("Traitee"));
                        txt_annulees.setText(o.getString("Annulee"));
                    }
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
        RequestQueue requestQueueC = Volley.newRequestQueue(getActivity());
        requestQueueC.add(jsonObjReqC);

        // La liste des offre
        list_offre = view.findViewById(R.id.listeView_offre_dash);

        //textView voirplus offre
        txtplusoffre = view.findViewById(R.id.txt_voir_plus_offe);
        txtplusoffre.setVisibility(View.GONE);
        //TextView Aucun Offre enregistré
        txt_offre_vide = view.findViewById(R.id.txt_dashboard_bottom_aucun_offre_enregiste);
        txt_offre_vide.setVisibility(View.GONE);

        // Onclic Voir plus Commande
        txtplusoffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminOffreFragment()).commit();
            }
        });

        //Récupération des offres
        final StringRequest jsonObjReq1 = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeOffreAdmin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final ArrayList<Offre> liste = new ArrayList<>();
                    liste.clear();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < 2; i++) {
                        JSONObject o = array.getJSONObject(i);
                        /*
                        HashMap<String,String> hmap = new HashMap<>();
                        hmap.put("id","ID: "+o.getString("id_offre"));
                        hmap.put("date","Date : "+o.getString("date"));
                        hmap.put("description",o.getString("description"));
                        hmap.put("taux","taux"+o.getString("taux")+" % ");
                        hmap.put("libelle",o.getString("libelle_food"));
                        */
                        liste.add(new Offre("ID: " + o.getString("id_offre"),
                                "Date : " + o.getString("date"),
                                o.getString("description"),
                                " - " + o.getString("taux") + " % ",
                                o.getString("libelle_food") + "",
                                "" + IP.images + o.getString("image")
                        ));
                    }
                    if (liste.size() != 0) {
                        txtplusoffre.setVisibility(View.VISIBLE);
                        txt_offre_vide.setVisibility(View.GONE);
                    } else {
                        txt_offre_vide.setVisibility(View.VISIBLE);
                    }

                    AdminOffreDashAdapter adapter = new AdminOffreDashAdapter(getActivity(), R.layout.item_offre_admin_dash, liste);
                    list_offre.setAdapter(adapter);

/*
                    ListAdapter adapter = new SimpleAdapter(getActivity(),liste, R.layout.item_offre_admin_dash,
                            new String[]{"id","date","description","taux","libelle"},
                            new int[]{R.id.item_offre_id,R.id.item_offre_date,R.id.item_offre_description,R.id.item_offre_taux,R.id.item_offre_libelle});
                    list_offre.setAdapter(adapter);
                    */


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

        RequestQueue requestQueue1 = Volley.newRequestQueue(getActivity());
        requestQueue1.add(jsonObjReq1);


        // La liste des lignes des commandes
        list_ligne = view.findViewById(R.id.listeView_ligne_commande_dash);

        // textView Liste commande vide
        txt_ID = view.findViewById(R.id.txt_dashboard_bottom_commandeID);
        txt_User = view.findViewById(R.id.txt_dashboard_bottom_commandeUSER);
        txt_Date = view.findViewById(R.id.txt_dashboard_bottom_commandeDATE);
        txt_Etat = view.findViewById(R.id.txt_dashboard_bottom_commandeETAT);
        txt_ID.setVisibility(View.GONE);
        txt_Date.setVisibility(View.GONE);
        txt_User.setVisibility(View.GONE);
        txt_Etat.setVisibility(View.GONE);

        txt_commande_vide = view.findViewById(R.id.txt_dashboard_bottom_aucune_commande_enregistee);
        txt_commande_vide.setVisibility(View.GONE);


        //textView voirplus cammdane
        txtpluscommande = view.findViewById(R.id.txt_voir_plus_ligne_commade);
        txtpluscommande.setVisibility(View.GONE);


        // Onclic Voir plus Commande
        txtpluscommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminLigneCommandeFragment()).commit();
            }
        });

        // ArrayList des lignes de commandes
        final ArrayList<CommandeLigne> list = new ArrayList<>();

        //Récupération
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeCommandeAdmin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        if (list.size() <= 2) {
                            list.add(new CommandeLigne(
                                    o.getString("id_commandeP"),
                                    o.getString("user_commandeP"),
                                    o.getString("date_commandeP"),
                                    o.getString("etat_commandeP")
                            ));
                        }
                    }

                    if (list.size() != 0) {
                        txtpluscommande.setVisibility(View.VISIBLE);
                        txt_ID.setVisibility(View.VISIBLE);
                        txt_Date.setVisibility(View.VISIBLE);
                        txt_User.setVisibility(View.VISIBLE);
                        txt_Etat.setVisibility(View.VISIBLE);
                    }
                    if (list.size() == 0) {
                        txt_commande_vide.setVisibility(View.VISIBLE);

                    }

                    AdminLigneCommandeAdapter adapter = new AdminLigneCommandeAdapter(getActivity(), R.layout.item_ligne_commande_admin, list);
                    list_ligne.setAdapter(adapter);

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


        // Oclick sur la liste ligne commande
        list_ligne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdminLigneCommandeFragment.idLigneCommande = list.get(position).getIdLc();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminCommandeFragment()).commit();
            }
        });
        return view;
    }
}
