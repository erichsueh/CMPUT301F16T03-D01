package com.example.ehsueh.appygolucky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        ArrayList<Ride> rides = uc.getCurrentUser().getAcceptedRides();
        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
        final ArrayAdapter rideAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(rideAdapter);
    }


    public void BackPending (View view) {
    finish();}
}
