package com.example.johnnie.ottawadriving.explore;



import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.listcomponent.MyListFragment;
import com.example.johnnie.ottawadriving.localdatabase.PersonDbAdapter;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.example.johnnie.ottawadriving.utils.PersonPullParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyListFragment.OnListFragmentSelected{

    private PersonDbAdapter dbHelper;
    ViewPager mViewPager;
    private ArrayList<PersonModel> mModels;
    private MyListFragment mFragment;


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



        mViewPager = (ViewPager)findViewById(R.id.mainPager);
        if (mViewPager!=null){
            mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),dbHelper));
        }




    }

    //initialize database and load data
    public void setLocalData() {
        dbHelper = new PersonDbAdapter(this);
        dbHelper.open();
        List<PersonModel> models = dbHelper.fetchAllPeople();
        if (models.size() == 0) {
            createData();
            dbHelper.close();
        }

    }

    private void createData() {
        PersonPullParser parser = new PersonPullParser();
        List<PersonModel> people = parser.parseXML(this);
        for (PersonModel person : people) {
            dbHelper.createDealer(person);
        }

    }


    // ======== back tab======
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    // =========search data by name



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    /*
    *    Here to do database query and pass the result of type ArrayList<PersonModel>
    *     as the parameter 'models' to fragment.displayListView();
    *
     */
    @Override
    public void OnListFragmentSelected(MyListFragment fragment,String title) {
        mFragment = fragment;
        new AsyncTask<String, Void, ArrayList<PersonModel>>() {

            @Override
            protected ArrayList<PersonModel> doInBackground(String... name) {
                return dbHelper.fetchPersonByName(name[0]);

            }
            @Override
            public void onPostExecute(ArrayList<PersonModel> models) {
                mModels = models;
                mFragment.displayListView(mModels);
            }
        }.execute(title);




    }
}
