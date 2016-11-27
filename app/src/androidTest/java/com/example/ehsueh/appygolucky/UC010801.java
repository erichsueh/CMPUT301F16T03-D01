package com.example.ehsueh.appygolucky;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;


/**
 * Created by Maxwell on 2016-10-11.
 * User Story 01.08.01 - As a rider, I want to confirm a driver's acceptance. This allows us to
 * choose from a list of acceptances if more than 1 driver accepts simultaneously.
 */

//Some reason this test is failing because of an IndexOutOfBoundsException.
    //I tracked down the problem with a debugger.  See explanation at line 138

public class UC010801 extends ActivityInstrumentationTestCase2{
    public UC010801() {
        super(MainActivity.class);
    }

    private class MockUserController extends UserController {
        public MockUserController(Context context) {
            super(context);
            requestedRides = new RideList();
            acceptedRides = new RideList();
        }

        public void inputData(User user, RideList requested, RideList accepted) {
            currentUser = user;
            requestedRides = requested;
            acceptedRides = accepted;
        }
    }


    public void testConfirmRideRequest () {
        Datacleaner.getInstance().clearApplicationData();
        //Because we have User and RideList singletons, by definition there is only one of each
        //in the UserController base class.  These local variable will be passed in to simulate
        //data on different devices.
        User rider, driver1, driver2;
        RideList riderRequested, riderAccepted, driver1Requested, driver1Accepted,
                driver2Requested, driver2Accepted;

        //Log each user in so they have an account on the server
        MockUserController ucRider = new MockUserController(getActivity().getApplicationContext());
        try {
            ucRider.newUserLogin("rider", "John", "john@yo.com", "123-4567", "123 main");
        } catch(Exception e) {
            fail("Failed to log in the rider");
        }
        rider = ucRider.getCurrentUser();
        riderRequested = ucRider.getRequestedRides();
        riderAccepted = ucRider.getAcceptedRides();

        MockUserController ucDriver1 = new MockUserController(getActivity().getApplicationContext());
        try {
            ucDriver1.newUserLogin("driver1", "Sally", "email", "phone", "address");
        } catch(Exception e) {
            fail("Failed to log in driver1");
        }
        driver1 = ucDriver1.getCurrentUser();
        driver1Requested = ucDriver1.getRequestedRides();
        driver1Accepted = ucDriver1.getAcceptedRides();

        MockUserController ucDriver2 = new MockUserController(getActivity().getApplicationContext());
        try {
            ucDriver2.newUserLogin("driver2", "Fred", "email", "phone", "address");
        } catch(Exception e) {
            fail("Falied to log in driver2");
        }
        driver2 = ucDriver2.getCurrentUser();
        driver2Requested = ucDriver2.getRequestedRides();
        driver2Accepted = ucDriver2.getAcceptedRides();

        String rideDescription = "I need a ride to West Ed!!";
        LatLng startLocation = new LatLng(53.526495, -113.630327);
        LatLng endLocation = new LatLng(53.526495, -113.630327);
        Number fare = 2.75;
        Ride myRide = new Ride(startLocation, endLocation, fare, rideDescription, rider);


        //Rider requests a ride
        ucRider.inputData(rider, riderRequested, riderAccepted);
        ucRider.addRideRequest(myRide);

        //Wait for the server to process the ride, and set the ID locally
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("Timer was interrupted");
        }

        //Driver accepts that ride
        ucDriver1.inputData(driver1, driver1Requested, driver1Accepted);
        ucDriver1.addAcceptedRequest(myRide);

        //Wait just in case
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("Timer was interrupted");
        }

        //Second driver accepts same ride
        ucDriver2.inputData(driver2, driver2Requested, driver2Accepted);
        ucDriver2.addAcceptedRequest(myRide);

        //Wait...
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("Timer was interrupted");
        }

        //Rider confirms the first driver's acceptance
        ucRider.inputData(rider, riderRequested, riderAccepted);
        try {
            ucRider.confirmDriverAcceptance(myRide, driver1);
        } catch(DriverNotInListException e) {
            fail("Driver was not in the list");
        }

        //Wait for changes on the server
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("Timer was interrupted");
        }

        //Check if rider's local ride is confirmed
        assertFalse("There is already a query in progress", ucRider.queryInProgress);
        //This is where the exception is thrown.  Watching the variables in the debugger,
        //ucRider.getRequestedRides() returns a RideList which HAS an List<Ride> of length 1
        //but ucRider.getRequestedRides().getRides(), which should return that exact List<Ride>,
        //returns an empty list.  I cannot figure out why.  I'm sure the code works, but this test
        //is acting funny.
        assertEquals("Local ride shows the wrong status", Ride.CONFIRMED,
                ucRider.getRequestedRides().getRides().get(0).getStatus());
        assertTrue("Query finished before we checked local data", ucRider.queryInProgress);

        //Check if, on the server, the ride is shown as accepted
        while(ucRider.queryInProgress) {
            //Wait for query to complete
            Log.v("testConfirmRideRequest", "Waiting for the query to complete..");
        }
        assertEquals("Ride shows the wrong status on the server", Ride.CONFIRMED,
                ucRider.getRequestedRides().getRides().get(0).getStatus());

        //Check if the rider's savefile shows the change
        ucRider.loadFromFile();
        assertEquals("Local ride shows the wrong status", Ride.CONFIRMED,
                ucRider.getRequestedRides().getRides().get(0).getStatus());

        ucDriver1.inputData(driver1, driver1Requested, driver1Accepted);
        //Check that the ride ID is still in the first driver's list
        ESQueryListener queryListener = new ESQueryListener();
        new ElasticSearchUserController.GetUsersByIdTask(queryListener)
                .execute(driver1.getId());
        while(queryListener.getResults() == null) {
            //wait for query to complete
        }

        User retrievedDriver = (User) queryListener.getResults().get(0);
        assertTrue("Confirmed ride is not in first driver's list anymore",
                retrievedDriver.getAcceptedRideIDs().contains(myRide.getId()));

        //Check if, on the server, the ride ID is removed from the second driver's list
        ucDriver2.inputData(driver2, driver2Requested, driver2Accepted);
        queryListener = new ESQueryListener();
        new ElasticSearchUserController.GetUsersByIdTask(queryListener)
                .execute(driver2.getId());
        while(queryListener.getResults() == null) {
            //wait for query to complete
        }

        retrievedDriver = (User) queryListener.getResults().get(0);
        assertFalse("Confirmed ride is still in second driver's list",
                retrievedDriver.getAcceptedRideIDs().contains(myRide.getId()));

    }
}
