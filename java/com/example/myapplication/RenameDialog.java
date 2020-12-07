package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.util.ArrayList;

public class RenameDialog extends DialogFragment {
    private OnRenameData callback;

    interface OnRenameData{
        void renameData(String name);
    }

    private ArrayList<Recordings> recordings = null;
    private String filePath = "";
    private int position;

    public RenameDialog(ArrayList<Recordings> recordings, String filePath,int position,OnRenameData data) {
        this.recordings = recordings;
        this.filePath = filePath;
        this.position = position;
        this.callback = data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rename_dialog, null);
        TextView rename = view.findViewById(R.id.rename);
        String name = recordings.get(position).getFileName();
        int index = name.indexOf(".3gp");
        name = name.substring(0,index);
        rename.setText(name);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = rename.getText().toString().trim()+".3gp";
                        if(name.length()==0){
                            Toast.makeText(getContext(),"Filename should not be empty",Toast.LENGTH_LONG).show();
                            return;
                        }
                        for(Recordings r:recordings){
                            if(r.getFileName().equals(name)){
                                Toast.makeText(getContext(),"Filename already exists",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        File file = new File(getContext().getExternalFilesDir(null)+"/AudioRec",recordings.get(position).getFileName());
                        File file1 = new File(getContext().getExternalFilesDir(null)+"/AudioRec",name);
                        boolean renamed =file.renameTo(file1);
                        if (renamed) {
                            Toast.makeText(getContext(),"Filename renamed",Toast.LENGTH_LONG).show();
                            callback.renameData(name);
                        }else {
                            Log.d("LOG","File not renamed...");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}
