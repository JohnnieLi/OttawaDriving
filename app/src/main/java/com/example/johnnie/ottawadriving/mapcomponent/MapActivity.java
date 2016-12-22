package com.example.johnnie.ottawadriving.mapcomponent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.listpagecomponent.RecyclerListAdapter;
import com.example.johnnie.ottawadriving.model.PersonModel;

import java.util.ArrayList;

/**
 * Created by Johnnie on 2016-09-22.
 */
public class MapActivity extends AppCompatActivity implements MapFragment.OnMapFragmentClicked{


    private ArrayList<PersonModel> models;
    private MapFragment mMapFragment;
    private static final String TAG_MAPACTIVITY = "MAPACTIVITY";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        models = (ArrayList<PersonModel>)getIntent().getSerializableExtra("PersonModels");
        //mMapFragment = new MapFragment();
        mMapFragment =MapFragment.newInstance(models);
        //mMapFragment.setModels(models);
        mMapFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mapactivity_container_map, mMapFragment).commit();



        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_items);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerListAdapter(models,this);
        mRecyclerView.setAdapter(mAdapter);


        // SnapHelper helper = new LinearSnapHelper();
        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null) {
                    return RecyclerView.NO_POSITION;
                }

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX > 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }


                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                mMapFragment.searchMarkerByTitle(models.get(targetPosition).getAddress());

                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(mRecyclerView);

    }


    //implement mapFragment interface
    @Override
    public void GoToMapActivity() {

    }


    @Override
    public void onStop() {
        super.onStop();
    }
}
