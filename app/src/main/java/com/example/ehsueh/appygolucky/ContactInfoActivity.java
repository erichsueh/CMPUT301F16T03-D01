package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactInfoActivity extends Activity {
    //private UserController uc;
    private TextView dName;
    private TextView dEmail;
    private TextView dPhone;
    private TextView dAddress;
    private TextView dRideDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        dName = (TextView) findViewById(R.id.dname);
        dEmail = (TextView) findViewById(R.id.dmail);
        dPhone = (TextView) findViewById(R.id.dphone);
        dAddress = (TextView) findViewById(R.id.daddress);
        dRideDescription=(TextView) findViewById(R.id.dride);

        //uc = new UserController(getApplicationContext());
        Intent intent = getIntent();
        String theDriver = intent.getStringExtra("driveruser");
        ElasticSearchUserController.GetUsersByUsernameTask getUsersByUsernameTask =
                new ElasticSearchUserController.GetUsersByUsernameTask(
                        new ESQueryListener() {
                            @Override
                            public void onQueryCompletion(List<?> results) {
                                //If the result comes back null, it means there was a
                                // network error
                                if (results == null) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "We couldn't contact the server.  Please check your " +
                                                    "connectivity and try again", Toast.LENGTH_SHORT);
                                    toast.show();
                                    finish();
                                } else {
                                    //If that username already exists on the server, we've already downloaded
                                    // their user object.  Set them to the current user.
                                    //results holds the user returned from the database.

                                    User newUser = (User) results.get(0);
                                    //uc.existingUserLogin(newUser);
                                    try {
                                        dName.setText(newUser.getName());
                                        dEmail.setText(newUser.getEmail());
                                        dPhone.setText(newUser.getPhone());
                                        dAddress.setText(newUser.getAddress());
                                        dRideDescription.setText(newUser.getRideDescription());
                                    }
                                    catch (Exception e){
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "We've experienced a problem with the server. " +
                                                        "Please" +
                                                        "try again", Toast.LENGTH_SHORT);
                                        toast.show();
                                        finish();
                                    }
                                }
                            }
                        });
        getUsersByUsernameTask.execute(theDriver);
    }
public void contback(View view){
    finish();
}
}
