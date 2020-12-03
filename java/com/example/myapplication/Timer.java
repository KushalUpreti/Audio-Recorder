package com.example.myapplication;

import android.os.Handler;
import android.widget.TextView;

import java.io.Serializable;

public class Timer implements Serializable {
    private boolean isRunning = false;
    private int seconds = 0;
    private TextView textView;

    Timer(TextView textView){
        this.textView = textView;
        timer();
    }

    private void timer() {
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / (3600);
                int min = (seconds % 3600) / 60;
                int sec = seconds % 60;
                String ans = String.format("%02d:%02d:%02d", hours, min, sec);
                textView.setText(ans);

                if (isRunning) {
                    seconds++;
                }
                h.postDelayed(this, 1000);
            }
        });
    }

    public void start(){
        isRunning = true;
    }

    public void stop(){
        isRunning = false;
        seconds = 0;
    }

    public  void pause(){
        isRunning = false;
    }

    public void resume(){
        isRunning = true;
    }

    public int getSeconds(){
        return seconds;
    }

    public boolean getIsRunning(){
        return isRunning;
    }

    public void setSeconds(int seconds1){
        seconds = seconds1;
    }

    public void setRunning(boolean running){
        isRunning = running;
    }
}
