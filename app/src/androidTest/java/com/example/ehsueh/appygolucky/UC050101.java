package com.example.ehsueh.appygolucky;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Corey on 2016-10-14.
 *
 * US 05.01.01 As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
 *
 * Related tests include:
 *<ul>
 *     <li>Associate driver with a ride request</li>
 *     <li>Try to accept a ride that has already been confirmed</li>
 *</ul>
 */

public class UC050101 extends ActivityInstrumentationTestCase2 {
    public UC050101() {
        super(MainActivity.class);
    }

    private class MockUserController extends UserController{
        public MockUserController(Context context, User user) {
            super(context);
            currentUser = user;
            super.currentUser = user;
        }
    }

    public void testAcceptRideRequest() {
        Datacleaner.getInstance().clearApplicationData();

        User rider = new User("rider", "John", "john@yo.com", "123-4567", "123 main");
        User driver = new User("driver", "Sally", "email", "phone", "address");
        MockUserController ucRider = new MockUserController(getActivity().getApplicationContext(),
                rider);


        String rideDescription = "I need a ride to West Ed!!";
        LatLng startLocation = new LatLng(53.526495, -113.630327);
        LatLng endLocation = new LatLng(53.526495, -113.630327);
        Number fare = 2.75;
        Ride myRide = new Ride(startLocation, endLocation, fare, rideDescription, rider);

        ucRider.addRideRequest(myRide);

        //Wait for the ride to be added to the server, and to receive its ID
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("Timer interrupted");
        }

        MockUserController ucDriver = new MockUserController(getActivity().getApplicationContext(),
                driver);


        ucDriver.addAcceptedRequest(myRide);

        //Test that the ride is added to the driver's list of accepted rides
        List<Ride> acceptedRides = ucDriver.getAcceptedRides().getRides();
        assertTrue("RideList didn't contain the ride", acceptedRides.contains(myRide));

        List<String> acceptedRideIDs = ucDriver.getAcceptedRideIDs();
        assertTrue("Ride ID isn't in the driver's list", acceptedRideIDs.contains(myRide.getId()));

        //Test that the Driver's username is saved to the ride's list of drivers
        List<String> driverUsernames = myRide.getDriverUsernames();
        assertTrue("Driver username was not saved to ride",
                driverUsernames.contains(driver.getUsername()));

        //Test that the ride ID is saved to the driver's list on file
        ucDriver.loadFromFile();

        acceptedRides = ucDriver.getAcceptedRides().getRides();
        assertEquals("This list has the wrong number of elements", 1, acceptedRides.size());
        assertTrue("RideList on file didn't contain the ride",
                acceptedRides.get(0).getId().equals(myRide.getId()));

        acceptedRideIDs = ucDriver.getAcceptedRideIDs();
        assertTrue(acceptedRideIDs.contains(myRide.getId()));

        //Test that the driver ID is saved to the ride's list on file
        driverUsernames = myRide.getDriverUsernames();
        assertTrue("Driver username was not saved to ride on file",
                driverUsernames.contains(driver.getUsername()));

        //Test that the ride ID is saved to the driver's list on the server
        ESQueryListener queryListener = new ESQueryListener();
        ElasticSearchUserController.GetUserByUsernameTask getUserTask =
                new ElasticSearchUserController.GetUserByUsernameTask(queryListener);
        getUserTask.execute(driver.getUsername());

        while(queryListener.getResults() == null) {
            //Wait...
        }

        User retrievedUser = (User) queryListener.getResults().get(0);
        assertEquals("Got the wrong number of ride Ids from driver's list on the server",
                1, retrievedUser.getAcceptedRideIDs().size());
        assertTrue("The ride id was not in the driver's list on the server",
                retrievedUser.getAcceptedRideIDs().get(0).equals(myRide.getId()));

        //Test that the driver ID is saved to the ride's list on the server
        queryListener = new ESQueryListener();
        ElasticSearchRideController.GetRidesByIdTask getRidesByIdTask =
                new ElasticSearchRideController.GetRidesByIdTask(queryListener);
        getRidesByIdTask.execute(myRide.getId());

        while(queryListener.getResults() == null) {
            //Wait...
        }

        Ride retrievedRide = (Ride) queryListener.getResults().get(0);
        assertEquals("Wrong number of driver Ids in the ride on the server", 1,
                retrievedRide.getDriverUsernames().size());
        assertTrue("Got the wrong driver username on the server",
                retrievedRide.getDriverUsernames().get(0).equals(driver.getUsername()));

    }
}
