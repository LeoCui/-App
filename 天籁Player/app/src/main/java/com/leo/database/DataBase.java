package com.leo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Leo on 15/12/8.
 */
public class DataBase extends SQLiteOpenHelper {
    public      static DataBase db1;
    public DataBase(Context context, String db, SQLiteDatabase.CursorFactory factory, int version){
        super(context,db,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("the database has been created");
        db.execSQL("CREATE TABLE SongInfo(_id INTEGER PRIMARY KEY AUTOINCREMENT,SongName TEXT DEFAULT NONE,SingerName TEXT DEFAULT NONE,Path TEXT DEFAULT NONE,lrcPath TEXT DEFAULT NONE)");
        db.execSQL("CREATE TABLE RecentSong(_id INTEGER PRIMARY KEY AUTOINCREMENT,SongName TEXT DEFAULT NONE,SingerName TEXT DEFAULT NONE,Path  TEXT DEFAULT NONE,lrcPath TEXT DEFAULT NONE)");
        db.execSQL("CREATE TABLE MyList(_id INTEGER PRIMARY KEY AUTOINCREMENT,ListName TEXT DEFAULT NONE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
