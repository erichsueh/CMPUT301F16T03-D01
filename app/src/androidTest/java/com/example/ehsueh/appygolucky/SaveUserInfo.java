package com.example.ehsueh.appygolucky;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.mock.MockContext;

/**
 * Created by Corey on 2016-11-12.
 *
 * This class is just meant to create some local test data
 */

public class SaveUserInfo extends ActivityInstrumentationTestCase2 {
    public SaveUserInfo() {
        super(MainActivity.class);
    }

    private class MockUserController extends UserController {
        public MockUserController(Context context, User user) {
            super(context);
            currentUser = user;
        }
    }

    public void testSaveInfo() {
        User testUser = new User("myUsername", "myName", "myEmail", "myPhone", "myAddress");
        MockUserController mockUserController =
                new MockUserController(getActivity().getApplicationContext(), testUser);

        Ride ride0 = new Ride("ride0", "start0", "end0");
        Ride ride1 = new Ride("ride1", "start1", "end1");
        ride1.setStatus(1);
        Ride ride2 = new Ride("ride2", "start2", "end2");
        ride2.setStatus(2);

        testUser.addRideRequest(ride0);
        testUser.addRideRequest(ride1);
        testUser.addRideRequest(ride2);
        testUser.addAcceptedRequest(ride0);
        testUser.addAcceptedRequest(ride1);
        testUser.addAcceptedRequest(ride2);

        mockUserController.saveInFile();

        assertTrue(Boolean.TRUE);
    }

}
