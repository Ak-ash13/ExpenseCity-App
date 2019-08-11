package com.example.tabactivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    private AwesomeValidation awesomeValidation;
    Button b;
    EditText e, e1, e3;
    Spinner sp;
    String[] category = {"Select Your Category", "Hotels", "Food", "Travels", "Shopping", "Others"};
    String local_cat = "";
    int trip_id;
    String date_ex;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        Bundle b1 = getIntent().getExtras();
        trip_id = b1.getInt("id");
        e = findViewById(R.id.editText);   // notes
        e1 = findViewById(R.id.editText1);  //balance
        e3 = findViewById(R.id.editText3);   //date
        Calendar cal = Calendar.getInstance() ;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        e3.setText(""+day+"/"+(month+1)+"/"+year);


        b = findViewById(R.id.button);
        sp = findViewById(R.id.spinner);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, category);
        sp.setAdapter(ad);
        sp.setOnItemSelectedListener(this);
        e3.setOnClickListener(this);
        b.setOnClickListener(this);

        awesomeValidation.addValidation(this, R.id.editText, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
     //   awesomeValidation.addValidation(this, R.id.editText3, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.toerror);
        awesomeValidation.addValidation(this, R.id.editText3, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
      //  awesomeValidation.addValidation(this, R.id.editText5, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        awesomeValidation.addValidation(this, R.id.editText1, Range.closed(0, 1000000000), R.string.balanceerror);


        e.setOnFocusChangeListener(this);
        e1.setOnFocusChangeListener(this);
        e3.setOnFocusChangeListener(this);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            local_cat = "Others";
        } else if (position == 1) {
            local_cat = category[position];
        } else if (position == 2) {
            local_cat = category[position];
        } else if (position == 3) {
            local_cat = category[position];
        } else if (position == 4) {
            local_cat = category[position];
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (v == e3) {
            Calendar cal = Calendar.getInstance() ;
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
           MyDateExpense myDate = new MyDateExpense();
            DatePickerDialog dp = new DatePickerDialog(this,myDate, year, month, day);
            dp.show();
        }
        if (v == b) {

            if (awesomeValidation.validate()) {


                String notes = e.getText().toString();
                int amount = Integer.parseInt(e1.getText().toString());
                String date = e3.getText().toString();

                String category = local_cat;

                SQLiteDatabase db = openOrCreateDatabase("Trip", MODE_PRIVATE, null);
                String sql2 = "insert into expenseDetails(notes,category,amount_spend,date,trip_id) values('" + notes + "','" + category + "','" + amount + "','" + date + "'," + trip_id + ")";

                db.execSQL(sql2);

                String countQuery = "SELECT  * FROM tripDetails where trip_id =" + trip_id;

                Cursor cursor = db.rawQuery(countQuery, null);
                int current_bal = -3;

                if (cursor.moveToFirst()) {
                    current_bal = Integer.parseInt(cursor.getString(cursor.getColumnIndex("balance")));
                }


                int update_bal = current_bal - amount;


                String sql4 = "update  tripDetails set balance =" + update_bal + " where trip_id =" + trip_id;
                db.execSQL(sql4);

                Toast.makeText(this, "You have Added an Expense!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("fromid",trip_id);
                startActivity(i);
            }

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class MyDateExpense  implements  DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date_ex = dayOfMonth+"/"+(month+1) +"/"+year;
            e3.setText(""+date_ex);
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

