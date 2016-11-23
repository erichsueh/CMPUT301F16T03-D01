package com.example.ehsueh.appygolucky;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.IOException;
import java.util.ArrayList;
        import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        DrawingLocationActivity {
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private Marker tripStartMarker;
    private Marker tripEndMarker;
    private Marker currentMarker;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //important! set your user agent to prevent getting banned from the osm servers
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;
    }
    /**
     * initialize map to edmonton by default
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        Intent intent = getIntent();
        //LatLng start_location= new LatLng(53.5444, -113.4909);
        //LatLng end_location= new LatLng(53.525288, -113.525454);
        LatLng start_location = intent.getParcelableExtra("start");
        LatLng end_location = intent.getParcelableExtra("end");

        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(start_location));
        currentMarker.setVisible(false);
        try {
            Geocoder geoCoder = new Geocoder(context);
            List<Address> matches = geoCoder.getFromLocation(start_location.latitude, start_location.longitude, 1);
            String address = "";
            if(!matches.isEmpty()){
                address = matches.get(0).getAddressLine(0) + ' ' +  matches.get(0).getLocality();
            }

            currentMarker.setTitle(address);
            currentMarker.showInfoWindow();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String startaddress = getString(R.string.start_location) + ": " + currentMarker.getTitle();


        tripStartMarker = mMap.addMarker(new MarkerOptions()
                .position(start_location)
                .title(startaddress)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        try {
            Geocoder geoCoder = new Geocoder(context);
            List<Address> matches = geoCoder.getFromLocation(end_location.latitude, end_location.longitude, 1);
            String address = "";
            if(!matches.isEmpty()){
                address = matches.get(0).getAddressLine(0) + ' ' +  matches.get(0).getLocality();
            }

            currentMarker.setTitle(address);
            currentMarker.showInfoWindow();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String endaddress = getString(R.string.end_location) + ": " + currentMarker.getTitle();
        tripEndMarker = mMap.addMarker(new MarkerOptions()
                .position(end_location)
                .title(endaddress)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //Set the initial spot to edmonton for now
        LatLng edmonton = new LatLng(53.5444, -113.4909);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(start_location)      // Sets the center of the map to location user
                .zoom(12)
                .bearing(0)
                .tilt(0)
                .build();



        ensureLocationPermissions();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        JSONMaps helper = new JSONMaps((MapsActivity) context);
        helper.drawPathCoordinates(tripStartMarker, tripEndMarker);
    }

    /**
     * A quick permission check to ensure that we location services enabled
     */
    private void ensureLocationPermissions(){
        //Check if we have the right permissions to use location
        Boolean i = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (i) {
            mMap.setMyLocationEnabled(true);
        }

        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            mMap.setMyLocationEnabled(true);
        }

    }

    public void drawRouteOnMap(List<LatLng> drawPoints, String distance){
        //Draw the lines on the map
        mMap.addPolyline( new PolylineOptions()
                .addAll(drawPoints)
                .width(12)
                .color(Color.parseColor("#4885ed"))//color of line
                .geodesic(true)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.LogoutButton) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}