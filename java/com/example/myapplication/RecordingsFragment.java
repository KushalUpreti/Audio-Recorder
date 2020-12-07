package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class RecordingsFragment extends Fragment implements AsyncData.OnDataDownload, RecyclerAdapter.OnItemClick {
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private RecyclerView recyclerView = null;
    private RecyclerAdapter adapter = null;
    private String filePath = null;
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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                asyncData.start();
            }
        });
        asyncData.start();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(recyclerView);
        FragmentManager manager = getParentFragmentManager();
        adapter = new RecyclerAdapter(arrayList,this, manager,getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDownloadComplete(ArrayList<Recordings> recordings) {
        if (recordings.size()!=0) {
            Log.d(TAG, "onDownloadComplete: "+recordings.size());
            adapter.setArrayList(recordings);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void OnItemClickListener(Recordings recordings) {
//        MediaPlayer mediaPlayer = new MediaPlayer();
            Dialog dialog = new Dialog(recordings);
            dialog.show(getParentFragmentManager(),"Play");
    }
}