package com.leo.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Leo on 15/12/9.
 */

public class OperateDataBase {
    static DataBase db;
    static SQLiteDatabase dbwrite;
    static SimpleCursorAdapter adapter;
    public static final int MaxRecentSongCount=20;  //最近播放的20首歌曲
    public static void insert(String TableName, String SongName, String SingerName, String Path, String lrcPath){
        db=DataBase.db1;
        dbwrite=db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SongName", SongName);
        values.put("SingerName",SingerName);
        values.put("Path",Path);
        values.put("lrcPath",lrcPath);
        dbwrite = db.getWritableDatabase();
        dbwrite.insert(TableName, null, values);
        Cursor cursor=dbwrite.query(TableName, null, null, null, null, null, null);
        int count=cursor.getCount();
        //refreshListView(adapter);
    }
    public static void insert(String TableName,String ListName){
        db=DataBase.db1;
        dbwrite=db.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("ListName",ListName);
        dbwrite.insert(TableName, null, values);
        dbwrite.close();
    }
    static void insertLrc(String lrcPath){
        SQLiteDatabase dbwrite=DataBase.db1.getWritableDatabase();

    }
    static public void insertRecentSong(String SongName,String SingerName,String Path,String lrcPath){
        db=DataBase.db1;
        dbwrite=db.getWritableDatabase();
        Cursor cursor1;
        cursor1=dbwrite.query("RecentSong",null,null,null,null,null,null);
        int count=cursor1.getCount();
        OperateDataBase.insert("RecentSong", SongName, SingerName, Path, lrcPath);
        dbwrite.close();
        while(count>=OperateDataBase.MaxRecentSongCount){
            cursor1.moveToNext();
            int id=cursor1.getInt(cursor1.getColumnIndex("_id"));
            OperateDataBase.delete("RecentSong",id);
            count--;
        }
    }
    public static void dropTable(String TableName){
        db=DataBase.db1;
        dbwrite=db.getWritableDatabase();
        dbwrite.execSQL("DROP TABLE " + TableName);
        dbwrite.close();
    }
    public static void delete(String TableName,int id){
        db=DataBase.db1;
        dbwrite=db.getWritableDatabase();
        dbwrite.delete(TableName, "_id=?", new String[]{id + ""});
        dbwrite.close();
    }
    public static void delete(String TableName,String ListName){
        db=DataBase.db1;
        dbwrite=db.getWritableDatabase();
        dbwrite.delete(TableName,"ListName=?",new String[]{ListName});
        dbwrite.close();
    }
//    private  void refreshListView(SimpleCursorAdapter adapter){
//        dbwrite=db.getWritableDatabase();
//        Cursor cursor = dbwrite.query("user_info", null, null, null, null, null, null);
//        adapter.changeCursor(cursor);
//        setListAdapter(adapter);
//        dbwrite.close();
//    }
}
