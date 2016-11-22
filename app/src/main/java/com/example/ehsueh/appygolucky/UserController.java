package com.example.ehsueh.appygolucky;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Corey on 2016-11-05.
 * <p>
 * This class handles indexing and querying user data on the server,
 * as well as operations on the currently active user on the app.
 */
public class UserController {

    protected static User currentUser;

    /**
     * Holds a list of actual ride objects associated with the current user.
     */
    protected static RideList requestedRides;

    /**
     * Holds a list of actual ride objects associated with the current user.
     */
    protected static RideList acceptedRides;
    protected Context applicationContext;
    protected final String USERFILENAME = "current_user.json";
    protected final String REQUESTEDRIDESFILENAME = "requested_rides.json";
    protected final String ACCEPTEDRIDESFILENAME = "accepted_rides.json";

    /**
     * Instantiates a new User controller.
     *
     * @param context Context used for file I/O
     */
    public UserController(Context context) {
        applicationContext = context;
    }

    /**
     * Add a new user to to the server, and save to file.
     *
     * @param username the username
     * @param name     the name
     * @param email    the email
     * @param phone    the phone
     * @param address  the address
     */
    public void newUserLogin(String username, String name, String email, String phone, String address)
            throws InterruptedException, ExecutionException {

            User newUser = new User(username, name, email, phone, address);

            //Add the user to the server
            ElasticSearchUserController.AddUsersTask addUsersTask =
                    new ElasticSearchUserController.AddUsersTask();
            addUsersTask.execute(newUser);

            //Log in, and save the user information to file
            currentUser = newUser;
            requestedRides = new RideList();
            acceptedRides = new RideList();
            saveInFile();

    }


    /**
     * Set the currently logged in user, and save to file.
     * This method may be called after retrieving the user's information from the server.
     *
     * @param user Object for the user who is logging in
     */
    //TODO: download requested rides and accepted rides.
    public void setCurrentUser(User user) {
        currentUser = user;
        saveInFile();
    }

    /**
     * Gets current user.
     *
     * @return User object for the user who is currently logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * This method may be used to retrieve the LOCALLY saved ride list
     *
     * @return requestedRides the LOCALLY saved ride list
     */
    public RideList getRequestedRides() {
        return requestedRides;
    }

    /**
     *
     * @return acceptedRides The LOCALLY saved ride list
     */
    public RideList getAcceptedRides() {
        return acceptedRides;
    }


    /**
     * Add the request to the user's list of requests, update both ride list and user list
     * on the server, and save to file.
     *
     */
    //TODO: make needed changes to the user as well.
    public void addRideRequest(LatLng start, LatLng end, Number fare, String description) {
        Ride rideRequest = new Ride(start, end, fare, description, currentUser);
        requestedRides.addRide(rideRequest);
        //currentUser.addRideRequestID(rideRequest);

        //Add the ride to the server.  When the query has completed, make the necessary changes
        //locally, using the returned ride ID
        ElasticSearchRideController.AddRideTask addRideTask =
                new ElasticSearchRideController.AddRideTask(new ESQueryListener() {

                    @Override
                    public void onQueryCompletion(List<?> results) {
                        String rideId = (String) results.get(0);
                        currentUser.addRideRequestID(rideId);
                        saveInFile();
                    }

                });

        addRideTask.execute(rideRequest);
    }


    /**
     * Driver accepts a ride request.  Add to the user's list, upload, and save to file
     *
     * @param acceptedRequest
     */
    //TODO: this should notify the rider
    //TODO: Update the user and ride list both locally and on the server
    public void addAcceptedRequest(Ride acceptedRequest) {
        //currentUser.addAcceptedRequest(acceptedRequest);
        saveInFile();
    }


    /**
     * Rider confirms a specific driver's acceptance.  Add that driver as the accepted driver,
     * make changes on the server, and save to file
     *
     * @param ride
     * @param Driver
     */
    //TODO: this should notify the driver
    public void confirmDriverAcceptance(Ride ride, User Driver) {

    }

    /**
     * Load the currently logged in user, including user profile and relevant rides, from file.
     */
    //TODO: We should probably queue a query to the server too, in case something changed.
    public void loadFromFile() {
        try {
            FileInputStream fis = applicationContext.openFileInput(USERFILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            currentUser = gson.fromJson(in, User.class);

        } catch (FileNotFoundException e) {
            currentUser = null;
        }

    }

    /**
     * Save the currently logged in user, including user info and relevant rides, to file.
     */
    //TODO: save the relevant ride lists too
    private void saveInFile() {
        try {
            //Save the user
            FileOutputStream fos = applicationContext.openFileOutput(USERFILENAME,0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(currentUser, out);
            out.flush();
            fos.close();



        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }


    /**
     * Whatstatus boolean.
     *
     * @param i the
     * @return the boolean
     */
//    public boolean whatstatus(int i) {
//        return Boolean.TRUE;
//    }

    /**
     * Modifies the profile for the currently logged in user.
     * This change will be reflected in the server, the locally saved file,
     * and in the object representing the current user.
     *
     * @param email The user's desired email
     * @param phone The user's desired phone number
     * @param address The user's desired address
     */
    public void editProfile(String email, String phone, String address,String name) {
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);
        currentUser.setName(name);
        saveInFile();

        new ElasticSearchUserController.AddUsersTask().execute(currentUser);
    }
}
