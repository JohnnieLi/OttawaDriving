package com.example.johnnie.ottawadriving.listcomponent;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.detailactivity.DetailActivity;
import com.example.johnnie.ottawadriving.explore.ExploreActivity;
import com.example.johnnie.ottawadriving.mapcomponent.MapActivity;
import com.example.johnnie.ottawadriving.model.PersonModel;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnnie on 2016-08-25.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {

    private ArrayList<PersonModel> models;
    private Context mContext;
    private PersonModel currentModel;

    private LruCache<Integer, Bitmap> imageCache;
    private RequestQueue queue;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView img;
        public TextView info;
        public TextView address;
        public TextView phone;
        public PersonModel model;
        public TextView email;

        public ViewHolder(View v, final Context context) {
            super(v);
            info = (TextView)v.findViewById(R.id.card_info);
            img = (ImageView)v.findViewById(R.id.card_img);
            address =  (TextView)v.findViewById(R.id.card_address);
            phone = (TextView)v.findViewById(R.id.card_phone);
            email = (TextView)v.findViewById(R.id.card_Email);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);

                    // pass the image and the model to detail activity
                    img.setDrawingCacheEnabled(true);
                    Bitmap bitmap = img.getDrawingCache();
                    intent.putExtra("imageResource", bitmap);
                    intent.putExtra("model",model);


                    // create the transition animation - the images in the layouts
                    // of both activities are defined with android:transitionName="image"
                    ActivityOptions options;
                    if(context instanceof  ExploreActivity){
                        options = ActivityOptions
                               .makeSceneTransitionAnimation((ExploreActivity)context,img,"image");
                   }else {
                         options = ActivityOptions
                               .makeSceneTransitionAnimation((MapActivity)context,img,"image");
                   }
                    // start the new activity
                   context.startActivity(intent, options.toBundle());

                }
            });

        }


        public void setModel(PersonModel model){
            this.model = model;
        }

    }



    public RecyclerListAdapter(ArrayList<PersonModel> models, Context context) {
        this.models = models;
        this.mContext = context;

        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);


        queue = Volley.newRequestQueue(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card, parent, false);

        //todo: set the card view size,margins, paddings and layout parameters


        return new ViewHolder(v, mContext);

    }


    //this holder is the viewHolder we created in onCreateViewHolder method
    // this method will handel each view(cardView in this case), position is the index of every single cardView
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        currentModel = models.get(position);
        holder.setModel(currentModel);
        holder.info.setText(currentModel.getName());
        holder.address.setText(currentModel.getAddress());
        holder.phone.setText(currentModel.getPhoneNumber());
        holder.email.setText(currentModel.getEmail());


        Bitmap bitmap = imageCache.get((int)currentModel.getId());
        if (bitmap != null){
            holder.img.setImageBitmap(bitmap);
        }else {
            ImageRequest request = new ImageRequest(currentModel.getImageUri(),
                    new Response.Listener<Bitmap>() {

                        @Override
                        public void onResponse(Bitmap response) {
                            holder.img.setImageBitmap(response);
                            imageCache.put((int)currentModel.getId(),response);
                        }
                    },
                    holder.img.getWidth(), holder.img.getHeight(),
                    Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("RecyclerListAdapter", error.getMessage());
                        }
                    });
            queue.add(request);

        }

//        new AsyncTask<String, Integer, Bitmap>(){
//            @Override
//            protected void onPreExecute(){
//                //do setup here
//            }
//            @Override
//            protected Bitmap doInBackground(String... params){
//                try {
//                    URL url = new URL(params[0]);
//                    Log.d("RECYCLE", "URL: "+url.toString());
//                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//                    if (httpCon.getResponseCode() != 200) {
//                        throw new Exception("Failed to connect");
//                    }
//                    InputStream is = httpCon.getInputStream();
//                    return BitmapFactory.decodeStream(is);
//                }catch(Exception e){
//                    Log.e("Image", "Failed to load image", e);
//                }
//                return null;
//            }
//            @Override
//            protected void onProgressUpdate(Integer... params){
//                //Update a progress bar
//            }
//            @Override
//            protected void onPostExecute(Bitmap img){
//                if (holder.img != null && img != null){
//                    holder.img.setImageBitmap(img);
//                }
//            }
//            @Override
//            protected void onCancelled(){
//                // Handle case where you called cancel
//            }
//        }.execute(currentModel.getImageUri());



    }


    @Override
    public int getItemCount() {
        return models.size();
    }


}
