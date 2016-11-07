package com.example.ehsueh.appygolucky;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corey on 2016-10-13.
 */

//TODO: possibly replace userList with a hashtable

public class UserList {
    private List<User> userList;

    public UserList() {
        this.userList = new ArrayList<User>();
    }

    public void addUser(User newUser) {
        this.userList.add(newUser);
    }

    public Boolean containsUsername(String username) {
        for (User user : userList) {
            if(user.getUsername() == username) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public User getUserByUsername(String username) {
        for (User user: userList) {
            if(user.getUsername() == username) {
                return user;
            }
        }

        //If username does not exist
        return null;
    }

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
