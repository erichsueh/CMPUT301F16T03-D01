package com.example.ehsueh.appygolucky;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Maxwell on 2016-10-11.
 */
public class RideList {
    private List<Ride> rides;

    public RideList() {
        this.rides = new ArrayList<Ride>();
    }

    public RideList(List<Ride> rides) {
        this.rides = rides;
    }

    public void addRide(Ride ride) {
        this.rides.add(ride);
    }

    /**
     * Sets the internal ride list to a new list.
     * Can be used to update data with data from the server
     * @param rides A new list of rides to store
     */
    public void setRides(List<Ride> rides) { this.rides = rides; }

    public List<Ride> getRides() {
        return this.rides;
    }

    public Ride getRideByID(String ID) {
        for(Ride ride: this.rides) {
            if(ride.getId().equals(ID)) {
                return ride;
            }
        }
        return null;
    }

    public Boolean containsID(String ID) {
        for(Ride ride: this.rides) {
            if(ride.getId().equals(ID)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public int size() {
        return rides.size();
    }

    public void deleteRide(Ride ride) {
        this.rides.remove(ride);
    }
}
