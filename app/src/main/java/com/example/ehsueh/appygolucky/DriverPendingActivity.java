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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_pending);

        ListView listView = (ListView) findViewById(R.id.PendingList);

        ArrayList<Ride> rides = RideController.getRideList();
        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
        final ArrayAdapter<Ride> rideAdapter = new ArrayAdapter<Ride>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(rideAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DriverPendingActivity.this);

                if (UserController.whatstatus(1)) {
                    adb.setMessage("Do you want to select this driver?");
                    adb.setCancelable(true);
                    final int finalPosition = position;
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                }
                return false;
            }
        });
    }


    public void BackPending (View view) {
    finish();}
}
