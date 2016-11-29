package com.example.admin.movieapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements DetailListener {
boolean mIsTowPane = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            MovieFragment mMovieFragment = new MovieFragment();
            ////Set The Activity to be a listener to the Fragment///////
            mMovieFragment.setmDetailListener(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mMovieFragment)
                    .commit();
            if(null != findViewById(R.id.container2))
            {
                mIsTowPane = true;
            }
        }
    }


    @Override
    public void setSelectedMovie(Movie m) {
        if(!mIsTowPane)
        {
            Intent in = new Intent(this,Details.class);
            Bundle b = new Bundle();
            b.putString("Original Title",m.getOriginalTitle());
            b.putString("Overview",m.getOverview());
            b.putString("Rating",m.getRating());
            b.putString("Release Date",m.getReleaseDate());
            b.putString("img_path",m.getImg_path());
            b.putInt("id",m.getID());
            in.putExtras(b);
            startActivity(in);
        }
        else
        {
            DetailFragment mDetailsFragment= new DetailFragment();
            Bundle b= new Bundle();
            b.putString("Original Title",m.getOriginalTitle());
            b.putString("Overview",m.getOverview());
            b.putString("Rating",m.getRating());
            b.putString("Release Date",m.getReleaseDate());
            b.putString("img_path",m.getImg_path());
            b.putInt("id",m.getID());
            mDetailsFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container2,mDetailsFragment,"").commit();
        }

    }
}
