package com.example.johnnie.ottawadriving.explore;



import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.listcomponent.InsuranceFragment;
import com.example.johnnie.ottawadriving.listcomponent.LawyerFragment;
import com.example.johnnie.ottawadriving.listcomponent.LicenseTranFragment;
import com.example.johnnie.ottawadriving.listcomponent.DealerFragment;
import com.example.johnnie.ottawadriving.listcomponent.TrainingCourseFragment;
import com.example.johnnie.ottawadriving.localdatabase.PersonDbAdapter;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.example.johnnie.ottawadriving.utils.PersonPullParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExploreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DealerFragment.OnDealerFragmentInteractionListener,
        LicenseTranFragment.OnFragmentInteractionListener,
        TrainingCourseFragment.OnTrainingFragmentInteractionListener,
        InsuranceFragment.OnInsuranceFragmentInteractionListener,
        LawyerFragment.OnLawyerFragmentInteractionListener{

    private PersonDbAdapter dbHelper;
    ViewPager mViewPager;
    private ArrayList<PersonModel> mModels;
    private DealerFragment mFragment;
    Locale myLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //create sample database
        setLocalData();

        // drawer and navigation view
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //create the page view
        mViewPager = (ViewPager)findViewById(R.id.mainPager);
        if (mViewPager!=null){
            mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),this));
        }




    }

    //initialize database and load data
    public void setLocalData() {
        dbHelper = new PersonDbAdapter(this);
        dbHelper.open();
        List<PersonModel> models = dbHelper.fetchAllPeople();
        //dbHelper.deleteAllDealers();
        if (models.size() == 0) {
            createData();
            dbHelper.close();
        }

    }

    //read values form xml file with PersonPullParser
    private void createData() {
        PersonPullParser parser = new PersonPullParser();
        List<PersonModel> people = parser.parseXML(this);
        for (PersonModel person : people) {
            dbHelper.createDealer(person);
        }

    }


    // ======== back tab ======
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    // ======================= Menu =================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                break;
            case R.id.action_english:
                setLocale("en");
                break;
            case R.id.action_chinese:
                setLocale("zh");
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, ExploreActivity.class);
        startActivity(refresh);
    }


    // =========================NavigationItem=======================
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


// ======================Fragment Interfaces=======================================
    /*
    *   From DealerFragment
    *   so far never use this fragment interface, most fragment transition events has handled within PagerAdapter.
     */
    @Override
    public void OnDealerFragmentInteraction(DealerFragment fragment, String title) {

    }

    /*
       *   From LicenseTranFragment
       *   so far never use this fragment interface, most fragment transition events has handled within PagerAdapter.
        */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

           /*
           *   From TrainingCourseFragment
           *   so far never use this fragment interface, most fragment transition events has handled within PagerAdapter.
            */
    @Override
    public void onTrainingFragmentInteraction(Uri uri) {

    }
    /*
               *   From InsuranceFragment
               *   so far never use this fragment interface, most fragment transition events has handled within PagerAdapter.
                */
    @Override
    public void onInsuranceFragmentInteraction(Uri uri) {

    }
    /*
               *   From LawyerFragment
               *   so far never use this fragment interface, most fragment transition events has handled within PagerAdapter.
                */
    @Override
    public void onLawyerFragmentInteraction(Uri uri) {

    }

    // ======================Fragment Interfaces  End=======================================
}
