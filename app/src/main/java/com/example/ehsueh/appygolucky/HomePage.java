package com.example.ehsueh.appygolucky;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

/**
 * this is our screen with 4 buttons, "EditProfileButton, DriverButton, RiderButton, LogoutButton
 */
public class HomePage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }
}
