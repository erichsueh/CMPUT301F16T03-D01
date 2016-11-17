package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditProfileActivity extends ActionBarActivity {
    private EditText bodyText1;
    private EditText bodyText2;
    private EditText bodyText3;
    private UserController uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        uc = new UserController(getApplicationContext());
    }

    public void AddRequest(View view) {
        bodyText1 = (EditText) findViewById(R.id.editText3);
        String email = bodyText1.getText().toString();

        bodyText2 = (EditText) findViewById(R.id.editText3);
        String phone = bodyText2.getText().toString();

        bodyText3 = (EditText) findViewById(R.id.editText3);
        String address = bodyText3.getText().toString();

        uc.editProfile(email,phone,address);

        finish();
    }
}
