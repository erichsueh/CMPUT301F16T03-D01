package com.example.ehsueh.appygolucky;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.BooleanResult;
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

public class SearchPageActivity extends AppCompatActivity {
    private EditText searchText;
    private UserController uc;
    private Boolean clicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        searchText = (EditText) findViewById(R.id.editText);
        uc = new UserController(getApplicationContext());
        clicked = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clicked.equals(true)){
            clicked = false;
            Toast toast = Toast.makeText(this, "True", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public void searchWithLocation(View view){

        String searchLocation = searchText.getText().toString();
        if (searchLocation.equals(""))
        {
            Toast toast = Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
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
                            if (rides.size() == 0)
                            {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Finished Searching, no rides found.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else{
                                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        final int finalPosition = position;
                                        final Ride ride = list.get(finalPosition);
                                        new AlertDialog.Builder(SearchPageActivity.this)
                                            .setTitle("Options")
                                            .setMessage("What would you like to do?")
                                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    uc.addAcceptedRequest(ride);

                                                }
                                            })
                                            .setNeutralButton("See Map", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(SearchPageActivity.this, MapsActivity.class);
                                                    LatLng start_location = ride.getStartLocation();
                                                    LatLng end_location = ride.getEndLocation();
                                                    intent.putExtra("start", start_location);
                                                    intent.putExtra("end", end_location);
                                                    startActivity(intent);
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

                            }
                        }

                        });
            getRidesByKeywordTask.execute(searchLocation);
        }


    }

    /**
     * renaim this to "back"
     * @param view
     */
    public void AcceptSearch (View view) {
        finish();}

    public void ShowMap(View view){
        clicked = true;
        Intent intent = new Intent(SearchPageActivity.this, MapSearchActivity.class);
        startActivity(intent);
    }
}
