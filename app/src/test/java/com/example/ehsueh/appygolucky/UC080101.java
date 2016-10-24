package com.example.ehsueh.appygolucky;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Vinson on 2016-10-13.
 * US 08.01.01 As an driver, I want to see requests that I already accepted while offline.
 * Related tests for this use case include:
 * <ul>
 *     <li>As a Driver, I want to see the requests that I already accepted while offline</li>
 * </ul>
 */

public class UC080101 {
    /**
     * Test the if the app is storing the request locally, and presenting in the list correctly when offline
     */
   /** MainActivity mactivity = new MainActivity();
    @Test
    //references from UC010201.rideListTest()
    public void offlineridelistTest() {
        //making sure the network is offline
        assertFalse(mactivity.isNetworkAvailable());
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
    }
    */

}
