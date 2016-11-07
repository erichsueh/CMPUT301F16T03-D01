package com.example.ehsueh.appygolucky;

import java.util.List;

/**
 * Created by Corey on 2016-10-13.
 */

public class User {
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;

    public User(String username, String name, String email, String phone, String address) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
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

    public void setEmail() {}

    public void setPhone() {}

    public void setAddress() {}

    public void addRideRequest() {}

    public void addAcceptedRequest() {}
}
