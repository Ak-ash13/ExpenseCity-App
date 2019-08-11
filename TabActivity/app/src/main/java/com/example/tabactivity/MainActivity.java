package com.example.tabactivity;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabactivity.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements LocationListener {

    public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
    public static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
    public static final String PINTEREST_PACKAGE_NAME = "com.pinterest";
    public static final String WHATS_PACKAGE_NAME =  "com.whatsapp";

    double lat;
    Date strDate;
    Date strDate2;
    double log;
    ProgressBar progressBar;
    protected static int trip_id;
    String trip_name;
    String ldate,date1;
    TextView t3,t2;
    int t_budget,left;
    LocationManager lm;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        Bundle bundle = getIntent().getExtras();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.newh);

        getSupportActionBar().setElevation(0);
        progressBar = findViewById(R.id.progressBar2);
        t3 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView2);
        t3.setTextColor(Color.WHITE);
        t2.setTextColor(Color.WHITE);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundColor(Color.parseColor("#ffffff"));
        SQLiteDatabase db = openOrCreateDatabase("Trip",MODE_PRIVATE,null);

     if(bundle == null) {
         String sql = "Select * from tripdetails";
         Cursor cursor = db.rawQuery(sql, null);

         while (cursor.moveToNext()) {
//
             trip_id = cursor.getInt(0);
             t_budget = cursor.getInt(6);
             left = cursor.getInt(7);
             trip_name = cursor.getString(1);
             ldate = cursor.getString(5);
         }
         t2.setText("LEFT: ₹" + left);
         int left2 = t_budget - left;
         t3.setText(" ₹ " + left2);
         if (trip_name == null) {
             actionBar.setTitle("No Trips");
         } else {
             actionBar.setTitle("" + trip_name);
         }
         progressBar.setMax(t_budget);
         progressBar.setProgress(left2);

     }
     else
     {
         trip_id = bundle.getInt("fromid");
         String sql = "Select * from tripdetails where trip_id="+trip_id;
         Cursor cursor = db.rawQuery(sql, null);

         while (cursor.moveToNext()) {

             t_budget = cursor.getInt(6);
             left = cursor.getInt(7);
             trip_name = cursor.getString(1);
             ldate = cursor.getString(5);

         }
         t2.setText("LEFT: ₹" + left);
         int left2 = t_budget - left;
         t3.setText(" ₹ " + left2);

         actionBar.setTitle("" + trip_name);

         progressBar.setMax(t_budget);
         progressBar.setProgress(left2);

     }

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date1= ""+day+"/"+(month+1)+"/"+year;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        strDate = null;
        strDate2 = null;


        try {
            strDate = sdf.parse(ldate);
            strDate2 = sdf.parse(date1);
        } catch (ParseException e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }




            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(strDate2.compareTo(strDate)<=0) {

                        Intent i = new Intent(MainActivity.this, AddExpenseActivity.class);
                        i.putExtra("id", trip_id);
                        startActivity(i);
                    }

                    else
                    {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Your Trip has Ended!! You can't add Expense Now! .", Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.parseColor("#eeeeee"));

                        snackbar.show();
                    }



                }
            });





        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);





    }

    @Override
    public void onLocationChanged(Location location) {
         lat = location.getLatitude();
         log = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disbaled", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(MainActivity.this,ViewAllTripActivity.class);
                startActivity(i);
                return true;

            case R.id.share:
                shareIt();
                
                return true;
            case R.id.maps:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+lat+"&daddr="+log));
                startActivity(intent);
                return true;
            case R.id.about:
                String number = "+91 9702620581";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url));
                startActivity(intent1);

            case R.id.exit:
                finishAffinity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareIt() {
        PackageManager pm = getPackageManager();
        ApplicationInfo appInfo;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("*/*");

        try {

            appInfo = pm.getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);

            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ExpenceCity - Manage your Trip Expenses here");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+appInfo.publicSourceDir));
            startActivity(Intent.createChooser(sharingIntent, "Share via"));


        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(0).getSubMenu().getItem(3).setVisible(false);
        menu.getItem(0).getSubMenu().getItem(4).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }



}