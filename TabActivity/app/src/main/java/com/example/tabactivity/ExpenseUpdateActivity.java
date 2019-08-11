package com.example.tabactivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ExpenseUpdateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnFocusChangeListener {

    Button b;
    EditText e,e1,e3;
    Spinner sp;
    String local_cat;
    String[] category= {"Select Your Category","Hotels","Food","Travels","Shop","Others"};
    String notes,date;
    int balance,id;
    int old_balance;
    int tid;
    String date_up;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_update);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        e = findViewById(R.id.editText);
        e1 = findViewById(R.id.editText1);
        e3 = findViewById(R.id.editText3);

        b = findViewById(R.id.button);
        sp = findViewById(R.id.spinner);
        ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,category);
        sp.setAdapter(ad);
        sp.setOnItemSelectedListener(this);
        e3.setOnClickListener(this);
        b.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        e.setText(b.getString("Notes"));
        //e1.setText(b.getInt("Balance"));

        awesomeValidation.addValidation(this, R.id.editText, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        //   awesomeValidation.addValidation(this, R.id.editText3, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.toerror);
        awesomeValidation.addValidation(this, R.id.editText3, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        //  awesomeValidation.addValidation(this, R.id.editText5, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        awesomeValidation.addValidation(this, R.id.editText1, Range.closed(0, 1000000000), R.string.balanceerror);



        int bal = b.getInt("Balance");
        e3.setText(b.getString("Date"));
        old_balance = b.getInt("Balance");
        tid = b.getInt("TID");
        id = b.getInt("E_Id");

        e.setOnFocusChangeListener(this);
        e1.setOnFocusChangeListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0)
        {
            local_cat= category[position];
        }
        else if(position==1)
        {
            local_cat= category[position];
        }
        else if(position==2)
        {
            local_cat= category[position];
        }
        else if(position==3)
        {
            local_cat= category[position];
        }
        else if(position==4)
        {
            local_cat= category[position];
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
           MyDateUpdate1 myDateUpdate = new MyDateUpdate1();
            DatePickerDialog dp = new DatePickerDialog(this,myDateUpdate, year, month, day);
            dp.show();
        }

        if (v == b) {

            if (awesomeValidation.validate()) {


                notes = e.getText().toString();
                date = e3.getText().toString();
                balance = Integer.parseInt(e1.getText().toString());

                SQLiteDatabase db = openOrCreateDatabase("Trip", MODE_PRIVATE, null);
                try {


                    String sql = "update  expenseDetails set notes='" + notes + "' ,date='" + date + "' , amount_spend = " + balance + ",category='" + local_cat + "'  where expense_id=" + id;


                    db.execSQL(sql);


                    int amount = balance - (old_balance);

                    String countQuery = "SELECT  * FROM tripDetails where trip_id =" + tid;

                    Cursor cursor = db.rawQuery(countQuery, null);
                    int current_bal_db = 0;

                    if (cursor.moveToFirst()) {
                        current_bal_db = Integer.parseInt(cursor.getString(cursor.getColumnIndex("balance")));
                    }


                    int update_bal = current_bal_db - amount;


                    String sql4 = "update  tripDetails set balance =" + update_bal + " where trip_id =" + tid;
                    db.execSQL(sql4);


                    Toast.makeText(this, "Updated Your Expense", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("fromid",tid);
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to Update " + e, Toast.LENGTH_SHORT).show();

                }

            }
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class MyDateUpdate1 implements  DatePickerDialog.OnDateSetListener{

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date_up = dayOfMonth+"/"+(month+1) +"/"+year;
            e3.setText(""+date_up);
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
