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
        Intent intent = getIntent();
        final Ride ride = intent.getParcelableExtra("theRide");
        ListView listView = (ListView) findViewById(R.id.PendingList);

        ArrayList<String> rides = (ArrayList<String>) ride.getDriverUsernames();
        final ArrayList<String> list = new ArrayList<String>(rides);
        final ArrayAdapter rideAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list) {

        };

        listView.setAdapter(rideAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DriverPendingActivity.this);
                final int finalPosition = position;
                final String driver = list.get(finalPosition);
                adb.setMessage("What would you like to do?");
                adb.setCancelable(true);

                adb.setPositiveButton("Confirm!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //uc.confirmDriverAcceptance(ride,driver);

                    }
                });
                adb.setNeutralButton("Contact Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DriverPendingActivity.this, ContactInfoActivity.class);
                        startActivity(intent);
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
