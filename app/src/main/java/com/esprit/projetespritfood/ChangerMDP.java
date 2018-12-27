package com.esprit.projetespritfood;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangerMDP extends AppCompatActivity {


    EditText edt_nv_mdp, edt_anc_mdp;
    Button bt_changer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changermdp);

        setTitle("Changer mot de passe");


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Log.d("State connection ", "CONNECTED");
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ChangerMDP.this);
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }


        edt_anc_mdp = (EditText) findViewById(R.id.edt_mdp_anc);
        edt_nv_mdp = (EditText) findViewById(R.id.edt_mdp_nv);
        bt_changer = (Button) findViewById(R.id.btn_chng_mdp);

        bt_changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Vérification des champs
                if ((edt_anc_mdp.getText().toString().equals("")) || (edt_nv_mdp.getText().toString().equals(""))) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ChangerMDP.this);
                    alert.setTitle("Erreur");
                    alert.setMessage("Veuillez remplir tout les champs");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                } else {

                    if ((edt_nv_mdp.getText().toString().contains(" "))) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ChangerMDP.this);
                        alert.setTitle("Erreur");
                        alert.setMessage("Le nouveau mot de passe ne doit pas contenir des espaces");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                        return;
                    }


                    //Si tout va bien --> CHANGER
                    StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "ChngMdp.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(ChangerMDP.this, response, Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(ChangerMDP.this, "Erreur ...", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("username", getIntent().getStringExtra("user"));
                            map.put("nouveauMotDePasse", edt_nv_mdp.getText().toString());
                            map.put("ancienMotDePasse", edt_anc_mdp.getText().toString());

                            return map;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(ChangerMDP.this);
                    requestQueue.add(sendData);
                }


            }

        });
    }
}
