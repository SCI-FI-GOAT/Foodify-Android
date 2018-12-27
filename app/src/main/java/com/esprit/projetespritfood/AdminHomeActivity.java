package com.esprit.projetespritfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.projetespritfood.config.IP;
import com.squareup.picasso.Picasso;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    SharedPreferences prefs;
    public static String admin;


    // a static variable to get a reference of our application context
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        contextOfApplication = getApplicationContext();

        setTitle("ESPRIT Food DashBoard");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.fragment_container_up, new AdminDashboardTopFragment()).commit();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminDashboardBottomFragment()).commit();
        }

        // listen to click events on navigation view
        navigationView = (NavigationView) findViewById(R.id.navig_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

        //Récupérer les informations des sharedpref
        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_navig_admin);
        TextView navUsermail = (TextView) headerView.findViewById(R.id.mail_navig_admin);
        ImageView navUserImage = (ImageView) headerView.findViewById(R.id.img_avatar_admin);

        //Modification du navigation header
        //si Rester connecté isChecked
        if ((prefs.getString("nom", "") != "") && (prefs.getString("email", "") != "")) {
            navUsername.setText(prefs.getString("nom", ""));
            navUsermail.setText(prefs.getString("email", ""));
            Picasso.with(AdminHomeActivity.this).load(IP.images + prefs.getString("image", "")).fit().into(navUserImage);

            admin = prefs.getString("login", "");
        } else {
            navUsername.setText(getIntent().getStringExtra("nom"));
            navUsermail.setText(getIntent().getStringExtra("email"));
            Picasso.with(AdminHomeActivity.this).load(IP.images + getIntent().getStringExtra("image")).fit().into(navUserImage);

            admin = getIntent().getStringExtra("user");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navig_home_admin:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminDashboardBottomFragment()).commit();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_up, new AdminDashboardTopFragment()).commit();
                break;

            case R.id.navig_commandes_admin:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminLigneCommandeFragment()).commit();
                break;

            case R.id.navig_offre_admin:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminOffreFragment()).commit();
                break;

            case R.id.navig_menu_admin:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminAjoutMenuFragment()).commit();
                break;

            case R.id.navig_restau_admin:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminRestaurantFragment()).commit();
                break;

            case R.id.navig_utilisateur_admin:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new AdminClientFragment()).commit();
                break;

            case R.id.navig_deconexion_admin:
                Intent intent1 = new Intent(AdminHomeActivity.this, LoginActivity.class);
                prefs.edit().clear().apply();
                startActivity(intent1);
                break;

            case R.id.navig_changermdp_admin:
                Intent intent2 = new Intent(AdminHomeActivity.this, ChangerMDP.class);
                intent2.putExtra("user", admin);
                startActivity(intent2);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
