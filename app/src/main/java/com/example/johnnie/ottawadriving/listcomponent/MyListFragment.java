package com.example.johnnie.ottawadriving.listcomponent;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.localdatabase.PersonDbAdapter;
import com.example.johnnie.ottawadriving.mapcomponent.MapActivity;
import com.example.johnnie.ottawadriving.model.PersonModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Johnnie on 2016-09-14.
 */
public class MyListFragment extends Fragment {

    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private View rootView;
    private String mTitle;
    private OnListFragmentSelected mCallbacks;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mapViewButton;
    private ArrayList<PersonModel> mModels;
    private PersonDbAdapter mDbHelper;



    // so far never used this
    public interface OnListFragmentSelected {
        void OnListFragmentSelected(MyListFragment fragment, String title);
    }



    /**
     * Create a new instance of MyListFragment, providing "title"
     * as an argument.
     */
    public static MyListFragment newInstance(String title) {
        MyListFragment f = new MyListFragment();
        // Supply title input as an argument.
        Bundle args = new Bundle();
        args.putString("title",title);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    //initialize the dataAdapter and open it.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mTitle = args.getString("title");
        mDbHelper = new PersonDbAdapter(getActivity());
        mDbHelper.open();

        }


    // called after rootView created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    // dataAdapter search some value as the models, put these models into recyclerList View
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.list_frag, container, false);
        mapViewButton = (Button) rootView.findViewById(R.id.list_map_button);
        mapViewButton.setClickable(false);
        new AsyncTask<String, Void, ArrayList<PersonModel>>() {

            @Override
            protected ArrayList<PersonModel> doInBackground(String... name) {
                return mDbHelper.fetchPersonByName(name[0]);

            }
            @Override
            public void onPostExecute(ArrayList<PersonModel> models) {
                mModels = models;
                //create recyclerView and recyclerListAdapter
                displayListView(mModels);
            }
        }.execute(mTitle);


        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallbacks = (OnListFragmentSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }


    //create the recyclerView and recyclerListAdapter
    public void displayListView(final ArrayList<PersonModel> models) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_items);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerListAdapter(models,getContext());
        mRecyclerView.setAdapter(mAdapter);

        mapViewButton.setClickable(true);
        mapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("PersonModels",models);
                startActivity(intent);
            }
        });
    }

}
