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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corey on 2016-11-05.
 *
 * This class handles indexing and querying user data on the server,
 * as well as operations on the currently active user on the app.
 */

public class UserController {
    protected static User currentUser;
    protected Context applicationContext;
    protected final String USERFILENAME = "appygolucky_user.json";

    //TODO: this userlist may be replaced entirely by the server
    private static UserList userList;

    public UserController(Context context) {
        applicationContext = context;
//        if(userList == null) {
//            userList = new UserList();
//        }
    }

    //TODO: replace with server
    public void addUser(String username, String name, String email, String phone, String address)
            throws UsernameNotUniqueException {
        if(!userList.containsUsername(username)) {
            User newUser = new User(username, name, email, phone, address);
            userList.addUser(newUser);
        }

        else {
            throw new UsernameNotUniqueException();
        }
    }

    //TODO: replace with server
    public User getUserByUsername(String username) {
        return userList.getUserByUsername(username);
    }

    //TODO: replace with server
    public void deleteUser(String username) {
        userList.deleteUser(username);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void loadFromFile() {
        try {
            FileInputStream fis = applicationContext.openFileInput(USERFILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            currentUser = gson.fromJson(in, User.class);

        } catch (FileNotFoundException e) {
            currentUser = null;
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    public void saveInFile() {
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


    public static boolean whatstatus(int i) {
        return Boolean.TRUE;
    }

    public static void editProfile(String email, String phone, String address) {
        User.setEmail(email);
        User.setPhone(phone);
        User.setAddress(address);
    }
}
