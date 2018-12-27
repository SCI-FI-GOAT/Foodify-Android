package com.esprit.projetespritfood;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.projetespritfood.config.IP;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class AdminRestaurantAjoutFragment extends Fragment {
    MapView mMapView;
    MapController mMapController;
    LocationManager locationManager;
    Marker position;

    String longitude = null;
    String latitude = null;
    String libelle_resto = null;
    String adresse_restau = null;

    Button btn_ajout, btn_upload;
    EditText edt_adresse;

    Context applicationContext = AdminHomeActivity.getContextOfApplication();
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_restaurant_ajout, container, false);

        btn_ajout = (Button) view.findViewById(R.id.btn_ajouter_restau_map);
        edt_adresse = (EditText) view.findViewById(R.id.edt_ajout_restau_adresse);

        btn_upload = view.findViewById(R.id.btn_upload_image_restau);
        imageView = view.findViewById(R.id.img_ajout_restau);
        btn_ajout.setVisibility(View.GONE);

        mMapView = (MapView) view.findViewById(R.id.mapview_ajout_restau);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setBuiltInZoomControls(true);
        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(15);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        position = new Marker(mMapView);

        Overlay touchOverlay = new Overlay(getActivity()) {
            ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = null;

            @Override
            public void draw(Canvas arg0, MapView arg1, boolean arg2) {

            }

            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {

                final Drawable marker = getActivity().getResources().getDrawable(R.drawable.marker_default);
                Projection proj = mapView.getProjection();

                GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
                longitude = Double.toString(((double) loc.getLongitudeE6()) / 1000000);
                latitude = Double.toString(((double) loc.getLatitudeE6()) / 1000000);
                btn_ajout.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "- Latitude = " + latitude + ", Longitude = " + longitude, Toast.LENGTH_LONG).show();

                ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
                OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double) loc.getLatitudeE6()) / 1000000), (((double) loc.getLongitudeE6()) / 1000000)));
                mapItem.setMarker(marker);
                overlayArray.add(mapItem);

                if (anotherItemizedIconOverlay == null) {
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getActivity(), overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                    mapView.invalidate();
                } else {
                    mapView.getOverlays().remove(anotherItemizedIconOverlay);
                    mapView.invalidate();
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getActivity(), overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                }
                //      dlgThread();
                return true;
            }
        };
        if (touchOverlay != null)
            mMapView.getOverlays().add(touchOverlay);

        /**** OPEN GALLERIE ****/
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                libelle_resto = edt_adresse.getText().toString();


                if (edt_adresse.getText().toString().equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Erreur");
                    alert.setMessage("Remplissez le champs \"Nom\".");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    return;
                }

                if (libelle_resto != null && latitude != null && longitude != null) {

                    //Ajout Offre
                    StringRequest sendData = new StringRequest(Request.Method.POST, IP.localhost + "AjouterRestau.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container_up, new AdminDashboardTopFragment()).commit();
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
                            map.put("villeRestau", libelle_resto);
                            map.put("adresseRestau", "11, rue ghazela");
                            map.put("cordonneeX", latitude);
                            map.put("cordonneeY", longitude);
                            map.put("imageRestau", getStringImage(bitmap));
                            return map;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(sendData);


                } else {
                    Toast.makeText(getActivity(), "données invalide ! ", Toast.LENGTH_SHORT).show();
                }

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
                btn_ajout.setVisibility(View.VISIBLE);
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