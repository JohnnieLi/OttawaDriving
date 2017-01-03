package com.example.johnnie.ottawadriving.myfavactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.example.johnnie.ottawadriving.utils.ImageUtil;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jiangqi on 2017-01-03.
 */

public class DragAndDropAdapter extends BaseAdapter implements Swappable {

    private ArrayList<PersonModel> myFavouriteList;
    private LayoutInflater mInflater;
    private Context mContext;


    public DragAndDropAdapter(Context context, ArrayList<PersonModel> myFavouriteList) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myFavouriteList = myFavouriteList;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public int getCount() {
        return this.myFavouriteList.size();
    }

    @Override
    public Object getItem(int i) {
        return myFavouriteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return myFavouriteList.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(
                    R.layout.list_item_drag_and_drop, viewGroup, false);
            holder = new ViewHolder();
            holder.image = (ImageView) view
                    .findViewById(R.id.item_drag_and_drop_image);
            holder.text = (TextView) view
                    .findViewById(R.id.item_drag_and_drop_name);
            holder.place = (TextView) view
                    .findViewById(R.id.item_drag_and_drop_place);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PersonModel model = myFavouriteList.get(i);
        holder.text.setText(model.getName());
        ImageUtil.displayRoundImage(holder.image, model.getImageUri(), null, mContext);
        holder.place.setText(model.getInformation());
        return view;
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(myFavouriteList, positionOne, positionTwo);
    }


    private static class ViewHolder {
        public ImageView image;
        public/* Roboto */ TextView text;
        public/* Roboto */ TextView subtext;
        public/* Fontello */ TextView icon;
        public/* Roboto */ TextView rating;
        public/* Roboto */ TextView place;
    }
}
