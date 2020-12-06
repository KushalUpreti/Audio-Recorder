package com.example.myapplication;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AsyncData  {
    interface OnDataDownload{
        void onDownloadComplete(ArrayList<Recordings> recordings);
    };

    private String path = null;
    private OnDataDownload callback;
    private ArrayList<Recordings> arrayList = null;

    public AsyncData(String path, OnDataDownload callback) {
        this.path = path;
        arrayList = new ArrayList<>();
        this.callback = callback;
    }

    public void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fetchData(path);
                callback.onDownloadComplete(arrayList);
            }
        });
        thread.start();
    }

    private void fetchData(String filePath){

        File file = new File(filePath);
        File[] files = file.listFiles();
        if(files.length==0){return;}
        for(File file1:files){
            String fileName = file1.getName();
            if(fileName.length()>20){
                fileName = fileName.substring(0,8)+"..."+fileName.substring(fileName.length()-8,fileName.length());
            }
            long dateCreated = file1.lastModified();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date(dateCreated));
            String path = file1.getAbsolutePath();
            double size = file1.length()/(1024f*1024f);
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            String fileSize =  numberFormat.format(size)+ " MB";
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int duration1 = Integer.parseInt(duration)/1000;
            int seconds = duration1 % 60;
            int min = duration1/60;
            String dur= String.format("%02d:%02d", min, seconds);
            Recordings recordings = new Recordings(fileName,date,dur,path,fileSize);
            arrayList.add(recordings);
        }
    }
}
