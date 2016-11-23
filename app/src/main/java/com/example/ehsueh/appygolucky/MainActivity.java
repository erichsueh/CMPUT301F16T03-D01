/*
AppyGoLucky: Simple application that allows for those who need rides to and from locations to
connect with a driver who could take them. This application permits for simple coordination,
payment agreement, and route agreement.

Copyright (C) 2016 J Maxwell Douglas jmdougla@ualberta.ca, Eric Hsueh, Corey Hunt, Vinson Lai

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

*/

package com.example.ehsueh.appygolucky;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * This is the main activity of the app, it loads the Homepage and just verifies that a network
 * connection is available.
 *
 */
public class MainActivity extends ActionBarActivity {
    UserController uc;
    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "default_account";
    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    // Global variables
    // A content resolver for accessing the provider
    ContentResolver mResolver;
    // Instance fields
    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Load from file, and open the home page if user already logged in
        uc = new UserController(getApplicationContext());
        uc.loadFromFile();
        if(uc.getCurrentUser() != null) {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the dummy account
        mAccount = CreateSyncAccount(this);
        // Get the content resolver for your app
        mResolver = getContentResolver();
        /*
         * Turn on periodic syncing
         */
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                SYNC_INTERVAL);
    }

    public void LoginClick(View view) {
        EditText usernameEditTxt = (EditText) findViewById(R.id.usernameInputTxt);
        final String desiredUsername = usernameEditTxt.getText().toString();

        //Only continue if the username field contains text
        if(desiredUsername.equals("")) {
            Toast toast = Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            //Create a GetUserByUsernameTask which includes an anonymous listener class to act on the data that
            //is retrieved from the server
            ElasticSearchUserController.GetUserByUsernameTask getUserByUsernameTask =
                    new ElasticSearchUserController.GetUserByUsernameTask(
                            new ESQueryListener() {
                                @Override
                                public void onQueryCompletion(List<?> results) {
                                    //If that username already exists on the server, we've already downloaded
                                    // their user object.  Set them to the current user.
                                    //results holds the user returned from the database.
                                    if (results.size() != 0) {
                                        User newUser = (User) results.get(0);
                                        uc.setCurrentUser(newUser);
                                    }
                                    //Else allow the user to create a new profile
                                    else {
                                        try {
                                            //Create a user with the desired username, and send them to
                                            //the edit profile activity
                                            uc.newUserLogin(desiredUsername, "", "", "", "");
                                            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    "We've experienced a problem with the server.  Please" +
                                                            "try again", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }
                                }
                            });
            getUserByUsernameTask.execute(desiredUsername);

            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }


    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return null;
    }

//    I don't think we need the three dot on main screen
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    //this is a method to check if network is available, it returns true if there is internet, false otherwise
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
