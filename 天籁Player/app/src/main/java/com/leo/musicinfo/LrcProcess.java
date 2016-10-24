package com.leo.musicinfo;

import android.content.Context;

import com.leo.activity.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Leo on 16/3/12.
 */
public class LrcProcess {
    //public List<HashMap<String,String>> lrcContent=new ArrayList<HashMap<String,String>>();
    public ArrayList<SingleLrc> lrcContent=new ArrayList<SingleLrc>();
    String lrcPath;
    boolean bool;
    public LrcProcess(String Path,boolean bool){
        lrcPath=Path;
        this.bool=bool;
    }

    public void Process(Context context){
        if(bool==true) {
            if(lrcPath==null){
                lrcContent=null;
            }
            else {
                File file = new File(lrcPath);
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String str;
                try {
                    while ((str = br.readLine()) != null) {
                        if(!str.equals("")) {
                            singleLrcProcess(str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            InputStream input = context.getResources().openRawResource(R.raw.example);
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(input));
            String str;
            try {
                while ((str = br.readLine()) != null) {
                    singleLrcProcess(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void singleLrcProcess(String lrc){
        if(lrc.charAt(1)>='0'&&lrc.charAt(1)<='9'){
            int len=lrc.length();
            int begin=lrc.lastIndexOf('[');
            int end=lrc.lastIndexOf(']');
            String timeStr=lrc.substring(begin + 1, end);
            String lrcStr=lrc.substring(end + 1, len);
            int time=timeTrans(timeStr);
            lrcContent.add(new SingleLrc(lrcStr,time));
            System.out.println(lrcContent.size());
        }
    }
    int timeTrans(String str){
        int time;
        str=str.replace('.',':');
        String[] time1=str.split(":");
        int minute=Integer.parseInt(time1[0]);
        int second=Integer.parseInt(time1[1]);
        int msecond=Integer.parseInt(time1[2]);
        time=(minute*60+second)*1000+msecond*10;
        return  time;
    }

    public static String timeTrans2(int time){
        time/=1000;
        int minute=time/60;
        int second=time%60;
        String result;
        if(second<10) {
             result = Integer.toString(minute) + ":0" + Integer.toString(second);
        }
        else{
             result=Integer.toString(minute)+":"+Integer.toString(second);
        }
        return result;
    }
}
