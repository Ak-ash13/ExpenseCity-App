package com.example.tabactivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(4000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {

                    SQLiteDatabase db = openOrCreateDatabase("Trip",MODE_PRIVATE,null);
                    String sql = "create table if not exists tripDetails (trip_id integer primary key AutoIncrement, trip_name varchar not null, place_from varchar not null, place_to varchar not null,start_date varchar not null, end_date varchar not null,budget integer not null, balance integer not null)";
                    db.execSQL(sql);

                    String sql2 = "create table if not exists expenseDetails(expense_id integer primary key AutoIncrement, notes varchar ,category varchar not null, amount_spend integer not null, date varchar not null, trip_id integer, FOREIGN KEY (trip_id) REFERENCES tripDetails(trip_id))";
                     db.execSQL(sql2);


                    String countQuery = "SELECT  * FROM tripDetails";

                    Cursor cursor = db.rawQuery(countQuery, null);
                    int count = cursor.getCount();


                    cursor.close();

                    if(count == 0) {
                        Intent i = new Intent(SplashActivity.this,
                                CreateTripActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(SplashActivity.this,
                                MainActivity.class);
                        startActivity(i);

                    }
                    finish();
                }
            }
        };
        welcomeThread.start();

    }
}
