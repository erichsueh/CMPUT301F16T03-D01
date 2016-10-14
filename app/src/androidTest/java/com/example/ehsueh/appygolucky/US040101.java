package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Collection;
import java.util.List;

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

public class US040101 extends ActivityInstrumentationTestCase2 {
    public US040101() {
        super(MainActivity.class);
    }

    public void testSearchRides() {
        String rideName1 = "I need a ride to West Ed!!";
        String startLocation1 = "Sherlock's on campus";
        String endLocation1 = "West Edmonton Mall";
        Ride ride1 = new Ride(rideName1, startLocation1, endLocation1);

        String rideName2 = "too drunk to drive";
        String startLocation2 = "the bar";
        String endLocation2 = "home";
        Ride ride2 = new Ride(rideName2, startLocation2, endLocation2);

        String rideName3 = "I want to see the game!";
        String startLocation3 = "My house";
        String endLocation3 = "Rogers place";
        Ride ride3 = new Ride(rideName3, startLocation3, endLocation3);

        RideList list = new RideList();

        list.addRide(ride1);
        list.addRide(ride2);
        list.addRide(ride3);

        List<Ride> results = (List<Ride>) list.getRidesByLocation(startLocation3);
        assertEquals("This list is not sorted by location", ride3, results.get(0));

        results = (List<Ride>) list.getRidesByName(rideName2);
        assertEquals("This list is not sorted by name", ride2, results.get(2));
    }
}
