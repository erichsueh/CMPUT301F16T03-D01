package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.TimeUnit;

import dalvik.annotation.TestTargetClass;

/**
 * Created by Corey on 2016-11-07.
 */


public class ElasticSearchControllerTest extends ActivityInstrumentationTestCase2 {
    public ElasticSearchControllerTest() {
        super(MainActivity.class);
    }

    public void testAddUsersTask() {
        String username = "test_myCoolName";
        String name1 = "John";
        String email1 = "john@yo.com";
        String phone1 = "478-5632";
        String address1 = "123 main";

        String name2 = "Sally";
        String email2 = "sally@yo.com";
        String phone2 = "123-4567";

        User myUser = new User(username, name1, email1, phone1, address1);
        assertNull("ID should be null, but is not", myUser.getId());

        //Create a new task, and add the user to the server
        ElasticSearchController.AddUsersTask addUsersTask = new ElasticSearchController.AddUsersTask();
        addUsersTask.execute(myUser);

        //If the adding worked, myUser should now have an Id assigned by the server
        //NOTE: it does seem to be adding the user to the server, but some reason this id is not
        //being set.

        //Wait until the AsyncTask is finished
        //http://stackoverflow.com/questions/7588584/android-asynctask-check-status
        //Nov 11, 2016 DeeV
        AsyncTask.Status taskStatus;
        do {
            taskStatus = addUsersTask.getStatus();
        } while (taskStatus != AsyncTask.Status.FINISHED);

        assertNotNull("The Id is still null, after trying to add to the server", myUser.getId());

        //Test if it is in server
        //Some reason this is failing.  Result is always null
        ElasticSearchController.CheckUsernameTask checkUserTask = new ElasticSearchController.CheckUsernameTask();
        User result = null;
        checkUserTask.execute(username);
        try {
            result = checkUserTask.get();
        } catch(Exception e) {
            fail("checkUserTask threw an exception");
        }
        //It checks that if it is in server
        assertNotNull(result);


        ElasticSearchController.CheckUsernameTask checkUserTask1 = new ElasticSearchController.CheckUsernameTask();
        //it checks that is is not in server
        checkUserTask1.execute("test_notMyCoolName");
        try {
            result = checkUserTask1.get();
        } catch(Exception e) {
            fail("checkUserTask1.get() threw an exception");
        }
        assertNull(result);
    }
}
