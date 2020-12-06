package com.example.myapplication;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    interface OnItemClick{
        void OnItemClickListener(Recordings recordings);
    }
    private ArrayList<Recordings> arrayList = null;
    public OnItemClick callback;

    public RecyclerAdapter(ArrayList<Recordings> arrayList,OnItemClick callback) {
        this.arrayList = new ArrayList<>(arrayList);
        this.callback = callback;
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
        String durationAudio =  arrayList.get(position).getDuration();
        holder.audioDuration.setText(durationAudio);
        String dateCreated = arrayList.get(position).getDateCreated();
        holder.dateCreated.setText(dateCreated);
        String size = arrayList.get(position).getFileSize();
        holder.size.setText(size);
    }



     class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Recordings recordings = arrayList.get(getAdapterPosition());
                    callback.OnItemClickListener(recordings);
                }
            });
        }

         @Override
         public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

         }
     }



    public void setArrayList(ArrayList<Recordings> arrayList){
        this.arrayList = arrayList;
    }

    public void clear(){
        this.arrayList.clear();
    }
}
