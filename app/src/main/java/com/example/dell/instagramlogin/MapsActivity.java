package com.example.dell.instagramlogin;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.dell.instagramlogin.model.instamedia.MyLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

/**
 * Created by navinpanchal3373@gmail.com on 11/4/2017.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getExtras() != null)
            location = getIntent().getExtras().getString("data");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        if (location != null && location.length() > 0) {
            try {

                MyLocation myLocation = new Gson().fromJson(location, MyLocation.class);
                LatLng sydney = new LatLng(Double.parseDouble(myLocation.getLatitude()),
                        Double.parseDouble(myLocation.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(sydney).title(myLocation.getName()));

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(sydney, 12);
                mMap.animateCamera(yourLocation);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
