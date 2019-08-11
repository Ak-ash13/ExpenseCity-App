package com.example.tabactivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class TabThree extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView t1,t2,t3,t4,t5;
    Spinner sp;
    int count=0;
    String[] dates ;
    int price[];

    ViewFlipper vf;
    String category;
    int trip_id;
    int amount;
    ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_tab,container,false);
        t1 = view.findViewById(R.id.textView19);        //food
        t2 = view.findViewById(R.id.textView20);        //travels
        t3 = view.findViewById(R.id.textView26);        //shopping
        t4 = view.findViewById(R.id.textView24);        //hotels
        t5 = view.findViewById(R.id.textView27);        // others
        lv = view.findViewById(R.id.list1);

        PieChartView pieChartView = view.findViewById(R.id.chart);

        SQLiteDatabase db = getActivity().openOrCreateDatabase("Trip", android.content.Context.MODE_PRIVATE, null);

       trip_id = MainActivity.trip_id;

        String sql2 = "select * from expenseDetails where trip_id ="+trip_id ;

        Cursor cursor1 = db.rawQuery(sql2, null);
        int sum1=0;
        int sum2=0;
        int sum3=0;
        int sum4=0;
        int sum5=0;

        while (cursor1.moveToNext()) {
//
            category = cursor1.getString(2);
            if(category.equals("Food"))
            {
                amount = cursor1.getInt(3);
                sum1 = sum1 + amount;
            }
            if(category.equals("Travels"))
            {
                amount = cursor1.getInt(3);
                sum2 = sum2 + amount;
            }
            if(category.equals("Shopping"))
            {
                amount = cursor1.getInt(3);
                sum3 = sum3 + amount;
            }
            if(category.equals("Hotels"))
            {
                amount = cursor1.getInt(3);
                sum4 = sum4 + amount;
            }
            if(category.equals("Others"))
            {
                amount = cursor1.getInt(3);
                sum5 = sum5 + amount;
            }


        }



        t1.setText(" ₹ " + sum1);
        t2.setText(" ₹ " + sum2);
        t3.setText(" ₹ " + sum3);
        t4.setText(" ₹ " + sum4);
        t5.setText(" ₹ " + sum5);

        List pieData = new ArrayList<>();

        PieChartData pieChartData;

        if(sum1 == 0 && sum2 == 0 && sum3 == 0 && sum4 == 0 && sum5 == 0)
        {
            pieData.add(new SliceValue(100,Color.parseColor("#28A08D")));
            pieChartData = new PieChartData(pieData);
        }

        else {
            if (sum1 != 0) {
                pieData.add(new SliceValue(sum1, Color.parseColor("#28A08D")).setLabel("Food"));
            }
            if (sum2 != 0) {
                pieData.add(new SliceValue(sum2, Color.parseColor("#81124A")).setLabel("Travel"));
            }
            if (sum3 != 0) {
                pieData.add(new SliceValue(sum3, Color.parseColor("#ED7152")).setLabel("Shopping"));
            }
            if (sum4 != 0) {
                pieData.add(new SliceValue(sum4, Color.parseColor("#ECDF9D")).setLabel("Hotels"));
            }
            if (sum5 != 0) {
                pieData.add(new SliceValue(sum5, Color.parseColor("#0A3D1D")).setLabel("Others"));
            }

            pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true);


        }

        pieChartData.setHasCenterCircle(true).setCenterText1("By Category").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#000000"));
        pieChartView.setPieChartData(pieChartData);

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("Trip", android.content.Context.MODE_PRIVATE, null);

        String sql3 = "Select SUM(amount_spend), date from expenseDetails  where trip_id="+trip_id+ " group by date";

        Cursor cursor2 = db2.rawQuery(sql3, null);

        count = cursor2.getCount();
        price = new int[count];
        int k=0;
        dates = new String[count];
        while(cursor2.moveToNext())
        {
            price[k]= cursor2.getInt(0);
            dates[k] = cursor2.getString(1);
            k++;
        }

        SimpleAdapter adapter;

        List<HashMap<String, String>> bList = new ArrayList<HashMap<String, String>>();

        if(count == 0)
        {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("date","Date");
            hm.put("price","₹ Price");
            hm.put("pic",Integer.toString(R.drawable.date));
            bList.add(hm);
        }

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < dates.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("date",dates[i]);
            hm.put("price","₹ "+price[i]);
            hm.put("pic",Integer.toString(R.drawable.date));
            aList.add(hm);
        }
        String[] from = {"date", "price","pic"};
        int[] to = {R.id.textView14, R.id.textView15,R.id.imageView9};
        if(count == 0)
        {
            adapter = new SimpleAdapter(getActivity().getBaseContext(), bList, R.layout.daywise_layout, from, to);
        }
        else {
            adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.daywise_layout, from, to);
        }

        lv.setAdapter(adapter);


        setListViewHeightBasedOnChildren(lv);


        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {


        }
        if(position==1)
        {

        }
        if(position==2)
        {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void setListViewHeightBasedOnChildren
            (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;


        int desiredWidth =  View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount()-1))+130;

        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
