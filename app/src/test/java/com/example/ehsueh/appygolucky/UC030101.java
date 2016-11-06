package com.example.ehsueh.appygolucky;

import org.junit.Test;
import static org.junit.Assert.*;

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

public class UC030101 {

    @Test
    public void testCreateUser() {

        //Create new user with unique username
        User myUser = new User("myCoolName", "John", "john@yo.com", "478-5632");
        assertEquals("Username does not match", "myCoolName", myUser.getUsername());

        //Attempt to create new user with same username
        try {
            User myUser2 = new User("myCoolName", "Sally", "sally@yo.com", "123-45678");
            fail("Exception not thrown when attempting to use a duplicate username");
        } catch (Exception e) {
        }
    }
}
