package com.example.admin.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

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
    private DetailListener mDetailListener;
    GridView gridView;
   // String MovieName;
    //String Img_path;
    MovieDatabaseHelper mDBHelper;
    View rootView;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    void setmDetailListener(DetailListener detailListener)
    {
        this.mDetailListener = detailListener;
    }

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
            updateTopRated(); //show the top rated
            return true;
        }

       else if (id == R.id.action_pref2) {
           updatepopular();
            return true;
        }

        else if (id == R.id.action_pref3) {
            Cursor result = mDBHelper.getAllData(); //fetch data from DB and add it to cursor object
           ArrayList<Movie> mlist= new ArrayList<Movie>(); //list to show the fav movies
            if(result.getCount() == 0)
            {
                Toast.makeText(rootView.getContext(), "You don't have any favouraites yet!", Toast.LENGTH_LONG).show();
            }
            else
            {
                while (result.moveToNext()) {
                    int m_id= result.getInt(0);
                    String img_path = result.getString(1);
                    String originalTitle = result.getString(2);
                    String overview = result.getString(3);
                    String rating = result.getString(4);
                    String releaseDate = result.getString(5);
                    Movie m = new Movie();
                    m.setid(m_id);
                    m.setimg_path(img_path);
                    m.setOriginalTitle(originalTitle);
                    m.setOverview(overview);
                    m.setRating(rating);
                    m.setReleaseDate(releaseDate);
                    mlist.add(m);
                }
               mAdapter.addmovie(mlist); //add the new arraylist
                gridView.setAdapter(mAdapter);

            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAdapter = new MovieAdapter(getContext(),new ArrayList<Movie>());
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie m = (Movie)mAdapter.getItem(position);
               // Log.e("test", m.getMovieName());
                mDetailListener.setSelectedMovie(m);
            }
        });
        mDBHelper =  new MovieDatabaseHelper(rootView.getContext());

        if(!isNetworkAvailable())
        {
            Toast.makeText(rootView.getContext(), "There is no internet connection!", Toast.LENGTH_LONG).show();
        }

        else {
            MovieTask nwTask = new MovieTask(mAdapter) {
            };
            nwTask.execute("popular");
        }
        return rootView;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        MovieTask nwTask = new MovieTask(mAdapter) {
//           };
//        nwTask.execute("popular");
//    }

    private void updateTopRated(){
        MovieTask nwTask = new MovieTask(mAdapter){
            @Override
            public void onPostExecute( ArrayList<Movie>  result) {
                super.onPostExecute(result);
            }
        };
        nwTask.execute("top_rated");
    }
    private void updatepopular(){
        MovieTask nwTask = new MovieTask(mAdapter){
            @Override
            public void onPostExecute( ArrayList<Movie>  result) {
                super.onPostExecute(result);
            }
        };
        nwTask.execute("popular");
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

                    MoviesList.clear(); //to update the gridView
                    for(int i = 0; i<results.length();i++)
                    {
                        JSONObject jsonobj = results.getJSONObject(i);
                        String Poster = jsonobj.getString("poster_path");
                        String Title = jsonobj.getString("title");
                        String originalTitle = jsonobj.getString("original_title");
                        String overview = jsonobj.getString("overview");
                        String Rating = jsonobj.getString("vote_average");
                        String releaseDate = jsonobj.getString("release_date");
                        int ID = jsonobj.getInt("id");
                        Movie movie = new Movie();
                        movie.setMovieName(Title);
                        movie.setimg_path(Poster);
                        movie.setOriginalTitle(originalTitle);
                        movie.setOverview(overview);
                        movie.setRating(Rating);
                        movie.setReleaseDate(releaseDate);
                        movie.setid(ID);
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