package com.waracle.androidtest;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tiagoornelas on 13/02/2018.
 */

public class MyAdapter extends BaseAdapter {

    // Can you think of a better way to represent these items???
    private ArrayList<Cake> mItems;
    private ImageLoader mImageLoader;

    public MyAdapter() {

        this(new ArrayList<Cake>());
    }

    public MyAdapter(ArrayList<Cake> items) {

        mItems = items;
        mImageLoader = new ImageLoader();
    }

    @Override
    public int getCount() {

        return mItems.size();
    }

    @Override
    public Cake getItem(int position) {

        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        //View holder pattern
        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.desc = convertView.findViewById(R.id.desc);
            viewHolder.image = convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) convertView.getTag();


        Cake cake =  getItem(position);
        viewHolder.title.setText(cake.getTitle());
        viewHolder.desc.setText(cake.getDescription());

        if (cake.getImageBitmap() == null)
            mImageLoader.load(cake, viewHolder.image);
        else
            viewHolder.image.setImageBitmap(cake.getImageBitmap());

        return convertView;
    }

    public void setItems(ArrayList<Cake> items) {

        mItems = items;
    }

    //View holder pattern
    public class ViewHolder {

        TextView title;
        TextView desc;
        ImageView image;
    }
}