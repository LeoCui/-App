package com.leo.musicinfo;

import com.leo.database.OperateDataBase;

import java.io.File;

/**
 * Created by Leo on 15/12/4.
 */
public class FileSearch {
    public static void main(String[] args){

    }
    public static void traverseFolder2(String path) {

        File file = new File(path);
        //System.out.println("文件路径为"+file.getAbsolutePath()+"文件名字"+file.getName());
        if (file.exists()) {
            File[] files = file.listFiles();   //file is not a dictionary,return null
            if (files==null) {
                return;
            } else {
                if(files.length==0){
                    return;
                }
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                         //System.out.println("文件夹:" + file2.getAbsolutePath());
                        try {
                            traverseFolder2(file2.getAbsolutePath());
                        }
                        catch (Exception e){
                            System.out.println(e);
                            System.out.println(file2.getAbsolutePath());
                        }
                    } else {
                        if (file2.getName().endsWith(".mp3")||file2.getName().endsWith(".aac")||file2.getName().endsWith(".m4a")){
                            System.out.println("文件:" + file2.getAbsolutePath());
                            MusicInfo temp = new MusicInfo(file2.getAbsolutePath());
                            MusicInfo.musicInfos.add(temp);
                            MusicInfo.musicInfoHashMap.put(temp.SongName,temp);
                        }
                        else{
                            if(file2.getName().endsWith(".lrc")) {  //歌词格式是  歌名_歌手.lrc  歌手有时没有,所以只匹配歌名
                                System.out.println("歌词" + file2.getAbsolutePath());
                                String pathLrc = file2.getAbsolutePath();
                                MusicInfo.ircPaths.add(pathLrc);
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }
    public static void insertDataBase(){
        int size1=MusicInfo.ircPaths.size();
        for(int i=0;i<size1;i++){
            String lrcPath=MusicInfo.ircPaths.get(i);
            int i1=lrcPath.lastIndexOf('/');
            int i2=lrcPath.indexOf('_');
            String songName=lrcPath.substring(i1 + 1, i2);
            MusicInfo tempMusicInfo;
            if((tempMusicInfo=MusicInfo.musicInfoHashMap.get(songName))!=null){
                tempMusicInfo.lrcPath=lrcPath;
            }
        }
        int size2=MusicInfo.musicInfos.size();
        System.out.println("size  "+size2);
        for(int i=0;i<size2;i++){
            OperateDataBase.insert
                    ("SongInfo", MusicInfo.musicInfos.get(i).SongName, MusicInfo.musicInfos.get(i).SingerName,
                            MusicInfo.musicInfos.get(i).path, MusicInfo.musicInfos.get(i).lrcPath);
            System.out.println(MusicInfo.musicInfos.get(i).path);
            if(MusicInfo.musicInfos.get(i).lrcPath!=null) {
                System.out.println(MusicInfo.musicInfos.get(i).lrcPath);
            }
        }
    }
}
