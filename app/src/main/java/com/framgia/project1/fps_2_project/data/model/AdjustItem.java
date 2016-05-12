package com.framgia.project1.fps_2_project.data.model;

import android.graphics.Bitmap;

/**
 * Created by nguyenxuantung on 11/05/2016.
 */
public class AdjustItem {
    private Bitmap mBitmap;

    public AdjustItem(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}