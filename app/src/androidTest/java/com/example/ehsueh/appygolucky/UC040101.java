package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Corey on 2016-10-13.
 *
 * US 04.02.01 As a driver, I want to browse and search for open requests by keyword.
 *
 * Related tests include:
 * <ul>
 *     <li>Retrieve list of open requests by keyword</li>
 * </ul>
 */

//TODO: this test should use the server
public class UC040101 extends ActivityInstrumentationTestCase2 {
    public UC040101() {
        super(MainActivity.class);
    }

    public void testGetRidesByLocation() {
        //Some rides with unique descriptions
        String rideDescriptionA = "abcd efgh ijkl mnop";
        Number fareA = 2.75;
        LatLng startLocationA = new LatLng(53.526495, -113.630327);
        LatLng endLocationA = new LatLng(53.526495, -113.630327);
        String rideDescriptionB = "02315 648523";
        Number fareB = 2.75;
        LatLng startLocationB = new LatLng(53.628964, -113.477291);
        LatLng endLocationB = new LatLng(53.628964, -113.477291);
        String rideDescriptionC = "abcd efgh 0987";
        Number fareC = 2.75;
        LatLng startLocationC = new LatLng(53.526495, -113.630327);
        LatLng endLocationC = new LatLng(53.526495, -113.630327);

        UserController uc = new UserController(getActivity().getApplicationContext());

        //Add the three rides to the server.  They will also be attached to the user.
        uc.addRideRequest(startLocationA, endLocationA, fareA, rideDescriptionA);
        uc.addRideRequest(startLocationB, endLocationB, fareB, rideDescriptionB);
        uc.addRideRequest(startLocationC, endLocationC, fareC, rideDescriptionC);

        //Wait for the changes to be reflected in the server
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("timer was interrupted");
        }

        //Try retrieving a ride by the exact description
        ESQueryListener queryListener = new ESQueryListener();
        ElasticSearchRideController.GetRidesByKeywordTask getRidesByKeywordTask =
                new ElasticSearchRideController.GetRidesByKeywordTask(queryListener);
        getRidesByKeywordTask.execute(rideDescriptionA);

        while(queryListener.getResults() == null) {
            //Wait...
        }

        List<Ride> results = queryListener.getResults();

        //Make sure we get a list of results in the right order.
        assertEquals("Returned the wrong number of rides", 2, results.size());
        assertEquals("Returned rides in the wrong order", rideDescriptionA,
                results.get(0).getDescription());
        assertEquals(rideDescriptionC, results.get(1).getDescription());


        //Try retrieving a list of rides by a keyword from the description
        queryListener = new ESQueryListener();
        getRidesByKeywordTask = new ElasticSearchRideController.GetRidesByKeywordTask(queryListener);
        getRidesByKeywordTask.execute("abcd 0987");
    }
}
