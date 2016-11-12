package com.example.ehsueh.appygolucky;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class AddRequest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
    }

    public void ClickGo(View view) {
        finish();}
}
