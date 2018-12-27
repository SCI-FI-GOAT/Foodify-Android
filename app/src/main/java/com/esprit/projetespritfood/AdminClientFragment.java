package com.esprit.projetespritfood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminClientFragment extends Fragment {
    ListView listView;
    ArrayList<Utilisateur> users_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_client, container, false);
        listView = view.findViewById(R.id.listView_client_admin);

        //Récupération utilisateurs
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeUtilisateur.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                users_list = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        users_list.add(new Utilisateur(
                                "Nom : " + o.getString("nom"),
                                "Prenom : " + o.getString("prenom"),
                                "Email : " + o.getString("email"),
                                "Telephone : " + o.getString("tel"),
                                "Username : " + o.getString("username"),
                                "" + o.getString("password"),
                                "" + o.getString("role"),
                                "" + o.getString("imageUser")
                        ));
                    }

                    /*
                    ArrayList<HashMap<String,String>> liste = new ArrayList<>();
                    for (Uitilisateur ut : users_list){
                        HashMap<String,String> hmap = new HashMap<>();
                        hmap.put("nom","Nom : "+ut.getNom());
                        hmap.put("prenom","Prenom : "+ut.getPrenom());
                        hmap.put("email","Email : "+ut.getEmail());
                        hmap.put("tel","Telephone : "+ut.getTel());
                        hmap.put("user","Username : "+ut.getUsername());
                        liste.add(hmap);
                    }

                    ListAdapter adapter = new SimpleAdapter(getActivity(),liste, R.layout.item_liste_client_admin,
                            new String[]{"nom","prenom","email","tel","user"},
                            new int[]{R.id.item_client_nom,R.id.item_client_prenom,R.id.item_client_email,R.id.item_client_tel,R.id.item_client_username}
                            );
                    listView.setAdapter(adapter);
                    */

                    AdminClientAdapter adapter = new AdminClientAdapter(getActivity(), R.layout.item_liste_client_admin, users_list);
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
