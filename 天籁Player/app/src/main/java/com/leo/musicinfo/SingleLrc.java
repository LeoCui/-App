package com.leo.musicinfo;

/**
 * Created by Leo on 16/3/12.
 */
public class SingleLrc {
    int  startTime;
    String lrc;
    SingleLrc(String lrc1,int time){
        lrc=lrc1;
        startTime=time;
    }
    public int getStartTime(){
        return startTime;
    }

    public String getLrc(){
        return lrc;
    }
}
