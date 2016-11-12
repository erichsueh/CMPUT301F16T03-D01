package com.example.ehsueh.appygolucky;

/**
 * Created by Corey on 2016-11-05.
 */
public class UserController {

    private static UserList userList;

    public UserController() {
        if(userList == null) {
            userList = new UserList();
        }
    }

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

    public User getUserByUsername(String username) {
        return userList.getUserByUsername(username);
    }

    public void deleteUser(String username) {
        userList.deleteUser(username);
    }
}
