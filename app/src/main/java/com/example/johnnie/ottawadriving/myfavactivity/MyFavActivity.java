package com.example.johnnie.ottawadriving.myfavactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.example.johnnie.ottawadriving.utils.JsonArrayParser;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jiangqi on 2017-01-03.
 */

public class MyFavActivity extends AppCompatActivity {

    private DynamicListView mDynamicListView;
    private ArrayList<PersonModel> mModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);

        mDynamicListView = (DynamicListView) findViewById(R.id.dynamic_listview);
        mDynamicListView.setDividerHeight(0);

        mDynamicListView
                .setBackgroundResource(R.drawable.drag_and_drop_background_image);

        String IPAddress = getString(R.string.httpIPAddress);
        String uri = "http://" + IPAddress + "/api/businessMen?type=dealer";
        requestData(uri);

        Toast.makeText(this, "Long press an item to start dragging",
                Toast.LENGTH_SHORT).show();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("My favourites");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void requestData(String uri) {

        JsonObjectRequest request = new JsonObjectRequest(uri, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            mModels = JsonArrayParser.parsePersonModelFromJson(response.getJSONArray("businessMen"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setUpDragAndDrop(mModels);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void setUpDragAndDrop(ArrayList<PersonModel> models) {
        final DragAndDropAdapter adapter = new DragAndDropAdapter(
                this, models);
        mDynamicListView.setAdapter(adapter);
        mDynamicListView.enableDragAndDrop();
        TouchViewDraggableManager tvdm = new TouchViewDraggableManager(
                R.id.drag_and_drop_icon);
        mDynamicListView.setDraggableManager(new TouchViewDraggableManager(
                R.id.icon));
        mDynamicListView.setDraggableManager(tvdm);
        mDynamicListView
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, long id) {
                        mDynamicListView.startDragging(position);
                        return true;
                    }
                });
    }
}
