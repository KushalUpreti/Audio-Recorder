package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<Recordings> arrayList = null;

    public RecyclerAdapter(ArrayList<Recordings> arrayList) {
        this.arrayList = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem,parent,false);
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
        String audioDuration = arrayList.get(position).getDuration();
        double duration = Double.parseDouble(audioDuration)/1000f;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        String durationAudio =  numberFormat.format(duration);
        holder.audioDuration.setText(durationAudio+" min");
        String dateCreated = arrayList.get(position).getDateCreated();
        holder.dateCreated.setText(dateCreated);
        String size = arrayList.get(position).getFileSize();
        holder.size.setText(size);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView audioName;
        TextView audioDuration;
        TextView dateCreated;
        TextView size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioName);
            audioDuration = itemView.findViewById(R.id.audioDuration);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            size = itemView.findViewById(R.id.size);
        }
    }

    public void setArrayList(ArrayList<Recordings> arrayList){
        this.arrayList = arrayList;
    }
}
