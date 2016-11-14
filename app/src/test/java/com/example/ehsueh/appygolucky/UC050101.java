package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Corey on 2016-10-14.
 *
 * US 05.01.01 As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
 *
 * Related tests include:
 *<ul>
 *     <li>Associate driver with a ride request</li>
 *</ul>
 */

public class UC050101 {

    @Test
    public void testAcceptRideRequest() {
        User driver = new User("username", "name", "email", "phone", "address");
        String rideDescription = "I need a ride to West Ed!!";
        LatLng startLocation = new LatLng(53.526495, -113.630327);
        LatLng endLocation = new LatLng(53.526495, -113.630327);
        Number fare = 2.75;
        User rider = new User("rider", "John", "john@yo.com", "123-4567", "123 main");
        Ride myRide = new Ride(startLocation, endLocation, fare, rideDescription, rider);
        //When the driver accepts a request, he id added to the request
        myRide.addDriver(driver);
        Collection<Driver> drivers = myRide.getDrivers();

        assertTrue("Driver is missing from list", drivers.contains(driver));
    }
}
