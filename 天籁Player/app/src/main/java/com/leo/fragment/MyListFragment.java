package com.leo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.leo.activity.R;
import com.leo.database.DataBase;
import com.leo.database.OperateDataBase;

/**
 * Created by Leo on 15/12/16.
 */
public class MyListFragment extends ListFragment {
    DataBase db;
    SQLiteDatabase dbread;
    SQLiteDatabase dbwrite;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    //EditText temp=new EditText(getActivity());
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=DataBase.db1;
        dbread = db.getReadableDatabase();
        cursor = dbread.query("MyList", null, null, null, null, null, null);
        System.out.println(cursor.getCount());
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.my_listview_cell, cursor, new String[]{"ListName"}, new int[]{R.id.TextViewListName});
        setListAdapter(adapter);
        dbread.close();
        System.out.println("the cursor is"+cursor.getCount());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root;
        root=inflater.inflate(R.layout.fragment_mylist,container,false);
        root.findViewById(R.id.btnNewList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText temp = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("输入列表名").setView(temp)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String ListName=null;
                                ListName=temp.getText().toString();
                                dbwrite = db.getWritableDatabase();
                                try {
                                    dbwrite.execSQL("CREATE TABLE " + ListName +
                                            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,SongName TEXT DEFAULT NONE,SingerName TEXT DEFAULT NONE,Path TEXT DEFAULT NONE,lrcPath TEXT DEFAULT NONE)");
                                    OperateDataBase.insert("MyList", ListName);
                                } catch (Exception e) {
                                    System.out.println(e);
                                    Toast toast;
                                    toast = Toast.makeText(getActivity(), "请输入合法的列表名", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                dbwrite.close();
                                refreshListView();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        return  root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView=getListView();
        System.out.println("the cursor is   "+cursor.getCount());
        //listView.setOnItemClickListener();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String TableName=cursor.getString(cursor.getColumnIndex("ListName"));
                Bundle bd=new Bundle();
                bd.putString("TableName", TableName);
                MyListMusicFragment fragmentUsualList=new MyListMusicFragment();
                fragmentUsualList.setArguments(bd);
                try{
                    getFragmentManager().beginTransaction().addToBackStack(null)
                            .remove(MyListFragment.this).add(R.id.main_content, fragmentUsualList).commit();
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //cursor.moveToPosition(position);
                System.out.println("长按后"+cursor.getCount());
                System.out.println(position);
                // dbread=db.getReadableDatabase();
                //cursor=dbread.query("MyList",null,null,null,null,null,null);
                cursor.moveToPosition(position);
                new AlertDialog.Builder(getActivity()).setTitle("你确定要删除该列表")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    String ListName=cursor.getString(cursor.getColumnIndex("ListName"));
                                    OperateDataBase.dropTable(ListName);
                                    OperateDataBase.delete("MyList", ListName);
                                    refreshListView();
                                }
                                catch (Exception e){
                                    System.out.println(e);
                                }
                            }
                        }).setNegativeButton("取消",null).show();
                return true;
            }
        });
    }
    private  void refreshListView(){
        dbwrite=db.getWritableDatabase();
        cursor = dbwrite.query("MyList", null, null, null, null, null, null);  //妈蛋,这两个应该是同一个cursor
        adapter.changeCursor(cursor);
        setListAdapter(adapter);
        dbwrite.close();
    }
}
