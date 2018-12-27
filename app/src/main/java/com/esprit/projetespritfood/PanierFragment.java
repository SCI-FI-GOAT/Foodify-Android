package com.esprit.projetespritfood;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.esprit.projetespritfood.config.DatabaseHelper;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanierFragment extends Fragment {

    TextView prix_total, nb;
    Button passer_commande;
    ListView listview;
    EditText adresse;
    DatabaseHelper myDB;

    List<Food> foodList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_panier, container, false);

        getActivity().setTitle("Mon panier");


        if (!isNetworkConnected()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }


        foodList = new ArrayList<Food>();

        foodList.clear();
        myDB = new DatabaseHelper(getActivity());
        listview = (ListView) root.findViewById(R.id.listView_panier);


        Cursor res = myDB.getAllData();

        if (res.getCount() == 0)
            Toast.makeText(getActivity(), "Aucun Produit", Toast.LENGTH_LONG).show();
        else {
            while (res.moveToNext()) {


                String img = res.getString(3);
                img = img.substring(img.lastIndexOf("/"));
                img = IP.images + img;


                foodList.add(new Food(Integer.parseInt(res.getString(0)), res.getString(1), img, Integer.parseInt(res.getString(2)), Float.parseFloat(res.getString(4))));

            }
        }
//        ArrayList<HashMap<String,String>> liste = new ArrayList<>();
//        for (MenuCarte mc : FoodDetailFragment.panier_liste){
//            HashMap<String,String> hmap = new HashMap<>();
//            hmap.put("libelle",mc.libelle);
//            hmap.put("img", Integer.toString(mc.img));
//            hmap.put("quantite",Integer.toString(mc.quantite));
//            hmap.put("prix",Float.toString(mc.prix)+" DT");
//            liste.add(hmap);
//        }
//


        PanierAdapter adapter = null;
        adapter = new PanierAdapter(getActivity(), R.layout.item_liste_panier, foodList);
        listview.setAdapter(adapter);

//        ListAdapter adapter = new SimpleAdapter(getActivity(),liste,R.layout.item_liste_panier,new String[]{"libelle","img","quantite","prix"},new int[]{R.id.item_panier_libelle,R.id.item_panier_img,R.id.item_panier_quantite,R.id.item_panier_prix});
//        listview.setAdapter(adapter);

        //Calcul de prix total
        prix_total = (TextView) root.findViewById(R.id.txt_total);
        prix_total.setText("Prix total = " + prixTotal() + " DT");

        adresse = (EditText) root.findViewById(R.id.txt_adresse);
        nb = (TextView) root.findViewById(R.id.txt_nb);
        passer_commande = (Button) root.findViewById(R.id.btn_panier_commande);

        //Visibilité des buttons
        if (prixTotal() == 0.0) {
            passer_commande.setVisibility(View.GONE);
            prix_total.setVisibility(View.GONE);
            adresse.setVisibility(View.GONE);
            nb.setText("Panier vide");
        } else {
            passer_commande.setVisibility(View.VISIBLE);
            prix_total.setVisibility(View.VISIBLE);
            adresse.setVisibility(View.VISIBLE);
            nb.setText("NB : Long appuie pour retirer un article ou décrémenter la quantité");
        }


        //Gestion panier
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (foodList.get(position).getQuantite() > 1) {
//                    FoodDetailFragment.panier_liste.get(position).quantite--;
                    boolean isUpdate = myDB.updateData(String.valueOf(foodList.get(position).getId()), foodList.get(position).getQuantite() - 1);
                    if (isUpdate == true) {
                        foodList.get(position).setQuantite(foodList.get(position).getQuantite() - 1);
                        Toast.makeText(getActivity(), "Quantité décrémentée", Toast.LENGTH_LONG).show();

                    }
                } else {
//                    FoodDetailFragment.panier_liste.remove(position);
                    Integer deletedRows = myDB.deleteData(String.valueOf(foodList.get(position).getId()));
                    if (deletedRows > 0) {
                        foodList.remove(position);
                        Toast.makeText(getActivity(), "Article retiré de votre panier", Toast.LENGTH_LONG).show();

                    }
                }

//
                foodList.clear();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new PanierFragment()).commit();
                return true;
            }
        });


        //Passer la commande
        passer_commande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adresse.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Erreur");
                    alert.setMessage("Remplissez le champs \"Adresse\".");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }

                //Si tout va bien --> ENVOYER
                StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "EnvoyerCommande.php",
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
                        for (int i = 0; i < foodList.size(); i++) {
                            map.put("libelleCommande" + i, foodList.get(i).libelle);
                            map.put("quantite" + i, String.valueOf(foodList.get(i).quantite));
                            map.put("adresse" + i, adresse.getText().toString());
                            map.put("username" + i, HomeActivity.user);
                            map.put("nb" + i, String.valueOf(foodList.size()));
                            map.put("etat", "en cours");
                        }
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(sendData);


                myDB.deleteAll();


            }
        });

        return root;
    }


    public float prixTotal() {
        float somme = 0;
        for (int i = 0; i < foodList.size(); i++) {
            somme += (foodList.get(i).prix * foodList.get(i).quantite);
        }
        return somme;
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
