package com.example.johnnie.ottawadriving.detailactivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class DetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar mActionBar;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set toolbar change with scroll view
        setToolbarChange();

        imageView = (ImageView)findViewById(R.id.detail_image_view);
//        textView = (TextView)findViewById(R.id.detail_text_address);



       // get the image value and model from card view Onclick(Within recycListAdapter)
        // TODO: 2016-10-01  so far Just pass the whole model. later, should be discussed about pass the model or pass the id
        // TODO: 2016-10-01  if pass model, that means the whole data are readed after we search, if pass id, means read from database here
        if (getIntent().hasExtra("imageResource")) {
           imageView.setImageBitmap((Bitmap) getIntent().getExtras().get("imageResource"));
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "imageResource");
        }

        if (getIntent().hasExtra("model")) {
            PersonModel model = (PersonModel) getIntent().getExtras().getSerializable("model");
            String address = model.getAddress();
            //textView.setText(address);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "model");
        }
    }




    public void setToolbarChange(){
        // set toolbar change with scroll view

        final ColorDrawable cd = new ColorDrawable(Color.rgb(68, 74, 83));

        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(cd);

        cd.setAlpha(0);

        mActionBar.setDisplayHomeAsUpEnabled(true); //to activate back pressed on home button press
        mActionBar.setDisplayShowHomeEnabled(false); //


        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nestedScroll);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("SCROLLVIEW", Integer.toString(getAlphaforActionBar(v.getScrollY())));
                cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
            }

            private int getAlphaforActionBar(int scrollY) {
                int minDist = 0, maxDist = 650;
                if (scrollY > maxDist) {
                    return 255;
                } else if (scrollY < minDist) {
                    return 0;
                } else {
                    int alpha = 0;
                    alpha = (int) ((255.0 / maxDist) * scrollY);
                    return alpha;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
