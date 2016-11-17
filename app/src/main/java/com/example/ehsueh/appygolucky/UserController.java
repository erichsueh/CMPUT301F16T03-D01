package com.example.ehsueh.appygolucky;

import android.content.Context;

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
    /**
     * The User currently logged in.
     */
    protected static User currentUser;
    /**
     * The Application context.
     */
    protected Context applicationContext;
    /**
     * The filename to save user data.
     */
    protected final String USERFILENAME = "appygolucky_user.json";

    /**
     * Instantiates a new User controller.
     *
     * @param context the context
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
     * @throws UsernameNotUniqueException If there already exists a user with this username.
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
            saveInFile();

    }

    public void newUserLogin(User user) throws InterruptedException, ExecutionException{
        this.newUserLogin(user.getUsername(), user.getName(), user.getEmail(),
                user.getPhone(), user.getAddress());
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        saveInFile();
    }


    /**
     * Gets user by username.  This method is handles creating and executing the relevant
     * asynchronous task.  This function does not return a value, to avoid freezing the UI.
     * Instead, the Asynchronous task will use a callback function.
     *
     * @param username the username
     */
    public void getUserByUsername(String username) {
        ElasticSearchUserController.GetUserByUsernameTask getUserByUsernameTask =
                new ElasticSearchUserController.GetUserByUsernameTask(new ESQueryListener());
        getUserByUsernameTask.execute(username);
    }


    /**
     * Delete user.
     *
     * @param ID the ID of the user to be removed
     */
    public void deleteUser(String ID) {
        ElasticSearchUserController.DeleteUserTask deleteUserTask =
                new ElasticSearchUserController.DeleteUserTask();
        deleteUserTask.execute(ID);
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
     * Load the currently logged in user, including user profile and relevant rides, from file.
     */
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
    private void saveInFile() {
        try {
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
    public boolean whatstatus(int i) {
        return Boolean.TRUE;
    }

    public void editProfile(String email, String phone, String address) {
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);
    }
}
