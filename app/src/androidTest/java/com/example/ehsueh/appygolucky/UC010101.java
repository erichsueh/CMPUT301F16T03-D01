package com.example.ehsueh.appygolucky;


import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Maxwell on 2016-10-11.
 * User Story US 01.01.01 - As a rider, I want to request rides between two locations.
 * Related tests for this use case include:
 * <ul>
 *     <li>I want to create a ride request</li>
 *     <li>I want to be able specify the starting place and destination</li>
 * </ul>
 *
 *
 *
 */

public class UC010101 extends ActivityInstrumentationTestCase2{

    public UC010101() {
        super(MainActivity.class);
    }

    public void testRequestRide() {
        String rideDescription = "I need a ride to West Ed!!";
        LatLng startLocation = new LatLng(53.526495, -113.630327);
        LatLng endLocation = new LatLng(53.526495, -113.630327);
        Number fare = 2.75;
        UserController uc = new UserController(getActivity().getApplicationContext());

        //Create a new user, with new rideLists
        try {
            uc.newUserLogin("test_johnnyBoy", "", "", "", "");
        } catch(Exception e) {
            fail("Failed to log in new user");
        }

        uc.addRideRequest(startLocation, endLocation, fare, rideDescription);


        //Wait for the changes to be reflected in the server
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("Timer interrupted");
        }

        //Test whether the ride is in the local list
        List<Ride> rides = uc.getRequestedRides().getRides();
        assertEquals("Wrong number of rides locally", 1, rides.size());
        Ride ride = rides.get(0);
        assertEquals("Wrong description", rideDescription, ride.getDescription());
        assertNotNull(ride.getId());

        //Test whether the ride ID is in the user's list locally
        String rideID = rides.get(0).getId();
        List<String> rideIDs = uc.getCurrentUser().getRideRequestIDs();
        assertTrue("Local User doesn't contain ride ID", rideIDs.contains(rideID));

        //Test whether the ride list is saved to file
        uc.loadFromFile();
        rides = uc.getRequestedRides().getRides();
        assertEquals("Wrong number of rides saved to file", 1, rides.size());
        assertEquals("Wrong description saved to file", rideDescription, rides.get(0).getDescription());

        //Test whether the ride ID is saved to the user's file
        rideIDs = uc.getCurrentUser().getRideRequestIDs();
        assertTrue("Ride ID isn't saved to file", rideIDs.contains(rideID));

        //Test whether the ride is saved to the server
        ESQueryListener queryListener = new ESQueryListener();
        ElasticSearchRideController.GetRidesByIdTask getRidesByIdTask =
                new ElasticSearchRideController.GetRidesByIdTask(queryListener);
        getRidesByIdTask.execute(rideID);
        while(queryListener.getResults() == null) {
            //wait...
        }
        assertEquals("Got the wrong number of rides from the server", 1,
                queryListener.getResults().size());
        List<Ride> results = queryListener.getResults();
        assertEquals("Wrong ride description from the server", rideDescription,
                results.get(0).getDescription());

        //Test whether the ride ID is saved to the user on the server
        queryListener = new ESQueryListener();
        ElasticSearchUserController.GetUserByUsernameTask getUserByUsernameTask =
                new ElasticSearchUserController.GetUserByUsernameTask(queryListener);
        getUserByUsernameTask.execute(uc.getCurrentUser().getUsername());
        while(queryListener.getResults() == null) {
            //wait...
        }
        User user = (User) queryListener.getResults().get(0);
        List<String> rideIds = user.getRideRequestIDs();
        assertTrue("User on the server didn't contain the ride Id", rideIds.contains(rideID));
    }

}
