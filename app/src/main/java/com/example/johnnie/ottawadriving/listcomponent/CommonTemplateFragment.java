package com.example.johnnie.ottawadriving.listcomponent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.mapcomponent.MapActivity;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.example.johnnie.ottawadriving.utils.JsonArrayParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommonTemplateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommonTemplateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommonTemplateFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private View rootView;
    private String mTitle;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mapViewButton;
    private ArrayList<PersonModel> mModels;
    //private PersonDbAdapter mDbHelper;


    public CommonTemplateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @return A new instance of fragment CommonTemplateFragment.
     */
    public static CommonTemplateFragment newInstance(String title) {
        CommonTemplateFragment fragment = new CommonTemplateFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            mTitle = args.getString("title");
        }
        // mDbHelper = new PersonDbAdapter(getActivity());
        //mDbHelper.open();
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
        rootView = inflater.inflate(R.layout.fragment_licensetran_page, container, false);
        mapViewButton = (Button) rootView.findViewById(R.id.license_map_button);
        mapViewButton.setClickable(false);


        if (isOnline()) {
            String uri = "http://192.168.2.12:5000/api/businessMen?type="+mTitle;
            Log.d("LicenseFragment",uri);
            requestData(uri);
        } else {
            Toast.makeText(getContext(), "Network isn't available", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    private void requestData(String uri) {

        JsonArrayRequest request = new JsonArrayRequest(uri,

                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        mModels = JsonArrayParser.parsePersonModelFromJson(response);
                        try {
                            JSONObject obj = response.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("LicenseFragmentModels",mModels.get(0).getName());
                        displayListView(mModels);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //create the recyclerView and recyclerListAdapter
    public void displayListView(final ArrayList<PersonModel> models) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_items);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerListAdapter(models, getContext());
        mRecyclerView.setAdapter(mAdapter);

        mapViewButton.setClickable(true);
        mapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("PersonModels", models);
                startActivity(intent);
            }
        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
