package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

/**
 * List view :PendingList
 * Button:PendingBack
 *
 * The pending shows the lsit of riders you've proposed a price to
 */

public class DriverPending extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_pending);
    }


    public void BackPending (View view) {
    finish();}
}
