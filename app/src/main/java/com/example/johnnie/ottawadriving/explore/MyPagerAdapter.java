package com.example.johnnie.ottawadriving.explore;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.johnnie.ottawadriving.listcomponent.MyListFragment;


/**
 * Created by Johnnie on 2016-09-14.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "bmw","audi","mercedes" };
    Context context;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                MyListFragment fragment = new MyListFragment();
                return fragment;

            // Open FragmentTab2.java
            case 1:

                return null;

            // Open FragmentTab3.java
            case 2:

                return null;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
