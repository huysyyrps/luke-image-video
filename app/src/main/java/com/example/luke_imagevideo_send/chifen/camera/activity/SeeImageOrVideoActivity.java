package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.util.FileUtils;
import com.example.luke_imagevideo_send.chifen.camera.view.CustomerVideoView;
import com.example.luke_imagevideo_send.chifen.camera.view.DrawView;
import com.example.luke_imagevideo_send.http.base.BaseActivity;

import java.io.File;

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
    Window window;
    Bitmap mBitmap;
    @BindView(R.id.videoView)
    CustomerVideoView videoView;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        path = getIntent().getStringExtra("path");
        String tag = getIntent().getStringExtra("tag");
        if (tag.equals("photo")) {
            drawView.setVisibility(View.VISIBLE);
            paintBar.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            int start = path.lastIndexOf("/");
            int end = path.lastIndexOf(".");
            if (start != -1 && end != -1) {
                fileName = path.substring(start + 1, end);
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options); //此时返回 bm 为空
            options.inJustDecodeBounds = false; //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//            int be = (int)(options.outHeight / (float)320);
//            if (be <= 0)
//                be = 1;
//            options.inSampleSize = be; //重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
            bitmap=BitmapFactory.decodeFile(path,options);
//            DisplayMetrics dm = getResources().getDisplayMetrics();
//            Bitmap originalBitmap = BitmapFactory.decodeFile(path).copy(Bitmap.Config.ARGB_8888, true);
//            Bitmap finalBitmap = Bitmap.createScaledBitmap(originalBitmap, dm.heightPixels, dm.widthPixels, true);//heightPixels
            loadImage(bitmap);
        } else if (tag.equals("video")) {
            drawView.setVisibility(View.GONE);
            paintBar.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            setupVideo();
        }
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

    public void loadImage(Bitmap bitmap) {
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
                /**
                 * 隐藏下部手机按键
                 */
                window = getWindow();
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                window.setAttributes(params);
                paintBar.setVisibility(View.GONE);
                View view1 = view.getRootView();
                view1.setDrawingCacheEnabled(true);
                view1.buildDrawingCache();
                mBitmap = view1.getDrawingCache();
                Log.e("TAG", mBitmap.getWidth() + "__________" + mBitmap.getHeight());
                if (mBitmap != null) {
                    saveImage();
                } else {
                    Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void saveImage() {
        String dir = Environment.getExternalStorageDirectory() + "/LUKEImage/";//图片保存的文件夹名
        String filename = dir + fileName;
        if (FileUtils.saveBitmap(filename, mBitmap, Bitmap.CompressFormat.PNG, 100)) {
            Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            paintBar.setVisibility(View.VISIBLE);
        }
    }

    public static Bitmap getViewBitmap(View view) {
        Bitmap bitmap;
        if (view.getWidth() > 0 && view.getHeight() > 0)
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        else if (view.getMeasuredWidth() > 0 && view.getMeasuredHeight() > 0)
            bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        else
            bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void setupVideo() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaybackVideo();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });

        try {
            Uri uri = Uri.parse(path);
            videoView.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.canPause()) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
    }

    private void stopPlaybackVideo() {
        try {
            videoView.stopPlayback();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
