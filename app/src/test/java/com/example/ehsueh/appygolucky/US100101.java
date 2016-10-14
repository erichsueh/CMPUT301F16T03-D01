package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

/**
 * Created by Eric Shay on 2016-10-14.
 * User Story:
 * As a rider, I want to specify a start and end geo locations on a map for a request.
 *
 * Checks if the location is saved within the map class
 * then checks if the info is correct
 * THen resets the map start and end location
 * and check to make sure its the same as the new set location
 */

public class US100101 extends TestCase {

    public void SpecifyStartandEndLocation(){

        Map themap = new Map("-100.29N,311.23E","-110.23N,330.34E");
        assertTrue(themap.getStart()=="-100.29N,311.23E");
        assertTrue(themap.getEnd()=="-110.23N,330.34E");

        themap.setstartlocation("555.99N,676.34E");
        themap.setendlocation("557.95N,776.35E");
        assertTrue(themap.getStart()=="555.99N,676.34E");
        assertTrue(themap.getEnd()=="557.95N,776.35E");
    }
}
