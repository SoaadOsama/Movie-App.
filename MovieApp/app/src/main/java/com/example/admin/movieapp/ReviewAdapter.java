package com.example.admin.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 11/28/2016.
 */

public class ReviewAdapter extends BaseAdapter {

    private ArrayList<Reviews> reviews;
    private Context mContext;

    public void setmContext(Context mContext) {

        this.mContext = mContext;
    }
    public ReviewAdapter(Context context, ArrayList<Reviews>mList) {
        reviews = mList;
        mContext = context;

    }

    public void setReviews(ArrayList<Reviews> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Reviews> getReviews() {
        return reviews;
    }



    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addreview(ArrayList<Reviews> reviewList) {
        reviews.clear();
        for (Reviews r : reviewList) {
            reviews.add(r);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list2_view_item, null);
        }

        TextView author = (TextView)view.findViewById(R.id.author);
        TextView content = (TextView)view.findViewById(R.id.content);
        author.setText(reviews.get(position).getAuthor());
        content.setText(reviews.get(position).getContent());
        return  view;
    }

}
