package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ElasticSearchUserControllerTest extends ActivityInstrumentationTestCase2 {
    public ElasticSearchUserControllerTest() {
        super(MainActivity.class);
    }

    //In order to test AddUsersTask, also tests GetUsersByUsernameTask and DeleteUserTask
    public void testAddUsersTask() {
        String username = "test_myCoolName";
        String name = "John";
        String email = "john@yo.com";
        String phone = "478-5632";
        String address = "123 main";
        String newAddress = "42 You Know Ave";


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
        ElasticSearchUserController.GetUsersByUsernameTask getUsersByUsernameTask =
                new ElasticSearchUserController.GetUsersByUsernameTask(myQueryListener);
        getUsersByUsernameTask.execute(username);

        while(myQueryListener.getResults() == null) {
            //Wait for the server to return the results
        }


        List<User> result = myQueryListener.getResults();

        //Check that we receive exactly one result
        assertEquals("Returned more or less than one user", 1, result.size());


        myQueryListener = new ESQueryListener();
        ElasticSearchUserController.GetUsersByUsernameTask checkUserTask1 =
                new ElasticSearchUserController.GetUsersByUsernameTask(myQueryListener);
        //it checks that is is not in server
        checkUserTask1.execute("test_notMyCoolName");

        while(myQueryListener.getResults() == null) {
            //Wait for the server to return any results
        }

        result = myQueryListener.getResults();
        assertEquals("List was not empty", 0, result.size());


        //Make a change to the user, and upload again
        myUser.setAddress(newAddress);
        new ElasticSearchUserController.AddUsersTask().execute(myUser);

        //Wait to ensure that the server has processed the user
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            Log.e("ESUserController", "Timer was interrupted");
        }

        //Retrieve the user and check whether it has the new address
        myQueryListener = new ESQueryListener();
        new ElasticSearchUserController.GetUsersByUsernameTask(myQueryListener)
                .execute(username);

        while(myQueryListener.getResults() == null) {
            //Wait for server to return results
        }

        result = myQueryListener.getResults();
        assertEquals("Returned the wrong number of users", 1, result.size());
        assertEquals(newAddress, result.get(0).getAddress());

        //delete the user that we added
        //Note: This will only be run if the other assertions passed
        ElasticSearchUserController.DeleteUserTask deleteUserTask =
                new ElasticSearchUserController.DeleteUserTask();
        deleteUserTask.execute(myUser.getId());
    }

}
