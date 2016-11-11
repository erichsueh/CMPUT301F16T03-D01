package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

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
        assertNull("Somehow the id is not null...", myUser.getId());

        //Create a new task, and add the user to the server
        ElasticSearchController.AddUsersTask addUsersTask = new ElasticSearchController.AddUsersTask();
        addUsersTask.execute(myUser);

        //If the adding worked, myUser should now have an Id assigned by the server
        //NOTE: it does seem to be adding the user to the server, but some reason this id is not
        //being set.
        assertNotNull("The Id is still null, after trying to add to the server", myUser.getId());

        //Test if it is in server
        ElasticSearchController.CheckUsernameTask checkUserTask = new ElasticSearchController.CheckUsernameTask();
        //It checks that if it is in server
        assertNotNull(checkUserTask.execute("test_myCoolName"));


        ElasticSearchController.CheckUsernameTask checkUserTask1 = new ElasticSearchController.CheckUsernameTask();
        //it checks that is is not in server
        //It returns something regardless???
        assertNull(checkUserTask1.execute("test_notmyCoolName"));
    }
}
