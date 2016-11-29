package com.example.admin.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class DetailFragment extends Fragment {
    //private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private Context mContext;
    protected KeyAdapter mAdapter;
    protected ReviewAdapter mRadapter;
    protected ArrayList<String> VList= new ArrayList<String>();
    protected ArrayList<Reviews> RList= new ArrayList<Reviews>();
    ListView listView ;
    ListView listView1;
    MovieDatabaseHelper mDBHelper;
    Button button;
    Button button2;
    String OriginalTitle;
    String OverView;
    String rDate;
    String rate;
    String imgPath;
    int id;
    View view;
    Movie m = new Movie();

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public DetailFragment() {
        setHasOptionsMenu(true);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //((Details)getActivity()).getSupportActionBar().hide();


        mAdapter = new KeyAdapter(getContext(),new ArrayList<String>());
        final View rootView = inflater.inflate(R.layout.details_fragment, container, false);
        listView = (ListView)rootView.findViewById(R.id.listv);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VList.get(position))));

            }
        });
        mRadapter = new ReviewAdapter(getContext(),new ArrayList<Reviews>());
        listView1 = (ListView)rootView.findViewById(R.id.listr);
        listView1.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView1.setAdapter(mRadapter);
        setListViewHeightBasedOnChildren(listView1);

        TextView originalTitle = (TextView)rootView.findViewById(R.id.originalTitle);
        TextView overview = (TextView)rootView.findViewById(R.id.overview);
        TextView releaseData = (TextView)rootView.findViewById(R.id.releaseDate);
        TextView rating = (TextView)rootView.findViewById(R.id.rating);
        ImageView img = (ImageView)rootView.findViewById(R.id.img);
        button = (Button)rootView.findViewById(R.id.fButton);
        button2 = (Button)rootView.findViewById(R.id.nfButton);
        mDBHelper =  new MovieDatabaseHelper(rootView.getContext());


        Bundle b = getArguments();
       // if (b != null) {

            OriginalTitle = b.getString("Original Title");
            OverView = b.getString("Overview");
            rDate = b.getString("Release Date");
            rate = b.getString("Rating");
            imgPath = b.getString("img_path");
             id =  b.getInt("id");

       // Log.e("TAG","http://image.tmdb.org/t/p/w185/"+ Movies.get(position).getImg_path());
       Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+ imgPath).into(img);

            originalTitle.setText(OriginalTitle);
            overview.setText(OverView);
            rating.setText(rate);
            releaseData.setText(rDate);
      // }
          m.setid(id);
          m.setOriginalTitle(OriginalTitle);
          m.setimg_path(imgPath);
          m.setReleaseDate(rDate);
          m.setRating(rate);
          m.setOverview(OverView);

        if(!isNetworkAvailable())
        {
            Toast.makeText(rootView.getContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
        }
        else {

            VTask nwTask = new VTask(mAdapter) {
            };
            nwTask.execute(String.valueOf(m.getID() + "/videos"));
        }
        if(!isNetworkAvailable())
        {
            Toast.makeText(rootView.getContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
        }
        else {
            RTask newTask = new RTask(mRadapter) {
            };
            newTask.execute(String.valueOf(m.getID() + "/reviews"));
        }


            button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = mDBHelper.insertData(m.getID(), m.getImg_path(), m.getOriginalTitle(), m.getOverview(),
                                m.getRating(), m.getReleaseDate());
                        if (isInserted) {
                            Toast.makeText(rootView.getContext(), "Movie is added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(rootView.getContext(), "Movie already exists", Toast.LENGTH_LONG).show();
                        }


                    }
                }

        );

        button2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       int delete = mDBHelper.deleteData(m.getOriginalTitle());
                        if (delete > 0) {
                            Toast.makeText(rootView.getContext(), "Movie is removed from your favourites", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(rootView.getContext(), "Movie doesn't exist", Toast.LENGTH_LONG).show();
                        }


                    }
                }

        );




        return rootView;
    }




    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = ViewGroup.MeasureSpec.makeMeasureSpec(listView.getWidth(), ViewGroup.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, ViewGroup.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public class VTask extends AsyncTask<String,Void,ArrayList<String>> {

            // private ArrayList<Movie> MoviesList=new ArrayList<>();

            public VTask(KeyAdapter kAdapter)
            {
                mAdapter = kAdapter;
            }

            @Override
            protected ArrayList<String> doInBackground(String... params) {

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

                    VList.clear();
                    for(int i = 0; i<results.length();i++)
                    {
                        JSONObject jsonobj = results.getJSONObject(i);
                        String tName = jsonobj.getString("name");
                        String Key = jsonobj.getString("key");
                        String trailerPath = "http://www.youtube.com/watch?v=" + Key;
                        VList.add(trailerPath);
                    }


                    return VList;

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

                return VList;
            }

            @Override
            public void onPostExecute( ArrayList<String> result) {
                super.onPostExecute(result);
                if (result != null) {
                    mAdapter.addkey(result);
                }
                mAdapter.notifyDataSetChanged();
            }
        }





    public class RTask extends AsyncTask<String,Void,ArrayList<Reviews>> {

        // private ArrayList<Movie> MoviesList=new ArrayList<>();

        public RTask(ReviewAdapter kAdapter)
        {
            mRadapter = kAdapter;
        }

        @Override
        protected ArrayList<Reviews> doInBackground(String... params) {

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

                RList.clear();
                for(int i = 0; i<results.length();i++)
                {
                    JSONObject jsonobj = results.getJSONObject(i);
                    String author = jsonobj.getString("author");
                    String content = jsonobj.getString("content");
                    Reviews reviews = new Reviews();
                    reviews.setAuthor(author);
                    reviews.setContent(content);
                    RList.add(reviews);
                }


                return RList;

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

            return RList;
        }

        @Override
        public void onPostExecute( ArrayList<Reviews> result) {
            super.onPostExecute(result);
            if (result != null) {
                mRadapter.addreview(result);
            }
            mRadapter.notifyDataSetChanged();
        }
    }


}
