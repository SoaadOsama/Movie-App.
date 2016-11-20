package com.example.admin.movieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    protected ArrayList<Movie> MoviesList= new ArrayList<Movie>();
    protected MovieAdapter mAdapter;
    GridView gridview;
    String MovieName;
    String Img_path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_pref1) {
            updateTopRated();
            return true;
        }

       else if (id == R.id.action_pref2) {
            MovieTask nwTask = new MovieTask(mAdapter){
                @Override
                public void onPostExecute( ArrayList<Movie>  result) {
                    super.onPostExecute(result);
                    mAdapter.notifyDataSetChanged();
                }
            };
            nwTask.execute("popular");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAdapter = new MovieAdapter(getContext(),new ArrayList<Movie>());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent in = new Intent(getActivity(),Details.class);

                Bundle b = new Bundle();
                Movie m = (Movie)mAdapter.getItem(position);
                b.putString("Original Title",m.getOriginalTitle());
                b.putString("Overview",m.getOverview());
                b.putString("Rating",m.getRating());
                b.putString("Release Date",m.getReleaseDate());
                b.putString("img_path",m.getImg_path());
                in.putExtras(b);
                startActivity(in);


            }
        });
        MovieTask nwTask = new MovieTask(mAdapter){};
        nwTask.execute("popular");
        return rootView;
    }

    private void updateTopRated(){
        MovieTask nwTask = new MovieTask(mAdapter){
            @Override
            public void onPostExecute( ArrayList<Movie>  result) {
                super.onPostExecute(result);
            }
        };
        nwTask.execute("top_rated");
    }
    @Override
    public void onStart() {
        super.onStart();
        updateTopRated();
    }

    public class MovieTask extends AsyncTask<String,Void,ArrayList<Movie>> {

        // private ArrayList<Movie> MoviesList=new ArrayList<>();

        public MovieTask(MovieAdapter movieAdapter)
        {
            mAdapter = movieAdapter;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if(params.length == 0){return  null;}

            HttpURLConnection urlConnection = null;
            String moviesJsonStr = null;
            BufferedReader reader = null;
            String format = "json";
            final String full_bath = "https://api.themoviedb.org/3/movie/";

            try
            {

                URL url = new URL(full_bath + params[0] + "?api_key=27f9099f4ea8768860d2a137501a2621");
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


                    JSONObject moviesJson = new JSONObject(moviesJsonStr);
                    JSONArray results = moviesJson.getJSONArray("results");


                    for(int i = 0; i<results.length();i++)
                    {
                        JSONObject jsonobj = results.getJSONObject(i);
                        String Poster = jsonobj.getString("poster_path");
                        String Title = jsonobj.getString("title");
                        String originalTitle = jsonobj.getString("original_title");
                        String overview = jsonobj.getString("overview");
                        String Rating = jsonobj.getString("vote_average");
                        String releaseDate = jsonobj.getString("release_date");
                        Movie movie = new Movie();
                        movie.setMovieName(Title);
                        movie.setimg_path(Poster);
                        movie.setOriginalTitle(originalTitle);
                        movie.setOverview(overview);
                        movie.setRating(Rating);
                        movie.setReleaseDate(releaseDate);
                        MoviesList.add(movie);

                    }
                    return MoviesList;

                }
            catch (IOException e) {
                Log.e("Error ", e.getMessage(), e);
                return null;
            }
            catch (JSONException e) {
                    //Log.e("Error", e.getMessage(), e);
                    e.printStackTrace();
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

            return MoviesList;
        }

        @Override
        public void onPostExecute( ArrayList<Movie> result) {
            super.onPostExecute(result);
            if (result != null) {
                    mAdapter.addmovie(result);
            }
            mAdapter.notifyDataSetChanged();
        }
    }


}