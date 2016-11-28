package com.example.ehsueh.appygolucky;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * this is our screen with 4 buttons, "EditProfileButton, DriverButton, RiderButton, LogoutButton
 */
public class HomePageActivity extends AppCompatActivity {
    UserController uc;
    private TextView ratingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        uc = new UserController(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    /**
     * This onresume function makes sure our rating is updated everytime we resume this page,
     * as well as makes sure the username is right
     */
    @Override
    protected void onResume() {
        super.onResume();
        TextView UsernameDisplayTxt = (TextView) findViewById(R.id.UsernameDisplayTxt);
        if(uc.getCurrentUser() != null) {
            UsernameDisplayTxt.setText("Welcome " + uc.getCurrentUser().getUsername());
        }

            if (uc.getCurrentUser().getNotification()){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You have a new notification!", Toast.LENGTH_SHORT);
                toast.show();
                uc.getCurrentUser().setNotification(false);}
        
        ratingView = (TextView) findViewById(R.id.textView13);
        ratingView.setText("As a driver your current rating out of 5 is:  "+ uc.getCurrentUser().getRating());

    }

    /**
     * this button will take us back to the home screen as well as clear the data an log out
     * @param view
     */
    public void LogoutClick(View view){
        Datacleaner.getInstance().clearApplicationData();
        finish();}

    /**
     * this button shows us the options for the riders, it will take us to the rider screen
     * @param view
     */
    public void RiderClick(View view) {
        Intent intent = new Intent(this, RiderRequestListActivity.class);
        startActivity(intent);}

    /**
     * This button sets a alert dialog builder which gives us 2 different options that the
     * driver can do, one is search for drivers, the other is to look at their proposed ones
     * @param view
     */
    public void DriverClick(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(HomePageActivity.this);
        adb.setMessage("What would you like to do?");
        adb.setCancelable(true);
        adb.setPositiveButton("Pending", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(HomePageActivity.this, DriverPendingActivity.class);
                startActivity(intent);
            }
        });
        if (isNetworkAvailable()) {
            adb.setNeutralButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(HomePageActivity.this, SearchPageActivity.class);
                    startActivity(intent);
                }
            });
        }
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        adb.show();
    }

    /**
     * this last click goes into the edit profile page
     * @param view
     */
    public void EditClick(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("isNew",false);
        startActivity(intent);}

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
