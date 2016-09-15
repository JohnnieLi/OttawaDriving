package com.example.johnnie.ottawadriving.explore;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.listcomponent.MyListFragment;

/**
 * Created by Johnnie on 2016-09-12.
 */
public class ExploreSlidingBasicFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";


    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    //private SlidingBasicLayout mSlidingTabLayout;


    private ViewPager mViewPager;


    public interface  basicFragmentPageSelectedListener{

        public void onBasicFragmentPageSelected(TextView view,String category);
    }



    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_sliding, container, false);
        return inflater.inflate(R.layout.fragment_pages, container, false);

    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.vpPager);

        //use MyPagerAdapter
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        //use inner adapter
       // mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)




    }


    private basicFragmentPageSelectedListener  mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (basicFragmentPageSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }



    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     *
     */
    class SamplePagerAdapter extends PagerAdapter {
        final String[] TITLES = {"HOME", "Information", "Address", "FAQ"};

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

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         *
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Log.d("SLIDINGTABSBASIC", "instantiateItem position: "+Integer.toString(position));

            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.explore_sliding_pager,
                    container, false);
            // Add the newly created View to the ViewPager

            MyListFragment listFragment = new MyListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.pager_fragment_container,listFragment)
                    .commit();
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mCallback.onBasicFragmentPageSelected("ss");
//                }
//            });
            String[] categories = {"bmw","audi","mercedes"};
            // "HOME", "Information", "Address", "FAQ"
            TextView tvLabel = (TextView) view.findViewById(R.id.sliding_textView);
            switch (position) {
                case 0:
                    mCallback.onBasicFragmentPageSelected(tvLabel,categories[0]);
                    break;
                case 1:
                    mCallback.onBasicFragmentPageSelected(tvLabel,categories[1]);
                    break;
                case 2:
                    mCallback.onBasicFragmentPageSelected(tvLabel,categories[2]);
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


}

