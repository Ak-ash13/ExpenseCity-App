package com.example.tabactivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;

import java.util.Calendar;

public class UpdateTripActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    Button b1;
    EditText e1,e2,e3,e5,e4,e6;
    int tripid;
    int left,budg,left2;
    String date_new;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_trip);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        Bundle bundle = getIntent().getExtras();

            b1 = findViewById(R.id.button);
            e1 = findViewById(R.id.editText);               //Trip name
            e2 = findViewById(R.id.editText2);              //From
            e3 = findViewById(R.id.editText3);              //To
            e4 = findViewById(R.id.editText4);              //start
            e5 = findViewById(R.id.editText5);              //end
            e6 = findViewById(R.id.editText6);              //budget






            e1.setText(bundle.getString("name"));
            e2.setText(bundle.getString("from"));
            e3.setText(bundle.getString("to"));
            e4.setText(bundle.getString("sdate"));
            e5.setText(bundle.getString("edate"));
            e6.setText(""+bundle.getInt("abudget"));
            tripid = bundle.getInt("tripid");

            b1.setOnClickListener(this);
            e4.setOnClickListener(this);
            e5.setOnClickListener(this);


        awesomeValidation.addValidation(this, R.id.editText, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editText2, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.fromerror);
        awesomeValidation.addValidation(this, R.id.editText3, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.toerror);
        awesomeValidation.addValidation(this, R.id.editText4, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        awesomeValidation.addValidation(this, R.id.editText5, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        awesomeValidation.addValidation(this, R.id.editText6, Range.closed(0, 1000000000), R.string.budgeterror);

        e1.setOnFocusChangeListener(this);
        e2.setOnFocusChangeListener(this);
        e3.setOnFocusChangeListener(this);
        e6.setOnFocusChangeListener(this);

    }

        @Override
        public void onClick(View v) {

            if(v==e4)
            {
                Calendar cal = Calendar.getInstance() ;
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                UpdateTripActivity.MyDate1 mydate1 = new UpdateTripActivity.MyDate1();
                DatePickerDialog dp1 =new DatePickerDialog(this,mydate1,year,month,day);
                dp1.show();

            }
            if(v==e5)
            {
                Calendar cal = Calendar.getInstance() ;
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                UpdateTripActivity.MyDate2 mydate2 = new UpdateTripActivity.MyDate2();
                DatePickerDialog dp2 =new DatePickerDialog(this,mydate2,year,month,day);
                dp2.show();

            }
            if (v == b1) {


                if (awesomeValidation.validate()) {
                    String trip_name = e1.getText().toString();
                    String place_from = e2.getText().toString();
                    String place_to = e3.getText().toString();
                    String start_date = e4.getText().toString();
                    String end_date = e5.getText().toString();
                    int budget = Integer.parseInt(e6.getText().toString());

                    // Toast.makeText(this, "" + trip_name + "\n" + place_from + "\n" + place_to + "\n" + start_date + "\n" + end_date + "\n" + budget, Toast.LENGTH_SHORT).show();
                    SQLiteDatabase db = openOrCreateDatabase("Trip", MODE_PRIVATE, null);

                    String sql3 = "Select * from tripdetails where trip_id=" + tripid;

                    Cursor cursor = db.rawQuery(sql3, null);

                    while (cursor.moveToNext()) {
//
                        budg = cursor.getInt(6);
                        left = cursor.getInt(7);
                    }

                    left2 = budget - (budg - left);


                    try {

                        String sql = "update  tripDetails set trip_name='" + trip_name + "' ,place_from='" + place_from + "',place_to='" + place_to + "',start_date='" + start_date + "',end_date='" + end_date + "' , budget = " + budget + ", balance = " + left2 + " where trip_id=" + tripid;
                        db.execSQL(sql);
                        Toast.makeText(this, "SuccessFully Updated", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(this, ViewAllTripActivity.class);
                        startActivity(intent2);

                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to Update"  + e, Toast.LENGTH_SHORT).show();

                    }


                }
            }
        }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public  class MyDate1 implements DatePickerDialog.OnDateSetListener {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date_new = dayOfMonth+"/"+(month+1) +"/"+year;
                e4.setText(date_new);
            }
        }
        public  class MyDate2 implements DatePickerDialog.OnDateSetListener {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date_new = dayOfMonth+"/"+(month+1) +"/"+year;
                e5.setText(date_new);
            }
        }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            hideKeyboard(v);
        }
    }

    }

