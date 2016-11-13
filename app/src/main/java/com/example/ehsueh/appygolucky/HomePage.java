package com.example.ehsueh.appygolucky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * this is our screen with 4 buttons, "EditProfileButton, DriverButton, RiderButton, LogoutButton
 */
public class HomePage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserController uc = new UserController(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TextView UsernameDisplayTxt = (TextView) findViewById(R.id.UsernameDisplayTxt);
        if(uc.getCurrentUser() != null) {
            UsernameDisplayTxt.setText(uc.getCurrentUser().getUsername());
        }
    }

    public void LogoutClick(View view) {
        finish();}

    public void RiderClick(View view) {
        Intent intent = new Intent(this, RiderRequestList.class);
        startActivity(intent);}

    public void DriverClick(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(HomePage.this);
        adb.setMessage("What would you like to do?");
        adb.setCancelable(true);
        adb.setPositiveButton("Pending", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(HomePage.this, DriverPending.class);
                startActivity(intent);
            }
        });
        adb.setNeutralButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(HomePage.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        adb.show();
    }
    public void EditClick(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);}
}
