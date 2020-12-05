package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordingsFragment extends Fragment implements AsyncData.OnDataDownload {
    private RecyclerView recyclerView = null;
    private RecyclerAdapter adapter = null;
    private String filePath;
    private ArrayList<Recordings> arrayList = null;
    private static final String TAG = "RecordingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getActivity().getExternalFilesDir(null).getAbsolutePath() + "/AudioRec";
        arrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recordings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AsyncData asyncData = new AsyncData(filePath, this);
        asyncData.start();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDownloadComplete(ArrayList<Recordings> recordings) {
        if (recordings.size()!=0) {
            adapter.setArrayList(recordings);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}