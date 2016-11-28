package com.example.ehsueh.appygolucky;

/**
 * The type Point.
 */
public class Point {
    private Location location;

    /**
     * Instantiates a new Point.
     *
     * @param lat the lat
     * @param lon the lon
     */
    Point(Double lat, Double lon){
        location = new Location(lat,lon);
    }


    /**
     * Set lat.
     *
     * @param newLat the new lat
     */
// Setters
    public void setLat(Double newLat){
        location.setLat(newLat);
    }

    /**
     * Set lon.
     *
     * @param newLon the new lon
     */
    public void setLon(Double newLon){
        location.setLon(newLon);
    }

    /**
     * Get lat double.
     *
     * @return the double
     */
//Getters
    public Double getLat(){
        return location.getLat();
    }

    /**
     * Get lon double.
     *
     * @return the double
     */
    public Double getLon(){
        return location.getLon();
    }

}
