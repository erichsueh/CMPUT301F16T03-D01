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
        super(HomePageActivity.class);
    }

    public void testGetRidesByKeyword() {
        //Some rides with unique descriptions
        String rideDescriptionA = "abcd efgh ijkl mnop 648523";
        Number fareA = 2.75;
        LatLng startLocationA = new LatLng(53.526495, -113.630327);
        LatLng endLocationA = new LatLng(53.526495, -113.630327);
        String rideDescriptionB = "02315 648523";
        Number fareB = 2.75;
        LatLng startLocationB = new LatLng(53.628964, -113.477291);
        LatLng endLocationB = new LatLng(53.628964, -113.477291);
        String rideDescriptionC = "abcd efgh 0987 648523";
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
        assertEquals("Returned the wrong number of rides", 3, results.size());
        assertEquals("Returned rides in the wrong order", rideDescriptionA,
                results.get(0).getDescription());
        assertEquals(rideDescriptionC, results.get(1).getDescription());


        //Retrieve all three rides (to get their IDs), and delete them from the server
        queryListener = new ESQueryListener();
        new ElasticSearchRideController.GetRidesByKeywordTask(queryListener)
                .execute("648523");
        while(queryListener.getResults() == null) {
            /*wait*/
        }
        Ride ride1 = (Ride) queryListener.getResults().get(0);
        Ride ride2 = (Ride) queryListener.getResults().get(1);
        Ride ride3 = (Ride) queryListener.getResults().get(2);
        new ElasticSearchRideController.DeleteRideTask().execute(ride1.getId());
        new ElasticSearchRideController.DeleteRideTask().execute(ride2.getId());
        new ElasticSearchRideController.DeleteRideTask().execute(ride3.getId());

    }
}
