package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeeImageOrVideoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.img_screenshot)
    DrawingView imgScreenshot;
    Bitmap bitmap;
    @BindView(R.id.color_panel)
    ImageButton colorPanel;
    @BindView(R.id.undo)
    ImageButton undo;
    @BindView(R.id.save)
    ImageButton save;
    @BindView(R.id.paint_bar)
    LinearLayout paintBar;
    private static int COLOR_PANEL = 0;
    String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        String path = getIntent().getStringExtra("path");
        BitmapFactory.Options options = new BitmapFactory.Options();

        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            fileName =  path.substring(start+1,end);
        }

        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(path, options);
        initViews();
        initPaintMode();
        loadImage();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_see_image_or_video;
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

    public void loadImage() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        imgScreenshot.loadImage(bitmap, width, height);
    }

    @OnClick({R.id.color_panel, R.id.undo, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.color_panel:
                colorPanel.setImageResource(COLOR_PANEL == 0 ? R.drawable.ic_color_blue : R.drawable.ic_color_red);
                imgScreenshot.setPenColor(COLOR_PANEL == 0 ? getColor(R.color.blue) : getColor(R.color.red));
                COLOR_PANEL = 1 - COLOR_PANEL;
                break;
            case R.id.undo:
                imgScreenshot.undo();
                break;
            case R.id.save:
                saveImage();
                break;
        }
    }

    public void saveImage() {
        String dir = Environment.getExternalStorageDirectory() + "/LUKEImage/";//图片保存的文件夹名
        String filename = dir + fileName;
        if (FileUtils.saveBitmap(filename, imgScreenshot.getImageBitmap(), Bitmap.CompressFormat.PNG, 100)) {
            Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
        }
    }

}
