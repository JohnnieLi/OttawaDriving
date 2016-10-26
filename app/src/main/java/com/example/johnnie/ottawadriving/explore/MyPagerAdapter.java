package com.example.johnnie.ottawadriving.explore;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;

/**
 * Created by Johnnie on 2016-09-15.
 * This is a test class for page adapter, never used in this application
 */


class MyPagerAdapter extends PagerAdapter {

    final String[] TITLES = {"HOME", "Information", "Address", "FAQ"};
    private Context mContext;

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return 4;
    }

    /**
     * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
     * same object as the {@link View} added to the {@link ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
    // END_INCLUDE (pageradapter_getpagetitle)


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Inflate a new layout from our resources
        // Log.d("SLIDINGTABSBASIC", "instantiateItem position: "+Integer.toString(position));
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.explore_sliding_pager,
                container, false);


        // Add the newly created View to the ViewPager

        String[] categories = {"bmw","audi","mercedes"};
        // "HOME", "Information", "Address", "FAQ"
        TextView tvLabel = (TextView) view.findViewById(R.id.sliding_textView);
        switch (position) {
            case 0:
                tvLabel.setText(categories[position]);
                break;
            case 1:
                tvLabel.setText(categories[position]);
                break;
            case 2:
                tvLabel.setText(categories[position]);
                break;
            case 3:
                break;
        }
        container.addView(view);


        // Return the View
        return view;
    }

    /**
     * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
     * {@link View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Log.i(LOG_TAG, "destroyItem position: "+Integer.toString(position));
        container.removeView((View) object);
    }

}
