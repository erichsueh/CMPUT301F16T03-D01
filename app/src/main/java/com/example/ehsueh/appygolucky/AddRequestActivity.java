package com.example.ehsueh.appygolucky;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.Place;
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
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

/**
 * Main activity for riders to select their pickup and drop off locations.
 * Google Map API v2 and Google Places was used for searching and picking locations.
 */
public class AddRequestActivity extends AppCompatActivity implements OnMapReadyCallback,
            DrawingLocationActivity{

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private Marker tripStartMarker;
    private Marker tripEndMarker;
    private Marker currentMarker;
    private Context context;
    private String Distance = "? km";
    private UserController uc;
    private Connectivity Con;

    //Oncreate we start the map
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        uc = new UserController(getApplicationContext());
        Con = new Connectivity();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

    }

    /**
     * Set up all the on click listeners for the addrequest map page
     */
    private void setButtonListeners(){
        final Button setLocation = (Button)findViewById(R.id.setLocationButton);
        final Button cancelTrip = (Button)findViewById(R.id.cancelTrip);
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        cancelTrip.setEnabled(false);

        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset middle button
                setLocation.setText("set start");
                //Once they cancel it will be enabled after a marker is added
                cancelTrip.setEnabled(false);
                //Reset all Markers and clear entire googlemap
                mMap.clear();
                tripStartMarker = null;
                tripEndMarker = null;
                currentMarker = null;
            }
        });
        //This Fragement is for the place searching bar to autocomplete what the user wrote
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng location = place.getLatLng();
                //camera move to the place selected and marker is added
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,12));
                if(currentMarker != null){
                    currentMarker.remove();
                }
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(place.getAddress().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                currentMarker.showInfoWindow();
            }

            @Override
            public void onError(Status status) {
                AlertDialog markerWarning = new AlertDialog.Builder(context).create();
                markerWarning.setMessage("Error in searching the places");
                markerWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                markerWarning.show();
            }
        });

        //Listener for the map so we know when the user clicks
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                if (currentMarker != null) {
                    currentMarker.remove();
                }
                if (tripEndMarker == null) {
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    currentMarker.setTitle(latLng.toString());
                    currentMarker.showInfoWindow();

                    if (Con.isNetworkAvailable()) {
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
        //This the middle button to check if markers are picked by user and show the address
        //for latlong of the locations, it will also change the word on the button so user
        //know which marker he's pointing.
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do not want to spawn a warning if the end marker is not null
                if (currentMarker == null && tripEndMarker == null) {
                    AlertDialog markerWarning = new AlertDialog.Builder(context).create();
                    markerWarning.setMessage(getString(R.string.warning_message));
                    markerWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    markerWarning.show();
                }

                //We put start marker as the current one since we have start point now
                else if (tripStartMarker == null) {
                    currentMarker.remove();
                    String s = currentMarker.getTitle();
                    if (!Con.isNetworkAvailable()) {
                        s = s.substring(9);
                    }
                    String address = getString(R.string.start_location) + ": " + (s);
                    tripStartMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentMarker.getPosition())
                            .title(address)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    tripStartMarker.showInfoWindow();


                    currentMarker = null;
                    setLocation.setText(R.string.set_end);
                    cancelTrip.setEnabled(true);

                }
                //We put End marker as the current one since we have end point now
                else if (tripEndMarker == null) {
                    currentMarker.remove();
                    String s = currentMarker.getTitle();
                    if (!Con.isNetworkAvailable()) {
                        s = s.substring(9);
                    }
                    String address = getString(R.string.end_location) + ": " + (s);
                    tripEndMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentMarker.getPosition())
                            .title(address)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    tripEndMarker.showInfoWindow();
                    currentMarker = null;
                    setLocation.setText(R.string.confirm_trip);
                    //JSONmaps will draw all the routes
                    JSONMaps helper = new JSONMaps((AddRequestActivity) context);
                    helper.drawPathCoordinates(tripStartMarker, tripEndMarker);

                } else {

                    displayRideConfirmationDlg(Distance);
                }

            }
        });
    }

    /**
     * When the map is initialized this is called by default
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();

        //Set the initial spot to edmonton
        LatLng edmonton = new LatLng(53.5444, -113.4909);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(edmonton)
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

    /**
     * This method is called after the route has been retrieved from the google servers. It creates and shows
     * the confirmation_trip dialog which prompts the user to enter a fare amount. It also draws polyline points
     * on the map of the route they will be taking.
     * @param drawPoints
     * @param distance
     */
    public void drawRouteOnMap(List<LatLng> drawPoints, String distance){
        Distance = distance;
        //Draw the lines on the map
        mMap.addPolyline( new PolylineOptions()
                .addAll(drawPoints)
                .width(12)
                .color(Color.parseColor("#4885ed"))//color of line
                .geodesic(true)
        );


    }
    //This is the dlg back bone for adding request, it will show start,end locations, distance and
    //estimated price based on distance, and also two edit text for user to propose price
    //and description.
    //When it is offline, it will behave differently, distance and price will not work and only show
    //user that it needs internet.
    void displayRideConfirmationDlg(String distance){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_requestconfirm);

        final TextView from = (TextView)dialog.findViewById(R.id.fromText);
        final TextView to = (TextView)dialog.findViewById(R.id.toText);
        final TextView distanceDlg = (TextView)dialog.findViewById(R.id.distance);
        final TextView fairEstimate = (TextView)dialog.findViewById(R.id.fairEstimate);
        final Button cancel = (Button)dialog.findViewById(R.id.cancelRequest);
        final Button confirm = (Button)dialog.findViewById(R.id.confirmRequest);
        final EditText amount= (EditText)dialog.findViewById(R.id.Fare);
        final EditText description = (EditText)dialog.findViewById(R.id.note_to_driver);
        fairEstimate.setText("Estimate Cost: **Requires internet**");
        distanceDlg.setText("Distance: **Requires internet**");
        //Set up info for dialog
        if (Con.isNetworkAvailable()) {
            final double fairAmount = FairEstimation.estimateFair(distance);
            fairEstimate.setText("Estimate Cost: $"+ Double.toString(fairAmount));
            distanceDlg.setText("Distance: "+ distance);
        }
        dialog.setTitle( "Enter Details" );
        from.setText(tripStartMarker.getTitle());
        to.setText(tripEndMarker.getTitle());



        //set up the on click listeners for dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        //this is edittext for fare
        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("");
            }
        });
        //this is edittext for description
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description.setText("");
            }
        });
        //button to add request and it checks if fare is entered
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double userFare = 0.00;
                String note = "";
                try {
                    userFare = Double.parseDouble(amount.getText().toString());
                    note = description.getText().toString();

                } catch (NumberFormatException e) {
                    //Tell them they have not entered a double
                    e.printStackTrace();
                }
                if (userFare == 0.00) {
                    AlertDialog doubleWarning = new AlertDialog.Builder(context).create();
                    doubleWarning.setMessage(getString(R.string.amout_warning_message));
                    doubleWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    doubleWarning.show();
                    return;
                }
                Ride request = new Ride(tripStartMarker.getPosition(), tripEndMarker.getPosition(),
                        userFare, note,
                        uc.getCurrentUser());

// Start the service for rider notifications
//                Intent intent = RiderNotificationService.createIntentStartNotificationService(context);
//                startService(intent);

                dialog.dismiss();
                if (!Con.isNetworkAvailable())
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You are offline, request will be added when online!", Toast.LENGTH_LONG);
                    toast.show();
                    //OFFLINE????
                    //uc.addRideRequest(request);
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Request Added Successfully!", Toast.LENGTH_SHORT);
                    toast.show();
                    uc.addRideRequest(request);
                }

                finish();
            }
        });

        //Show the dialog
        dialog.show();

    }

    //This is based on taxi fare in edmonton
    //https://www.numbeo.com/taxi-fare/in/Edmonton
    static class FairEstimation{

        static public double estimateFair(String strDistance){
            Scanner sc = new Scanner(strDistance);
            double distanceKm = sc.nextDouble();

            //base rate is at 3.6
            double rate = 3.60;

            //add 1.48 for each km
            rate += distanceKm * 1.48;

            DecimalFormat df = new DecimalFormat("#.##");
            String dx=df.format(rate);
            rate= Double.valueOf(dx);

            return rate;
        }
    }
//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

}



