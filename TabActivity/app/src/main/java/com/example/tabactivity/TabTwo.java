package com.example.tabactivity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabTwo extends Fragment implements View.OnClickListener {


TextView t1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.two_tab,container,false);

           t1 = view.findViewById(R.id.textView7);
           t1.setOnClickListener(this);



        return view;
    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(),AddTripActivity.class);
        startActivity(i);
    }
}
