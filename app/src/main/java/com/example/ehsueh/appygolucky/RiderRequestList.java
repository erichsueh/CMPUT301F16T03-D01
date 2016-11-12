package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * List: RiderRequestLst
 * Button1: AddRider
 * Button:BackRiderReq
 *
 * This is our rider request page, with each colour different status
 */
public class RiderRequestList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_request_list);
    }

    public void AddRequest(View view) {
        Intent intent = new Intent(this, AddRequest.class);
        startActivity(intent);}

    public void BackRequest(View view) {
        finish();}
}
