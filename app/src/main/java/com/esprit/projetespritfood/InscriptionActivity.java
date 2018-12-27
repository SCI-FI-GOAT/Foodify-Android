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
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {
    TextView txt_login;
    EditText edt_nom, edt_prenom, edt_email, edt_mdp, edt_user, edt_tel;
    Button bt_inscription;

    String nom, prenom, mail, tel, user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        setTitle("Inscription");


        txt_login = (TextView) findViewById(R.id.txt_inscrit_deja);
        edt_nom = (EditText) findViewById(R.id.edt_insc_nom);
        edt_prenom = (EditText) findViewById(R.id.edt_insc_prenom);
        edt_email = (EditText) findViewById(R.id.edt_insc_email);
        edt_mdp = (EditText) findViewById(R.id.edt_insc_mdp);
        edt_tel = (EditText) findViewById(R.id.edt_insc_tel);
        edt_user = (EditText) findViewById(R.id.edt_insc_user);
        bt_inscription = (Button) findViewById(R.id.btn_sinsrire);

        // Si l'utilisateur à deja fait le login on passe directement à l'interface Home
        // on verifie s'il y a un contenu dans la sharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if (prefs.getString("login", "") != "" && prefs.getString("mdp", "") != "") {

            if (prefs.getString("role", "").equals("0")) {
                Intent intent = new Intent(InscriptionActivity.this, HomeActivity.class);
                startActivity(intent);
            } else if (prefs.getString("role", "").equals("1")) {
                Intent intent = new Intent(InscriptionActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }

        }

        // On click sur Textview Login pour passer à l'interface Login
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InscriptionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bt_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    Log.d("State connection ", "CONNECTED");
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InscriptionActivity.this);
                    alert.setTitle("Connexion requise");
                    alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }


                nom = edt_nom.getText().toString();
                prenom = edt_prenom.getText().toString();
                tel = edt_tel.getText().toString();
                mail = edt_email.getText().toString();
                user = edt_user.getText().toString();
                pass = edt_mdp.getText().toString();


                //Vérification des champs
                if ((nom.equals("")) || (prenom.equals("")) || (tel.equals("")) || (mail.equals("")) || (user.equals("")) || (pass.equals(""))) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InscriptionActivity.this);
                    alert.setTitle("Erreur");
                    alert.setMessage("Veuillez remplir tout les champs");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                } else {
                    //Si tout va bien --> INSCRIPTION
                    StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "InscriptionClient.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(InscriptionActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(InscriptionActivity.this, "Erreur ...", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("nom", nom);
                            map.put("prenom", prenom);
                            map.put("email", mail);
                            map.put("tel", tel);
                            map.put("username", user);
                            map.put("password", pass);

                            return map;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(InscriptionActivity.this);
                    requestQueue.add(sendData);
                }


            }
        });
    }
}
