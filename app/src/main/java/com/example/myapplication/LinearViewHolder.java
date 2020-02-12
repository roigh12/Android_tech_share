package com.example.myapplication;

import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;

public class LinearViewHolder extends AbsViewHolder{

    private Format format = new SimpleDateFormat("M월 d일 a h:mm");


    public LinearViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.title = view.findViewById(R.id.title);
        this.date = view.findViewById(R.id.date);
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
        this.date.setText(format.format(data.lastModified()));
        if (data.isDirectory() && data.listFiles() != null) {
            this.size.setText("" + data.listFiles().length + "개");
        } else {
            this.size.setText("");
        }

    }
}
