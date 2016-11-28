package com.example.ehsueh.appygolucky;

import android.content.Context;
import android.widget.Toast;

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
    protected Boolean queryInProgress;

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
        queryInProgress = Boolean.FALSE;
        if(requestedRides == null) {
            requestedRides = new RideList();
            acceptedRides = new RideList();
        }
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
     * Set the currently logged in user, download their rides, and save to file.
     * This method may be called after retrieving the user's information from the server.
     *
     * @param user Object for the user who is logging in
     */
    public void existingUserLogin(User user) {
        queryInProgress = Boolean.TRUE;
        currentUser = user;
        saveInFile();

        List<String> requestedRideIds = currentUser.getRideRequestIDs();
        //http://stackoverflow.com/questions/9572795/convert-list-to-array-in-java
        //November 22, 2016  Eng.Fouad
        //Convert list to array so we can use it as a varargs for the task
        String[] requestedIdsArray = new String[requestedRideIds.size()];
        requestedRideIds.toArray(requestedIdsArray);
        new ElasticSearchRideController.GetRidesByIdTask(new ESQueryListener() {
            @Override
            public void onQueryCompletion(List<?> results) {
                List<Ride> rides = (List<Ride>) results;
                requestedRides = new RideList(rides);
            }
        }).execute(requestedIdsArray);


        List<String> acceptedRideIds = currentUser.getAcceptedRideIDs();
        //Convert to array
        String[] acceptedIdsArray = new String[acceptedRideIds.size()];
        acceptedRideIds.toArray(acceptedIdsArray);
        new ElasticSearchRideController.GetRidesByIdTask(new ESQueryListener() {
            @Override
            public void onQueryCompletion(List<?> results) {
                List<Ride> rides = (List<Ride>) results;
                acceptedRides = new RideList(rides);
                queryInProgress = Boolean.FALSE;

                Toast toast = Toast.makeText(applicationContext, "Download complete",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }).execute(acceptedIdsArray);
    }

    /**
     * Whether data is currently being fetched from the server.
     * Ex. when refreshing user data.
     * @return queryInProgress
     */
    public Boolean queryInProgress() {
        return queryInProgress;
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
     * This method may be used to retrieve the list of rides the user has requested.
     * Will first return the local list, but then will refresh the list.
     *
     * @return requestedRides the LOCALLY saved ride list
     */
    public RideList getRequestedRides() {
        queryInProgress = Boolean.TRUE;

        //Return the RideList, which will be updated when the query completes.
        return requestedRides;
    }

    /**
     * Retrieves an updated list of the accepted rides for this user.
     */
    public void syncAcceptedRides() {
        queryInProgress = Boolean.TRUE;

        List<String> acceptedRideIds = currentUser.getAcceptedRideIDs();
        //Convert list to array so we can use it as a varargs for the task
        String[] acceptedIdsArray = new String[acceptedRideIds.size()];
        acceptedRideIds.toArray(acceptedIdsArray);

        //Retrieve the list of rides for this user
        new ElasticSearchRideController.GetRidesByIdTask(new ESQueryListener() {
            @Override
            public void onQueryCompletion(List<?> results) {
                acceptedRides.setRides((List<Ride>) results);
                queryInProgress = Boolean.FALSE;
                saveInFile();
            }
        }).execute(acceptedIdsArray);
    }

    /**
     * Retrieves an updated list of the requested rides for this user.
     */
    public void syncRequestedRides() {
        queryInProgress = Boolean.TRUE;

        List<String> requestedRideIds = currentUser.getRideRequestIDs();
        //Convert list to array so we can use it as a varargs for the task
        String[] requestedIdsArray = new String[requestedRideIds.size()];
        requestedRideIds.toArray(requestedIdsArray);

        //Retrieve the list of rides for this user
        new ElasticSearchRideController.GetRidesByIdTask(new ESQueryListener() {
            @Override
            public void onQueryCompletion(List<?> results) {
                requestedRides.setRides((List<Ride>) results);
                queryInProgress = Boolean.FALSE;
                saveInFile();
            }
        }).execute(requestedIdsArray);
    }

    /**
     * This method returns the IDs for the rides the user has requested.  This can be used
     * to retrieve updated information from the server
     */
    public List<String> getRequestedRideIDs() {
        return currentUser.getRideRequestIDs();
    }

    /**
     * Returns the IDs for the rides the user has accepted.  These can be used to retrieve
     * updated information from the server
     */
    public List<String> getAcceptedRideIDs() {
        return currentUser.getAcceptedRideIDs();
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
    public void addRideRequest(Ride rideRequest) {
        queryInProgress = Boolean.TRUE;
        requestedRides.addRide(rideRequest);

        //Add the ride to the server.  When the query has completed, make the necessary changes
        //locally, using the returned ride ID
        ElasticSearchRideController.AddRideTask addRideTask =
                new ElasticSearchRideController.AddRideTask(new ESQueryListener() {

                    @Override
                    public void onQueryCompletion(List<?> results) {
                        String rideId = (String) results.get(0);
                        //Add the ID to the user locally
                        currentUser.addRideRequestID(rideId);
                        //Save the user and the ride lists
                        saveInFile();
                        //Update the user info on the server
                        new ElasticSearchUserController.AddUsersTask().execute(currentUser);
                        queryInProgress = Boolean.FALSE;
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
        currentUser.addAcceptedRequestID(acceptedRequest.getId());
        acceptedRides.addRide(acceptedRequest);
        acceptedRequest.addDriverUsername(currentUser.getUsername());
        saveInFile();

        //Update the user's data on the server
        new ElasticSearchUserController.AddUsersTask()
                .execute(currentUser);

        //Update the ride data on the server
        //Note this time we don't care about the ID that is returned, so we do nothing
        //with the ESQueryListener
        new ElasticSearchRideController.AddRideTask(new ESQueryListener())
                .execute(acceptedRequest);
    }


    /**
     * Rider confirms a specific driver's acceptance.  Add that driver as the accepted driver,
     * make changes on the server, and save to file
     *
     * @param ride
     * @param confirmedDriver
     */
    //TODO: this should notify the driver
    public void confirmDriverAcceptance(Ride ride, User confirmedDriver)
            throws DriverNotInListException {
        //Update the ride locally
        ride.riderConfirms(confirmedDriver);

        //Update the ride on the server
        new ElasticSearchRideController.AddRideTask(new ESQueryListener()).execute(ride);

        //Make a listener class that will remove ride IDs from the users who are
        //not the confirmed driver.  (Note: this is run AFTER the query finishes)
        class confirmQueryListener extends ESQueryListener {
            private String rideID;
            private String confirmedDriverUsername;

            public confirmQueryListener(String rideID, String confirmedDriverUsername) {
                this.rideID = rideID;
                this.confirmedDriverUsername = confirmedDriverUsername;
            }

            @Override
            public void onQueryCompletion(List<?> results) {
                List<User> drivers = (List<User>) results;
                //For each user, if they aren't the confirmed driver, remove the ride ID
                for (User driver : drivers) {
                    if (!driver.getUsername().equals(confirmedDriverUsername)) {
                        //Remove the ID from the list
                        driver.removeAcceptedRequestID(rideID);
                        //Update the user's info on the server
                        new ElasticSearchUserController.AddUsersTask().execute(driver);
                    }
                }
            }
        }

        List<String> usernames = ride.getDriverUsernames();
        //Convert list to array so we can use it as a varargs for the task
        String[] usernamesArray = new String[ride.getDriverUsernames().size()];
        usernames.toArray(usernamesArray);

        new ElasticSearchUserController.GetUsersByUsernameTask(
                new confirmQueryListener(ride.getId(), confirmedDriver.getUsername()))
                    .execute(usernamesArray);


        //Save user and ride data
        saveInFile();
    }

    /**
     * Load the currently logged in user, including user profile and relevant rides, from file.
     */
    //TODO: We should probably queue a query to the server too, in case something changed.
    public void loadFromFile() {
        try {
            //Load the current user
            FileInputStream fis = applicationContext.openFileInput(USERFILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            currentUser = gson.fromJson(in, User.class);

            //Load the requested rides
            fis = applicationContext.openFileInput(REQUESTEDRIDESFILENAME);
            in = new BufferedReader(new InputStreamReader(fis));

            requestedRides = gson.fromJson(in, RideList.class);

            //Load the accepted rides
            fis = applicationContext.openFileInput(ACCEPTEDRIDESFILENAME);
            in = new BufferedReader(new InputStreamReader(fis));

            acceptedRides = gson.fromJson(in, RideList.class);

        } catch (FileNotFoundException e) {
            currentUser = null;
            requestedRides = new RideList();
            acceptedRides = new RideList();
        }

    }

    /**
     * Save the currently logged in user, including user info and relevant rides, to file.
     */
    protected void saveInFile() {
        try {
            //Save the user
            FileOutputStream fos = applicationContext.openFileOutput(USERFILENAME,0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(currentUser, out);
            out.flush();
            fos.close();

            //Save the requested rides
            fos = applicationContext.openFileOutput(REQUESTEDRIDESFILENAME,0);

            out = new BufferedWriter(new OutputStreamWriter(fos));

            gson.toJson(requestedRides, out);
            out.flush();
            fos.close();

            //Save the accepted rides
            fos = applicationContext.openFileOutput(ACCEPTEDRIDESFILENAME,0);

            out = new BufferedWriter(new OutputStreamWriter(fos));

            gson.toJson(acceptedRides, out);
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
    public void editProfile(String email, String phone, String address,String name,String rideDescription) {
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);
        currentUser.setName(name);
        currentUser.setRideDescription(rideDescription);
        saveInFile();

        //Update user info on the server
        new ElasticSearchUserController.AddUsersTask().execute(currentUser);
    }

    public void deleteRequestedRide(Ride ride) {
        //Delete from the list of rides
        requestedRides.deleteRide(ride);
        //Delete from the user's list of ride IDs
        currentUser.deleteRideRequestID(ride);

        //This listener will delete the ride ID from every relevant driver,
        //once the query returns the needed list of drivers.
        class DeleteQueryListener extends ESQueryListener {
            private String rideID;
            public DeleteQueryListener(String rideID) {
                this.rideID = rideID;
            }
            @Override
            public void onQueryCompletion(List<?> results) {
                List<User> drivers = (List<User>) results;
                for (User driver : drivers) {
                    driver.removeAcceptedRequestID(rideID);
                    new ElasticSearchUserController.AddUsersTask().execute(driver);
                }
            }
        }

        //Create a listener with the ID to be deleted and pass it into a new get user task.
        DeleteQueryListener queryListener = new DeleteQueryListener(ride.getId());
        ElasticSearchUserController.GetUsersByUsernameTask getUsersByUsernameTask =
                new ElasticSearchUserController.GetUsersByUsernameTask(queryListener);

        List<String> usernames = ride.getDriverUsernames();
        //Convert list to array so we can use it as a varargs for the task
        String[] usernamesArray = new String[ride.getDriverUsernames().size()];
        usernames.toArray(usernamesArray);
        getUsersByUsernameTask.execute(usernamesArray);

        saveInFile();
        //Update the user data online.
        new ElasticSearchUserController.AddUsersTask().execute(currentUser);
    }

    public void deleteAcceptedRide(Ride ride) {
        //Remove the driver ID from the ride
        ride.removeDriverUsername(currentUser.getUsername());
        //Remove the ride from the local list
        acceptedRides.deleteRide(ride);
        //Remove the ID from the user object
        currentUser.removeAcceptedRequestID(ride.getId());

        saveInFile();

        //make changes to user and ride on the server
        new ElasticSearchUserController.AddUsersTask().execute(currentUser);
        new ElasticSearchRideController.AddRideTask(new ESQueryListener()).execute(ride);
    }
}
