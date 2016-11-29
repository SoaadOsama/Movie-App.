package com.example.admin.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Details extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            Intent sentIntent = getIntent();
            Bundle sentBundle = sentIntent.getExtras();
            DetailFragment mDetailsFragment = new DetailFragment();
            mDetailsFragment.setArguments(sentBundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container2, mDetailsFragment, "")
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }


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

}


