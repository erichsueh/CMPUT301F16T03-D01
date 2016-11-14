package com.example.ehsueh.appygolucky;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by Maxwell on 2016-10-11.
 *
 */
public class Ride implements Parcelable {
    private String rideName;
//    private String startLocation;
//    private String endLocation;
    private Integer status;
    private Collection<Driver> drivers;
    Boolean finalAcceptance;


//    public Ride(String rideName, String startLocation, String endLocation) {
//        this.rideName = rideName;
//        this.startLocation = startLocation;
//        this.endLocation = endLocation;
//        this.status = 0;
//    }
    //////////////////////////////////////////////////////////////////////////////

    private ArrayList<User> acceptedDrivers;
    private User confirmedDriver;
    private User rider;
    private Point startLocation;
    private Point endLocation;

    private Number fare;
    private Double distance;
    private long timeCreatedInMillis;
    private Number requestId;
    private boolean accepted;
    private boolean completed;
    private boolean paymentReceived;

    @JestId
    private String id;

    public Ride(LatLng start, LatLng end, Number fare, User rider){
        this.startLocation = new Point(start.latitude,start.longitude);
        this.endLocation = new Point(end.latitude,end.longitude);
        this.fare = fare;
        this.rider = rider;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
        //RequestController.addOpenRequest(this);
        this.id = null;
        acceptedDrivers = new ArrayList<User>();

    }

    public Ride(LatLng start, LatLng end, Number fare, User rider, Double distance) {
        this.startLocation = new Point(start.latitude,start.longitude);
        this.endLocation = new Point(end.latitude,end.longitude);
        this.fare = fare;
        this.rider = rider;
        this.distance = distance;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
        //RequestController.addOpenRequest(this);
        this.id = null;
        acceptedDrivers = new ArrayList<User>();

    }

//    public void clearAcceptedDrivers(){
//        acceptedDrivers = new ArrayList<User>();
//    }

    // Getters
    public User getConfirmedDriver() {
        return this.confirmedDriver;
    }

    public ArrayList<User> getAcceptedDrivers() {
        return this.acceptedDrivers;
    }
    public User getRider() {
        return rider;
    }
    public LatLng getEndLocation() {
        return new LatLng(this.endLocation.getLat(),this.endLocation.getLon());
    }
    public Number getFare() {
        return fare;
    }
    public LatLng getStartLocation() {
        return new LatLng(this.startLocation.getLat(),this.startLocation.getLon());
    }
    public boolean isCompleted() {
        return completed;
    }
    public boolean sentNotification() {
        return true;
    }
    public long getTimeCreatedInMillis() {
        return this.timeCreatedInMillis;
    }

    public String getDateString() {
        return new Date(this.timeCreatedInMillis).toString();
    }

    public Double getDistance() {
        return this.distance;
    }

    public String getId() {
        return id;
    }



    public String getRideName() { return this.rideName; }
    public Integer getStatus() { return this.status; }
    public Collection<Driver> getDrivers() {return null;}
    public Boolean acceptedByRider() {return null;}
    public Boolean acceptedByDriver(Driver driver) {return Boolean.FALSE;}

    public void setStatus(Integer newStatus) {
        this.status = newStatus;
    }
    public void addDriver(Driver driver) {}
    public void riderAccepts() {}

    // setters

    public void setId(String id) {
        this.id = id;
    }

    public void setConfirmedDriver(User d) {
        this.confirmedDriver = d;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation.setLat(startLocation.latitude);
        this.startLocation.setLon(startLocation.longitude);

    }
    public void setEndLocation(LatLng endLocation) {
        this.endLocation.setLat(endLocation.latitude);
        this.endLocation.setLon(endLocation.longitude);
    }
    public void setPaymentReceived(boolean paymentRecived) {
        this.paymentReceived = paymentRecived;
    }
    public void setFare(Number fare) {
        this.fare = fare;
    }
    public void setAcceptedStatus(Boolean bool){
        accepted = bool;
    }
    public void setCompletedStatus(boolean completed) {
        this.completed = completed;
    }
    public boolean isPaymentRecived() {
        return paymentReceived;
    }
    public boolean hasConfirmedRider() {
        return this.confirmedDriver != null;
    }


    /* Parcelable Stuff */

    /**
     * Writes data to the inputted parcel
     * http://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
     *
     * @param out : Parcel
     * @param flags : int
     */
    public void writeToParcel(Parcel out, int flags) {
        //out.writeParcelable(startLocation, flags);
        //out.writeParcelable(endLocation, flags);
        out.writeInt((int)fare);
        out.writeLong(timeCreatedInMillis);
        out.writeInt((int)requestId);
        out.writeByte((byte)(accepted?1:0));
        out.writeByte((byte)(completed?1:0));
        out.writeByte((byte)(paymentReceived?1:0));
    }

    /**
     * Creator to create Ride based on a parcel
     *
     * @param in : Parcel
     */
    public Ride(Parcel in) {
        startLocation = in.readParcelable(LatLng.class.getClassLoader());
        endLocation = in.readParcelable(LatLng.class.getClassLoader());
        fare = in.readInt();
        timeCreatedInMillis = in.readLong();
        requestId = in.readInt();
        accepted = in.readByte() != 0;
        completed = in.readByte() != 0;
        paymentReceived = in.readByte() != 0;
    }

    /**
     * create rides from a parcel
     *
     */
    public static final Parcelable.Creator<Ride> CREATOR =
            new Parcelable.Creator<Ride>() {
                public Ride createFromParcel(Parcel in) {
                    return new Ride(in);
                }

                public Ride[] newArray(int size) {
                    return new Ride[size];
                }
            };

    public int describeContents() {
        // Not sure what goes here
        return 0;
    }



    public String toString() {
        String temp = "Rider: " + this.getRider() + "\n";
        if (this.getConfirmedDriver() != null) {
            temp = temp + "Confirmed Driver: " + this.getConfirmedDriver() + "\n";
        }
        if (this.getDistance() != null) {
            temp = temp + "Distance: " + this.getDistance().toString() + "\n";
        }
        temp = temp + "Fare: " + this.getFare() + "\n" + "Created on: " +
                this.getDateString() + "\n";
        return temp;
    }
}
