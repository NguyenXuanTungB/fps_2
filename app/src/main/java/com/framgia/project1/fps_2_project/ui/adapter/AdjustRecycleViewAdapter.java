package com.framgia.project1.fps_2_project.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framgia.project1.fps_2_project.R;
import com.framgia.project1.fps_2_project.data.model.AdjustItem;
import com.framgia.project1.fps_2_project.ui.mylistener.MyOnClickListener;

import java.util.ArrayList;

/**
 * Created by nguyenxuantung on 11/05/2016.
 */
public class AdjustRecycleViewAdapter extends RecyclerView.Adapter<AdjustRecycleViewAdapter
    .AdjustViewHolder> {
    MyOnClickListener mMyOnClickListener;
    private ArrayList<AdjustItem> mListAdjust;
    private Context mContext;

    public AdjustRecycleViewAdapter(ArrayList<AdjustItem> mListAdjust, Context mContext) {
        this.mContext = mContext;
        this.mListAdjust = mListAdjust;
    }

    public void setItemClickListener(MyOnClickListener mMyOnClickListener) {
        this.mMyOnClickListener = mMyOnClickListener;
    }

    @Override
    public AdjustRecycleViewAdapter.AdjustViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adjust_row,
            parent, false);
        return new AdjustViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdjustRecycleViewAdapter.AdjustViewHolder holder, int position) {
        AdjustItem adjustItem = mListAdjust.get(position);
        Bitmap bitmap = adjustItem.getmBitmap();
        holder.mImageView.setImageBitmap(bitmap);
        holder.mPosition = position;
    }

    @Override
    public int getItemCount() {
        return mListAdjust.size();
    }

    class AdjustViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private int mPosition;

        public AdjustViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (ImageView) itemView.findViewById(R.id.imageViewAdjust);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mMyOnClickListener != null)
                mMyOnClickListener.onItemClick(v, mPosition);
        }
    }
}