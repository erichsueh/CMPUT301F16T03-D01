package com.example.ehsueh.appygolucky;

import java.sql.Driver;
import java.util.Collection;

/**
 * Created by Maxwell on 2016-10-11.
 * User Story US 01.03.01 - As a rider, I want to be notified if my request is accepted.
 * Related tests for this use case include:
 * <ul>
 *     <li>A component of each "ride object" should be a list of drivers who have accepted my
 *     request</li>
 *     <li>If this list is larger than size 0 then my ride request should provide some visual
 *     indication of such</li>
 * </ul>
 *
 */

public class US010301 {
    DriversWhoHaveAcceptedList driversWhoHaveAcceptedList = new DriversWhoHaveAcceptedList();
    Collection<Driver> Drivers = driversWhoHaveAcceptedList.getDrivers();

}
