package com.example.ehsueh.appygolucky;


import org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by Maxwell on 2016-10-11.
 * User Story US 01.01.01 - As a rider, I want to request rides between two locations.
 * Related tests for this use case include:
 * <ul>
 *     <li>I want to create a ride request</li>
 *     <li>I want to be able specify the starting place and destination</li>
 * </ul>
 *
 */

public class US010101{
    public US010101() {
        super();
    }

    @Test
    public void createRideTest() {
        String rideName = "I need a ride to West Ed!!";
        String startLocation = "Sherlock's on campus";
        String endLocation = "West Edmonton Mall";
        Ride ride = new Ride(rideName, startLocation, endLocation);
        assertTrue("Ride name is not correct", rideName.equals(ride.getRideName()));
        assertTrue("Ride start is not correct", rideName.equals(ride.getStartLocation()));
        assertTrue("Ride end is not correct", rideName.equals(ride.getEndLocation()));
    }

}
