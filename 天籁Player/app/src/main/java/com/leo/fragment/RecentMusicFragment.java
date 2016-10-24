package com.leo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.leo.activity.PlayActivity;
import com.leo.activity.R;
import com.leo.database.DataBase;
import com.leo.database.OperateDataBase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leo on 15/12/15.
 */
public class RecentMusicFragment extends ListFragment {
    DataBase db;
    SQLiteDatabase dbread;
    SQLiteDatabase dbwrite;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_recent_list, container, false);
        //System.out.println("FragementList is created");
        return  root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("fragmentRecentList  on  created");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        System.out.println("fragmentRecentList has OnViewCreated");
        super.onViewCreated(view, savedInstanceState);
        try{
            listView=getListView();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dbread=db.getReadableDatabase();
                    Cursor cursor=dbread.query("RecentSong",null,null,null,null,null,null);
                    int count=cursor.getCount();
                    position=count-1-position;
                    dbread.close();
                    Bundle bd=new Bundle();
                    bd.putInt("id", position);
                    bd.putString("tableName","RecentSong");
                    Intent i=new Intent(getActivity(),PlayActivity.class);
                    i.putExtras(bd);
                    try {
                        System.out.println("跳到另一个活动");
                        startActivity(i);
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                }
            });}
        catch (Exception e){
            System.out.println(e);
            System.out.println("error");
        }
    }

    @Override
    public void onStart() {
        System.out.println("fragmentRecent has onStart");
        super.onStart();
        db=DataBase.db1;
        dbread = db.getReadableDatabase();
        ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
        Cursor cursor = dbread.query("RecentSong", null, null, null, null, null,null);
        int count=cursor.getCount();
        ArrayList<String> tempSongName=new ArrayList<String>();
        ArrayList<String> tempSingleName=new ArrayList<String>();
        for(int i=count;i>=1;i--){
            cursor.moveToNext();
            String songName=cursor.getString(cursor.getColumnIndex("SongName"));
            String singerName=cursor.getString(cursor.getColumnIndex("SingerName"));
            tempSongName.add(songName);
            tempSingleName.add(singerName);
        }
        for(int i=count-1;i>=0;i--){
            HashMap<String,String> item=new HashMap<String,String>();
            item.put("songName",tempSongName.get(i));
            item.put("singerName",tempSingleName.get(i));
            arrayList.add(item);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),arrayList,R.layout.listview_cell,new String[]{"songName","singerName"},new int[]{R.id.TextViewSongName,R.id.TextViewSingerName});
        listView.setAdapter(simpleAdapter);
        dbread.close();
    }
}
