package com.example.ehsueh.appygolucky;


import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Eric Shay on 2016-10-14.
 * User Story:
 * As a driver, I want to view start and end geo locations on a map for a request.
 * Creates a new map then tests that all the geo locations are still in the class and entered correctly.
 */

public class UC100201 {

    @Test
    public void ShowMapGeo() {
        Map themap = new Map("-100.29N,311.23E", "-110.23N,330.34E");
        assertTrue(themap.getStart() == "-100.29N,311.23E");
        assertTrue(themap.getEnd() == "-110.23N,330.34E");
    }


}
