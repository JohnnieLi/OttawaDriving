package com.example.johnnie.ottawadriving.listcomponent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.model.PersonModel;

import java.util.ArrayList;


/**
 * Created by Johnnie on 2016-09-14.
 */
public class MyListFragment extends Fragment {

    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private View rootView;
    private OnListFragmentSelected mCallbacks;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public interface OnListFragmentSelected {
        void OnListFragmentSelected(MyListFragment fragment, String title);
    }


    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static MyListFragment newInstance(ArrayList<PersonModel> models) {
        MyListFragment f = new MyListFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("models", models);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    // called after rootView created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // to set models to displayListView();
        mCallbacks.OnListFragmentSelected(this, "title1");
    }

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

//            displayListView(models);
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


    public void displayListView(ArrayList<PersonModel> models) {
        TextView text = (TextView) rootView.findViewById(R.id.list_frag_text);
        text.setText("one");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_items);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerListAdapter(models);
        mRecyclerView.setAdapter(mAdapter);
    }

}
