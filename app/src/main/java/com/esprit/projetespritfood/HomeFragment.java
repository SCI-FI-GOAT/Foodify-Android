package com.esprit.projetespritfood;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;
import com.darwindeveloper.horizontalscrollmenulibrary.extras.MenuItem;
import com.esprit.projetespritfood.config.IP;
import com.esprit.projetespritfood.models.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    HorizontalScrollMenuView mesCommandes;

    private RecyclerView top5, recommended, promos;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public static String selected = "";
    public static int index = 0;

    ArrayList<Food> top, myTop, promo;

    TextView txt_commande, txt_top5, txt_promos, txt_recommandation;

    ProgressDialog loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Bienvenue à ESPRIT Food");


        if (!isNetworkConnected()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }


        loading = ProgressDialog.show(getActivity(), "Bienvenue à ESPRIT Food", "Traitement en cours ...", false, false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.dismiss();
            }
        }, 1500);


        txt_commande = root.findViewById(R.id.txt_user_commandes);
        txt_top5 = root.findViewById(R.id.txt_user_top5);
        txt_promos = root.findViewById(R.id.txt_user_promos);
        txt_recommandation = root.findViewById(R.id.txt_user_recommandation);

        txt_commande.setVisibility(View.GONE);
        txt_top5.setVisibility(View.GONE);
        txt_promos.setVisibility(View.GONE);
        txt_recommandation.setVisibility(View.GONE);

        ///////////////////Commandes
        mesCommandes = (HorizontalScrollMenuView) root.findViewById(R.id.liste_commandes);

        //MES COMMANDES
        //Récupération du webservice
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeCommandeUser.php?username=" + HomeActivity.user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);

                        String txt = "Commande N° " + o.getString("id_commandeP") + "\n" + o.getString("date_commandeP") + "\n" + o.getString("etat_commandeP");

                        String etat = txt.substring(txt.lastIndexOf("\n") + 1);

                        int img = 0;
                        switch (etat) {
                            case "En cours":
                                img = R.drawable.attente;
                                break;
                            case "Traitee":
                                img = R.drawable.ok;
                                break;
                            case "Annulee":
                                img = R.drawable.refus;
                                break;
                            default:
                                img = 0;
                        }


                        mesCommandes.addItem(txt, img);
                        mesCommandes.editItem(i, txt, img, false, 0);

                        txt_commande.setVisibility(View.VISIBLE);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erreur lors de la récupération ... ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjReq);


        mesCommandes.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(MenuItem menuItem, int position) {
                String id = mesCommandes.getItem(position).getText();
                id = id.substring(12, id.indexOf("\n"));


                CommandeFragmentUser cfu = new CommandeFragmentUser();
                Bundle bundle = new Bundle();
                bundle.putString("num", id);

                cfu.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, cfu).commit();
            }
        });


///////////////////TOP5

        top5 = (RecyclerView) root.findViewById(R.id.lst_top5);
        top = new ArrayList<>();

        //Récupération du webservice
        final StringRequest jsonObjReqTop5 = new StringRequest(Request.Method.GET,
                IP.localhost + "TOP5.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);

//                        String txt=o.getString("libelleTOP")+"\n"+o.getString("RatioTOP")+" %";
                        top.add(new Food(o.getString("libelleTOP") + "\n" + o.getString("RatioTOP") + " %", IP.images + o.getString("imageTOP")));


                        txt_top5.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                top5.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                top5.setLayoutManager(layoutManager);
                recyclerAdapter = new RecyclerAdapter(top, getActivity());
                top5.setAdapter(recyclerAdapter);

                if (selected != "") {
                    RecyclerAdapter.libelle = "";
                    getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new FoodDetailFragment()).commit();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erreur lors de la récupération ... ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueueTop5 = Volley.newRequestQueue(getActivity());
        requestQueueTop5.add(jsonObjReqTop5);


        ///////////////////Recommanded
        recommended = (RecyclerView) root.findViewById(R.id.lst_recommended);

        myTop = new ArrayList<>();


        //Récupération du webservice
        final StringRequest jsonObjReqRecommanded = new StringRequest(Request.Method.GET,
                IP.localhost + "Recommanded.php?username=" + HomeActivity.user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);

//                        String txt=o.getString("libelleTOP");

                        myTop.add(new Food(o.getString("libelleTOP") + "\n", IP.images + o.getString("imageTOP")));

                        txt_recommandation.setVisibility(View.VISIBLE);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recommended.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recommended.setLayoutManager(layoutManager);
                recyclerAdapter = new RecyclerAdapter(myTop, getActivity());
                recommended.setAdapter(recyclerAdapter);

                if (selected != "") {
                    RecyclerAdapter.libelle = "";
                    getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new FoodDetailFragment()).commit();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erreur lors de la récupération ... ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueueRecommanded = Volley.newRequestQueue(getActivity());
        requestQueueRecommanded.add(jsonObjReqRecommanded);


        /////////////PROMOS
        promos = (RecyclerView) root.findViewById(R.id.lst_offre);

        promo = new ArrayList<>();

        //Récupération du webservice
        final StringRequest jsonObjReqOffres = new StringRequest(Request.Method.GET,
                IP.localhost + "ListeOffreClient.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);

//                        String txt=o.getString("libelleTOP")+"\n"+o.getString("RatioTOP")+" %";
                        promo.add(new Food(o.getString("libelle_food") + "\n" + "- " + o.getString("taux") + " %", IP.images + o.getString("image")));

                        txt_promos.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                promos.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                promos.setLayoutManager(layoutManager);
                recyclerAdapter = new RecyclerAdapter(promo, getActivity());
                promos.setAdapter(recyclerAdapter);

                if (selected != "") {
                    RecyclerAdapter.libelle = "";
                    getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new FoodDetailFragment()).commit();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erreur lors de la récupération ... ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueueOffres = Volley.newRequestQueue(getActivity());
        requestQueueOffres.add(jsonObjReqOffres);

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
