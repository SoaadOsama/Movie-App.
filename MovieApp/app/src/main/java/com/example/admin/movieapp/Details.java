package com.example.admin.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {


   static Movie m = new Movie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }
    // Fetch and store ShareActionProvider

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingActivity.class));
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    public static class DetailFragment extends Fragment {
        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        private Context mContext;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.details_fragment, container, false);
            TextView originalTitle = (TextView)rootView.findViewById(R.id.originalTitle);
            TextView overview = (TextView)rootView.findViewById(R.id.overview);
            TextView releaseData = (TextView)rootView.findViewById(R.id.releaseDate);
            TextView rating = (TextView)rootView.findViewById(R.id.rating);
            ImageView img = (ImageView)rootView.findViewById(R.id.img);
            Intent in = getActivity().getIntent();
            Bundle b = in.getExtras();
           // if (b != null) {

                String OriginalTitle = b.getString("Original Title");
                String OverView = b.getString("Overview");
                String rDate = b.getString("Release Date");
                String rate = b.getString("Rating");
                String imgPath = b.getString("img_path");
           // Log.e("TAG","http://image.tmdb.org/t/p/w185/"+ Movies.get(position).getImg_path());
           Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+ imgPath).into(img);

                originalTitle.setText(OriginalTitle);
                overview.setText(OverView);
                rating.setText(rate);
                releaseData.setText(rDate);


          // }


            return rootView;
        }


    }

}
