package com.waracle.androidtest;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tiagoornelas on 13/02/2018.
 */

public class MyAdapter extends BaseAdapter {

    // Can you think of a better way to represent these items???
    private JSONArray mItems;
    private ImageLoader mImageLoader;

    public MyAdapter() {

        this(new JSONArray());
    }

    public MyAdapter(JSONArray items) {

        mItems = items;
        mImageLoader = new ImageLoader();
    }

    @Override
    public int getCount() {

        return mItems.length();
    }

    @Override
    public Object getItem(int position) {

        try {

            return mItems.getJSONObject(position);
        } catch (JSONException e) {

            Log.e("", e.getMessage());
        }
        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.desc = convertView.findViewById(R.id.desc);
            viewHolder.image = convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.image.setImageBitmap(null);
            JSONObject object = (JSONObject) getItem(position);
            viewHolder.title.setText(object.getString("title"));
            viewHolder.desc.setText(object.getString("desc"));
            mImageLoader.load(object.getString("image"), viewHolder.image);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return convertView;
    }

    public void setItems(JSONArray items) {

        mItems = items;
    }

    private class ViewHolder {

        TextView title;
        TextView desc;
        ImageView image;
    }
}