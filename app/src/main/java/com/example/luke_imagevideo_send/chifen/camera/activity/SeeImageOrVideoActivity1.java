package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeeImageOrVideoActivity1 extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.img_screenshot)
    DrawingView imgScreenshot;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        String path = getIntent().getStringExtra("path");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(path, options);
        initViews();
        initPaintMode();
        loadImage();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_see_image_or_video1;
    }

    @Override
    protected boolean isHasHeader() {
        return false;
    }

    @Override
    protected void rightClient() {

    }

    private void initViews() {
        imgScreenshot = (DrawingView) findViewById(R.id.img_screenshot);
//        mUndo = (ImageButton) findViewById(R.id.undo);
//        mSave = (ImageButton) findViewById(R.id.save);
//        mUndo.setOnClickListener(this);
//        mSave.setOnClickListener(this);
        initPaintMode();
    }

    private void initPaintMode() {
        imgScreenshot.initializePen();
        imgScreenshot.setPenSize(4);
        imgScreenshot.setPenColor(getResources().getColor(R.color.red));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.undo:
//                mDrawingView.undo();
//                break;
//            case R.id.save:
//                saveImage();
//                break;
            default:
                break;
        }
    }

    public void loadImage() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        imgScreenshot.loadImage(bitmap,width,height);
    }

    public void saveImage() {
        String sdPicturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String filename = sdPicturesPath + "/DrawImage_" + timeStamp;
//        if (FileUtils.saveBitmap(filename, mDrawingView.getImageBitmap(), Bitmap.CompressFormat.PNG, 100)) {
//            Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
//        }
    }
}
