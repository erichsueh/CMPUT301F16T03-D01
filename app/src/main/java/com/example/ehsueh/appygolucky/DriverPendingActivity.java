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
 *
 * The pending shows the lsit of riders you've proposed a price to
 */

public class DriverPendingActivity extends ActionBarActivity {
    private UserController uc;
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
                    view.setBackgroundColor(Color.parseColor("#00FF00"));
                }
                else if(ride.getStatus()==Ride.ACCEPTED)
                {
                    view.setBackgroundColor(Color.parseColor("#FFFF00"));
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#000000"));
                }
                return view;
            }


        };
        listView.setAdapter(rideAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DriverPendingActivity.this);
                final int finalPosition = position;
                final Ride ride = list.get(finalPosition);
                adb.setMessage("Confirm Deletion?");
                adb.setCancelable(true);
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Ride ride = list.get(finalPosition);
                        uc.deleteAcceptedRide(ride);

                    }
                });
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                return false;
            }
        });
    }




    public void BackPending (View view) {
    finish();}
}
