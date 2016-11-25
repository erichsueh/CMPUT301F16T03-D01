package com.example.ehsueh.appygolucky;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends ActionBarActivity {
    private EditText editName;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editAddress;
    private UserController uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        uc = new UserController(getApplicationContext());
        editName  = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editName.setText(uc.getCurrentUser().getName(), TextView.BufferType.EDITABLE);
        editEmail.setText(uc.getCurrentUser().getEmail(), TextView.BufferType.EDITABLE);
        editPhone.setText(uc.getCurrentUser().getPhone(), TextView.BufferType.EDITABLE);
        editAddress.setText(uc.getCurrentUser().getAddress(),TextView.BufferType.EDITABLE);
    }

    public void SaveEdits(View view) {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();
        String address = editAddress.getText().toString();

        uc.editProfile(email,phone,address,name);

        finish();
    }
}
