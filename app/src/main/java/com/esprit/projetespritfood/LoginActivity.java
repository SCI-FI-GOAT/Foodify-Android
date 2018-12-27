package com.esprit.projetespritfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    TextView txt_mdp_oublie;
    CheckBox chbx_resterco;
    EditText edt_login, edt_mdp;
    Button btn_login;

    String login, mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Authentification");


        edt_login = (EditText) findViewById(R.id.edt_login);
        edt_mdp = (EditText) findViewById(R.id.edt_mdp);
        chbx_resterco = (CheckBox) findViewById(R.id.chbx_rester_co);
        txt_mdp_oublie = (TextView) findViewById(R.id.txt_mdp_oublie);
        btn_login = (Button) findViewById(R.id.btn_login);

        txt_mdp_oublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });

        // On click sur le boutton se connecter
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    Log.d("State connection ", "CONNECTED");
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Connexion requise");
                    alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }


                // Recuperer les valeurs de login et mot de passe
                login = edt_login.getText().toString();
                mdp = edt_mdp.getText().toString();


                //Remplissage des champs
                if (login.equals("") || (mdp.equals(""))) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Erreur");
                    alert.setMessage("Veuillez remplir tout les champs");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }

                //Récupération du webservice
                final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                        IP.localhost + "LoginClient.php?username=" + login + "&password=" + mdp, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject o = array.getJSONObject(i);
                                //Vérification des coordonnées
                                if (login.equals(o.getString("userName")) && mdp.equals(o.getString("passWord"))) {

                                    //Si tout va bien ....
                                    //Verifier si la case rester-connecter est activée
                                    if (chbx_resterco.isChecked()) {
                                        // Declarer un SaredPreferen ces pour le user
                                        SharedPreferences pref = getSharedPreferences("user_prefs", MODE_PRIVATE);

                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("login", login);
                                        editor.putString("mdp", mdp);
                                        editor.putString("nom", o.getString("Nom") + " " + o.getString("Prenom"));
                                        editor.putString("email", o.getString("Email"));
                                        editor.putString("role", o.getString("Role"));
                                        editor.putString("image", o.getString("Image"));
                                        editor.putString("tel", o.getString("Tel"));
                                        editor.commit();
                                    }

                                    // Passer à l'interface home

                                    switch (o.getString("Role")) {
                                        case "0":
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            intent.putExtra("user", o.getString("userName"));
                                            intent.putExtra("mdp", o.getString("passWord"));
                                            intent.putExtra("nom", o.getString("Nom") + " " + o.getString("Prenom"));
                                            intent.putExtra("email", o.getString("Email"));
                                            intent.putExtra("image", o.getString("Image"));
                                            intent.putExtra("tel", o.getString("Tel"));
                                            startActivity(intent);
                                            break;
                                        case "1":
                                            Intent intent2 = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                            intent2.putExtra("user", o.getString("userName"));
                                            intent2.putExtra("nom", o.getString("Nom") + " " + o.getString("Prenom"));
                                            intent2.putExtra("email", o.getString("Email"));
                                            intent2.putExtra("image", o.getString("Image"));

                                            startActivity(intent2);
                                            break;
                                    }
                                } else {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                                    alert.setTitle("Erreur");
                                    alert.setMessage("Vérifier vos données SVP.");
                                    alert.setPositiveButton("OK", null);
                                    alert.show();
                                    return;
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Erreur lors de la récupération ... ", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(jsonObjReq);

            }
        });

    }
}
