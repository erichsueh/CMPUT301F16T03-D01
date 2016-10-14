package com.example.ehsueh.appygolucky;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Corey on 2016-10-13.
 */

public class Driver extends User {
    Collection<Ride> acceptedRides;
    public Driver() {
        //TODO: replace this with something useful
        super(null, null, null, null);
        this.acceptedRides = new ArrayList<Ride>();
    }

    public void addRide() {}

    public Collection<Ride> getAcceptedRides() {
        return null;
    }
}
