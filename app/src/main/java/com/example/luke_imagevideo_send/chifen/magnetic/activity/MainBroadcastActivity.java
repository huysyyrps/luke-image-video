package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.GetTime;
import com.example.luke_imagevideo_send.cehouyi.util.GetTimeCallBack;
import com.example.luke_imagevideo_send.chifen.magnetic.rtmptump.ScreenLive;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.chifen.magnetic.view.TBSWebView;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.views.Header;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    MediaProjectionManager projectionManager;
    MediaProjection mediaProjection;
    String url = "rtmp://221.2.36.238:2012/live/live1";
    private String project = "", workName = "", workCode = "", address = "";
    //声明一个操作常量字符串
    public static final String ACTION_SERVICE_NEED = "action.ServiceNeed";
    //声明一个内部广播实例
//    public ServiceNeedBroadcastReceiver broadcastReceiver;
    ScreenLive screenLive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        /**
         * 注册广播实例（在初始化的时候）
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_NEED);
//        broadcastReceiver = new ServiceNeedBroadcastReceiver();
//        registerReceiver(broadcastReceiver, filter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        project = intent.getStringExtra("project");
        workName = intent.getStringExtra("etWorkName");
        workCode = intent.getStringExtra("etWorkCode");
        if (project.trim().equals("") && workName.trim().equals("") && workCode.trim().equals("")) {
            linearLayout.setVisibility(View.GONE);
        }
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
        tbsView.loadUrl("http://192.168.43.104:8080");
        ScreenRecoder();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ScreenRecoder() {
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(projectionManager.createScreenCaptureIntent(), 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (screenLive != null) {
//            screenLive.stopData();
            screenLive = null;
        }
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
                if (screenLive == null) {
                    screenLive = new ScreenLive();
                }
                screenLive.startLive(url, mediaProjection);
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
    public void onDestroyView() {
        super.onDestroyView();
        tbsView.goBack();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tbsView.loadUrl(address);
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