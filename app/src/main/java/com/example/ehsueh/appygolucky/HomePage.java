package com.example.ehsueh.appygolucky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

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
                Intent intent = new Intent(HomePage.this, SearchPage.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
