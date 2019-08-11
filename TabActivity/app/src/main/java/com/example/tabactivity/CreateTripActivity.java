package com.example.tabactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateTripActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        tv = findViewById(R.id.textView7);

        tv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        Intent i = new Intent(this,AddTripActivity.class);
        startActivity(i);
    }
}
