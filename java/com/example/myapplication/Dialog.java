package com.example.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

public class Dialog extends DialogFragment {
    private static final String TAG = "Dialog";
    private ImageButton button = null;
    private SeekBar seekBar = null;
    private TextView textView;
    private TextView timeDisplay;
    private SeekBar volumeBar = null;
    private Switch aSwitch;
    private boolean isPaused = false;
    private boolean isPlaying = true;

    Recordings recording = null;
    MediaPlayer mediaPlayer;

    public Dialog(Recordings recording) {
        this.recording = recording;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.play_dialog, null);
        textView = view.findViewById(R.id.audioName);
        timeDisplay = view.findViewById(R.id.timeDisplay);
        textView.setText(recording.getFileName());
        play();
        button = view.findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlaying){
                    play();
                    isPlaying = true;
                    button.setImageResource(android.R.drawable.ic_media_pause);
                }else {
                    if(!isPaused){
                        button.setImageResource(android.R.drawable.ic_media_play);
                        mediaPlayer.pause();
                    }else{
                        button.setImageResource(android.R.drawable.ic_media_pause);
                        mediaPlayer.start();
                    }
                    isPaused = !isPaused;
                }
            }
        });

        volumeBar = view.findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress/100f;
                setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer!=null && isPlaying){
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });

        aSwitch = view.findViewById(R.id.loop);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isPlaying){return;}
                if(isChecked){
                    mediaPlayer.setLooping(isChecked);
                }else {
                    mediaPlayer.setLooping(isChecked);
                }
            }
        });

        askSeek();
        builder.setView(view);
        // Add action buttons
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mediaPlayer!=null){
            stop();
        }
    }

    private void play() {
        mediaPlayer = new MediaPlayer();
        Uri uri = Uri.parse(recording.getPath());

        try {
            mediaPlayer.setDataSource(getContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private void stop(){
        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void setVolume(float volume) {
        mediaPlayer.setVolume(volume,volume);
    }

    private void askSeek() {
        int duration = mediaPlayer.getDuration();
        seekBar.setMax(duration);
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                int position=0;
                int currentPos = 0;
                if(mediaPlayer!=null){
                    currentPos = mediaPlayer.getCurrentPosition();
                    position = currentPos/1000;
                }
                if(currentPos>=duration){
                    button.setImageResource(android.R.drawable.ic_media_play);
                    stop();
                }else{
                    int seconds = position % 60;
                    int min = position/60;
                    String seek= String.format("%02d:%02d", min, seconds);
                    timeDisplay.setText(seek);
                    seekBar.setProgress(currentPos);
                }

                h.postDelayed(this, 100);
            }
        });
    }
}
