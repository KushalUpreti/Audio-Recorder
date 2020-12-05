package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;

public class RecordFragment extends Fragment {
    private TextView timer;
    private Timer timerClass;
    private ImageButton recordButton;
    private ImageButton pauseButton;
    private MyRecorder myRecorder = null;

    private boolean isRecording = false;
    private boolean permissionToRecordAccepted = false;
    private boolean isPaused = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String filePath = null;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private String fileName = null;
    private String savedName = null;

    private String key = "MediaRecorder";
    private String savedIsRecording = "isRecording";
    private String savedIsPaused = "isPaused";
    private String savedSeconds = "Seconds";
    private String savedIssRunning = "isRunning";
    private String savedFileName = "filename";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timer = view.findViewById(R.id.textView);
        recordButton = view.findViewById(R.id.recordButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        filePath = getActivity().getExternalFilesDir(null).getAbsolutePath() + "/AudioRec";

        if (savedInstanceState != null) {
            timerClass = new Timer(timer);
            timerClass.setSeconds(savedInstanceState.getInt(savedSeconds));
            timerClass.setRunning(savedInstanceState.getBoolean(savedIssRunning));

            MyRecorder recorder = (MyRecorder) savedInstanceState.getSerializable(key);
            isRecording = savedInstanceState.getBoolean(savedIsRecording);
            isPaused = savedInstanceState.getBoolean(savedIsPaused);
            if (recorder != null) {
                myRecorder = recorder;
            }

            if (isRecording) {
                myRecorder.resume();
                animateButton(pauseButton,true);
                recordButton.setImageResource(R.drawable.ic_baseline_stop_24);
            }
            if(isPaused){
                pauseButton.setImageResource(android.R.drawable.ic_media_play);
            }else {
                pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
            savedName = savedInstanceState.getString(savedFileName);
        }else {
            timerClass = new Timer(timer);
        }

        recordButton.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                permissionToRecordAccepted = true;
            }
            if (!permissionToRecordAccepted) {
                return;
            }
            if (!isRecording) {
                startRecording();
                animateButton(pauseButton, true);
                timerClass.start();
            } else {
                stopRecording();
                timerClass.stop();
                animateButton(pauseButton, false);
            }
            isRecording = !isRecording;
        });

        pauseButton.setOnClickListener(v -> {
            if (!isPaused) {
                pauseRecording();
                pauseButton.setImageResource(android.R.drawable.ic_media_play);
            } else {
                resumeRecording();
                pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
            isPaused = !isPaused;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) {
            getActivity().finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int seconds = timerClass.getSeconds();
        boolean isRunning = timerClass.getIsRunning();
        if (isRecording) {
            myRecorder.pause();
        }
        outState.putSerializable(key, myRecorder);
        outState.putBoolean(savedIsRecording, isRecording);
        outState.putBoolean(savedIsPaused, isPaused);
        outState.putInt(savedSeconds, seconds);
        outState.putBoolean(savedIssRunning, isRunning);
        outState.putString(savedFileName,savedName);
        super.onSaveInstanceState(outState);
    }

//    <-- ---------------------------------------------------------------------------------------------- -->
//    Regular methods below

    private void startRecording() {
        recordButton.setImageResource(R.drawable.ic_baseline_stop_24);
        String name = getFileName();
        myRecorder = new MyRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setOutputFile(name);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            myRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
        myRecorder.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void pauseRecording() {
        myRecorder.pause();
        timerClass.pause();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void resumeRecording() {
        myRecorder.resume();
        timerClass.resume();
    }

    private void stopRecording() {
        recordButton.setImageResource(R.drawable.ic_baseline_audiotrack_24);
        myRecorder.stop();
        myRecorder.release();
        myRecorder = null;
        Toast.makeText(getContext(), "Saved as " + savedName, Toast.LENGTH_LONG).show();
    }

    private String getFileName() {
        File file = new File(filePath);
        File[] list = file.listFiles();
        int max = 0;
        int acCount;
        for (File f : list) {
            int dotIndex = f.getName().indexOf(".");
            try {
                acCount = Integer.parseInt(f.getName().substring(9, dotIndex));
            } catch (NumberFormatException e) {
                continue;
            } catch (StringIndexOutOfBoundsException oob) {
                continue;
            }
            if (acCount > max) {
                max = acCount;
            }
        }
        max++;
        savedName = "Recording" + max + ".3gp";
        fileName = filePath + "/" + savedName;

        return fileName;
    }

    private void animateButton(ImageButton button, boolean appear) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return;
        }
        if (appear) {
            button.setAlpha(0f);
            button.setVisibility(View.VISIBLE);
            button.animate()
                    .alpha(1f)
                    .setDuration(300);
        } else {
            button.animate()
                    .alpha(0f)
                    .setDuration(300).withEndAction(() -> button.setVisibility(View.GONE));
        }
    }
}