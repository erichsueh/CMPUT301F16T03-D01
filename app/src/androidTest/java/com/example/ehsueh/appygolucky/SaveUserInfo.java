package com.example.ehsueh.appygolucky;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.mock.MockContext;

import com.google.android.gms.maps.model.LatLng;

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

        String rideDescriptionA = "I need a ride to West Ed!!";
        Number fareA = 2.75;
        LatLng startLocationA = new LatLng(53.526495, -113.630327);
        LatLng endLocationA = new LatLng(53.526495, -113.630327);
        String rideDescriptionB = "Gotta get to South Common ASAP :)";
        User riderA = new User("usernameA", "nameA", "emailA", "phoneA", "addressA");
        Number fareB = 2.75;
        LatLng startLocationB = new LatLng(53.526495, -113.630327);
        LatLng endLocationB = new LatLng(53.526495, -113.630327);
        User riderB = new User("usernameC", "nameC", "emailC", "phoneC", "addressC");
        String rideDescriptionC = "Ride to Grocery Store";
        Number fareC = 2.75;
        User riderC = new User("usernameC", "nameC", "emailC", "phoneC", "addressC");
        LatLng startLocationC = new LatLng(53.526495, -113.630327);
        LatLng endLocationC = new LatLng(53.526495, -113.630327);

        Ride rideA = new Ride(startLocationA, endLocationA, fareA, rideDescriptionA, riderA);
        Ride rideB = new Ride(startLocationB, endLocationB, fareB, rideDescriptionB, riderB);
        rideB.setStatus(1);
        Ride rideC = new Ride(startLocationC, endLocationC, fareC, rideDescriptionC, riderC);
        rideC.setStatus(2);

        testUser.addRideRequest(rideA);
        testUser.addRideRequest(rideB);
        testUser.addRideRequest(rideC);
        testUser.addAcceptedRequest(rideA);
        testUser.addAcceptedRequest(rideB);
        testUser.addAcceptedRequest(rideC);

        mockUserController.saveInFile();

        assertTrue(Boolean.TRUE);
    }

}
