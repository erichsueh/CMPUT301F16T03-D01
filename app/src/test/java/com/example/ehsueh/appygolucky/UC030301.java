package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Corey on 2016-10-13.
 *
 * US 03.03.01 As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.
 *
 * Related tests for this use case include:
 * <ul>
 *     <li>Retrieve the email and phone number for a user</li>
 * </ul>
 */

public class UC030301 extends ActivityInstrumentationTestCase2 {
    public UC030301() {
        super(MainActivity.class);
    }

    public void testRetrieveInfo() {
        User myUser = new User("myCoolName", "John", "john@yo.com", "478-5632", "123 main");
        UserList list = new UserList();
        list.addUser(myUser);

        User retrievedUser = list.getUserByUsername("myCoolName");
        assertEquals("Returned the wrong user", myUser, retrievedUser);
    }
}
