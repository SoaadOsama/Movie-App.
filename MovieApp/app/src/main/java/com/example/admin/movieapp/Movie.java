package com.example.admin.movieapp;

/**
 * Created by Admin on 10/26/2016.
 */

public class Movie {

    private String MovieName;
    private String img_path;
    private String originalTitle;
    private String rating;
    private String overview;
    private String releaseDate;
    int id;


    public Movie(){}

    public Movie(String mName, String img_path){

        this.MovieName = mName;
        this.img_path  = img_path;
    }

    public Movie(String mName, String img_path, String r, String o, String ov,String rd){
        this.MovieName = mName;
        this.img_path  = img_path;
        this.originalTitle  = o;
        this.rating  = r;
        this.overview  = ov;
        this.releaseDate = rd;
    }

    public String getMovieName(){return MovieName;}
    public String getImg_path () {return img_path;}
    public String getOriginalTitle () {return originalTitle;}
    public String getOverview () {return overview;}
    public String getRating () {return rating;}
    public String getReleaseDate () {return releaseDate;}
    public int getID () {return id ;}

    public void setMovieName(String mName)
    {

        MovieName = mName;
    }
    public void setimg_path(String img_path)
    {

        this.img_path = img_path;
    }
    public void setOriginalTitle(String or)

    {
        this.originalTitle = or;
    }
    public void setOverview(String ov)
    {
        this.overview = ov;
    }
    public void setRating(String r)
    {
        this.rating = r;
    }

    public void setReleaseDate(String rd)
    {
        this.releaseDate = rd;
    }

    public void setid(int id)
    {
        this.id = id;
    }

}
