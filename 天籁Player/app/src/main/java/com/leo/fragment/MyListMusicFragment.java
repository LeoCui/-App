package com.leo.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.leo.activity.PlayActivity;
import com.leo.activity.R;
import com.leo.database.DataBase;
import com.leo.database.OperateDataBase;

/**
 * Created by Leo on 15/12/15.
 */
public class MyListMusicFragment extends ListFragment {
    DataBase db;
    SQLiteDatabase dbread;
    SQLiteDatabase dbwrite;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    String TableName;
    int Id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragement_usual_list, container, false);
        //System.out.println("FragementList is created");
        TextView temp;
        temp=(TextView)root.findViewById(R.id.TextViewListName);
        temp.setText(TableName);
        root.findViewById(R.id.btnBackToMyList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return  root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=DataBase.db1;
        dbread = db.getReadableDatabase();
        TableName=getArguments().getString("TableName");
        cursor = dbread.query(TableName, null, null, null, null, null, "_id");
        int count=cursor.getCount();
        System.out.println(cursor.getCount());
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_cell, cursor, new String[]{"SongName","SingerName"}, new int[]{R.id.TextViewSongName, R.id.TextViewSingerName});
        setListAdapter(adapter);
        dbread.close();
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
                    bd.putString("tableName",TableName);
                    Intent i=new Intent(getActivity(),PlayActivity.class);
                    i.putExtras(bd);
                    try {
                        System.out.println("跳到另一个活动");
                        startActivity(i);
                    }
                    catch (Exception e){
                        System.out.println(Id);
                        System.out.println(e);
                    }
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor.moveToPosition(position);
                    Context context=getActivity();
                    Id=cursor.getInt(cursor.getColumnIndex("_id"));
                    new AlertDialog.Builder(getActivity()).setNegativeButton("取消",null)
                            .setTitle("将此歌曲移出该列表?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OperateDataBase.delete(TableName, Id);
                                    refreshListView();
                                }
                            }).show();
                    return  true;
                }
            });
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("error");
        }
    }
    private  void refreshListView(){
        dbwrite=db.getWritableDatabase();
        cursor = dbwrite.query(TableName, null, null, null, null, null, null);  //妈蛋,这两个应该是同一个cursor
        adapter.changeCursor(cursor);
        setListAdapter(adapter);
        dbwrite.close();
    }
}
