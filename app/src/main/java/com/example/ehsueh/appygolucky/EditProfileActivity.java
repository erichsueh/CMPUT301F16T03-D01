package com.example.ehsueh.appygolucky;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends ActionBarActivity {
    private EditText bodyText1;
    private EditText bodyText2;
    private EditText bodyText3;
    private EditText bodyText4;
    private UserController uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        uc = new UserController(getApplicationContext());
        bodyText1 = (EditText) findViewById(R.id.editText2);
        bodyText2 = (EditText) findViewById(R.id.editText3);
        bodyText3 = (EditText) findViewById(R.id.editText4);
        bodyText4 = (EditText) findViewById(R.id.editText5);
        bodyText1.setText(uc.getCurrentUser().getName(), TextView.BufferType.EDITABLE);
        bodyText2.setText(uc.getCurrentUser().getEmail(), TextView.BufferType.EDITABLE);
        bodyText3.setText(uc.getCurrentUser().getPhone(), TextView.BufferType.EDITABLE);
        bodyText4.setText(uc.getCurrentUser().getAddress(),TextView.BufferType.EDITABLE);
    }

    public void SaveEdits(View view) {
        String name = bodyText1.getText().toString();
        String email = bodyText2.getText().toString();
        String phone = bodyText3.getText().toString();
        String address = bodyText4.getText().toString();

        uc.editProfile(email,phone,address,name);

        finish();
    }
}
