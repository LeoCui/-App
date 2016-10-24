package com.leo.playview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.leo.activity.PlayActivity;
import com.leo.activity.R;
import com.leo.database.DataBase;
import com.leo.database.OperateDataBase;
import com.leo.musicinfo.LrcProcess;
import com.leo.musicinfo.SingleLrc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Leo on 16/3/15.
 */
public class playControl {

    static boolean threadStop;
    Cursor cursor;
    DataBase db;
    SQLiteDatabase dbread;
    public static String SongName;
    public static String SingerName;
    public static String path;
    public static String lrcPath;
    static int progress1;
    boolean mediaIsRead=false;
    public static MediaPlayer mediaPlayer=null;
    public int changeSong(final Context context,final int n, final String tableName){    //tablename, num: 表的第n个,n>=0
        db=DataBase.db1;
        threadStop=false;
        //mediaIsRead=false;
        if(mediaPlayer!=null){
            this.mediaPlayer.release();
            this.mediaPlayer=null;
            while(threadStop==false){
               // System.out.println(threadStop);
            }
        }
        try {
            dbread = db.getReadableDatabase();
            cursor = dbread.query(tableName, null, null, null, null, null, null);
        }
        catch (Exception e){
            System.out.println(e);
        }
        int count=cursor.getCount();
        int n1=n%count;
        for(int i=0;i<=n1;i++){
            cursor.moveToNext();
            SongName=cursor.getString(cursor.getColumnIndex("SongName"));
            SingerName=cursor.getString(cursor.getColumnIndex("SingerName"));
            dbread.close();
        }
        if(SongName==null){
            SongName="未知歌曲";
        }
        if(SingerName==null){
            SingerName="未知歌手";
        }
        if(SongName.length()>10){
            SongName=SongName.substring(0,10);
        }
        PlayActivity.songNameView.setText(SongName);
        PlayActivity.singerNameView.setText(SingerName);
        if(SongName.equals("示例音乐-珊瑚海")){
            mediaPlayer= MediaPlayer.create(context,R.raw.example2);
            showLrc(context, null, false);
            mediaPlayer.start();
            PlayActivity.duration.setText(LrcProcess.timeTrans2(mediaPlayer.getDuration()));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    next(context, n, tableName);
                }
            });
            OperateDataBase.insertRecentSong(SongName, SingerName,null,null);
        }
        else {
            dbread = db.getReadableDatabase();
            path = cursor.getString(cursor.getColumnIndex("Path"));
            lrcPath = cursor.getString(cursor.getColumnIndex("lrcPath"));
            dbread.close();
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    PlayActivity.duration.setText(LrcProcess.timeTrans2(mediaPlayer.getDuration()));
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //被逼无奈,才这样写
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next(context, n, tableName);
                        }
                    });
                }
            });
            try {
                mediaPlayer.setDataSource(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (IllegalStateException e){
                e.printStackTrace();
            //    mediaPlayer=null;  //这里会出现奇怪的错误,没有规律,只好让他重新搞一遍
           //     changeSong(context,n,tableName);
                System.out.println("莫名error");
            }
            showLrc(context,lrcPath,true);
            OperateDataBase.insertRecentSong(SongName, SingerName, path, lrcPath);
        }
        return n1;
    }


    public void showProcess(){
        Handler handler=new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        int currentTime = mediaPlayer.getCurrentPosition();
                        int time = mediaPlayer.getDuration();
                        int process = (int) (currentTime * 100.0 / time);
                        PlayActivity.seekBar.setProgress(process);
                        //PlayActivity.Process.setText(Integer.toString(mediaPlayer.getCurrentPosition()));
                        PlayActivity.Process.setText(LrcProcess.timeTrans2(mediaPlayer.getCurrentPosition()));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        });
        thread.start();
    }

    public void next(Context context,int n,String tableName){  //n>=0
        changeSong(context,n+1,tableName);
    }



    public  void last(Context context,int n,String tableName){
        changeSong(context,n-1,tableName);
    }

    public void pause(ImageView pause){
        pause.setImageResource(R.drawable.pause);
        mediaPlayer.pause();
    }

    public void play(ImageView play){
        play.setImageResource(R.drawable.play);
        mediaPlayer.start();
    }


    private  void showLrc(Context context,String lrcPath,boolean bool){    //true: path  false: raw,inside file

        final LrcProcess lrc=new LrcProcess(lrcPath,bool);
        lrc.Process(context);
        final lrcView lrcView=PlayActivity.lrcView1;
        lrcView.setmLrcList(lrc.lrcContent);
        final Handler handler=new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(mediaPlayer!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            int currentTime=mediaPlayer.getCurrentPosition();
                            int Time=mediaPlayer.getDuration();
                            int currentRow=getCurrentRow(currentTime,Time,lrc.lrcContent);
                            PlayActivity.lrcView1.setIndex(currentRow);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                threadStop=true;
            }
        });
        thread.start();
    }

    int getCurrentRow(int currentTime,int time,ArrayList<SingleLrc> lrcContent){
        if(lrcContent==null)
            return -1;         //歌词为空
        int size=lrcContent.size();
        for(int i=0;i<size-1;i++){
            if(currentTime<lrcContent.get(i).getStartTime()){
                return -2;     //当前位置无歌词
            }
            else{
                if(currentTime<lrcContent.get(i+1).getStartTime()){
                    return i;
                }
            }
        }
        return size-1;
    }

}
