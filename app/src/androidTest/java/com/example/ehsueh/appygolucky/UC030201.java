package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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

public class UC030201 extends ActivityInstrumentationTestCase2 {

    public UC030201() {
        super(MainActivity.class);
    }

    public void testEditData() {
        UserController uc = new UserController(getActivity().getApplicationContext());
        String username = "test_myCoolName";
        String name = "John";
        String email = "john@yo.com";
        String newEmail = "h4cker@yo.com";
        String phone = "123-4567";
        String address = "123 main";

        //Add the user to the server, set currentUser, and save to file
        try {
            uc.newUserLogin(username, name, email, phone, address);
        } catch(Exception e) {
            fail("Encountered error when attempting to add new user");
        }
        assertEquals("Email wrong before even modifying it", email, uc.getCurrentUser().getEmail());

        //Edit the user information, which should be reflected across the server,
        //currentUser, and the save file.
        uc.editProfile(newEmail, phone, address, name, "");

        //Give the server time to process the user
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            Log.e("UC030201", "Timer threw exception");
        }

        //Check whether the currentUser has been updated
        User currentUser = uc.getCurrentUser();
        assertEquals("currentUser doesn't have the new email", newEmail, currentUser.getEmail());

        //Check whether the update was saved to file
        uc.loadFromFile();
        currentUser = uc.getCurrentUser();
        assertEquals("New email was not saved to file", newEmail, currentUser.getEmail());


        //Check whether the update was saved to the server
        ESQueryListener queryListener = new ESQueryListener();
        new ElasticSearchUserController.GetUsersByUsernameTask(queryListener)
                .execute(username);

        while(queryListener.getResults() == null) {
            //Wait for results to be returned from the server
        }

        List<User> results = queryListener.getResults();
        assertEquals("Returned the wrong number of users", 1, results.size());
        assertEquals("Email change is not reflected in the server", newEmail,
                results.get(0).getEmail());


        //Delete the user from the server
        new ElasticSearchUserController.DeleteUserTask().execute(uc.getCurrentUser().getId());

    }

}
