package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Collection;

import static junit.framework.Assert.*;

/**
 * Created by Maxwell on 2016-10-11.
 * User Story US 01.04.01 - As a rider, I want to cancel requests.
 * Related tests for this use case include:
 * <ul>
 *     <li>I want to be able to call a delete function to remove a ride from the list of "my
 *     ride requests"</li>
 * </ul>
 *
 * This test will likely be combined at some future point in time with a more general test of the
 * rideList structure.
 */

public class UC010401 extends ActivityInstrumentationTestCase2 {
    public UC010401() {super(MainActivity.class); }

    public void rideListTest() {
        RideList rideList = new RideList();
        Collection<Ride> Rides = rideList.getRides();
        String rideNameA = "I need a ride to West Ed!!";
        String startLocationA = "Sherlock's on campus";
        String endLocationA = "West Edmonton Mall";
        String rideNameB = "Gotta get to South Common ASAP :)";
        String startLocationB = "Sherlock's on campus";
        String endLocationB = "South Common";
        String rideNameC = "Ride to Grocery Store";
        String startLocationC = "116 St & 85 Ave, Edmonton";
        String endLocationC = "5728 111 St NW,";

        Ride rideA = new Ride(rideNameA, startLocationA, endLocationA);
        Ride rideB = new Ride(rideNameB, startLocationB, endLocationB);
        Ride rideC = new Ride(rideNameC, startLocationC, endLocationC);
        rideList.addRide(rideA);
        rideList.addRide(rideB);
        rideList.addRide(rideC);

        assertTrue("This ride-list contains three elements!", Rides.size() == 3);
        assertTrue("The rides are in the list", Rides.contains(rideA));
        assertTrue("The rides are in the list", Rides.contains(rideB));
        assertTrue("The rides are in the list", Rides.contains(rideC));

        rideList.deleteRide(rideA);
        rideList.deleteRide(rideB);
        rideList.deleteRide(rideC);

        assertFalse("This ride-list contains three elements!", Rides.size() == 3);
        assertFalse("The rides are in the list", Rides.contains(rideA));
        assertFalse("The rides are in the list", Rides.contains(rideB));
        assertFalse("The rides are in the list", Rides.contains(rideC));
    }
}
