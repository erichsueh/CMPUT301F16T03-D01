package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

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

public class UC010401 {

    @Test
    public void rideListTest() {
        RideList rideList = new RideList();
        Collection<Ride> Rides = rideList.getRides();
        String rideDescriptionA = "I need a ride to West Ed!!";
        Number fareA = 2.75;
        LatLng startLocationA = new LatLng(53.526495, -113.630327);
        LatLng endLocationA = new LatLng(53.526495, -113.630327);
        String rideDescriptionB = "Gotta get to South Common ASAP :)";
        User riderA = new User("usernameA", "nameA", "emailA", "phoneA", "addressA");
        Number fareB = 2.75;
        LatLng startLocationB = new LatLng(53.526495, -113.630327);
        LatLng endLocationB = new LatLng(53.526495, -113.630327);
        User riderB = new User("usernameC", "nameC", "emailC", "phoneC", "addressC");
        String rideDescriptionC = "Ride to Grocery Store";
        Number fareC = 2.75;
        User riderC = new User("usernameC", "nameC", "emailC", "phoneC", "addressC");
        LatLng startLocationC = new LatLng(53.526495, -113.630327);
        LatLng endLocationC = new LatLng(53.526495, -113.630327);

        Ride rideA = new Ride(startLocationA, endLocationA, fareA, rideDescriptionA, riderA);
        Ride rideB = new Ride(startLocationB, endLocationB, fareB, rideDescriptionB, riderB);
        Ride rideC = new Ride(startLocationC, endLocationC, fareC, rideDescriptionC, riderC);

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
