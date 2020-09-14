package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeeImageOrVideoActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    Bitmap bitmap;
    Canvas canvas;
    Paint paint, painttext;
    private int width, height;
    private static int startX, startY, moveX, moveY, stopX, stopY;
    public static int[] point1, point2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        String tag = getIntent().getStringExtra("tag");
        String path = getIntent().getStringExtra("path");
        if (tag.equals("photo")) {
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(this)
                    .load(path)
                    .placeholder(R.color.app_color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            getAndroiodScreenProperty();
            getData();
        } else if (tag.equals("video")) {
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(path);//指定要播放的视频
            //控制视频播放
            MediaController mc = new MediaController(SeeImageOrVideoActivity.this);
            videoView.setMediaController(mc);//让VideoView与MediaControl关联
            videoView.requestFocus();//让VideoView获取焦点
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(SeeImageOrVideoActivity.this, "视频播放完毕", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_see_image_or_video;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;         // 屏幕宽度（像素）
        height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        Log.d("h_bl", "屏幕宽度（像素）：" + width);
        Log.d("h_bl", "屏幕高度（像素）：" + height);
        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
    }

    public void getData() {
        paint = new Paint();
        paint.setStrokeWidth(5);//笔宽5像素
        paint.setColor(Color.RED);//设置为红笔
        paint.setTextSize(24);

        painttext = new Paint();
        painttext.setStrokeWidth(5);//笔宽5像素
        painttext.setColor(Color.BLUE);//设置为红笔
        painttext.setTextSize(24);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //设置位图的宽高,bitmap为透明
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//设置为透明，画布也是透明
        canvas.drawLine(0, 20, 750, 200, paint);
        Drawable drawable = new BitmapDrawable(bitmap);
        linearLayout.setBackgroundDrawable(drawable);
    }

    public int getlength(int x, int y, int x1, int y1) {
        double a = (y - y1) * (y - y1) + (x - x1) * (x - x1);
        int length = (int) Math.sqrt(a);
        return length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                startX = (int) event.getX();
                startY = (int) event.getY();


                break;
            case MotionEvent.ACTION_MOVE://按下移动
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                if (point2 == null) {
                    drawLineOne(startX, startY, moveX, moveY);
                } else {
                    Log.e("x:" + moveX, "  y:" + moveY);
                    if ((startX < point2[0] + 20 && startX > point2[0] - 20) && (startY < point2[1] + 20 && startY > point2[1] - 20)) {//当按下的点在point2点范围内，则进行画线
                        drawLineOne(point1[0], point1[1], moveX, moveY);

                    } else if ((startX < point1[0] + 20 && startX > point1[0] - 20) && (startY < point1[1] + 20 && startY > point1[1] - 20)) {//当按下的点在point1点范围内，则进行画线
                        drawLineOne(point2[0], point2[1], moveX, moveY);
                        Log.e("第一个点再移动:" + moveX, "  y:" + moveY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP://离开屏幕
                stopX = (int) event.getX();
                stopY = (int) event.getY();
                if (point1 == null) {
                    point1 = new int[]{startX, stopY};
                    point2 = new int[]{stopX, stopY};
                } else {
                    if ((startX < point1[0] + 20 && startX > point1[0] - 20) && (startY < point1[1] + 20 && startY > point1[1] - 20)) {
                        //当改变第一个点时：将实时动的点赋值给第一个点，要不然，不改变值
                        point1 = new int[]{moveX, moveY};
                    } else if ((startX < point2[0] + 20 && startX > point2[0] - 20) && (startY < point2[1] + 20 && startY > point2[1] - 20)) {
                        //当改变第二个点时：将实时动的点赋值给第二个点，要不然，不改变值
                        point2 = new int[]{moveX, moveY};
                    }
                }
                break;

        }
        return true;
    }

    public void drawLineOne(int x, int y, int x1, int y1) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//设置为透明，画布也是透明
        canvas.drawLine(x, y, x1, y1, paint);
        paint.setTextAlign(Paint.Align.CENTER);//居中显示
        painttext.setTextAlign(Paint.Align.CENTER);//居中显示

        int length = getlength(x, y, x1, y1);
        Path path = new Path();
        path.moveTo(x, y - 18);//直线的起始点,-12表示向y轴上移12个单位
        path.lineTo(x1, y1 - 18);//直线终点，设置标注直线的路径
        canvas.drawTextOnPath("" + length, path, 0, 0, paint);//vOffset设置垂直方向位移的距离。
        Drawable drawable = new BitmapDrawable(bitmap);
        linearLayout.setBackgroundDrawable(drawable);
    }

}