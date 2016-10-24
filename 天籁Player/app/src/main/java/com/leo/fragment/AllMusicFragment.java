package com.leo.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.leo.activity.PlayActivity;
import com.leo.activity.R;
import com.leo.database.DataBase;
import com.leo.database.OperateDataBase;
import com.leo.playview.playControl;

import java.util.ArrayList;

/**
 * Created by Leo on 15/12/4.
 */
public class AllMusicFragment extends ListFragment {

    DataBase db;
    SQLiteDatabase dbread;
    SQLiteDatabase dbwrite;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    public int flag;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_list, container, false);
        //System.out.println("FragementList is created");
        root.findViewById(R.id.btnReturnToPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bd=new Bundle();
                bd.putInt("id", -1);
                bd.putString("tableName",null);
                Intent i=new Intent(getActivity(),PlayActivity.class);
                i.putExtras(bd);
                startActivity(i);
            }
        });
        return  root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=DataBase.db1;
        dbread = db.getReadableDatabase();//没有数据库会建一个数据库,new db
        String SongName="示例音乐-珊瑚海";
        String SingerName="周杰伦";
        String Path="android.resource://com.leo.activity/raw/example1.mp3";
        String lrcPath="android.resource://com.leo.activity/raw/example.lrc";
        cursor = dbread.query("SongInfo", null, null, null, null, null, null);
        if(cursor.getCount()==0)
            OperateDataBase.insert("SongInfo", SongName, SingerName, Path,lrcPath);
        dbread.close();
        dbread=db.getReadableDatabase();
        cursor = dbread.query("SongInfo", null, null, null, null, null, null);
        System.out.println(cursor.getCount());
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_cell, cursor, new String[]{"SongName","SingerName"}, new int[]{R.id.TextViewSongName, R.id.TextViewSingerName});
        setListAdapter(adapter);
        dbread.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("FragmentList onStart");
        if(playControl.mediaPlayer==null)
            root.findViewById(R.id.btnReturnToPlay).setVisibility(View.GONE);
        else
            root.findViewById(R.id.btnReturnToPlay).setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            ListView listView=getListView();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bd=new Bundle();
                    bd.putInt("id", position);
                    bd.putString("tableName","SongInfo");
                    Intent i=new Intent(getActivity(),PlayActivity.class);
                    i.putExtras(bd);
                    try {
                        System.out.println("跳到另一个活动");
                        startActivity(i);
                    }
                    catch (Exception e){
                        System.out.println(position);
                        System.out.println(e);
                    }
                }
            });
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("error");
        }
    }
}
