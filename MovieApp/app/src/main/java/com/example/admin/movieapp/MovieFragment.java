package com.example.admin.movieapp;

//import android.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Admin on 10/26/2016.
 */

public class MovieFragment extends Fragment {

    public MovieFragment(){}
    private ArrayList<Movie> MoviesList= new ArrayList<>();
    private MovieAdapter mAdapter;
    GridView gridview;
    String MovieName;
    String Img_path;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



       final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       final GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        MovieTask nwTask ; nwTask= new MovieTask(){
            @Override
            public void onPostExecute(ArrayList<Movie> result) {
                super.onPostExecute(result);
               mAdapter = new MovieAdapter(rootView.getContext(),result);
            }
        };
        mAdapter = new MovieAdapter(this.getContext(),MoviesList);
        gridView.setAdapter(mAdapter);
        DataConnection newConnection = new DataConnection(mAdapter,"https://api.themoviedb.org/3/movie/top_rated?api_key=27f9099f4ea8768860d2a137501a2621");
        nwTask.execute(newConnection);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }

    public class MovieTask extends AsyncTask<DataConnection,Void,ArrayList<Movie>> {

        // private ArrayList<Movie> MoviesList=new ArrayList<>();

        @Override
        protected ArrayList<Movie> doInBackground(DataConnection... params) {

            HttpURLConnection urlConnection = null;
            String moviesJsonStr = null;
            BufferedReader reader = null;
            String format = "json";

            try
            {
                URL url = new URL(params[0].getFullUrl());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;

                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonStr = buffer.toString();
            }

            catch (IOException e) {
                Log.e("Error ", e.getMessage(), e);
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Error closing stream",e.getMessage(), e);
                    }
                }
            }
            try {
                JSONObject moviesJson = new JSONObject(moviesJsonStr);
                JSONArray results = moviesJson.getJSONArray("results");


                for(int i = 0; i<results.length();i++)
                {
                    JSONObject jsonobj = results.getJSONObject(i);
                    String Poster = jsonobj.getString("poster_path");
                    String Title = jsonobj.getString("title");
                    Movie movie = new Movie();
                    movie.setMovieName(Title);
                    movie.setimg_path(Poster);
                    MoviesList.add(movie);

                }


            } catch (JSONException e) {
                //Log.e("Error", e.getMessage(), e);
                e.printStackTrace();
            }

            return MoviesList;
        }

        @Override
        public void onPostExecute( ArrayList<Movie> result) {
            super.onPostExecute(result);
            if (result != null) {
                MoviesList.clear();
                for(Movie movie : result) {
                    MoviesList.add(movie);
                }

            }
        }
    }


}