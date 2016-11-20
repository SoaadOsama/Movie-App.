package com.example.admin.movieapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 10/26/2016.
 */

public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Movie> Movies;



    public MovieAdapter(Context context, ArrayList<Movie>mList) {
        Movies = mList;
        mContext = context;
       // mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return  Movies.size();
       // return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return Movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addmovie(ArrayList<Movie> moviesList) {
        Movies.clear();
        for (Movie mv : moviesList) {
            Movies.add(mv);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ImageView imageView;
        if (view == null) {
            //GridView gv = new GridView(mContext);
          // imageView = new ImageView(mContext);
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_view_item,null);
        }


           // TextView MovieName = (TextView) view.findViewById(R.id.txt_item);
            ImageView MoviePoster = (ImageView)view.findViewById(R.id.img_item);
           // MovieName.setText(Movies.get(position).getMovieName());
        Log.e("TAG","http://image.tmdb.org/t/p/w185/"+ Movies.get(position).getImg_path());
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+ Movies.get(position).getImg_path()).into(MoviePoster);
       return view;


    }






}
