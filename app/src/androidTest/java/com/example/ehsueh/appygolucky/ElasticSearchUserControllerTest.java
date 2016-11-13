package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Corey on 2016-11-07.
 */


public class ElasticSearchUserControllerTest extends ActivityInstrumentationTestCase2 {
    public ElasticSearchUserControllerTest() {
        super(MainActivity.class);
    }

    public void testAddUsersTask() {
        String username = "test_myCoolName";
        String name = "John";
        String email = "john@yo.com";
        String phone = "478-5632";
        String address = "123 main";

        User myUser = new User(username, name, email, phone, address);
        assertNull("ID should be null, but is not", myUser.getId());

        //Create a new task, and add the user to the server
        ElasticSearchUserController.AddUsersTask addUsersTask = new ElasticSearchUserController.AddUsersTask();
        addUsersTask.execute(myUser);

        //Wait until the AsyncTask is finished
        //http://stackoverflow.com/questions/7588584/android-asynctask-check-status
        //Nov 11, 2016 DeeV
        AsyncTask.Status taskStatus;
        do {
            taskStatus = addUsersTask.getStatus();
        } while (taskStatus != AsyncTask.Status.FINISHED);

        //If the adding worked, myUser should now have an Id assigned by the server
        assertNotNull("The Id is still null, after trying to add to the server", myUser.getId());

        //Test if it is in server
        //This is currently returning all but the most recent entry.
        //Thus, it will fail if the index is empty when starting the test.
        //Corey is currently looking into this.
        ElasticSearchUserController.CheckUsernameTask checkUserTask = new ElasticSearchUserController.CheckUsernameTask();
        List<User> result = null;
        checkUserTask.execute(username);

        try {
            result = checkUserTask.get(10000, TimeUnit.MILLISECONDS);
        } catch(TimeoutException e) {
            fail("checkUserTask timed out");
        } catch (Exception e) {
            fail("checkUserTask threw an exception");
        }
        //It checks that if it is in server
        assertNotNull(result);

        //Print some data to the logs for debugging
        if(result != null) {
            Integer i = 1;
            for(User user: result) {
                Log.d("ESUserController", "Result " + i + " with ID " + user.getId());
                i++;
            }
        }


        ElasticSearchUserController.CheckUsernameTask checkUserTask1 = new ElasticSearchUserController.CheckUsernameTask();
        //it checks that is is not in server
        checkUserTask1.execute("test_notMyCoolName");
        try {
            result = checkUserTask1.get(10000, TimeUnit.MILLISECONDS);
        } catch(TimeoutException e) {
            fail("checkUserTask1 timed out");
        } catch(Exception e) {
            fail("checkUserTask1 threw an exception");
        }
        assertNull(result);

        //delete the user that we added
        //Note: This will only be run if the other assertions passed
//        ElasticSearchUserController.DeleteUserTask deleteUserTask = new ElasticSearchUserController.DeleteUserTask();
//        deleteUserTask.execute(myUser.getId());
    }
}
