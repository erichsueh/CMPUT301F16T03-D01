package com.example.ehsueh.appygolucky;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corey on 2016-10-13.
 */
//TODO: possibly replace userList with a hashtable
    //Also, this may be replaced entirely by the server...

public class UserList {
    private List<User> userList;

    /**
     * Instantiates a new User list.
     */
    public UserList() {
        this.userList = new ArrayList<User>();
    }

    /**
     * Add user.
     *
     * @param newUser the new user
     */
    public void addUser(User newUser) {
        this.userList.add(newUser);
    }

    /**
     * Contains username boolean.
     *
     * @param username the username
     * @return the boolean
     */
    public Boolean containsUsername(String username) {
        for (User user : userList) {
            if(user.getUsername() == username) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    public User getUserByUsername(String username) {
        for (User user: userList) {
            if(user.getUsername() == username) {
                return user;
            }
        }

        //If username does not exist
        return null;
    }

    /**
     * Delete user.
     *
     * @param username the username
     */
    public void deleteUser(String username) {
        User userToDelete = null;
        for (User user: userList) {
            if(user.getUsername() == username) {
                userToDelete = user;
            }
        }
        try {
            userList.remove(userToDelete);
        }
        catch(NullPointerException e) {
            Log.d("UserList", "Trying to delete a username that doesn't exist");
        }
    }
}
