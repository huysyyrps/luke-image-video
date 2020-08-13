package com.example.luke_imagevideo_send.main.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.camera.CameraUtils;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.StatusBarUtils;
import com.example.luke_imagevideo_send.magnetic.activity.MainActivity;
import com.example.luke_imagevideo_send.magnetic.activity.SendSelectActivity;
import com.example.luke_imagevideo_send.magnetic.service.ScreenRecordService;
import com.example.luke_imagevideo_send.main.activity.WelcomeActivity;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestActivity extends AppCompatActivity {
    private static final int RECORDER_CODE = 0;
    private static final String TAG = "TAG";

    int width;
    int height;
    int dpi;

    MediaProjectionManager projectionManager;
    MediaProjection mediaProjection;
    MediaCodec mediaCodec;
    MediaMuxer mediaMuxer;

    Surface surface;
    VirtualDisplay virtualDisplay;
    private MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    private int videoTrackIndex = -1;

    String filePath;
    private AtomicBoolean mQuit = new AtomicBoolean(false);
    private boolean muxerStarted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        //context的方法，获取windowManager
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        width = 1080;
        height = 1920;
        dpi = 1;

//        String sdcardPath = System.getenv("EXTERNAL_STORAGE");      //获得sd卡路径
//        String dir = sdcardPath + "/LUKEVideo/";                    //图片保存的文件夹名
//        File file = new File(dir, System.currentTimeMillis() + ".mp4");                             //已File来构建
//        if (!file.exists()) {                                     //如果不存在  就mkdirs()创建此文件夹
//            file.mkdirs();
//        }
        File file = new File(Environment.getExternalStorageDirectory(),
                "record-" + width + "x" + height + "-" + System.currentTimeMillis() + ".mp4");
        filePath = file.getAbsolutePath();

        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
//        if (mediaProjection==null){
//
//        }
        StartRecorder();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                StopRecorder();
            }
        }, 3500); //延迟3.5秒跳转
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==Constant.TAG_ONE){
//            if (resultCode==RESULT_OK){
        mediaProjection = projectionManager.getMediaProjection(resultCode, data);
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        prepareEncoder();
                        mediaMuxer = new MediaMuxer(filePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    virtualDisplay = mediaProjection.createVirtualDisplay(TAG + "-display",
                            width, height, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                            surface, null, null);
                    recordVirtualDisplay();

                } finally {
                    release();
                }
            }
        }.start();

        Toast.makeText(this, "Recorder is running...", Toast.LENGTH_SHORT).show();
//                moveTaskToBack(true);
//            }
//        }
    }

    private void recordVirtualDisplay() {
        while (!mQuit.get()) {
            int index = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
            if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                resetOutputFormat();
            } else if (index >= 0) {
                encodeToVideoTrack(index);
                mediaCodec.releaseOutputBuffer(index, false);
            }
        }
    }

    private void encodeToVideoTrack(int index) {
        ByteBuffer encodedData = mediaCodec.getOutputBuffer(index);

        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            bufferInfo.size = 0;
        }
        if (bufferInfo.size == 0) {
            encodedData = null;
        }
        if (encodedData != null) {
            encodedData.position(bufferInfo.offset);
            encodedData.limit(bufferInfo.offset + bufferInfo.size);
            mediaMuxer.writeSampleData(videoTrackIndex, encodedData, bufferInfo);
        }
    }

    private void resetOutputFormat() {
        MediaFormat newFormat = mediaCodec.getOutputFormat();
        videoTrackIndex = mediaMuxer.addTrack(newFormat);
        mediaMuxer.start();
        muxerStarted = true;
    }

    private void prepareEncoder() throws IOException {
        MediaFormat format = MediaFormat.createVideoFormat("video/avc", width, height);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);

        mediaCodec = MediaCodec.createEncoderByType("video/avc");
        mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        surface = mediaCodec.createInputSurface();
        mediaCodec.start();
    }

    public void StartRecorder() {
        Intent intent = new Intent();
        intent.setClassName("com.android.systemui", "com.android.systemui.media.MediaProjectionPermissionActivity");
        startActivityForResult(intent, RECORDER_CODE);
    }

    public void StopRecorder() {
        mQuit.set(true);
        Toast.makeText(this, "Recorder stop", Toast.LENGTH_SHORT).show();
    }

    private void release() {
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
        if (virtualDisplay != null) {
            virtualDisplay.release();
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
        if (mediaMuxer != null) {
            mediaMuxer.release();
            mediaMuxer = null;
        }
    }
}
