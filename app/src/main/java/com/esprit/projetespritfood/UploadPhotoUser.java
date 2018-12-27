package com.esprit.projetespritfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class UploadPhotoUser extends AppCompatActivity {

    private Bitmap bitmap;
    private Uri filePath;
    CircleImageView profile_image;
    TextView txt_user_name, txt_user_username, txt_user_email, txt_user_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo_user);
        setTitle("Mon profil");


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Log.d("State connection ", "CONNECTED");
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(UploadPhotoUser.this);
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }


        profile_image = findViewById(R.id.profile_image);
        txt_user_name = findViewById(R.id.txt_user_nom);
        txt_user_username = findViewById(R.id.txt_user_username);
        txt_user_email = findViewById(R.id.txt_user_email);
        txt_user_tel = findViewById(R.id.txt_user_tel);

        txt_user_name.setText(HomeActivity.nom);
        txt_user_username.setText(HomeActivity.user);
        txt_user_email.setText(HomeActivity.mail);
        txt_user_tel.setText(HomeActivity.tel);


        // SET IMAGE FROM HomeActivity (Intent or SearedPreferences)
        if (HomeActivity.imgUser != null || HomeActivity.imgUser != "") {
            Picasso.with(getApplicationContext()).load(IP.images + HomeActivity.imgUser).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(profile_image);
        }

        // ON LONG CLICK ON IMAGE
        profile_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //Alert Dialog Changer PHOTO
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhotoUser.this);
                builder.setTitle("Photo de Profil");
                builder.setMessage("Voulez vous changer votre photo de profil ?");
                // Add the buttons
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        //OPEN GALLERIE
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);

                //ALERT DIALOG POUR ENREGISTRER LA NOUVELLE IMAGE
                AlertDialog.Builder alert = new AlertDialog.Builder(UploadPhotoUser.this);
                alert.setTitle("Photo de Profil");
                alert.setMessage("Voulez vous enregistrer ?");
                alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Appel au WebService Modifier Photo
                        StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "modifierPhotoUser.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(UploadPhotoUser.this, response, Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(UploadPhotoUser.this, "Erreur ...", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("username", HomeActivity.user);
                                map.put("image", getStringImage(bitmap));
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(UploadPhotoUser.this);
                        requestQueue.add(sendData);
                    }
                });
                alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Retouner L'image precedente
                        Picasso.with(getApplicationContext()).load(IP.images + HomeActivity.imgUser).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(profile_image);
                    }
                });
                alert.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
