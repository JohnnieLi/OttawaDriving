package com.example.johnnie.ottawadriving.listcomponent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.model.PersonModel;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Johnnie on 2016-08-25.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {

    private List<PersonModel> models;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView img;
        public TextView info;
        public TextView address;
        public TextView phone;

        public ViewHolder(View v) {
            super(v);
            info = (TextView)v.findViewById(R.id.card_info);
            img = (ImageView)v.findViewById(R.id.card_img);
            address =  (TextView)v.findViewById(R.id.card_address);
            phone = (TextView)v.findViewById(R.id.card_phone);

        }

    }

    public RecyclerListAdapter(List<PersonModel> models) {
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card, parent, false);

        //todo: set the card view size,margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        PersonModel model = models.get(position);
        holder.info.setText(model.getInformation());
        holder.address.setText(model.getAddress());
        holder.phone.setText(model.getPhoneNumber());
        new AsyncTask<String, Integer, Bitmap>(){
            @Override
            protected void onPreExecute(){
                //do setup here
            }
            @Override
            protected Bitmap doInBackground(String... params){
                try {
                    URL url = new URL(params[0]);

                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    if (httpCon.getResponseCode() != 200) {
                        throw new Exception("Failed to connect");
                    }
                    InputStream is = httpCon.getInputStream();
                    return BitmapFactory.decodeStream(is);
                }catch(Exception e){
                    Log.e("Image", "Failed to load image", e);
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(Integer... params){
                //Update a progress bar
            }
            @Override
            protected void onPostExecute(Bitmap img){
                if (holder.img != null && img != null){
                    holder.img.setImageBitmap(img);
                }
            }
            @Override
            protected void onCancelled(){
                // Handle case where you called cancel
            }
        }.execute(model.getImageUri());


    }


    @Override
    public int getItemCount() {
        return models.size();
    }


}
