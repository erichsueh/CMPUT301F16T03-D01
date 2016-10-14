package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Collection;

/**
 * Created by Maxwell on 2016-10-11.
 * User Story US 01.06.01 - As a rider, I want to see current requests I have open.
 *
 * This test will be expanded to ensure we are testing for a specific user and not just if the
 * rideList will work in general.
 *
 */

public class US010601 extends ActivityInstrumentationTestCase2 {
    public US010601 () {super(MainActivity.class); }

    public void testGivenUsersRideRequests() {
        RideList rideList = new RideList();
        Collection<Ride> Rides = rideList.getRides();
    }

}
