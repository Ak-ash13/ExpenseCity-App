package com.example.tabactivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ViewAllTripActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    Button b1;
    String[] places;
    String[] dates;
    String[] amounts;
    String[] tname;
    String[] from;
    String[] to;
    String[] sdate;
    String[] edate;
    int[] abudget;
    int[] img;
    int bimages[] = {R.drawable.scene1,R.drawable.scene2,R.drawable.scene3,R.drawable.scene4,R.drawable.scene5};
    int[] tripid;
    int delete_id;
    int count;
    ListView lv;
    Intent intent,intent3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_trip);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        intent = new Intent(this, UpdateTripActivity.class);
        intent3 = new Intent(this,MainActivity.class);

        SQLiteDatabase db = openOrCreateDatabase("Trip", MODE_PRIVATE, null);
        String sql = "select * from tripDetails order by trip_id desc";

        Cursor c = db.rawQuery(sql, null);

        count = c.getCount();

        if(count == 0)
        {
            setContentView(R.layout.noexpense_layout);
            FloatingActionButton fab = findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(ViewAllTripActivity.this, AddTripActivity.class);
                    startActivity(i);


                }
            });
        }
        else {
            setContentView(R.layout.activity_view_all_trip);


            lv = findViewById(R.id.list);


            FloatingActionButton fab = findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(ViewAllTripActivity.this, AddTripActivity.class);
                    startActivity(i);


                }
            });

            Random random = new Random();

            places = new String[count];
            dates = new String[count];
            amounts = new String[count];
            img = new int[count];
            tname = new String[count];
            tripid = new int[count];
            from = new String[count];
            to = new String[count];
            sdate = new String[count];
            edate = new String[count];
            abudget = new int[count];
            int j = 0;
            while (c.moveToNext()) {
                int trip_id = c.getInt(0);
                String trip_name = c.getString(1);
                String place_from = c.getString(2);
                String place_to = c.getString(3);
                String start_date = c.getString(4);
                String end_date = c.getString(5);
                int budget = c.getInt(6);
                int balance = c.getInt(7);

                String place = place_from + " - " + place_to;
                String date = start_date + " - " + end_date;
                String money = (budget-balance) + " / " + budget;

                from[j] = place_from;
                to[j] = place_to;
                sdate[j] = start_date;
                edate[j] = end_date;
                abudget[j] = budget;
                tname[j] = trip_name;

                places[j] = place;
                dates[j] = date;
                amounts[j] = money;
                tripid[j] = trip_id;
                img[j] = bimages[j%5];

                j++;

            }

            c.close();


            ArrayAdapter ad = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, tname);

            lv.setAdapter(ad);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view: {

                startActivity(intent3);
                return true;
            }
            case R.id.edit: {

                startActivity(intent);
               return true;
            }

            case R.id.delete: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Delete");
                builder.setMessage("Are You Sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = openOrCreateDatabase("Trip", android.content.Context.MODE_PRIVATE, null);
                        try {
                            String sql = "delete from tripDetails where trip_id= "+delete_id;

                            db.execSQL(sql);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(ViewAllTripActivity.this, ""+e, Toast.LENGTH_SHORT).show();

                        }
                        Intent intent2 = getIntent();
                        finish();
                        startActivity(intent2);

                    }
                });
                builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
                return true;
            }
            default:
                return true;


        }


    }


    private class MyArrayAdapter extends ArrayAdapter {
        public MyArrayAdapter(ViewAllTripActivity a, int b, String[] a1) {
            super(a, b, a1);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.viewtrip_layout, parent, false);

            TextView t1 = v.findViewById(R.id.textView9);
            TextView t2 = v.findViewById(R.id.textView10);
            TextView t3 = v.findViewById(R.id.textView11);
            TextView t4 = v.findViewById(R.id.textView12);
            b1 = v.findViewById(R.id.button3);

            ImageView im = v.findViewById(R.id.imageView4);
            im.setImageResource(img[position]);

            ImageView im2 = v.findViewById(R.id.imageView5);
            im2.setImageResource(R.drawable.destination);

            ImageView im3 = v.findViewById(R.id.imageView6);
            im3.setImageResource(R.drawable.date);

            ImageView im4 = v.findViewById(R.id.imageView7);
            im4.setImageResource(R.drawable.budget);

            t1.setText(tname[position]);

            t2.setText(places[position]);

            t3.setText(dates[position]);

            t4.setText(amounts[position]);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(getContext(), v);
                    popup.setOnMenuItemClickListener(ViewAllTripActivity.this);
                    popup.inflate(R.menu.trip_menu);
                    popup.show();

                    intent3.putExtra("fromid",tripid[position]);
                    intent.putExtra("name",tname[position]);
                    intent.putExtra("from",from[position]);
                    intent.putExtra("to",to[position]);
                    intent.putExtra("sdate",sdate[position]);
                    intent.putExtra("edate",edate[position]);
                    intent.putExtra("abudget",abudget[position]);
                    intent.putExtra("tripid",tripid[position]);


                    delete_id = tripid[position];
                }
            });


            return v;
        }
    }

}


