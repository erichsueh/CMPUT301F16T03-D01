package com.example.ehsueh.appygolucky;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Corey on 2016-11-12.
 *
 * This class is just meant to create some local test data
 */

public class SaveUserInfo extends ActivityInstrumentationTestCase2 {
    public SaveUserInfo() {
        super(MainActivity.class);
    }


    public void testSaveInfo() {
        UserController uc =
                new UserController(getActivity().getApplicationContext());

        try {
            uc.newUserLogin("myUsername", "myName", "myEmail", "myPhone", "myAddress");
        } catch(InterruptedException e) {
            fail("threw InterruptedException");
        } catch(ExecutionException e) {
            fail("threw ExecutionException");
        }

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
        Ride rideC = new Ride(startLocationC, endLocationC, fareC, rideDescriptionC, riderC);

        uc.addRideRequest(rideA);
        uc.addRideRequest(rideB);
        uc.addRideRequest(rideC);
        uc.addAcceptedRequest(rideB);
        uc.addAcceptedRequest(rideC);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(Exception e) {
            fail("Timer interrupted");
        }

        assertEquals(3,uc.getCurrentUser().getRideRequestIDs().size());
    }

}
