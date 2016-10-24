package com.leo.musicinfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leo on 15/12/4.
 */

public class MusicInfo {
    static HashMap<String,MusicInfo> musicInfoHashMap=new HashMap<String,MusicInfo>();
    static ArrayList<MusicInfo> musicInfos=new ArrayList<MusicInfo>();
    static ArrayList<String> ircPaths=new ArrayList<String>();
    String path;
    String lrcPath;
    String SongName;
    String SingerName;
    MusicInfo(String path){
        this.path=path;
        this.lrcPath=null;
        getSongAndSinger(path);
    }
    void getSongAndSinger(String path){
        String netEase="netease";
        String xiaMi="xiami";
        String kuwoMusic="KuwoMusic";
        if(path.contains(netEase)) {
            usualSplitMethood('-',false);
        }
        else {
            if (path.contains(xiaMi)) {
                usualSplitMethood('_', true);
            }
            else {
                if (path.contains(kuwoMusic)) {
                    usualSplitMethood('-', true);
                }
                else{
                    usualSplitMethood('-',true);
                }
            }
        }
    }
    void usualSplitMethood(char splitChar,boolean bool){  //splitChar 表示歌手和歌曲之间的分割符,bool=true表示歌曲-歌手顺序
        int i1 = path.lastIndexOf("/");
        int i2;
        if(bool==true){
            i2=path.indexOf(splitChar);
        }
        else {
            i2 = path.lastIndexOf(splitChar);
        }
        int i3 = path.lastIndexOf(".");
        if (i2 == -1) {
            SongName = path.substring(i1 + 1, i3);
            SingerName = "未知";
        } else {
            try {
                if(bool==true) {
                    SongName = path.substring(i1 + 1, i2);
                    SingerName = path.substring(i2 + 1, i3);
                }
                else {
                    SongName = path.substring(i1 + 1, i2-1);
                    SingerName = path.substring(i2 + 2, i3);
                }
            } catch (Exception e) {
                System.out.println(path);
                System.out.println(e);
            }
        }
        if(bool==false){
            String temp=SongName;
            SongName=SingerName;
            SingerName=temp;
        }
    }
}
