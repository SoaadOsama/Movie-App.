package com.example.admin.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 11/27/2016.
 */

public class KeyAdapter extends BaseAdapter {

    private Context mContext;


    private ArrayList<String> Keys;

    public KeyAdapter(Context context, ArrayList<String>mList) {
        Keys = mList;
        mContext = context;

    }

    public void setKeys(ArrayList<String> keys) {
        Keys = keys;
    }

    public ArrayList<String> getKeys() {

        return Keys;
    }

    public void setmContext(Context mContext) {

        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return Keys.size();
    }

    @Override
    public Object getItem(int position) {
        return Keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addkey(ArrayList<String> keysList) {
        Keys.clear();
        for (String k : keysList) {
            Keys.add(k);
        }
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        int i = position + 1;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_item, null);
        }

        TextView MovieTrailer = (TextView)view.findViewById(R.id.video);
        MovieTrailer.setText("Trailer" + i);

        ImageView youtube = (ImageView)view.findViewById(R.id.youtubeicon);
        return  view;
    }

}
