package com.esprit.projetespritfood;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Offre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminOffreFragment extends Fragment {

    ProgressDialog loading;

    ListView list_offre;
    static ArrayList<Offre> arrayliste_offre;
    FloatingActionButton ajout_offre;
    TextView txt_offreVide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_offre, container, false);
        //loading.dismiss();
        list_offre = view.findViewById(R.id.listView_offre_admin);
        ajout_offre = view.findViewById(R.id.fb_add_offre);
        txt_offreVide = view.findViewById(R.id.txt_admin_liste_offre_vide);
        txt_offreVide.setVisibility(View.GONE);

        // Récupération des menus
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeOffreAdmin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<HashMap<String, String>> liste = new ArrayList<>();

                    arrayliste_offre = new ArrayList<>();
                    arrayliste_offre.clear();

                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        arrayliste_offre.add(
                                new Offre(
                                        "ID Offe : " + o.getString("id_offre"),
                                        "Date : " + o.getString("date"),
                                        "Description : " + o.getString("description"),
                                        " - " + o.getString("taux") + " % ",
                                        o.getString("libelle_food"),
                                        IP.images + o.getString("image")));

                        /*
                        HashMap<String,String> hmap = new HashMap<>();
                        hmap.put("id","ID Offe : "+o.getString("id_offre"));
                        hmap.put("date","Date : "+o.getString("date"));
                        hmap.put("description","Description : "+o.getString("description"));
                        hmap.put("taux",""+o.getString("taux")+" % ");
                        hmap.put("libelle",""+o.getString("libelle_food"));
                        liste.add(hmap);
                        */
                    }
                    if (arrayliste_offre.size() == 0) {
                        txt_offreVide.setVisibility(View.VISIBLE);
                    }
                    AdminOffreAdapter adapter = new AdminOffreAdapter(getActivity(), R.layout.item_offre_admin, arrayliste_offre);
                    list_offre.setAdapter(adapter);
                    /*
                    ListAdapter adapter = new SimpleAdapter(getActivity(),liste, R.layout.item_offre_admin,
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjReq);


        // on Long Click sur la liste
        list_offre.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(arrayliste_offre.get(position).getId_food());
                builder.setMessage("Voulez vous supprimer l'offre ?");
                // Add the buttons
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        //Supprimer Offre
                        StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "SupprimerOffre.php",
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
                                map.put("id_offre", arrayliste_offre.get(position).getIdOffre());
                                map.put("taux", arrayliste_offre.get(position).getTaux());
                                map.put("libelle_food", arrayliste_offre.get(position).getId_food());
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(sendData);

                        loading = ProgressDialog.show(getActivity(), "Traitement en cours...", "Attendez SVP ...", false, false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminOffreFragment()).commit();
                                // Mise a jour des valeurs dans le dashboard TOP
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container_up, new AdminDashboardTopFragment()).commit();
                            }
                        }, 3000);


                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        // On click sur le boutton
        ajout_offre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminOffreAjoutFragment()).commit();
            }
        });
        return view;
    }
}