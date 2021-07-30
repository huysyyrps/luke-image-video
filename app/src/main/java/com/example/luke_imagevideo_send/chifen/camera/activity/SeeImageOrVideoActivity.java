package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.util.FileUtils;
import com.example.luke_imagevideo_send.chifen.camera.view.CustomerVideoView;
import com.example.luke_imagevideo_send.chifen.camera.view.DrawView;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeeImageOrVideoActivity extends BaseActivity implements View.OnClickListener, DrawView.CloseActivity {
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
    @BindView(R.id.ivStart)
    ImageView ivStart;
    @BindView(R.id.ivPause)
    ImageView ivPause;
    SharePreferencesUtils sharePreferencesUtils;
    private String project = "", workName = "", workCode = "",compName = "",device = "";
    private static boolean isExit = false;
    AlertDialogUtil alertDialogUtil;
    String tag = "open";


    //推出程序
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //推出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            if (drawView.undoNum().equals("open")){
                alertDialogUtil.showDialog("是否保存图片", new AlertDialogCallBack() {

                    @Override
                    public void confirm(String name) {
                        getImage(drawView.getRootView());
                    }

                    @Override
                    public void cancel() {
                        isExit = false;
                        finish();
                    }

                    @Override
                    public void save(String name) {

                    }

                    @Override
                    public void checkName(String name) {

                    }
                });
                mHandler.sendEmptyMessageDelayed(0, 4000);
            }else {
                finish();
            }

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        ButterKnife.bind(this);
        alertDialogUtil = new AlertDialogUtil(this);
        sharePreferencesUtils = new SharePreferencesUtils();
        project = sharePreferencesUtils.getString(SeeImageOrVideoActivity.this,"project","");
        workName = sharePreferencesUtils.getString(SeeImageOrVideoActivity.this,"workName","");
        workCode = sharePreferencesUtils.getString(SeeImageOrVideoActivity.this,"workCode","");
        drawView.setCallback(this);
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
            bitmap = BitmapFactory.decodeFile(path, options);
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

    @OnClick({R.id.color_panel, R.id.undo, R.id.save, R.id.ivStart, R.id.ivPause,R.id.videoView})
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
                getImage(view);
                break;
            case R.id.ivStart:
                videoView.pause();
                ivStart.setVisibility(View.GONE);
                ivPause.setVisibility(View.VISIBLE);
                break;
            case R.id.ivPause:
                videoView.start();
                ivStart.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                break;
            case R.id.videoView:
                if (ivPause.getVisibility()==View.VISIBLE){
                    ivPause.setVisibility(View.GONE);
                    ivStart.setVisibility(View.VISIBLE);
                }else if (ivStart.getVisibility()==View.VISIBLE){
                    ivStart.setVisibility(View.GONE);
                    ivPause.setVisibility(View.VISIBLE);
                }else {
                    ivStart.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void getImage(View view) {
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
    }

    public void saveImage() {
        String dir = Environment.getExternalStorageDirectory() + "/LUKEImage/"+project+"/"+"设备/"+workName+"/"+workCode+"/";
        String filename = dir + fileName;
        File file = new File(filename + ".png");
        file.delete();
        if (FileUtils.saveBitmap(filename, mBitmap, Bitmap.CompressFormat.PNG, 100)) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            paintBar.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void closeThisActivity() {
        finish();
    }
}
