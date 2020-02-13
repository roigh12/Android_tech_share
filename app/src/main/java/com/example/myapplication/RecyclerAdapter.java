package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class RecyclerAdapter extends RecyclerView.Adapter<AbsViewHolder> {
    public File[] mDataset;
    private int layoutType = ItemType.LAYOUT_LINEAR;

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }



    @NonNull
    @Override
    public AbsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemType.LAYOUT_LINEAR) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recycler, parent, false);
            return new LinearViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recycler_grid, parent, false);
            return new GridViewHolder(v);
        }
    }

    public static final int POSITION_TAG = 0xFFFFFFF1;

    public interface OnItemClickListener {
        public void onItemClick(View v);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AbsViewHolder holder, int position) {
        holder.setData(mDataset[position]);

        holder.view.setTag(POSITION_TAG, position);
        ((MainActivity) MainActivity.mcontext).registerForContextMenu(holder.view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    @Override
    public int getItemViewType(int position) {
        if(layoutType == ItemType.LAYOUT_LINEAR){
            return ItemType.LAYOUT_LINEAR;
        }
        else {
            return ItemType.LAYOUT_GRID;
        }
    }

    public void setDataList(File[] files) {
        mDataset = files;
        Arrays.sort(mDataset, new FileSort());
        notifyDataSetChanged();
    }

    // 폴더 안의 파일 개수 내림차순 정
    class FileSort implements Comparator<File> {
        public int compare(File f1, File f2) {
            int f1_len = f1.listFiles() != null ? f1.listFiles().length:0;
            int f2_len = f2.listFiles() != null ? f2.listFiles().length:0;
            return Integer.compare(f2_len, f1_len);
        }
    }
}
