package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import java.sql.*;
import java.sql.Driver;
import java.util.Collection;


/**
 * Created by Maxwell on 2016-10-11.
 * User Story US 01.05.01 - As a rider, I want to be able to phone or email the driver who
 * accepted a request.
 * Related tests for this use case include:
 * <ul>
 *     <li>The "ride request" item should have a list of drivers who accepted the ride request.
 *     From this list I should be able to see their contact information (email address and phone #.
 *     This means that each ride request item should have a list of "potential driver" items that
 *     are viewable by me the rider.
 *     </li>
 * </ul>
 *
 */

public class US010501 extends ActivityInstrumentationTestCase2 {
    public US010501 () {super(MainActivity.class); }

    DriversWhoHaveAcceptedList driversWhoHaveAcceptedList = new DriversWhoHaveAcceptedList();
    Collection<Driver> Drivers = driversWhoHaveAcceptedList.getDrivers();
}
