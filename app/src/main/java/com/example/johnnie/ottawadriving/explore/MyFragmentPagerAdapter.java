package com.example.johnnie.ottawadriving.explore;



import android.content.Context;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.example.johnnie.ottawadriving.listcomponent.MyListFragment;
import com.example.johnnie.ottawadriving.localdatabase.PersonDbAdapter;
import com.example.johnnie.ottawadriving.model.PersonModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Johnnie on 2016-09-14.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private MyListFragment mMyListFragment;
    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "bmw","audi","mercedes" };
    Context context;
    PersonDbAdapter mDbHelper;

    public MyFragmentPagerAdapter(FragmentManager fm, PersonDbAdapter dbHelper) {
        super(fm);
        mDbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("MYFRAGMENTADAPTER", "position: "+Integer.toString(position));
        switch (position){
            case 0:
                mMyListFragment =  MyListFragment.newInstance("bmw");
                break;
            case 1:
                mMyListFragment =  MyListFragment.newInstance("audi");

                break;
            case 2:
                mMyListFragment =  MyListFragment.newInstance("mercedes");

                break;
            default:
                mMyListFragment =  MyListFragment.newInstance("bmw");

        }


        return mMyListFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
