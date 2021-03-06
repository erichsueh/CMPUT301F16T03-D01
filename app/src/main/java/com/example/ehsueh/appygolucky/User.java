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
    private String rideDescription;
    private Integer rating;
    private Integer timesrated;
    private ArrayList<String> requestedRideIDs;
    private ArrayList<String> acceptedRideIDs;
    private boolean notification;

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
        rating = 0;
        timesrated = 0;
        notification = false;
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
     * @param name the name*/
    public void setName(String name) { this.name = name; }

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

    public void deleteRideRequestID(Ride ride) {
        requestedRideIDs.remove(ride.getId());
    }


    /**
     * Add accepted request.
     *
     * @param acceptedRequestID the accepted request
     */
    public void addAcceptedRequestID(String acceptedRequestID) {
        this.acceptedRideIDs.add(acceptedRequestID);
    }

    public void removeAcceptedRequestID(String requestIDToRemove) {
        this.acceptedRideIDs.remove(requestIDToRemove);
    }

    public Boolean getNotification(){return notification;}

    public void setNotification(Boolean notify){notification = notify;}

    public String getRideDescription() {
        return rideDescription;
    }

    public void setRideDescription(String description) {
        rideDescription = description;
    }

    public void updateRating(int progress) {
        timesrated = timesrated +1;
        rating = (rating + progress)/timesrated;
    }

    public Integer getRating() {
        return rating;
    }

}

