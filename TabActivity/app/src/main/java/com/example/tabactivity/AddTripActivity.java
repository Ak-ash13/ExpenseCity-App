package com.example.tabactivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
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

import java.util.Calendar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;

public class AddTripActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    Button b1;
    EditText e1,e2,e3,e5,e4,e6;
    private AwesomeValidation awesomeValidation;
    String date_new;
    String channel_id = "RCPL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        b1 = findViewById(R.id.button);
        e1 = findViewById(R.id.editText);               //Trip name
        e2 = findViewById(R.id.editText2);              //From
        e3 = findViewById(R.id.editText3);              //To
        e4 = findViewById(R.id.editText4);              //start
        e5 = findViewById(R.id.editText5);              //end
        e6 = findViewById(R.id.editText6);              //budget

        Calendar cal = Calendar.getInstance() ;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        e4.setText(""+day+"/"+(month+1)+"/"+year);
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
            MyDate1 mydate1 = new MyDate1();
            DatePickerDialog dp1 =new DatePickerDialog(this,mydate1,year,month,day);
            dp1.show();

        }
        if(v==e5)
        {
            Calendar cal = Calendar.getInstance() ;
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            MyDate2 mydate2 = new MyDate2();
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


                SQLiteDatabase db = openOrCreateDatabase("Trip", MODE_PRIVATE, null);

                try {
                    String sql = "insert into tripDetails(trip_name,place_from,place_to,start_date,end_date,budget,balance) values('" + trip_name + "','" + place_from + "','" + place_to + "','" + start_date + "','" + end_date + "'," + budget + "," + budget + ")";

                    db.execSQL(sql);
                    Toast.makeText(this, "SuccessFully Registered a Trip", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed" + e, Toast.LENGTH_SHORT).show();

                }

                /* Adding Notification  */

                createChannel();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id);

                builder.setSmallIcon(R.drawable.ic_near_me_black_24dp);

                builder.setContentTitle("Your Trip Alive!");
                builder.setContentText("You have Successfully Added trip!! \nEnjoy Your Journey");
                builder.setOngoing(false);
                Intent i = new Intent(this, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pi);
                Notification n = builder.build();
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(101, n);


                startActivity(i);
            }
        }
    }

    private void createChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            String channel_name = "stp";
            String description = "for noti";
            NotificationChannel channel = new NotificationChannel(channel_id,channel_name,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);


        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public  class MyDate1 implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date_new = dayOfMonth+"/"+(month+1) +"/"+year;
            e4.setText(""+date_new);
        }
    }
    public  class MyDate2 implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date_new = dayOfMonth+"/"+(month+1) +"/"+year;
            e5.setText(""+date_new);
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
