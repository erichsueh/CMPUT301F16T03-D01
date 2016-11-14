package com.example.ehsueh.appygolucky;

public class Location {
    Double lat;
    Double lon;
    Location(Double newLat, Double newLon){
        lat = newLat;
        lon = newLon;
    }
    public void setLat(Double newLat){
        lat = newLat;
    }
    public void setLon(Double newLon){
        lon = newLon;
    }

    //Getters
    public Double getLat(){
        return lat;
    }
    public Double getLon(){
        return lon;
    }

}
