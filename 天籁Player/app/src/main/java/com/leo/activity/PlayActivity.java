package com.leo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.leo.database.DataBase;
import com.leo.database.OperateDataBase;
import com.leo.musicinfo.LrcProcess;
import com.leo.musicinfo.SingleLrc;
import com.leo.playview.lrcView;
import com.leo.playview.playControl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import android.os.Handler;

/**
 * Created by Leo on 15/12/10.
 */
public class PlayActivity extends AppCompatActivity {

    Cursor cursor;
    DataBase db;
    SQLiteDatabase dbread;
    static boolean firstPlay=true;
    static int PresentSong;       //当前正在播放的歌曲是第几首
    Bundle bd;
    String Path;
    String lrcPath;
    String SongName;
    String SingerName;
    static  String TableName;
    static  int n;
    public static  lrcView lrcView1;
    public static  TextView songNameView;
    public static  TextView singerNameView;
    public static  TextView Process;
    public static  TextView duration;
    public static  Context context;
    public static  SeekBar seekBar;
    ImageView AddToList;
    ImageView Pause;
    ImageView Next;
    ImageView Last;
    int index;
    boolean isPause;
    String[] MyList=new String[20];
    private int progress1;


    protected void onDestroy() {
        super.onDestroy();
        //mediaPlayer.release();
        System.out.println("PlayMusic has been destoryed");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_play_music1);
        inflatePicture();
        db=DataBase.db1;
        bd=getIntent().getExtras();
        n=bd.getInt("id");      //n>=0
        if(n>=0) {
            TableName = bd.getString("tableName");
        }
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        lrcView1=(lrcView)findViewById(R.id.lrcView);
        songNameView=(TextView)findViewById(R.id.TextViewSongName);
        singerNameView=(TextView)findViewById(R.id.TextViewSingerName);
        Process=(TextView)findViewById(R.id.TextViewProcess);
        duration=(TextView)findViewById(R.id.TextViewDuration);
        Pause=(ImageView)findViewById(R.id.btnPause);
        if (firstPlay = true) {
            new playControl().showProcess();
            firstPlay = false;
            PlayActivity.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress1 = (int) (progress * 1.0 / seekBar.getMax() * playControl.mediaPlayer.getDuration());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    playControl.mediaPlayer.seekTo(progress1);
                }
            });
            findViewById(R.id.btnNextSong).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new playControl().next(PlayActivity.this, PresentSong, TableName);
                    PresentSong = (PresentSong + 1);
                }
            });
            findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayActivity.this.finish();

                }
            });
            findViewById(R.id.btnPause).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPause == false) {
                        new playControl().pause(Pause);
                        isPause = true;
                    } else {
                        new playControl().play(Pause);
                        isPause = false;
                    }
                }
            });
            findViewById(R.id.btnLast).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new playControl().last(PlayActivity.this,PresentSong,TableName);
                    PresentSong--;
                }
            });
            findViewById(R.id.btnAddTo).setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    Cursor CursorTemp;
                    dbread=db.getReadableDatabase();
                    CursorTemp=dbread.query("MyList", null, null, null, null, null, null);
                    System.out.println("CursorTemp的值为"+CursorTemp.getCount());
                    int count=CursorTemp.getCount();
                    String[] StringTemp=new String[count];
                    int i=0;
                    while(CursorTemp.moveToNext()){
                        try {
                            String temp=CursorTemp.getString(CursorTemp.getColumnIndex("ListName"));
                            StringTemp[i] = temp;
                            MyList[i++]=temp;
                        }
                        catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    new AlertDialog.Builder(PlayActivity.this).setTitle("添加到列表")
                            .setSingleChoiceItems(StringTemp, 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    index = which;
                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println(index);
                            insert(index);
                        }
                    }).setNegativeButton("取消",null).show();
                }
            });
        }
        if(n==-1){    //从主界面返回
            new playControl().play(Pause);
        }
        else {
            PresentSong = new playControl().changeSong(this, n, TableName);
            isPause = false;
        }

    }
    private  void inflatePicture() {
        AddToList = (ImageView) findViewById(R.id.btnAddTo);
        Pause=(ImageView)findViewById(R.id.btnPause);
        //  Play=(ImageView)findViewById(R.id.btnPlay);
        Next = (ImageView) findViewById(R.id.btnNextSong);
        Last = (ImageView) findViewById(R.id.btnLast);
        AddToList.setImageResource(R.drawable.backup);
        Pause.setImageResource(R.drawable.play);

//        Play.setImageResource(R.drawable.play);
        Next.setImageResource(R.drawable.next_song);
        Last.setImageResource(R.drawable.last_song);
        // Play.setVisibility(Play.INVISIBLE);
    }


    void insert(int index) {
        String TableName = MyList[index];
        OperateDataBase.insert(TableName, playControl.SongName, playControl.SingerName, playControl.path, playControl.lrcPath);
    }

}
