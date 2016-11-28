package com.example.ehsueh.appygolucky;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * List view :PendingList
 * Button:PendingBack
 *As a driver, this page allows you to delete your said proposals
 * The pending shows the lsit of riders you've proposed to
 */

public class DriverPendingActivity extends ActionBarActivity {
    private UserController uc;
    private Listener acceptedRidesListener;

    /**
     * in the oncreate method, we set the array adapter using the user controller,
     * then we override some of the ride adapters to get our different color to show our status's
     * Red status means there's things to be taken care of, and green mean that your proposed ride
     * has been accepted by the rider.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_pending);
        uc = new UserController(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.PendingList);

        ArrayList<Ride> rides = (ArrayList<Ride>) uc.getAcceptedRides().getRides();
        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
        final ArrayAdapter rideAdapter = new ArrayAdapter<Ride>(this,android.R.layout.simple_list_item_1, list) {


            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                Ride ride = list.get(position);
                if(ride.getStatus()==Ride.CONFIRMED)
                {
                    view.setBackgroundColor(Color.parseColor("#13B627"));
                }
                else if(ride.getStatus()==Ride.ACCEPTED)
                {
                    view.setBackgroundColor(Color.parseColor("#C13029"));
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#000000"));
                }
                return view;
            }


        };
        listView.setAdapter(rideAdapter);

        //Create an object of an abstract class, using the listener interface
        //From studentPicker videos
        acceptedRidesListener = new Listener() {
            @Override
            public void update() {
                list.clear();
                list.addAll(uc.getRequestedRides().getRides());
                rideAdapter.notifyDataSetChanged();
            }
        };

        uc.getAcceptedRides().addListener(acceptedRidesListener);

        /**
         * this place is our "long click listener" used to set up our ADB
         * so the driver can cancel his proposal should he choose to
         */

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int finalPosition = position;
                final Ride ride = list.get(finalPosition);
                new AlertDialog.Builder(DriverPendingActivity.this)
                        .setTitle("Options")
                        .setMessage("What would you like to do?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                uc.deleteAcceptedRide(ride);
                                rideAdapter.notifyDataSetChanged();

                            }
                        })

                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .show();
                return false;
            }
        });

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                AlertDialog.Builder adb = new AlertDialog.Builder(DriverPendingActivity.this);
//                final int finalPosition = position;
//                final Ride ride = list.get(finalPosition);
//                adb.setMessage("Confirm Deletion?");
//                adb.setCancelable(true);
//                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //Ride ride = list.get(finalPosition);
//                        uc.deleteAcceptedRide(ride);
//                        rideAdapter.notifyDataSetChanged();
//
//                    }
//                });
//                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                return false;
//            }
//        });
    }

    protected void onDestroy() {
        super.onDestroy();
        uc.getAcceptedRides().removeListener(acceptedRidesListener);
    }


    /**
     * standard back button we have on all pages
     * @param view
     */

    public void BackPending (View view) {
    finish();}
}
