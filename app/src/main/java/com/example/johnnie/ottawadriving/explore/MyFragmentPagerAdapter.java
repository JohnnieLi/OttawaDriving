package com.example.johnnie.ottawadriving.explore;



import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.johnnie.ottawadriving.listcomponent.MyListFragment;


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

        // pass title to listFragment
        MyListFragment listFragment = MyListFragment.newInstance(tabtitles[position]);
        return listFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
