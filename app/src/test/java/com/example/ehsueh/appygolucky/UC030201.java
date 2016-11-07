package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Corey on 2016-10-13.
 *
 * US 03.02.01 As a user, I want to edit the contact information in my profile.
 *
 * Related tests for this use case include:
 * <ul>
 *     <li>Attempt to change the data associated with a certain user</li>
 * </ul>
 */

public class UC030201 extends ActivityInstrumentationTestCase2{
    public UC030201() {
        super(MainActivity.class);
    }

    public void testEditData() {
        String newName = "Fred";
        User myUser = new User("myCoolName", "John", "john@yo.com", "478-5632", "123 main");
        assertFalse(myUser.getName() == newName);

        //Try changing the name, and ensure that it has indeed changed
        myUser.setName(newName);
        assertTrue(myUser.getName() == newName);
    }

}
