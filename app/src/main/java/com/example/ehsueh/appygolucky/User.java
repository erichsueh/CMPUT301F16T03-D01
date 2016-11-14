package com.example.ehsueh.appygolucky;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.annotations.JestId;

/**
 * Created by Corey on 2016-10-13.
 */

public class User {
    @JestId
    private String id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    private ArrayList<Ride> requestedRides;
    private ArrayList<Ride> acceptedRides;

    public User(String username, String name, String email, String phone, String address) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.id = null;
        this.requestedRides = new ArrayList<Ride>();
        this.acceptedRides = new ArrayList<Ride>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public List<String> getContactInfo() { return null; }

    public List<Ride> getRideRequests() { return null; }

    public List<Ride> getAcceptedRides() { return null; }

    public void setUsername() {}

    public void setName(String name) {}

    public static void setEmail(String mail) {
        email = mail;
    }

    public static void setPhone(String phonenumber) {
        phone = phonenumber;
    }

    public static void setAddress(String adress) {
        address = adress;
    }

    public void addRideRequest(Ride rideRequest) {
        this.requestedRides.add(rideRequest);
    }

    public void addAcceptedRequest(Ride acceptedRequest) {
        this.acceptedRides.add(acceptedRequest);
    }

}
