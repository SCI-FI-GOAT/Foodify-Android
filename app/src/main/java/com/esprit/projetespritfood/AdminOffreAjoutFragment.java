package com.esprit.projetespritfood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminOffreAjoutFragment extends Fragment {

    Spinner sp_cat, sp_food;
    TextView txt_cat, txt_food;
    EditText edt_desc, edt_taux;
    Button btn_ajout;

    ArrayList<String> liste_categorie;
    ArrayList<String> liste_food;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_offre_ajout, container, false);

        txt_cat = view.findViewById(R.id.txt_categorie_food_offre_ajout);
        txt_food = view.findViewById(R.id.txt_food_offe_ajout);
        edt_desc = view.findViewById(R.id.edt_ajout_offre_description);
        edt_taux = view.findViewById(R.id.edt_ajout_offre_taux_reduction);
        btn_ajout = view.findViewById(R.id.btn_ajouter_offre);

        // declaration et remplissage spinner
        sp_cat = view.findViewById(R.id.spinner_categirifood_offre);
        sp_food = view.findViewById(R.id.spinner_food_offre);

        // Categorie food
        liste_categorie = new ArrayList<>();
        liste_categorie.add("Pizza");
        liste_categorie.add("Plat");
        liste_categorie.add("Sandwich");
        liste_categorie.add("Boisson");
        liste_categorie.add("Portion");

        final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, liste_categorie);
        sp_cat.setAdapter(adapter1);

        // On Click sur le sinner des CategorieFood
        sp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Récupération de la liste des varieté (Food)
                final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                        IP.localhost + "ListeFood.php?libelleFood=" + liste_categorie.get(position), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        liste_food = new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                liste_food.add(o.getString("libelleFood"));
                            }
                            ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, liste_food);
                            sp_food.setAdapter(adapter2);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(edt_taux.getText().toString().equals(""))) {
                    if ((Integer.parseInt(edt_taux.getText().toString()) < 1) || (Integer.parseInt(edt_taux.getText().toString()) > 99)) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("Erreur");
                        alert.setMessage("Donnez un taux valide (entre 1% et 99%)");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                        return;
                    }
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Erreur");
                    alert.setMessage("Remplissez le champs TAUX");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }


                if (edt_desc.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Erreur");
                    alert.setMessage("Donnez une description");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;

                }

                // Ajout Offre
                StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "AjouterOffre.php",
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
                        map.put("description", edt_desc.getText().toString());
                        map.put("taux", edt_taux.getText().toString());
                        map.put("libelle_food", sp_food.getSelectedItem().toString());
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(sendData);
                //edt_desc.setText("");
                //edt_taux.setText("");

                // Mise a jour des valeurs dans le dashboard TOP
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_up, new AdminDashboardTopFragment()).commit();
            }
        });
        return view;
    }
}


