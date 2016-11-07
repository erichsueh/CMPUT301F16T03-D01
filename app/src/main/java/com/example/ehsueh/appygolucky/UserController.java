package com.example.ehsueh.appygolucky;

/**
 * Created by Corey on 2016-11-05.
 */
public class UserController {

    private UserList userList;

    public UserController() {
        this.userList = new UserList();
    }

    public void addUser(String username, String name, String email, String phone, String address)
            throws UsernameNotUniqueException {
        if(!userList.containsUsername(username)) {
            User newUser = new User(username, name, email, phone, address);
            this.userList.addUser(newUser);
        }

        else {
            throw new UsernameNotUniqueException();
        }
    }

    public User getUserByUsername(String username) {
        return this.userList.getUserByUsername(username);
    }

    public void deleteUser(String username) {
        this.userList.deleteUser(username);
    }
}
