package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.VideoCaptureScreen;
import com.example.luke_imagevideo_send.cehouyi.util.BottomUI;
import com.example.luke_imagevideo_send.cehouyi.util.GetTime;
import com.example.luke_imagevideo_send.cehouyi.util.GetTimeCallBack;
import com.example.luke_imagevideo_send.chifen.magnetic.util.KeyCenter;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.chifen.magnetic.view.TBSWebView;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.views.Header;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoPublisherUpdateCdnUrlCallback;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.entity.ZegoCustomVideoCaptureConfig;
import im.zego.zegoexpress.entity.ZegoEngineProfile;
import im.zego.zegoexpress.entity.ZegoUser;
import im.zego.zegoexpress.entity.ZegoVideoConfig;

public class MainBroadcastActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvGPS)
    TextView tvGPS;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.tvCompName)
    TextView tvCompName;
    @BindView(R.id.tvWorkName)
    TextView tvWorkName;
    @BindView(R.id.tvWorkCode)
    TextView tvWorkCode;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.tbsView)
    TBSWebView tbsView;
    public static MediaProjectionManager mMediaProjectionManager;
    public static MediaProjection mMediaProjection;
    private String project = "", workName = "", workCode = "", address = "";
    //声明一个操作常量字符串
    public static final String ACTION_SERVICE_NEED = "action.ServiceNeed";
    //声明一个内部广播实例
//    public ServiceNeedBroadcastReceiver broadcastReceiver;
//    ScreenLive screenLive;
    String userID;
    String publishStreamID;
    String playStreamID;
    String roomID;
    ZegoExpressEngine engine;
    ZegoUser user;
    private static final int DEFAULT_VIDEO_WIDTH = 1280;
    private static final int DEFAULT_VIDEO_HEIGHT = 720;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        tbsView.setVerticalScrollBarEnabled(false); //垂直不显示
//        tbsView.setHorizontalScrollBarEnabled(false);//水平不显示

        /**
         * 注册广播实例（在初始化的时候）
         */
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ACTION_SERVICE_NEED);
//        broadcastReceiver = new ServiceNeedBroadcastReceiver();
//        registerReceiver(broadcastReceiver, filter);

        //不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        project = intent.getStringExtra("project");
        workName = intent.getStringExtra("etWorkName");
        workCode = intent.getStringExtra("etWorkCode");
        if (project.trim().equals("") && workName.trim().equals("") && workCode.trim().equals("")) {
            linearLayout.setVisibility(View.GONE);
        }
        if (!project.trim().equals("")) {
            tvCompName.setText(project);
        }
        if (!workName.trim().equals("")) {
            tvWorkName.setText(workName);
        }
        if (!workCode.trim().equals("")) {
            tvWorkCode.setText(workCode);
        }
        header.setVisibility(View.GONE);
        frameLayout.setBackgroundColor(getResources().getColor(R.color.black));

        try {
            address = new getIp().getConnectIp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        address = "http://" + address + ":8080";
        Log.e("XXXXX", address);
        tbsView.setBackgroundColor(Color.BLACK);
        tbsView.loadUrl(address);
//        ScreenRecoder();
        setDefaultValue();
    }

    public void setDefaultValue() {
        userID = "Android_" + Build.MODEL.replaceAll(" ", "_");
        roomID = "0033";
        publishStreamID = "0033";
        ZegoEngineProfile profile = new ZegoEngineProfile();
        profile.appID = KeyCenter.appID();
        profile.appSign = KeyCenter.appSign();
        profile.scenario = ZegoScenario.GENERAL;
        profile.application = getApplication();
        engine = ZegoExpressEngine.createEngine(profile, null);
        user = new ZegoUser(userID);
        engine.loginRoom(roomID, user);
        engine.enableCamera(true);
        engine.muteMicrophone(false);
        engine.muteSpeaker(false);

        ZegoVideoConfig videoConfig = new ZegoVideoConfig();
        videoConfig.captureHeight = 720;
        videoConfig.captureWidth = 1280;
        videoConfig.encodeHeight = 720;
        videoConfig.encodeWidth = 1280;
        // 设置视频配置
        engine.setVideoConfig(videoConfig);
        //停止或恢复发送音频流。
        engine.mutePublishStreamAudio(true);
        prepareScreenCapture();
    }

    public void prepareScreenCapture() {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(this, "Require root permission", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // 5.0及以上版本
            // 请求录屏权限，等待用户授权
            mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), Constant.TAG_ONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.TAG_ONE && resultCode == RESULT_OK) {
            //Target版本低于10.0直接获取MediaProjection
            mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            setCustomCapture();
//            ZegoCDNConfig config = new ZegoCDNConfig();
//            // set CDN URL
//            config.url = "rtmp://221.2.36.238:2012/live/live1";
//            engine.enablePublishDirectToCDN(true, config);
            engine.startPublishingStream(publishStreamID);
            engine.addPublishCdnUrl(publishStreamID, "rtmp://221.2.36.238:2012/live/live1", new IZegoPublisherUpdateCdnUrlCallback() {
                @Override
                public void onPublisherUpdateCdnUrlResult(int errorCode) {
                    if (errorCode == 0){
                        // Add CDN URL successfully
                        Toast.makeText(MainBroadcastActivity.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
                    } else {
                        // Fail to add CDN URL.
                        Toast.makeText(MainBroadcastActivity.this, getString(R.string.faile_code), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    public void setCustomCapture() {
        // VideoCaptureScreen inherits IZegoCustomVideoCaptureHandler, which is used to monitor custom capture onStart and onStop callbacks
        // VideoCaptureScreen继承IZegoCustomVideoCaptureHandler，用于监听自定义采集onStart和onStop回调
        VideoCaptureScreen videoCapture = new VideoCaptureScreen(mMediaProjection, DEFAULT_VIDEO_WIDTH, DEFAULT_VIDEO_HEIGHT, engine);
        engine.setCustomVideoCaptureHandler(videoCapture);
        ZegoCustomVideoCaptureConfig videoCaptureConfig = new ZegoCustomVideoCaptureConfig();
        videoCaptureConfig.bufferType = ZegoVideoBufferType.SURFACE_TEXTURE;
        // Start Custom Capture
        engine.enableCustomVideoCapture(true, videoCaptureConfig, ZegoPublishChannel.MAIN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取当前时间
        new GetTime().initTime(new GetTimeCallBack() {
            @Override
            public void backTime(String time) {
                tvTime.setText(time);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tbsView.goBack();
        engine.logoutRoom(roomID);
        engine.stopPublishingStream();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tbsView.loadUrl(address);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new BottomUI().hideBottomUIMenu(this.getWindow());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_mainbroatcast;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
    }


//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void ScreenRecoder() {
//        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
//        startActivityForResult(projectionManager.createScreenCaptureIntent(), 1);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (screenLive != null) {
////            screenLive.stopData();
//            screenLive = null;
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
//            if (mediaProjection == null) {
//                Log.e("@@", "media projection is null");
//                return;
//            } else {
//                if (screenLive == null) {
//                    screenLive = new ScreenLive();
//                }
//                screenLive.startLive(url, mediaProjection);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("@@", "---------------->>>1" + e);
//        }
//    }

//    /**
//     * 定义广播接收器，用于执行Service服务的需求（内部类）
//     */
//    private class ServiceNeedBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //这里是要在Activity活动里执行的代码
//            new AlertDialogUtil(context).showDialog("111", new AlertDialogCallBack() {
//                @Override
//                public void confirm(String name) {
//                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void cancel() {
//                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void save(String name) {
//                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void checkName(String name) {
//                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

}