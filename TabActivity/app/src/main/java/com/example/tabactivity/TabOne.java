package com.example.tabactivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Color;
import android.os.Bundle;


import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class TabOne  extends ListFragment implements PopupMenu.OnMenuItemClickListener {

    Intent intent;
    CoordinatorLayout coordinatorLayout;
    String[] category ;
    int delete_id;
    int [] amount_spend ;
    int [] trip_id ;
    String[] date ;
    String[] notes ;
    int[] images ;
    int[] e_id;
    int delete_amount;
    int tid;
    int count;
    String ldate;
    ListView lv;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        SQLiteDatabase db = getActivity().openOrCreateDatabase("Trip", android.content.Context.MODE_PRIVATE, null);

        tid = MainActivity.trip_id;


        String sql = "Select * from tripdetails where trip_id="+tid;
        Cursor c2 = db.rawQuery(sql, null);

        while (c2.moveToNext()) {

            ldate = c2.getString(5);
        }





        String countQuery = "SELECT  * FROM expenseDetails where trip_id ="+tid+" order by expense_id desc";
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

         category = new String[count];
         amount_spend = new int[count];
        trip_id = new int[count];
        date = new String[count];
        notes = new String[count];
        images = new int[count];
        e_id = new int[count];
//
//
        Cursor c = db.rawQuery(countQuery, null);
        int j = 0;
        while (c.moveToNext()) {
//
           // int  expense_id1 = Integer.parseInt(c.getString(0));
            String notes1 = c.getString(1);
            String category1 = c.getString(2);
            int  amount_spend1 = c.getInt(3);
            String date1 = c.getString(4);
            int trip_id1 = Integer.parseInt(c.getString(5));
            int expense_id1 = c.getInt(0);

            if (category1.equals("Food")) {
                images[j] = R.drawable.asd3;
            } else if (category1.equals("Travels")) {
                images[j] = R.drawable.flight;
            } else if (category1.equals("Shopping")) {
                images[j] = R.drawable.shop;
            }else if(category1.equals("Hotels"))
            {
                images[j] = R.drawable.hotel;
            }
            else
            {
                images[j] = R.drawable.asd2;
            }


            category[j] = category1;
            amount_spend[j] = amount_spend1;
            trip_id[j] = trip_id1;
            date[j] = date1;
            notes[j] = notes1;
            e_id[j] =  expense_id1;
            j++;
//
//
        }
//



        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < category.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", "Notes : " + notes[i]);
            hm.put("cur", "Date : " + date[i]);
            hm.put("flag","₹ "+ amount_spend[i]);
            hm.put("pic", Integer.toString(images[i]));

            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"flag", "txt", "cur", "pic"};

        // Ids of views in listview_layout
        int[] to = {R.id.textView8, R.id.textView, R.id.textView4, R.id.imageView};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date1= ""+day+"/"+(month+1)+"/"+year;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        Date strDate2 = null;


        try {
            strDate = sdf.parse(ldate);
            strDate2 = sdf.parse(date1);
        } catch (ParseException e) {
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }


        if(strDate2.compareTo(strDate)<=0) {

            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.menu_list_expense);
            popup.show();

        }
        else
        {
            Snackbar snackbar = Snackbar.make(getView(), "Your Trip has Ended!! No Modifications Allowed.", Snackbar.LENGTH_LONG)
                      .setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor("#eeeeee"));

            snackbar.show();
        }



        intent = new Intent(getActivity(),ExpenseUpdateActivity.class);
        intent.putExtra("Notes",notes[position]);
        intent.putExtra("Balance",amount_spend[position]);
        intent.putExtra("Date",date[position]);
        intent.putExtra("Category",category[position]);
        intent.putExtra("E_Id",e_id[position]);
        intent.putExtra("TID",tid);


        delete_id =e_id[position];
        delete_amount = amount_spend[position];


    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.edit: {

                startActivity(intent);
                return true;
            }

            case R.id.delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Delete");
                builder.setMessage("Are You Sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = getActivity().openOrCreateDatabase("Trip", android.content.Context.MODE_PRIVATE, null);
                        try {
                            String sql = "delete from expenseDetails where expense_id= "+delete_id;

                            db.execSQL(sql);

                            String countQuery = "SELECT  * FROM tripDetails where trip_id ="+tid;

                            Cursor cursor = db.rawQuery(countQuery, null);

                            int current_bal_db = 0;

                            if(cursor.moveToFirst()) {
                                current_bal_db = Integer.parseInt(cursor.getString(cursor.getColumnIndex("balance")));
                            }



                            int update_bal = current_bal_db + delete_amount ;

                            String sql4 = "update  tripDetails set balance ="+ update_bal+ " where trip_id ="+tid;
                            db.execSQL(sql4);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(), "Failed to Delete "+e, Toast.LENGTH_LONG).show();

                        }
                        Intent intent2 = getActivity().getIntent();
                        getActivity().finish();
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        if(count==0)
        {
            listView.setBackgroundResource(R.drawable.capture);


        }
        else
        {

        }
        //listView.setDividerHeight(3); // 3 pixels height
    }


}




