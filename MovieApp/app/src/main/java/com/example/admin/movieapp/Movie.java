package com.example.admin.movieapp;

/**
 * Created by Admin on 10/26/2016.
 */

public class Movie {

    private String MovieName;
    private String img_path;

    public Movie(){}

    public Movie(String mName, String img_path){

        this.MovieName = mName;
        this.img_path  = img_path;
    }

    public String getMovieName(){return MovieName;}
    public String getImg_path () {return img_path;}

    public void setMovieName(String mName)
    {
        MovieName = mName;
    }
    public void setimg_path(String img_path)
    {
        img_path = img_path;
    }






}
