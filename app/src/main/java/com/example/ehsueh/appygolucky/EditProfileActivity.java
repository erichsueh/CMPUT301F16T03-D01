package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends ActionBarActivity {
    private EditText editName;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editAddress;
    private EditText editRideDescription;
    private UserController uc;
    private boolean isNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        uc = new UserController(getApplicationContext());
        Intent intent = getIntent();
        isNewUser = intent.getBooleanExtra("isNew",false);
        editName  = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editRideDescription = (EditText) findViewById(R.id.editRideDescription);
        editName.setText(uc.getCurrentUser().getName(), TextView.BufferType.EDITABLE);
        editEmail.setText(uc.getCurrentUser().getEmail(), TextView.BufferType.EDITABLE);
        editPhone.setText(uc.getCurrentUser().getPhone(), TextView.BufferType.EDITABLE);
        editAddress.setText(uc.getCurrentUser().getAddress(),TextView.BufferType.EDITABLE);
        editRideDescription.setText(uc.getCurrentUser().getRideDescription(),TextView.BufferType.EDITABLE);
    }

    public void SaveEdits(View view) {
        if (isNewUser == true){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Created New User, please log in again with the same username.",Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Information saved!",Toast.LENGTH_SHORT);
            toast.show();
        }
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();
        String address = editAddress.getText().toString();
        String rideDescription = editRideDescription.getText().toString();
        uc.editProfile(email,phone,address,name,rideDescription);

        finish();
    }
}
