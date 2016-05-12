package com.framgia.project1.fps_2_project.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.framgia.project1.fps_2_project.R;
import com.framgia.project1.fps_2_project.data.model.AdjustItem;
import com.framgia.project1.fps_2_project.data.model.Constant;
import com.framgia.project1.fps_2_project.ui.adapter.AdjustRecycleViewAdapter;
import com.framgia.project1.fps_2_project.ui.mylistener.MyOnClickListener;
import com.framgia.project1.fps_2_project.util.ImageUtils;
import com.framgia.project1.fps_2_project.util.ListEffect;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class EditImageActivity extends AppCompatActivity implements View.OnClickListener,
    MyOnClickListener, Constant {
    public static Bitmap sBitmap;
    private Bitmap origin;
    private Button mBtnSave, mBtnCancel, mBtnSaveSeekBar;
    private Dialog mDialog;
    private String mImageName = "";
    private ImageView mImage;
    private SeekBar mSeekBarBrightness, mSeekBarContrast, mSeekBarHue;
    private ArrayList<AdjustItem> mListAdjust;
    private LinearLayout mLayoutSeek;
    private Animation mSlideUp, mSlideDown;
    private boolean mIsRunning, mCheckSeekBarVisiable = false;
    private RecyclerView mRecyclerView;
    private int mCheckSeekBar;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private float mConstrast = 0, mBrightness = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        try {
            origin = sBitmap;
        } catch (OutOfMemoryError e) {
            sBitmap = ImageUtils.decodeSampleBitmapFromResource(getResources(), R.drawable
                .usb_android, 150, 150);
            origin = ImageUtils.decodeSampleBitmapFromResource(getResources(), R.drawable
                .usb_android, 150, 150);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mBtnSaveSeekBar = (Button) findViewById(R.id.saveSeekBar);
        mBtnSaveSeekBar.setOnClickListener(this);
        mImage = (ImageView) findViewById(R.id.image_edit);
        mImage.setImageBitmap(sBitmap);
        initSeekBar();
        setSeekBar();
        mListAdjust = new ArrayList<>();
        mLayoutSeek = (LinearLayout) findViewById(R.id.layout_seek);
        initView();
        mSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        mSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
    }

    private void initSeekBar() {
        mSeekBarBrightness = (SeekBar) findViewById(R.id.seekBar_brightness);
        mSeekBarBrightness.setMax(510);
        mSeekBarBrightness.setProgress(255);
        mSeekBarContrast = (SeekBar) findViewById(R.id.seekBar_constrast);
        mSeekBarContrast.setMax(400);
        mSeekBarContrast.setProgress(200);
        mSeekBarHue = (SeekBar) findViewById(R.id.seekBar_hue);
        mSeekBarHue.setMax(100);
        mSeekBarHue.setProgress(50);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_adjust);
        AdjustItem item0 = new AdjustItem(origin);
        mListAdjust.add(item0);
        AdjustItem item1 = new AdjustItem(ListEffect.createInvertedBitmap(origin));
        mListAdjust.add(item1);
        AdjustItem item2 = new AdjustItem(ListEffect.highlightImage(origin));
        mListAdjust.add(item2);
        AdjustItem item3 = new AdjustItem(ListEffect.setPopArtGradientFromBitmap(origin));
        mListAdjust.add(item3);
        AdjustItem item4 = new AdjustItem(ListEffect.grayImage(origin));
        mListAdjust.add(item4);
        AdjustItem item5 = new AdjustItem(getBitmapFromResouce(R.drawable.rotate_left));
        mListAdjust.add(item5);
        AdjustItem item6 = new AdjustItem(getBitmapFromResouce(R.drawable.rotate_right));
        mListAdjust.add(item6);
        AdjustItem item7 = new AdjustItem(getBitmapFromResouce(R.drawable.adjust));
        mListAdjust.add(item7);
        AdjustItem item8 = new AdjustItem(getBitmapFromResouce(R.drawable.crop));
        mListAdjust.add(item8);
        AdjustRecycleViewAdapter adapter = new AdjustRecycleViewAdapter(mListAdjust, this);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
            .HORIZONTAL, false));
    }

    private void setSeekBar() {
        mSeekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mIsRunning == false) {
                    mCheckSeekBar = BRIGHTNESS;
                    EffectAsync ea = new EffectAsync();
                    ea.execute(progress);
                    mBrightness = (float) progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mSeekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mIsRunning == false) {
                    mCheckSeekBar = CONTRAST;
                    EffectAsync ea = new EffectAsync();
                    ea.execute(progress / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mSeekBarHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mIsRunning == false) {
                    mCheckSeekBar = HUE;
                    EffectAsync ea = new EffectAsync();
                    ea.execute(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private Bitmap getBitmapFromResouce(int src) {
        return ImageUtils.decodeSampleBitmapFromResource(getResources(), src, 150, 150);
    }

    private void setDialog() {
        mDialog = new Dialog(EditImageActivity.this);
        mDialog.setContentView(R.layout.custom_dialog);
        mDialog.setTitle(R.string.save_image_title);
        final EditText edt = (EditText) mDialog.findViewById(R.id.edtSave);
        mImageName = edt.getText().toString();
        mBtnSave = (Button) mDialog.findViewById(R.id.save);
        mBtnSave.setOnClickListener(this);
        mBtnCancel = (Button) mDialog.findViewById(R.id.cancel);
        mBtnCancel.setOnClickListener(this);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mImageName = edt.getText().toString();
                if (mImageName.length() > 0)
                    mBtnSave.setEnabled(true);
                else
                    mBtnSave.setEnabled(false);
            }
        });
        mDialog.show();
    }

    private void saveBitmapToFile(Bitmap bitmap, String name) {
        OutputStream out = null;
        try {
            File file =
                new File(Environment.getExternalStorageDirectory() + "/" + name + getString(R
                    .string.png));
            out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            this.sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                mDialog.dismiss();
                saveBitmapToFile(sBitmap, mImageName);
                Toast.makeText(EditImageActivity.this, R.string.notice_save, Toast
                    .LENGTH_LONG).show();
                break;
            case R.id.cancel:
                mDialog.dismiss();
                break;
            case R.id.saveSeekBar:
                mCheckSeekBarVisiable = false;
                slideSeekBar();
                mImage.setImageBitmap(sBitmap);
            default:
                break;
        }
    }

    private void slideSeekBar() {
        initSeekBar();
        if (mCheckSeekBarVisiable == true) {
            mLayoutSeek.startAnimation(mSlideUp);
            mLayoutSeek.setVisibility(View.VISIBLE);
            mRecyclerView.startAnimation(mSlideDown);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mLayoutSeek.startAnimation(mSlideDown);
            mLayoutSeek.setVisibility(View.GONE);
            mRecyclerView.startAnimation(mSlideUp);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                sBitmap = mListAdjust.get(0).getmBitmap();
                mImage.setImageBitmap(sBitmap);
                initSeekBar();
                break;
            case 1:
                sBitmap = mListAdjust.get(1).getmBitmap();
                mImage.setImageBitmap(sBitmap);
                initSeekBar();
                break;
            case 2:
                sBitmap = mListAdjust.get(2).getmBitmap();
                mImage.setImageBitmap(sBitmap);
                initSeekBar();
                break;
            case 3:
                sBitmap = mListAdjust.get(3).getmBitmap();
                mImage.setImageBitmap(sBitmap);
                initSeekBar();
                break;
            case 4:
                sBitmap = mListAdjust.get(4).getmBitmap();
                mImage.setImageBitmap(sBitmap);
                initSeekBar();
                break;
            case 5:
                sBitmap = ListEffect.rotate(sBitmap, -90);
                mImage.setImageBitmap(sBitmap);
                initSeekBar();
                break;
            case 6:
                sBitmap = ListEffect.rotate(sBitmap, 90);
                mImage.setImageBitmap(sBitmap);
                initSeekBar();
                break;
            case 7:
                mCheckSeekBarVisiable = true;
                slideSeekBar();
                break;
            case 8:
                Intent intent = new Intent(EditImageActivity.this, CropImageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_image:
                setDialog();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImage.setImageBitmap(sBitmap);
    }

    private class EffectAsync extends AsyncTask<Integer, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Integer... params) {
            mIsRunning = true;
            Bitmap test = null;
            if (mCheckSeekBar == CONTRAST) {
                test = ListEffect.changeBitmapContrast(sBitmap, (float) (params[0]) - 2);
            } else if (mCheckSeekBar == BRIGHTNESS) {
                test = ListEffect.changeBitmapBrightness(sBitmap, (float) (params[0] - 255));
            } else if (mCheckSeekBar == HUE) {
                test = ListEffect.changeHue(sBitmap, (float) params[0]);
            }
            sBitmap = test;
            return test;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mIsRunning = false;
            mImage.setImageBitmap(bitmap);
        }
    }
}