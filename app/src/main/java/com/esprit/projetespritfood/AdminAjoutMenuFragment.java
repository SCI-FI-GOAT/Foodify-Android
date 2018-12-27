package com.esprit.projetespritfood;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AdminAjoutMenuFragment extends Fragment {
    ArrayList<String> liste_categorie;
    Spinner sp_cat;
    EditText edt_libelle, edt_prix, edt_descrip;
    Button btn_valider, btn_upload;

    Context applicationContext = AdminHomeActivity.getContextOfApplication();
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_menu_ajout, container, false);

        sp_cat = view.findViewById(R.id.spinner_categirifood_menu);
        edt_libelle = view.findViewById(R.id.edt_ajout_menu_libelle);
        edt_prix = view.findViewById(R.id.edt_edt_ajout_menu_prix);
        edt_descrip = view.findViewById(R.id.edt_ajout_menu_description);
        btn_valider = view.findViewById(R.id.btn_ajouter_menu);
        btn_upload = view.findViewById(R.id.btn_upload_image_food);
        imageView = view.findViewById(R.id.img_ajout_food);

        btn_valider.setVisibility(View.GONE);

        // Category food Spinner
        liste_categorie = new ArrayList<>();
        liste_categorie.add("Pizza");
        liste_categorie.add("Plat");
        liste_categorie.add("Sandwich");
        liste_categorie.add("Boisson");
        liste_categorie.add("Portion");
        SpinnerAdapter adapter1 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, liste_categorie);
        sp_cat.setAdapter(adapter1);

        // on click open gallery
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Control

                if (edt_libelle.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Erreur");
                    alert.setMessage("Donnez un libellé");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }

                if (edt_descrip.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Erreur");
                    alert.setMessage("Donnez une description");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }

                if (isDouble(edt_prix.getText().toString())) {
                    if ((Double.parseDouble(edt_prix.getText().toString()) <= 0)) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("Erreur");
                        alert.setMessage("Donnez un prix valide");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                        return;
                    }
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Erreur");
                    alert.setMessage("Donnez un prix");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }

                // Add Food
                StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "AjouterFood.php",
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
                        map.put("libelle_food", edt_libelle.getText().toString());
                        map.put("prix_food", edt_prix.getText().toString());
                        map.put("description", edt_descrip.getText().toString());
                        map.put("image", getStringImage(bitmap));
                        map.put("id_categorie", String.valueOf(sp_cat.getSelectedItemPosition() + 1));
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(sendData);

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                //InputStream inputStream = applicationContext.getContentResolver().openInputStream(filePath);
                bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                btn_valider.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
