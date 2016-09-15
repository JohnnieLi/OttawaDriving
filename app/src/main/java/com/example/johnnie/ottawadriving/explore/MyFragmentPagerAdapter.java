package com.example.johnnie.ottawadriving.explore;



import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.johnnie.ottawadriving.listcomponent.MyListFragment;
import com.example.johnnie.ottawadriving.listcomponent.TestFragment;
import com.example.johnnie.ottawadriving.model.PersonModel;

import java.util.ArrayList;


/**
 * Created by Johnnie on 2016-09-14.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private Fragment fragment;
    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "bmw","audi","mercedes" };
    Context context;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        //return TestFragment.newInstance(position);
        MyListFragment listFragment = new MyListFragment();
       // listFragment.displayListView(models);
        return listFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
