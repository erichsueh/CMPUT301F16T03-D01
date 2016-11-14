package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by Corey on 2016-10-14.
 *
 * US 05.02.01 As a driver, I want to view a list of things I have accepted that are pending,
 *             each request with its description, and locations.
 * US 05.03.01 As a driver, I want to see if my acceptance was accepted.
 *
 * Related tests include:
 * <ul>
 *     <li>Retrieve list of accepted rides per driver</li>
 *     <li>Retrieve information about whether a rider has accepted a driver's acceptance.</li>
 * </ul>
 */

public class UC050201 {

    @Test
    public void testViewAcceptedRequests() {
        Driver myDriver = new Driver();
        String rideDescription = "I need a ride to West Ed!!";
        LatLng startLocation = new LatLng(53.526495, -113.630327);
        LatLng endLocation = new LatLng(53.526495, -113.630327);
        Number fare = 2.75;
        User rider = new User("rider", "John", "john@yo.com", "123-4567", "123 main");
        Ride myRide = new Ride(startLocation, endLocation, fare, rideDescription, rider);
        RideController rc = new RideController();
        rc.addRide(myRide);

        assertEquals(0, rc.getRidesAcceptedByDriver(myDriver).size());

        rc.driverAcceptsRide(myDriver, myRide);
        assertEquals("Acceptance unsuccessful", 0, rc.getRidesAcceptedByDriver(myDriver));
    }

    @Test
    public void testRiderAcceptance() {
        //TODO: maybe also add exception if system tries to let
        // the wrong rider accept a driver offer
        Driver myDriver = new Driver();
        Rider myRider = new Rider();
        String rideDescription = "I need a ride to West Ed!!";
        LatLng startLocation = new LatLng(53.526495, -113.630327);
        LatLng endLocation = new LatLng(53.526495, -113.630327);
        Number fare = 2.75;
        User rider = new User("rider", "John", "john@yo.com", "123-4567", "123 main");
        Ride myRide = new Ride(startLocation, endLocation, fare, rideDescription, rider);        RideController rc = new RideController();
        rc.addRide(myRide);

        assertFalse(rc.rideAcceptedByRider(myRide));

        //Rider shouldn't be able to accept a driver's offer, if that driver hasn't
        //accepted the ride request
        try {
            rc.riderAcceptsDriverOffer(myRider, myRide, myDriver);
            fail("Failed to throw acception");
        }
        catch (Exception e) {}

        //Driver accepts, then rider should be able to accept that offer
        rc.driverAcceptsRide(myDriver, myRide);
        rc.riderAcceptsDriverOffer(myRider, myRide, myDriver);
        assertTrue(rc.rideAcceptedByRider(myRide));

    }
}
