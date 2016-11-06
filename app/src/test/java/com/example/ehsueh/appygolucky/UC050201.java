package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

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

public class UC050201 extends ActivityInstrumentationTestCase2 {
    public UC050201() {
        super(MainActivity.class);
    }

    public void testViewAcceptedRequests() {
        Driver myDriver = new Driver();
        Ride myRide = new Ride("Take me to the game", "my hour", "Rogers Place");
        RideController rc = new RideController();
        rc.addRide(myRide);

        assertEquals(0, rc.getRidesAcceptedByDriver(myDriver).size());

        rc.driverAcceptsRide(myDriver, myRide);
        assertEquals("Acceptance unsuccessful", 0, rc.getRidesAcceptedByDriver(myDriver));
    }

    public void testRiderAcceptance() {
        //TODO: maybe also add exception if system tries to let
        // the wrong rider accept a driver offer
        Driver myDriver = new Driver();
        Rider myRider = new Rider();
        Ride myRide = new Ride("Take me to the game", "my hour", "Rogers Place");
        RideController rc = new RideController();
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
