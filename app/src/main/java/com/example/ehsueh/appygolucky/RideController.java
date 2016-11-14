package com.example.ehsueh.appygolucky;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;

/**
 * Created by Corey on 2016-10-14.
 */

public class RideController {
    private Collection<Ride> rides;

    public RideController() {}

    public void addRide(LatLng start, LatLng end, Number fare, User rider) {}

    public void addRide(Ride ride) {}

    //May want to deal with driver acceptance differently later.
    public void driverAcceptsRide(Driver driver, Ride ride) {}

    public void riderAcceptsDriverOffer(Rider rider, Ride ride, Driver driver) {}

    public Collection<Ride> getRidesByName(String name) {return null;}

    public Collection<Ride> getRidesByLocation(String startLocation) {return null;}

    public Collection<Ride> getRidesAcceptedByDriver(Driver driver) {return null;}

    public Boolean rideAcceptedByRider(Ride ride) {return Boolean.FALSE;}
}
