package com.example.ehsueh.appygolucky;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

//This activity will return a latlng object based on what point the user
//picked on.
public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private Marker currentMarker;
    private Context context;
    private UserController uc;
    //set up googlemap
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        uc = new UserController(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;
    }

    /**
     * Set up all the listener for buttons and map markers
     */
    private void setButtonListeners(){
        final Button setLocation = (Button)findViewById(R.id.setLocationButton);
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.search_place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng location = place.getLatLng();
                if(currentMarker != null){
                    currentMarker.remove();
                }
                //this will move the camera to the start point
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,12));
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(place.getAddress().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                currentMarker.showInfoWindow();
            }

            @Override
            public void onError(Status status) {
                AlertDialog markerWarning = new AlertDialog.Builder(context).create();
                markerWarning.setMessage("Something went wrong in trying to search address.");
                markerWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                markerWarning.show();
            }
        });
        //this is the button for pass back the latlng object
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do not want to spawn a warning if the end marker is not null
                if (currentMarker == null) {
                    AlertDialog markerWarning = new AlertDialog.Builder(context).create();
                    markerWarning.setMessage(getString(R.string.select_place));
                    markerWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    markerWarning.show();
                }
                //this is when we know user is set on this marker, and it returns
                //the location of it.
                else {
                    LatLng search_location = currentMarker.getPosition();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("search",search_location);
                    setResult(Activity.RESULT_OK,resultIntent);
                    finish();
                }
            }
        });

        //Listener for the map so we know when the user clicks
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if (currentMarker == null) {
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    currentMarker.setTitle(latLng.toString());
                    currentMarker.showInfoWindow();
                    //check if phone have connection
                    if (isNetworkAvailable()) {
                        try {
                            Geocoder geoCoder = new Geocoder(context);
                            List<Address> matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            String address = "";
                            if (!matches.isEmpty()) {
                                address = matches.get(0).getAddressLine(0) + ' ' + matches.get(0).getLocality();
                            }

                            currentMarker.setTitle(address);
                            currentMarker.showInfoWindow();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
                else if (currentMarker != null) {
                    currentMarker.remove();
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    currentMarker.setTitle(latLng.toString());
                    currentMarker.showInfoWindow();

                    if (isNetworkAvailable()) {
                        try {
                            Geocoder geoCoder = new Geocoder(context);
                            List<Address> matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            String address = "";
                            if (!matches.isEmpty()) {
                                address = matches.get(0).getAddressLine(0) + ' ' + matches.get(0).getLocality();
                            }

                            currentMarker.setTitle(address);
                            currentMarker.showInfoWindow();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    /**
     * When the map is initialized this is called by default
     */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();

        //Set the initial spot to edmonton for now
        LatLng edmonton = new LatLng(53.5444, -113.4909);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(edmonton)      // Sets the center of the map to location user
                .zoom(10)
                .bearing(0)
                .tilt(0)
                .build();



        ensureLocationPermissions();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        setButtonListeners();
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
    //this is a method to check if network is available, it returns true if there is internet, false otherwise
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
