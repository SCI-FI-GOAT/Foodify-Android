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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminLigneCommandeFragment extends Fragment {

    ListView list_ligne;
    TextView txt_etat;
    TextView txt_liste_vide, txtID, txtUser, txtDate, txtETAT;
    public static String idLigneCommande = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_ligne_commande, container, false);

        txt_liste_vide = (TextView) view.findViewById(R.id.txt_admin_lgne_commande_vide);
        txtID = (TextView) view.findViewById(R.id.txt_ligne_commandeID);
        txtUser = (TextView) view.findViewById(R.id.txt_ligne_commandeUSER);
        txtDate = (TextView) view.findViewById(R.id.txt_ligne_commandeDATE);
        txtETAT = (TextView) view.findViewById(R.id.txt_ligne_commandeETAT);

        txt_liste_vide.setVisibility(View.GONE);
        txtID.setVisibility(View.GONE);
        txtUser.setVisibility(View.GONE);
        txtDate.setVisibility(View.GONE);
        txtETAT.setVisibility(View.GONE);

        // La liste des lignes des commandes
        list_ligne = view.findViewById(R.id.listeView_ligne_commande);

        // ArrayList des lignes de commandes
        final ArrayList<CommandeLigne> list = new ArrayList<>();

        // Récupération
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeCommandeAdmin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        list.add(new CommandeLigne(
                                o.getString("id_commandeP"),
                                o.getString("user_commandeP"),
                                o.getString("date_commandeP"),
                                o.getString("etat_commandeP")
                        ));
                    }

                    if (list.size() == 0) {
                        txt_liste_vide.setVisibility(View.VISIBLE);
                    }
                    if (list.size() != 0) {
                        txtID.setVisibility(View.VISIBLE);
                        txtUser.setVisibility(View.VISIBLE);
                        txtDate.setVisibility(View.VISIBLE);
                        txtETAT.setVisibility(View.VISIBLE);
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

        // On Click sur une ligne de la liste
        list_ligne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idLigneCommande = list.get(position).getIdLc();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminCommandeFragment()).commit();
            }
        });
        return view;
    }
}
