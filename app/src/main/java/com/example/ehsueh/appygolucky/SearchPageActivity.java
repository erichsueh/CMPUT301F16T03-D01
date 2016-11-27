package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
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
    //private UserController uc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        searchText = (EditText) findViewById(R.id.editText);
        //uc = new UserController(getApplicationContext());
    }

    public void searchWithLocation(View view){
        String searchLocation = searchText.getText().toString();

        ListView listView = (ListView) findViewById(R.id.searchList);
        ESQueryListener queryListener = new ESQueryListener();
        ElasticSearchRideController.GetRidesByKeywordTask getRidesByKeywordTask =
                new ElasticSearchRideController.GetRidesByKeywordTask(queryListener);
        getRidesByKeywordTask.execute(searchLocation);

        while(queryListener.getResults() == null) {
            //Wait...
        }

        List<Ride> results = queryListener.getResults();

        ArrayList<Ride> rides = (ArrayList<Ride>) results;
        final ArrayList<Ride> list = new ArrayList<Ride>(rides);
        final ArrayAdapter rideAdapter = new ArrayAdapter<Ride>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(rideAdapter);
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
