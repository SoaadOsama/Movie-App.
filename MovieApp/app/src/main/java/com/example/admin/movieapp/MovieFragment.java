package com.example.admin.movieapp;

//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Admin on 10/26/2016.
 */

public class MovieFragment extends Fragment {

    public MovieFragment(){}
    private ArrayList<Movie> MoviesList;
    private MovieAdapter mAdapter;
    GridView gridview;
    String MovieName;
    String Img_path;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MoviesList = new ArrayList<>();

       final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       final GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        MovieTask nwTask = new MovieTask(){
            @Override
            public void onPostExecute(ArrayList<Movie> result) {
                super.onPostExecute(result);
                mAdapter = new MovieAdapter(rootView.getContext(),result);
                gridView.setAdapter(mAdapter);

            }
        };
        mAdapter = new MovieAdapter(this.getContext(),MoviesList);
        DataConnection newConnection = new DataConnection(mAdapter,"https://api.themoviedb.org/3/movie/top_rated?api_key=27f9099f4ea8768860d2a137501a2621");

        nwTask.execute(newConnection);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }



}