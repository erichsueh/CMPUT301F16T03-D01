package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import junit.framework.Assert;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * Addresses user story US 03.01.01
 * As a user, I want a profile with a unique username and my contact information.
 *
 * Related tests for this use case include:
 * <ul>
 *     <li>Create a new user object with a unique username and other relevant information</li>
 * </ul>
 */

public class UC030101 extends ActivityInstrumentationTestCase2{

    public UC030101() {
        super(MainActivity.class);
    }

    //TODO: test whether the user is successfully saved to file
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
            uc.newUserLogin(username, name1, email1, phone1, address1);
        } catch(InterruptedException e) {
            fail("GetUserByUsernameTask was interrupted");
        } catch(ExecutionException e) {
            fail("GetUserByUsernameTask threw ExecutionException");
        }



        //Pause to ensure the server is ready
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            Log.d("ESUserController", "Timeout was interrupted");
        }
        //Query for user with the matching username
        ESQueryListener myQueryListener = new ESQueryListener();

        new ElasticSearchUserController.GetUserByUsernameTask(myQueryListener).execute(username);
        while(myQueryListener.getResults() == null) {
            //wait until the results are returned from the server
        }
        List<User> returnedUsers = myQueryListener.getResults();

        assertEquals("The query returned the wrong number of users", 1, returnedUsers.size());
        Assert.assertEquals("Username does not match", username, returnedUsers.get(0).getUsername());
        new ElasticSearchUserController.DeleteUserTask().execute(returnedUsers.get(0).getId());

        //Pause to ensure the server is ready
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            Log.d("ESUserController", "Timeout was interrupted");
        }

        //Create a user without a username and phone
        User myUser = null;
        try {
            myUser = new User(username, name1, "", "", "");
            uc.newUserLogin(myUser);
        } catch(InterruptedException e) {
            fail("uc.newUserLogin threw interrupedException");
        } catch(ExecutionException e) {
            fail("uc.newUserLogin threw ExecutionException");
        }
        new ElasticSearchUserController.DeleteUserTask().execute(myUser.getId());
    }

}
