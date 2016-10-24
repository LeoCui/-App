package com.leo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.leo.database.DataBase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {

    DataBase db;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("mainactivity has been destoryed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db= new DataBase(this, "MusicInfo", null, 1);
        DataBase.db1=db;
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                LaunchActivity.this.finish();
                Intent i=new Intent(LaunchActivity.this,MainActivity.class);
                startActivity(i);
            }
        };
        timer.schedule(timerTask,1500);
//        ArrayList<String>  temp=MusicList.SongList;
//        int len=temp.size();
//        for(int i=0;i<len;i++)
//            System.out.println(temp.get(i));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
