package com.example.tabactivity.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tabactivity.MainActivity;
import com.example.tabactivity.R;
import com.example.tabactivity.TabOne;
import com.example.tabactivity.TabThree;
import com.example.tabactivity.TabTwo;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0 :


                TabOne tabOne = new TabOne();
                return tabOne;

            case 1 :
                TabTwo tabTwo =new TabTwo();
                return tabTwo;
            case 2 :
                TabThree tabThree  = new TabThree();
                return tabThree;
            default: return null;

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}