package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class DeleteDialog  extends DialogFragment {
    private OnDeleteData callback;

    interface OnDeleteData{
        void deleteData();
    }

    String path = null;
    public DeleteDialog(String path,OnDeleteData callback) {
        this.path = path;
        this.callback = callback;
    }


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this sound?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File file = new File(path);
                        file.delete();
                        callback.deleteData();
                        Toast.makeText(getContext(),"Audio recording deleted",Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(),"Deletion cancelled",Toast.LENGTH_LONG).show();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

