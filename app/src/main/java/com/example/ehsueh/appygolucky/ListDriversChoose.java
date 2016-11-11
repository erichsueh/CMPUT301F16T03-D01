package com.example.ehsueh.appygolucky;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

/**
 * This is the List of drivers to choose from,
 * Contains a List called DriversToChoose
 * and a back button called BackDriverChoose
 * it shows the list of driver that have requested to pick you up
 */

public class ListDriversChoose extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drivers_choose);
    }
}
