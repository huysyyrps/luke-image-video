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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.util.FileUtils;
import com.example.luke_imagevideo_send.chifen.camera.view.DrawView;
import com.example.luke_imagevideo_send.http.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeeImageOrVideoActivity extends BaseActivity implements View.OnClickListener {
    Bitmap bitmap;
    @BindView(R.id.color_panel)
    ImageButton colorPanel;
    @BindView(R.id.undo)
    ImageButton undo;
    @BindView(R.id.save)
    ImageButton save;
    @BindView(R.id.paint_bar)
    LinearLayout paintBar;
    @BindView(R.id.drawView)
    DrawView drawView;
    private static int COLOR_PANEL = 0;
    String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        String path = getIntent().getStringExtra("path");
        BitmapFactory.Options options = new BitmapFactory.Options();

        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            fileName = path.substring(start + 1, end);
        }

        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(path, options);
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

    public void loadImage() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        drawView.loadImage(bitmap, width, height);
    }

    @OnClick({R.id.color_panel, R.id.undo, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.color_panel:
                colorPanel.setImageResource(COLOR_PANEL == 0 ? R.drawable.ic_color_blue : R.drawable.ic_color_red);
                drawView.setPenColor(COLOR_PANEL == 0 ? getColor(R.color.blue) : getColor(R.color.red));
                COLOR_PANEL = 1 - COLOR_PANEL;
                break;
            case R.id.undo:
                drawView.undo();
                break;
            case R.id.save:
                saveImage();
                break;
        }
    }

    public void saveImage() {
        String dir = Environment.getExternalStorageDirectory() + "/LUKEImage/";//图片保存的文件夹名
        String filename = dir + fileName;
        if (FileUtils.saveBitmap(filename, drawView.getImageBitmap(), Bitmap.CompressFormat.PNG, 100)) {
            Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
        }
    }

}
