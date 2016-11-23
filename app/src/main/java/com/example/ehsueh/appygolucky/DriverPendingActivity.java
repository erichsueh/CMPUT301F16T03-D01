package com.example.ehsueh.appygolucky;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        ArrayList<Ride> rides = (ArrayList<Ride>) uc.getAcceptedRides().getRides();
        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
        final ArrayAdapter rideAdapter = new ArrayAdapter<Ride>(this,android.R.layout.simple_list_item_1, list) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                Ride ride = list.get(position);
                if(ride.getStatus()==2)
                {
                    view.setBackgroundColor(Color.parseColor("#00FF00"));
                }
                else if(ride.getStatus()==1)
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
    }




    public void BackPending (View view) {
    finish();}
}
