package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Corey on 2016-10-13.
 *
 * US 04.01.01 As a driver, I want to browse and search for open requests by geo-location.
 * US 04.02.01 As a driver, I want to browse and search for open requests by keyword.
 *
 * Related tests include:
 * <ul>
 *     <li>Retrieve list of open requests</li>
 *     <li>Retrieve list of open requests by location</li>
 *     <li>Retrieve list of open requests by keyword</li>
 * </ul>
 */

//TODO: this test should use the server
public class UC040101 {

    @Test
    public void testSearchRides() {
        String rideDescriptionA = "I need a ride to West Ed!!";
        Number fareA = 2.75;
        LatLng startLocationA = new LatLng(53.526495, -113.630327);
        LatLng endLocationA = new LatLng(53.526495, -113.630327);
        String rideDescriptionB = "Gotta get to South Common ASAP :)";
        User riderA = new User("usernameA", "nameA", "emailA", "phoneA", "addressA");
        Number fareB = 2.75;
        LatLng startLocationB = new LatLng(53.628964, -113.477291);
        LatLng endLocationB = new LatLng(53.628964, -113.477291);
        User riderB = new User("usernameC", "nameC", "emailC", "phoneC", "addressC");
        String rideDescriptionC = "Ride to Grocery Store";
        Number fareC = 2.75;
        User riderC = new User("usernameC", "nameC", "emailC", "phoneC", "addressC");
        LatLng startLocationC = new LatLng(53.526495, -113.630327);
        LatLng endLocationC = new LatLng(53.526495, -113.630327);

        Ride rideA = new Ride(startLocationA, endLocationA, fareA, rideDescriptionA, riderA);
        Ride rideB = new Ride(startLocationB, endLocationB, fareB, rideDescriptionB, riderB);
        Ride rideC = new Ride(startLocationC, endLocationC, fareC, rideDescriptionC, riderC);

        RideList list = new RideList();

        list.addRide(rideA);
        list.addRide(rideB);
        list.addRide(rideC);

        List<Ride> results = (List<Ride>) list.getRidesByLocation(startLocationC);
        assertEquals("This list is not sorted by location", rideC, results.get(0));

        results = (List<Ride>) list.getRidesByName(rideDescriptionB);
        assertEquals("This list is not sorted by name", rideB, results.get(2));
    }
}
