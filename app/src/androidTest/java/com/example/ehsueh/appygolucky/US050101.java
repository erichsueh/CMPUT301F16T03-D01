package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Collection;

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

public class US050101 extends ActivityInstrumentationTestCase2 {
    public US050101() {
        super(MainActivity.class);
    }

    public void testAcceptRideRequest() {
        Driver myDriver = new Driver();
        Ride myRide = new Ride("Take me to the game", "my hour", "Rogers Place");

        //When the driver accepts a request, he id added to the request
        myRide.addDriver(myDriver);
        Collection<Driver> drivers = myRide.getDrivers();

        assertTrue("Driver is missing from list", drivers.contains(myDriver));
    }
}
