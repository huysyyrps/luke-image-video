package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.GetTime;
import com.example.luke_imagevideo_send.cehouyi.util.GetTimeCallBack;
import com.example.luke_imagevideo_send.chifen.camera.activity.SettingActivity;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.ModbusInstanceCallBack;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.dialog.ProgressHUD;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.modbus.ModBusUtil;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpideMainActivity extends BaseActivity implements View.OnLongClickListener, View.OnTouchListener {

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
    @BindView(R.id.btnTop)
    Button btnTop;
    @BindView(R.id.btnLeft)
    Button btnLeft;
    @BindView(R.id.btnLight)
    Button btnLight;
    @BindView(R.id.btnRight)
    Button btnRight;
    @BindView(R.id.btnBotton)
    Button btnBotton;
    @BindView(R.id.tcCH)
    TextView tcCH;
    @BindView(R.id.tvSDJia)
    TextView tvSDJia;
    @BindView(R.id.tvSDJian)
    TextView tvSDJian;
    @BindView(R.id.tvDG)
    TextView tvDG;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.llItem)
    LinearLayout llItem;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.tvSetting)
    TextView tvSetting;
    @BindView(R.id.frameLayoutitem)
    FrameLayout frameLayoutitem;
    private Intent intent;
    private String project = "", workName = "", workCode = "";
    private int clickNum = 0;
    String tag = "", log = "";
    boolean loadError = false;
    String address = null;
    private KProgressHUD progressHUD;
    private WindowManager mWindowManager;
    private Handler handler = new Handler();

    static String TAG = "SpideMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        btnTop.setOnLongClickListener(this);
        btnBotton.setOnLongClickListener(this);
        btnLeft.setOnLongClickListener(this);
        btnRight.setOnLongClickListener(this);
        btnTop.setOnTouchListener(this);
        btnBotton.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        Intent intent = getIntent();
        project = intent.getStringExtra("project");
        workName = intent.getStringExtra("etWorkName");
        workCode = intent.getStringExtra("etWorkCode");
