package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    interface OnItemClick {
        void OnItemClickListener(Recordings recordings);
    }

    private ArrayList<Recordings> arrayList = null;
    public OnItemClick callback;
    private FragmentManager manager;
    private Context context;

    public RecyclerAdapter(ArrayList<Recordings> arrayList, OnItemClick callback, FragmentManager manager, Context context) {
        this.arrayList = new ArrayList<>(arrayList);
        this.callback = callback;
        this.manager = manager;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String audioName = arrayList.get(position).getFileName();
        holder.audioName.setText(audioName);
        String durationAudio = arrayList.get(position).getDuration();
        holder.audioDuration.setText(durationAudio);
        String dateCreated = arrayList.get(position).getDateCreated();
        holder.dateCreated.setText(dateCreated);
        String size = arrayList.get(position).getFileSize();
        holder.size.setText(size);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements DeleteDialog.OnDeleteData, RenameDialog.OnRenameData {
        TextView audioName;
        TextView audioDuration;
        TextView dateCreated;
        TextView size;
        ImageButton menu;
        DeleteDialog.OnDeleteData deleteCallback;
        RenameDialog.OnRenameData renameCallback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioName);
            audioDuration = itemView.findViewById(R.id.audioDuration);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            size = itemView.findViewById(R.id.size);
            menu = itemView.findViewById(R.id.popMenu);
            deleteCallback =this;
            renameCallback = this;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Recordings recordings = arrayList.get(getAdapterPosition());
                    callback.OnItemClickListener(recordings);
                }
            });

            menu.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "onClick: Clicked");
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), itemView, Gravity.RIGHT);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.rename:
                                    RenameDialog dialogFragment = new RenameDialog(arrayList,arrayList.get(getAdapterPosition()).getPath(),getAdapterPosition(),renameCallback);
                                    dialogFragment.show(manager, "Rename");
                                    return true;

                                case R.id.share:
                                    Uri uri = Uri.parse(arrayList.get(getAdapterPosition()).getPath());
                                    Log.d("TAG", "onMenuItemClick: " + arrayList.get(getAdapterPosition()).getPath());
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("audio/*");
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    context.startActivity(Intent.createChooser(intent, "Share Sound File"));
                                    return true;
                                case R.id.delete:
                                    DeleteDialog deleteDialog = new DeleteDialog(arrayList.get(getAdapterPosition()).getPath(),deleteCallback);
                                    deleteDialog.show(manager,"Delete");
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.inflate(R.menu.popmenu);
                    popupMenu.show();
                    popupMenu.setGravity(Gravity.RIGHT);
                }
            });

        }

        @Override
        public void deleteData() {
            arrayList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }

        @Override
        public void renameData(String name) {
            Log.d("TAG", "renameData: Renamed");
            Recordings recording = arrayList.get(getAdapterPosition());
            recording.setFileName(name);
            arrayList.set(getAdapterPosition(),recording);
            notifyItemChanged(getAdapterPosition());
        }
    }


    public void setArrayList(ArrayList<Recordings> arrayList) {
        this.arrayList = arrayList;
    }

    public void clear() {
        this.arrayList.clear();
    }
}
