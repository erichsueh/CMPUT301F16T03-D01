package com.example.ehsueh.appygolucky;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * List: RiderRequestLst
 * Button1: AddRider
 * Button:BackRiderReq
 *
 * This is our rider request page, with each colour different status
 */
public class RiderRequestListActivity extends ActionBarActivity {
    private UserController uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_request_list);
        uc = new UserController(getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.RiderRequestLst);

//        ArrayList<Ride> rides = uc.getCurrentUser().getAcceptedRideIDs();
//        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
//        final ArrayAdapter rideAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list) {
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//                View view = super.getView(position,convertView,parent);
//                Ride ride = list.get(position);
//                if(ride.getStatus()==2)
//                {
//                    view.setBackgroundColor(Color.parseColor("#00FF00"));
//                }
//                else if(ride.getStatus()==1)
//                {
//                    view.setBackgroundColor(Color.parseColor("#FFFF00"));
//                }
//                else
//                {
//                    view.setBackgroundColor(Color.parseColor("#000000"));
//                }
//                return view;
//            }
//        };
//        listView.setAdapter(rideAdapter);
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                AlertDialog.Builder adb = new AlertDialog.Builder(RiderRequestListActivity.this);
//                final int finalPosition = position;
//                Ride ride = list.get(finalPosition);
//                if (ride.getStatus()==1) {
//                    adb.setMessage("What would you like to do?");
//                    adb.setCancelable(true);
//
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
//                            Intent intent = new Intent(RiderRequestListActivity.this, ListDriversChooseActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    });
//                } else if (ride.getStatus()==0) {
//                    adb.setMessage("Confirm Deletion?");
//                    adb.setCancelable(true);
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
//    }
//
//
//    public void AddRequest(View view) {
//        Intent intent = new Intent(RiderRequestListActivity.this, AddRequestActivity.class);
//        startActivity(intent);
    }

    public void BackRequest(View view) {
        finish();
    }
}
