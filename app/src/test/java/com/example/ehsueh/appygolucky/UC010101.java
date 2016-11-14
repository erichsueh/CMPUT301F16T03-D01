package com.example.ehsueh.appygolucky;


import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

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

public class UC010101 {

    @Test
    public void createRideTest() {
        String rideDescription = "I need a ride to West Ed!!";
        LatLng startLocation = new LatLng(53.526495, -113.630327);
        LatLng endLocation = new LatLng(53.526495, -113.630327);
        Number fare = 2.75;
        User rider = new User("rider", "John", "john@yo.com", "123-4567", "123 main");
        Ride ride = new Ride(startLocation, endLocation, fare, rideDescription, rider);
        assertTrue("Ride name is not correct", rideDescription.equals(ride.getDescription()));
        assertTrue("Ride start is not correct", startLocation.equals(ride.getStartLocation()));
        assertTrue("Ride end is not correct", endLocation.equals(ride.getEndLocation()));
    }

}
