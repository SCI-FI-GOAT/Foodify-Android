package com.esprit.projetespritfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    SharedPreferences prefs;

    public static String user;
    public static String password;

    public static String imgUser;
    public static String nom;
    public static String mail;
    public static String tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Bienvenue à ESPRIT Food");


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Log.d("State connection ", "CONNECTED");
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }

        // Utiliser la Toolbar comme une ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        user = getIntent().getStringExtra("user");

        //creer le menu button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        //listen to click events on navigation view
        navigationView = (NavigationView) findViewById(R.id.navig_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Récupérer les informations des sharedpref
        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_navig);
        TextView navUsermail = (TextView) headerView.findViewById(R.id.mail_navig);

        //Modification du navigation header
        //si Rester connecté isChecked
        if ((prefs.getString("nom", "") != "") && (prefs.getString("email", "") != "")) {
            navUsername.setText(prefs.getString("nom", ""));
            navUsermail.setText(prefs.getString("email", ""));

            user = prefs.getString("login", "");
            password = prefs.getString("mdp", "");
            nom = prefs.getString("nom", "");
            mail = prefs.getString("email", "");
            tel = prefs.getString("tel", "");
            imgUser = prefs.getString("image", "");
        } else {
            navUsername.setText(getIntent().getStringExtra("nom"));
            navUsermail.setText(getIntent().getStringExtra("email"));

            user = getIntent().getStringExtra("user");
            password = getIntent().getStringExtra("mdp");
            nom = getIntent().getStringExtra("nom");
            mail = getIntent().getStringExtra("email");
            tel = getIntent().getStringExtra("tel");
            imgUser = getIntent().getStringExtra("image");
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navig_home:
                getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.navig_map:
                getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new MapFragment()).commit();
                break;

            case R.id.navig_menu:
                getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new MenuFragment()).commit();
                break;

            case R.id.navig_panier:
                getFragmentManager().beginTransaction().addToBackStack("fragment").replace(R.id.fragment_container, new PanierFragment()).commit();
                break;


        }
        drawer.closeDrawer(GravityCompat.START);

        //Déconnexion
        if (item.getItemId() == R.id.navig_deconexion) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            prefs.edit().clear().apply();
            startActivity(intent);
        }

        //Déconnexion
        if (item.getItemId() == R.id.navig_changermdp) {
            Intent intent = new Intent(HomeActivity.this, ChangerMDP.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }


        //Upload
        if (item.getItemId() == R.id.navig_upload) {
            Intent intent = new Intent(HomeActivity.this, UploadPhotoUser.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {/* Bloquer le retour a la page Login*/ }

    }
}
