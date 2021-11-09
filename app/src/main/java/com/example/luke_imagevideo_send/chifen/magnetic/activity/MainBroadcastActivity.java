package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.GetTime;
import com.example.luke_imagevideo_send.cehouyi.util.GetTimeCallBack;
import com.example.luke_imagevideo_send.chifen.magnetic.rtmptump.ScreenLive;
import com.example.luke_imagevideo_send.chifen.magnetic.util.Notifications;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.views.Header;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainBroadcastActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.webView)
    WebView webView;
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
    private Notifications mNotifications;
    private int clickNum = 0;
    private boolean isStart;
    MediaProjectionManager projectionManager;
    MediaProjection mediaProjection;
    private Surface mSurface;
    //    String url = "rtmp://221.2.36.238:6062/live";
    String url = "rtmp://221.2.36.238:2012/live";
    private Handler handler = new Handler();
    private String project = "", workName = "", workCode = "", address = "";
    //声明一个操作常量字符串
    public static final String ACTION_SERVICE_NEED = "action.ServiceNeed";
    //声明一个内部广播实例
    public ServiceNeedBroadcastReceiver broadcastReceiver;
    ScreenLive screenLive;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        /**
         * 注册广播实例（在初始化的时候）
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_NEED);
        broadcastReceiver = new ServiceNeedBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);

        mNotifications = new Notifications(getApplicationContext());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        Intent intent = getIntent();
//        project = intent.getStringExtra("project");
//        workName = intent.getStringExtra("etWorkName");
//        workCode = intent.getStringExtra("etWorkCode");
//        if (project.trim().equals("") && workName.trim().equals("") && workCode.trim().equals("")) {
//            linearLayout.setVisibility(View.GONE);
//        }
        project = "1";
        workName = "1";
        workCode = "1";
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
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView.setBackgroundColor(Color.BLACK);
        webView.getSettings().setJavaScriptEnabled(true);
        try {
            address = new getIp().getConnectIp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        address = "http://" + address + ":8080";
        webView.loadUrl(address);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.setVisibility(View.GONE);
//                imageView.setVisibility(View.GONE);
                linearLayout.setVerticalGravity(View.GONE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //DialogShow.showRoundProcessDialog();
            }
        });
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //全屏设置
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        getGPS();
        ScreenRecoder();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ScreenRecoder() {
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(projectionManager.createScreenCaptureIntent(), 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection == null) {
                Log.e("@@", "media projection is null");
                return;
            } else {
                if (screenLive==null){
                    screenLive = new ScreenLive();
                    screenLive.startLive(url, mediaProjection);
                }else {
                    screenLive.startLive(url, mediaProjection);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("@@", "---------------->>>1" + e);
        }
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

    /**
     * 获取定位信息
     */
    private void getGPS() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //发送设置数据
                    SSHExcuteCommandHelper.writeBefor("192.168.1.251", "cat /dev/ttyUSB1", new SSHCallBack() {
                        @Override
                        public void confirm(String data) {
                            try {
                                String[] GpsData = data.split(getResources().getString(R.string.special_data));
                                if (GpsData.length != 0) {
                                    String lastData = GpsData[GpsData.length - 1];
                                    String[] NEData = lastData.split(",");
                                    String firstN = NEData[1];
                                    String firstE = NEData[3];
                                    BigDecimal DataN = new BigDecimal(Double.valueOf(firstN) / 100).setScale(6, BigDecimal.ROUND_HALF_UP);
                                    BigDecimal DataE = new BigDecimal(Double.valueOf(firstE) / 100).setScale(6, BigDecimal.ROUND_HALF_UP);
                                    String s = DataE + "," + DataN;
                                    tvGPS.setText(s);
                                }
                            } catch (Exception ex) {
                                Log.e("XXX", ex.toString());
                            }
                        }

                        @Override
                        public void error(String s) {
                            (MainBroadcastActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainBroadcastActivity.this, s, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        webView.loadUrl(address);
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

    /**
     * 定义广播接收器，用于执行Service服务的需求（内部类）
     */
    private class ServiceNeedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //这里是要在Activity活动里执行的代码
            new AlertDialogUtil(context).showDialog("111", new AlertDialogCallBack() {
                @Override
                public void confirm(String name) {
                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void cancel() {
                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void save(String name) {
                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void checkName(String name) {
                    Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}