package com.example.ehsueh.appygolucky;

/**
 * Created by Maxwell on 2016-10-11.
 */
public class Ride {
    private String rideName;
    private String startLocation;
    private String endLocation;

    public Ride(String rideName, String startLocation, String endLocation) {
        this.rideName = rideName;
        this.startLocation = startLocation;
        this.endLocation = endLocation;

    }

    public String getRideName() { return this.rideName; }
    public String getStartLocation() { return this.startLocation; }
    public String getEndLocation() { return this.endLocation; }


}
