package com.example.ehsueh.appygolucky;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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

/**
 * Main activity for riders to select their pickup and drop off locations.
 */
public class AddRequest extends ActionBarActivity implements OnMapReadyCallback,
            DrawingLocationActivity {

    private GoogleMap mMap;
    private Marker tripStartMarker;
    private Marker tripEndMarker;
    private Marker currentMarker;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

    }

    /**
     * Set up all the on click listeners for this activity
     */
    private void setButtonListeners(){
        final Button setLocation = (Button)findViewById(R.id.setLocationButton);
        final Button cancelTrip = (Button)findViewById(R.id.cancelTrip);
//        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        cancelTrip.setEnabled(false);

        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripStartMarker != null){
                    tripStartMarker.remove();
                    tripStartMarker = null;
                }
                if(tripEndMarker != null){
                    tripEndMarker.remove();
                    tripEndMarker = null;
                }
                if(currentMarker != null){
                    currentMarker.remove();
                    currentMarker = null;
                }

                //Reset button state when they cancel
                setLocation.setText("set start");

                //Once they cancel it disable this button until they place another pin
                cancelTrip.setEnabled(false);

                mMap.clear();
            }
        });

//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                LatLng location = place.getLatLng();
//                if(currentMarker != null){
//                    currentMarker.remove();
//                }
//
//                currentMarker = mMap.addMarker(new MarkerOptions()
//                        .position(location)
//                        .title(place.getAddress().toString())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                currentMarker.showInfoWindow();
//            }
//
//            @Override
//            public void onError(Status status) {
//                AlertDialog markerWarning = new AlertDialog.Builder(context).create();
//                markerWarning.setMessage("Something went wrong in trying to search address.");
//                markerWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                markerWarning.show();
//            }
//        });
//        new AlertDialog.Builder(context)
//                .setTitle("Delete entry")
//                .setMessage("Are you sure you want to delete this entry?")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();

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
                    String address = getString(R.string.start_location) + ": " + currentMarker.getTitle();
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
                    String address = getString(R.string.end_location) + ": " + currentMarker.getTitle();
                    tripEndMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentMarker.getPosition())
                            .title(address)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    tripEndMarker.showInfoWindow();
                    currentMarker = null;
                    setLocation.setText(R.string.confirm_trip);

                } else {

                    JSONMaps helper = new JSONMaps((AddRequest) context);
                    helper.drawPathCoordinates(tripStartMarker, tripEndMarker);
                }

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

                    try {
                        Geocoder geoCoder = new Geocoder(context);
                        List<Address> matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        String address = "";
                        if(!matches.isEmpty()){
                            address = matches.get(0).getAddressLine(0) + ' ' +  matches.get(0).getLocality();
                        }

                        currentMarker.setTitle(address);
                        currentMarker.showInfoWindow();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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

        //Set the initial spot to edmonton for now
        LatLng edmonton = new LatLng(53.5444, -113.4909);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(edmonton)      // Sets the center of the map to location user
                .zoom(11)
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
        //Draw the lines on the map
        mMap.addPolyline( new PolylineOptions()
                .addAll(drawPoints)
                .width(12)
                .color(Color.parseColor("#696969"))//color of line
                .geodesic(true)
        );

        displayRideConfirmationDlg(distance);
    }

    void displayRideConfirmationDlg(String distance){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.requestconfirm);

        final TextView from = (TextView)dialog.findViewById(R.id.fromText);
        final TextView to = (TextView)dialog.findViewById(R.id.toText);
        final TextView distanceDlg = (TextView)dialog.findViewById(R.id.distance);
        final TextView fairEstimate = (TextView)dialog.findViewById(R.id.fairEstimate);

        final Button cancel = (Button)dialog.findViewById(R.id.cancelRequest);
        final Button confirm = (Button)dialog.findViewById(R.id.confirmRequest);
        final EditText amount= (EditText)dialog.findViewById(R.id.Fare);

        //Set all the right values in the dialog
        final double fairAmount = FairEstimation.estimateFair(distance);
        dialog.setTitle( "Enter Details" );
        from.setText(tripStartMarker.getTitle());
        to.setText(tripEndMarker.getTitle());
        distanceDlg.setText("Distance: "+ distance);
        fairEstimate.setText("Estimate Cost: $"+ Double.toString(fairAmount));

        //set up the on click listeners for dialog


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("");
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double userFare = 0.00;
                try {
                    userFare = Double.parseDouble(amount.getText().toString());

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
                        userFare,
                        UserController.currentUser);
                //RideController.addRide(request);

// TODO: will add this once we have a way to terminate the service when the user requests
// Start the service for rider notifications
//                Intent intent = RiderNotificationService.createIntentStartNotificationService(context);
//                startService(intent);

                dialog.dismiss();
            }
        });

        //Show the dialog
        dialog.show();
    }

    static class FairEstimation{

        static public double estimateFair(String strDistance){
            Scanner sc = new Scanner(strDistance);
            double distanceKm = sc.nextDouble();

            //Rate starts at 2.50
            double rate = 2.50;

            //add 1.25 for each km
            rate += distanceKm * 1.25;

            DecimalFormat df = new DecimalFormat("#.##");
            String dx=df.format(rate);
            rate= Double.valueOf(dx);

            return rate;
        }
    }
}



