package com.example.johnnie.ottawadriving.listpagecomponent;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;

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
        public TextView name;
        public TextView address;
        public TextView phone;
        public PersonModel model;
        public TextView email;

        public ViewHolder(View v, final Context context) {
            super(v);
            //origian version card
//            name = (TextView)v.findViewById(R.id.list_item_origin_card_info);
//            img = (ImageView)v.findViewById(R.id.list_item_origin_cards_image);
//            address =  (TextView)v.findViewById(R.id.list_item_origin_card_address);
//            phone = (TextView)v.findViewById(R.id.list_item_origin_card_phone);
//            email = (TextView)v.findViewById(R.id.list_item_origin_card_Email);


            //google version card
            name = (TextView) v.findViewById(R.id.list_item_google_card_name);
            img = (ImageView) v.findViewById(R.id.list_item_google_cards_image);
            address = (TextView) v.findViewById(R.id.list_item_google_cards_address);
            phone = (TextView) v.findViewById(R.id.list_item_google_cards_phone_number);
            email = (TextView) v.findViewById(R.id.list_item_google_cards_email);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);

                    // pass the image and the model to detail activity
                    // img.setDrawingCacheEnabled(true);
                    // Bitmap bitmap = img.getDrawingCache();
                    //intent.putExtra("imageResource", bitmap);
                    intent.putExtra("model", model);


                    // create the transition animation - the images in the layouts
                    // of both activities are defined with android:transitionName="image"
                    ActivityOptions options;
                    if (context instanceof ExploreActivity) {
                        options = ActivityOptions
                                .makeSceneTransitionAnimation((ExploreActivity) context, img, "image");
                    } else {
                        options = ActivityOptions
                                .makeSceneTransitionAnimation((MapActivity) context, img, "image");
                    }
                    // start the new activity
                    context.startActivity(intent, options.toBundle());

                }
            });

        }


        public void setModel(PersonModel model) {
            this.model = model;
        }

    }


    public RecyclerListAdapter(ArrayList<PersonModel> models, Context context) {
        this.models = models;
        this.mContext = context;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);


        queue = Volley.newRequestQueue(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view:original version
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_origin_version_card, parent, false);

//        //create a new view: Googld card version
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_google_version_card, parent, false);


        return new ViewHolder(v, mContext);

    }


    //this holder is the viewHolder we created in onCreateViewHolder method
    // this method will handel each view(cardView in this case), position is the index of every single cardView
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        currentModel = models.get(position);
        holder.setModel(currentModel);
        holder.name.setText(currentModel.getName());
        holder.address.setText(currentModel.getAddress());
        holder.phone.setText(currentModel.getPhoneNumber());
        holder.email.setText(currentModel.getEmail());


        Bitmap bitmap = imageCache.get((int) currentModel.getId());
        if (bitmap != null) {
            holder.img.setImageBitmap(bitmap);
        } else {
            ImageRequest request = new ImageRequest(currentModel.getImageUri(),
                    new Response.Listener<Bitmap>() {

                        @Override
                        public void onResponse(Bitmap response) {
                            holder.img.setImageBitmap(response);
                            imageCache.put((int) currentModel.getId(), response);
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

    }


    @Override
    public int getItemCount() {
        return models.size();
    }


}
