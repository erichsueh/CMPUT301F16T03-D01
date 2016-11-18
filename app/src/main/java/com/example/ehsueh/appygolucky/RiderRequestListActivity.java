package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * List: RiderRequestLst
 * Button1: AddRider
 * Button:BackRiderReq
 *
 * This is our rider request page, with each colour different status
 */
public class RiderRequestListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_request_list);

//        ListView listView = (ListView) findViewById(R.id.RiderRequestLst);
//
//        ArrayList<Ride> rides = RideController.getRideList();
//        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
//        final ArrayAdapter<Ride> rideAdapter = new ArrayAdapter<Ride>(this,android.R.layout.simple_list_item_1, list);
//        listView.setAdapter(rideAdapter);
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                AlertDialog.Builder adb = new AlertDialog.Builder(RiderRequestListActivity.this);
//
//                if (UserController.whatstatus(1)) {
//                    adb.setMessage("What would you like to do?");
//                    adb.setCancelable(true);
//                    final int finalPosition = position;
//                    adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            // TODO: 2016-11-14
//
//                        }
//                    });
//                    adb.setNeutralButton("See List", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(RiderRequestListActivity.this, DriverPendingActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    });
//                } else if (UserController.whatstatus(0)) {
//                    adb.setMessage("Confirm Deletion?");
//                    adb.setCancelable(true);
//                    final int finalPosition = position;
//                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            // TODO: 2016-11-14
//
//                        }
//                    });
//                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                } else {
//                    adb.setMessage("Confirm Completion");
//                    adb.setCancelable(true);
//                    final int finalPosition = position;
//                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            // TODO: 2016-11-14
//
//                        }
//                    });
//                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                }
//                adb.show();
//                return false;
//            }
//        });
    }


    public void AddRequest(View view) {
        Intent intent = new Intent(RiderRequestListActivity.this, AddRequestActivity.class);
        startActivity(intent);}

    public void BackRequest(View view) {
        finish();
    }
}
