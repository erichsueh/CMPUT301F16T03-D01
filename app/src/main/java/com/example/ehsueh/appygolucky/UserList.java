package com.example.ehsueh.appygolucky;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corey on 2016-10-13.
 */

public class UserList {
    private List<User> userList;

    public UserList() {
        this.userList = new ArrayList<User>();
    }

    public void add(User newUser) {

    }

    public User getUserByUsername(String username) {
        return new User(null, null, null, null);
    }
}
