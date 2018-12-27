package com.esprit.projetespritfood;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapFragment extends Fragment implements LocationListener {

    MapView mMapView;
    MapController mMapController;
    LocationManager locationManager;
    LocationListener locationListener;
    Context mContext;

    private MyLocationNewOverlay mLocationOverlay;

    Marker position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        getActivity().setTitle("Nos restaurants");


        if (!isNetworkConnected()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Connexion requise");
            alert.setMessage("Cette application nécessite une connexion internet. Veuillez vous connecter s'il vous plaît.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        mMapView = (MapView) root.findViewById(R.id.mapview);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setBuiltInZoomControls(true);
        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(15);
        //Position
//        GeoPoint gPt = new GeoPoint(36.7301504, 10.335570599999983);
//        mMapController.setCenter(gPt);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        position = new Marker(mMapView);


        Location l = new Location("");
        l.setLatitude(36.7301504);
        l.setLongitude(10.335570599999983);
        onLocationChanged(l);

        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                IP.localhost + "Restaurants.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        //Marker
                        final Marker startMarker = new Marker(mMapView);
                        GeoPoint rest = new GeoPoint(Double.parseDouble(o.getString("cordonneeX")), Double.parseDouble(o.getString("cordonneeY")));
                        startMarker.setPosition(rest);
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        mMapView.getOverlays().add(startMarker);
                        startMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
                        startMarker.setTitle("ESPRITFood " + o.getString("villeRestau"));
                        startMarker.setSubDescription("Adresse : " + o.getString("adresseRestau"));
                        //Ajouter la photo du restaurant

                        Drawable image = getResources().getDrawable(R.drawable.icon_menu);
                        startMarker.setImage(image);


                        /*
                        String url = IP.images+o.getString("imageRestau");
                        Picasso.with(getActivity()).load(url).into(new Target() {
                            @Override
                            public void onPrepareLoad(Drawable arg0) {
                            }
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                                // TODO Create your drawable from bitmap and append where you like.
                                final Drawable image = new BitmapDrawable(getResources(),bitmap);
                                startMarker.setImage(image);
                            }
                            @Override
                            public void onBitmapFailed(Drawable arg0) {
                            }
                        });
                        */

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erreur ... ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjReq);


        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return root;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


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

    @Override
    public void onLocationChanged(Location location) {
        GeoPoint gPt = new GeoPoint(location.getLatitude(), location.getLongitude());
        mMapController.setCenter(gPt);

        position.setPosition(gPt);
        position.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(position);
        position.setIcon(getResources().getDrawable(R.drawable.ic_menu_mylocation));
        Toast.makeText(getActivity(), location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}

