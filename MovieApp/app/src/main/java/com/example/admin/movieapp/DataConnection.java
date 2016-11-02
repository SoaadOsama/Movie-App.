package com.example.admin.movieapp;

/**
 * Created by Admin on 10/26/2016.
 */

public class DataConnection {

    MovieAdapter mAdapter;

    String FullUrl ;

    public DataConnection(MovieAdapter mAdapter, String FullUrl){

        this.mAdapter = mAdapter;
        this.FullUrl  = FullUrl;
    }

    public MovieAdapter getmAdapter(){return  mAdapter;}
    public String getFullUrl () {return FullUrl;}

    public void setmAdapter(MovieAdapter mAdapter1)
    {
        mAdapter = mAdapter1;
    }
    public void setFullUrl(String url)
    {
        FullUrl = url;
    }


}
