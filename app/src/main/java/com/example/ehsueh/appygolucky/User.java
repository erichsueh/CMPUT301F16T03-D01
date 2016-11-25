package com.example.ehsueh.appygolucky;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 *
 */

//NOTE: Methods that add data (setters etc.) should rarely be used directly.
//UserController should be used instead, as it deals with saving.

public class User {
    @JestId
    private String id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    private ArrayList<String> requestedRideIDs;
    private ArrayList<String> acceptedRideIDs;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param name     the name
     * @param email    the email
     * @param phone    the phone
     * @param address  the address
     */
    public User(String username, String name, String email, String phone, String address) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.id = null;
        this.requestedRideIDs = new ArrayList<String>();
        this.acceptedRideIDs = new ArrayList<String>();
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }


    /**
     * Gets username./
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets contact info.
     *
     * @return the contact info
     */
    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAddress() {
        return this.address;
    }

    /**
     * Gets ride requests.
     *
     * @return the ride requests
     */
    public ArrayList<String> getRideRequestIDs() { return requestedRideIDs; }

    /**
     * Gets accepted rides.
     *
     * @return the accepted rides
     */
    public ArrayList<String> getAcceptedRideIDs() { return acceptedRideIDs; }

    /**
     * Sets username.
     */
    public void setUsername() {}

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {}

    public void setEmail(String mail) {
        email = mail;
    }

    public void setPhone(String phonenumber) {
        phone = phonenumber;
    }

    public void setAddress(String adress) {
        address = adress;
    }

    /**
     * Add ride request.
     *
     * @param rideRequestID the ride request ID
     */
    public void addRideRequestID(String rideRequestID) {
        this.requestedRideIDs.add(rideRequestID);
    }

    /**
     * Add accepted request.
     *
     * @param acceptedRequestID the accepted request
     */
    public void addAcceptedRequestID(String acceptedRequestID) {
        this.acceptedRideIDs.add(acceptedRequestID);
    }


}
