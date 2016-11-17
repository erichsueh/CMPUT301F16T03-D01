package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



public class ElasticSearchUserControllerTest extends ActivityInstrumentationTestCase2 {
    public ElasticSearchUserControllerTest() {
        super(MainActivity.class);
    }

    //In order to test AddUsersTask, also tests GetUserByUsernameTask and DeleteUserTask
    public void testAddUsersTask() {
        String username = "test_myCoolName";
        String name = "John";
        String email = "john@yo.com";
        String phone = "478-5632";
        String address = "123 main";

        User myUser = new User(username, name, email, phone, address);
        assertNull("ID should be null, but is not", myUser.getId());

        //Create a new task, and add the user to the server
        ElasticSearchUserController.AddUsersTask addUsersTask =
                new ElasticSearchUserController.AddUsersTask();
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

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {}

        //Test if it is in server
        ESQueryListener myQueryListener = new ESQueryListener();
        ElasticSearchUserController.GetUserByUsernameTask getUserByUsernameTask =
                new ElasticSearchUserController.GetUserByUsernameTask(myQueryListener);
        getUserByUsernameTask.execute(username);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {}


        List<User> result = myQueryListener.getResults();

        //It checks that if it is in server
        assertEquals("Returned more or less than one user", 1, result.size());


        myQueryListener = new ESQueryListener();
        ElasticSearchUserController.GetUserByUsernameTask checkUserTask1 =
                new ElasticSearchUserController.GetUserByUsernameTask(myQueryListener);
        //it checks that is is not in server
        checkUserTask1.execute("test_notMyCoolName");
        result = myQueryListener.getResults();
        assertNull(result);

        //delete the user that we added
        //Note: This will only be run if the other assertions passed
        ElasticSearchUserController.DeleteUserTask deleteUserTask =
                new ElasticSearchUserController.DeleteUserTask();
        deleteUserTask.execute(myUser.getId());
    }
}
