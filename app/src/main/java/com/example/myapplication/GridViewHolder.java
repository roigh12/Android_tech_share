package com.example.myapplication;

import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;

public class GridViewHolder extends AbsViewHolder {


    public GridViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.title = view.findViewById(R.id.title);
        this.size = view.findViewById(R.id.size);
        this.icon = view.findViewById(R.id.icon);
    }

    @Override
    public void setData(File data) {
        if (data.isDirectory()) {
            this.icon.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp);
        } else {
            this.icon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);

        }
        this.title.setText(data.getName());
        if (data.isDirectory() && data.listFiles() != null) {
            this.size.setText("" + data.listFiles().length + "개");
        } else {
            this.size.setText("");
        }

    }
}
