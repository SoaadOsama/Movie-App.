package com.example.admin.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 11/25/2016.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    public static final String DataBaseName =  "Movies.db";
    public static final String TableName =  "moviesTable";
    public static final String Col_1 =  "ID";
    public static final String Col_2 =  "IMG_PATH";
    public static final String Col_3 =  "ORIGINAL_TITLE";
    public static final String Col_4 =  "OVERVIEW";
    public static final String Col_5 =  "RATING";
    public static final String Col_6 =  "RELEASE_DATE";



    public MovieDatabaseHelper(Context context) {
        super(context,DataBaseName , null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TableName + " (ID INTEGER PRIMARY KEY, IMG_PATH TEXT, ORIGINAL_TITLE TEXT , OVERVIEW TEXT , RATING TEXT, RELEASE_DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + TableName);
        onCreate(db);

    }

   public boolean insertData(int id, String img_path, String originalTitle, String overview, String Rating, String releasedate)
   {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(Col_1,id);
       contentValues.put(Col_2,img_path);
       contentValues.put(Col_3,originalTitle);
       contentValues.put(Col_4,overview);
       contentValues.put(Col_5,Rating);
       contentValues.put(Col_6,releasedate);
      long result =  db.insert(TableName,null,contentValues);
       if(result == -1)
           return false;
       else
           return true;
   }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TableName, null);
        return result;

    }

    public int deleteData (String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
       return db.delete(TableName, " ORIGINAL_TITLE = ? ", new String[] { title });
    }
}
