package com.example.johnnie.ottawadriving.explore;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.listpagecomponent.UniversalTemplateFragment;
import com.example.johnnie.ottawadriving.listpagecomponent.DealerFragment;
import com.example.johnnie.ottawadriving.localdatabase.PersonDbAdapter;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.example.johnnie.ottawadriving.userlogin.LogInPageActivity;
import com.example.johnnie.ottawadriving.view.PagerSlidingTabStrip;
import java.util.ArrayList;
import java.util.Locale;

public class ExploreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        //DealerFragment.OnDealerFragmentInteractionListener,
        UniversalTemplateFragment.OnFragmentInteractionListener {
       // TrainingCourseFragment.OnTrainingFragmentInteractionListener,
       // InsuranceFragment.OnInsuranceFragmentInteractionListener,
       // LawyerFragment.OnLawyerFragmentInteractionListener{

    //    private ArrayList<PersonModel> mModels;
//    private DealerFragment mFragment;
//    private PersonDbAdapter dbHelper;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip tabs;
    private Locale myLocale;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        sharedPref = ExploreActivity.this.getSharedPreferences("FEDSa", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // drawer and navigation view
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set login on clicked event
        View headerLayout = navigationView.getHeaderView(0);
        View loginPart = headerLayout.findViewById(R.id.login_part);


        String token = sharedPref.getString("token", "notoken");
        Log.d("GLOBALTOKEN", token);
        //if user has logged in, has token,
        if (!token.equalsIgnoreCase("notoken")) {
            TextView logincontent = (TextView) loginPart.findViewById(R.id.login_content);
            TextView loginUsername = (TextView) loginPart.findViewById(R.id.login_username);
            String username = sharedPref.getString("username", "default");
            String firstName = sharedPref.getString("firstName", "default");
            logincontent.setText(firstName);
            loginUsername.setText(username);
            //show layout dialog
            loginPart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //show dialog
                    ExploreActivity.this.createLogOutDialog().show();
                }
            });
            //if user didn't logged in or token expired, no token, switch to login activity
        } else {
            loginPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
        });

        }


        tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_tab_universal_tabs);
        //create the page view
        mViewPager = (ViewPager)findViewById(R.id.mainPager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);
        if (mViewPager!=null){
            mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),this));
        }
        tabs.setViewPager(mViewPager);


    }
     /*  unable local database

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
     */


    //return AlertDialog for logout
    private AlertDialog createLogOutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Log Out");
        // set dialog message
        alertDialogBuilder
                .setMessage("Want to log out?")
                .setCancelable(false)
                .setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", "notoken");
                        editor.commit();
                        Intent intent = new Intent(ExploreActivity.this, ExploreActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        return alertDialogBuilder.create();
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

    //change language
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
       *   From UniversalTemplateFragment
       *   so far never use this fragment interface, most fragment transition events has handled within PagerAdapter.
        */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    // ======================Fragment Interfaces  End=======================================
}
