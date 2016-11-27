package com.example.ehsueh.appygolucky;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 2 Buttons and a list
 * List:SearchList
 * Buttons: AcceptGeoButton
 * Button2: SeeMapButton
 *
 * This is our "can look for riders by geolocation or by a keyword
 */

public class SearchPageActivity extends ActionBarActivity {
    private EditText searchText;
    private UserController uc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        searchText = (EditText) findViewById(R.id.editText);
        uc = new UserController(getApplicationContext());
    }

    public void searchWithLocation(View view){
        String searchLocation = searchText.getText().toString();


        final ListView listView = (ListView) findViewById(R.id.searchList);
        ElasticSearchRideController.GetRidesByKeywordTask getRidesByKeywordTask =
                new ElasticSearchRideController.GetRidesByKeywordTask(new ESQueryListener() {
                    @Override
                    public void onQueryCompletion(List<?> results) {
                    ArrayList<Ride> rides = (ArrayList<Ride>) results;
                        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
                        final ArrayAdapter rideAdapter = new ArrayAdapter<Ride>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(rideAdapter);
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                                AlertDialog.Builder adb = new AlertDialog.Builder(SearchPageActivity.this);
                                final int finalPosition = position;
                                final Ride ride = list.get(finalPosition);
                                adb.setMessage("What would you like to do?");
                                adb.setCancelable(true);

                                adb.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        uc.addAcceptedRequest(ride);

                                    }
                                });
                                adb.setNeutralButton("See Map", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(SearchPageActivity.this, MapsActivity.class);
                                        LatLng start_location= ride.getStartLocation();
                                        LatLng end_location= ride.getEndLocation();
                                        intent.putExtra("start",start_location);
                                        intent.putExtra("end",end_location);
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

                });
        getRidesByKeywordTask.execute(searchLocation);



    }

    /**
     * renaim this to "back"
     * @param view
     */
    public void AcceptSearch (View view) {
        finish();}

    public void ShowMap(View view){
        Intent intent = new Intent(SearchPageActivity.this, MapSearchActivity.class);
        //LatLng start_location= new LatLng(53.5444, -113.4909);
        //LatLng end_location= new LatLng(53.525288, -113.525454);
        //intent.putExtra("start",start_location);
        //intent.putExtra("end",end_location);
        startActivity(intent);
    }
}