//        if (project.trim().equals("") && workName.trim().equals("") && workCode.trim().equals("")) {
//            linearLayout1.setVisibility(View.GONE);
//        }
//        if (!project.trim().equals("")) {
//            tvCompName.setText(project);
//        }
//        if (!workName.trim().equals("")) {
//            tvWorkName.setText(workName);
//        }
//        if (!workCode.trim().equals("")) {
//            tvWorkCode.setText(workCode);
//        }
        project = "1";
        workName = "1";
        workCode = "1";
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
                loadError = true;
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
        instanceModBus();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_spidemain;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
    }

    /**
     * modbus建立连接
     */
    private void instanceModBus() {
        new ModBusUtil().modbusInstance(new ModbusInstanceCallBack() {
            @Override
            public void confirm(String string) {
                Log.e(TAG, string);
                frameLayoutitem.setVisibility(View.VISIBLE);
            }

            @Override
            public void error(String string) {
                Log.e(TAG, string);
                frameLayoutitem.setVisibility(View.GONE);
            }
        });
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
                            (SpideMainActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SpideMainActivity.this, s, Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        webView.loadUrl(address);
    }

    @OnClick({R.id.tvSetting, R.id.btnTop, R.id.btnLeft, R.id.btnLight, R.id.btnRight, R.id.btnBotton, R.id.tcCH,
            R.id.tvSDJia, R.id.tvSDJian, R.id.tvDG, R.id.ivBack, R.id.tvRefresh})
    public void onClick(View view1) {
        switch (view1.getId()) {
            case R.id.tvSetting:
                intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, Constant.TAG_TWO);
                break;
            case R.id.btnTop:
                onBtnClick("向上单击", "向上双击");
                break;
            case R.id.btnLeft:
                onBtnClick("向左单击", "向左双击");
                break;
            case R.id.btnRight:
                onBtnClick("向右单击", "向右双击");
                break;
            case R.id.btnBotton:
                onBtnClick("向下单击", "向下双击");
                break;
            case R.id.btnLight:
                if (llItem.getVisibility() == View.VISIBLE) {
                    llItem.setVisibility(View.GONE);
                } else {
                    llItem.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tcCH:
                Toast.makeText(this, "磁化", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvSDJia:
                Toast.makeText(this, "速度+", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvSDJian:
                Toast.makeText(this, "速度—", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvDG:
                if (tvDG.getText().equals("白光")) {
                    tvDG.setText("黑光");
                } else {
                    tvDG.setText("白光");
                }
                break;
            case R.id.ivBack:
                llItem.setVisibility(View.GONE);
                break;
            case R.id.tvRefresh:
                ShowDialog("/etc/init.d/mjpg-streamer restart");
                break;
        }
    }


    /**
     * 重启服务刷新视频
     *
     * @param data1
     */
    private void ShowDialog(String data1) {
        try {
            address = new getIp().getConnectIp();
            progressHUD = ProgressHUD.show(SpideMainActivity.this);
            progressHUD.setLabel("重启中");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SSHExcuteCommandHelper.writeBefor(address, data1, new SSHCallBack() {
                        @Override
                        public void confirm(String data) {
                            handlerSetting.sendEmptyMessage(Constant.TAG_ONE);
                        }

                        @Override
                        public void error(String s) {
                            handlerSetting.sendEmptyMessage(Constant.TAG_TWO);
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按钮单击按监听
     *
     * @param
     * @return
     */
    public void onBtnClick(String value1, String value2) {
        clickNum++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (clickNum == 1) {
                    Toast.makeText(SpideMainActivity.this, value1, Toast.LENGTH_SHORT).show();
                } else if (clickNum == 2) {
                    Toast.makeText(SpideMainActivity.this, value2, Toast.LENGTH_SHORT).show();
                }
                //防止handler引起的内存泄漏
                handler.removeCallbacksAndMessages(null);
                clickNum = 0;
            }
        }, 800);
    }

    /**
     * 按钮长按监听
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btnTop:
                Toast.makeText(SpideMainActivity.this, "向上长按", Toast.LENGTH_SHORT).show();
                tag = "longUp";
                log = "向上长按松开";
                break;
            case R.id.btnBotton:
                Toast.makeText(SpideMainActivity.this, "向下长按", Toast.LENGTH_SHORT).show();
                tag = "longUp";
                log = "向下长按松开";
                break;
            case R.id.btnLeft:
                Toast.makeText(SpideMainActivity.this, "向左长按", Toast.LENGTH_SHORT).show();
                tag = "longUp";
                log = "向左长按松开";
                break;
            case R.id.btnRight:
                Toast.makeText(SpideMainActivity.this, "向右长按", Toast.LENGTH_SHORT).show();
                tag = "longUp";
                log = "向右长按松开";
                break;
        }
        return true;
    }

    /**
     * 按钮松开监听
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (v.getId()) {
            case R.id.btnTop:
            case R.id.btnBotton:
            case R.id.btnLeft:
            case R.id.btnRight:
                if (action == MotionEvent.ACTION_UP && tag.equals("longUp")) {
                    Toast.makeText(SpideMainActivity.this, log, Toast.LENGTH_SHORT).show();
                    tag = "";
                    log = "";
                }
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent backdata) {
        super.onActivityResult(requestCode, resultCode, backdata);
        switch (requestCode) {
            case Constant.TAG_TWO:
                if (resultCode == Constant.TAG_ONE) {
                    Setting setting = (Setting) backdata.getSerializableExtra("data");
                    Gson gson = new Gson();
                    String obj2 = gson.toJson(setting);
                }
                break;
        }
    }

    Handler handlerSetting = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TAG_ONE:
                    Toast.makeText(SpideMainActivity.this, "重启成功", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                    address = "http://" + address + ":8080";
                    webView.loadUrl(address);
                    break;
                case Constant.TAG_TWO:
                    Toast.makeText(SpideMainActivity.this, "重启失败", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
            }
        }
    };
}