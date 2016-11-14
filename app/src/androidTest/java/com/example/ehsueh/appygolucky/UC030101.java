package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;


/**
 * Created by Corey on 2016-10-13.
 *
 * Addresses user story US 03.01.01
 * As a user, I want a profile with a unique username and my contact information.
 *
 * Related tests for this use case include:
 * <ul>
 *     <li>Create a new user object with a unique username and other relevant information</li>
 *     <li>Ensure an exception is thrown if the username is not unique</li>
 * </ul>
 */

public class UC030101 extends ActivityInstrumentationTestCase2{
    public UC030101() {
        super(MainActivity.class);
    }

    public void testCreateUser() {
        UserController uc = new UserController(getActivity().getApplicationContext());
        String username = "test_myCoolName";
        String name1 = "John";
        String email1 = "john@yo.com";
        String phone1 = "478-5632";
        String address1 = "123 main";

        String name2 = "Sally";
        String email2 = "sally@yo.com";
        String phone2 = "123-4567";

        //Create new user with unique username
        try {
            uc.addUser(username, name1, email1, phone1, address1);
        }
        catch (UsernameNotUniqueException e) {
            Assert.fail("Threw exception");
        }
        User myUser = uc.getUserByUsername(username);
        Assert.assertEquals("Username does not match", username, myUser.getUsername());

        //Attempt to create new user with same username
        try {
            uc.addUser(username, name2, email2, phone2, address1);
            Assert.fail("Exception not thrown when attempting to use a duplicate username");
        } catch (Exception e) {
        }
        uc.deleteUser(username);


        //Create a user without a username and phone number
        try {
            uc.addUser(username, name1, "", "", "");
        }
        catch (UsernameNotUniqueException e) {
            Assert.fail("Threw exception");
        }
        uc.deleteUser(username);
    }
}
