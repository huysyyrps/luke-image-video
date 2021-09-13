package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodecInfo;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.BottomUI;
import com.example.luke_imagevideo_send.cehouyi.util.GetTime;
import com.example.luke_imagevideo_send.cehouyi.util.GetTimeCallBack;
import com.example.luke_imagevideo_send.chifen.camera.activity.SettingActivity;
import com.example.luke_imagevideo_send.chifen.camera.util.CustomToast;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.chifen.magnetic.util.AudioEncodeConfig;
import com.example.luke_imagevideo_send.chifen.magnetic.util.MainUI;
import com.example.luke_imagevideo_send.chifen.magnetic.util.Notifications;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.ScreenRecorder;
import com.example.luke_imagevideo_send.chifen.magnetic.util.VideoEncodeConfig;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.DialogCallBackTwo;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.dialog.ProgressHUD;
import com.example.luke_imagevideo_send.http.views.Header;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.licheedev.modbus4android.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.media.MediaFormat.MIMETYPE_AUDIO_AAC;
import static android.media.MediaFormat.MIMETYPE_VIDEO_AVC;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.rbCamera)
    RadioButton rbCamera;
    @BindView(R.id.rbVideo)
    RadioButton rbVideo;
    @BindView(R.id.rbAlbum)
    RadioButton rbAlbum;
    @BindView(R.id.rbSetting)
    RadioButton rbSetting;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
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
    @BindView(R.id.rbSound)
    RadioButton rbSound;
    @BindView(R.id.tvCompName)
    TextView tvCompName;
    @BindView(R.id.tvWorkName)
    TextView tvWorkName;
    @BindView(R.id.tvWorkCode)
    TextView tvWorkCode;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.rbRefresh)
    RadioButton rbRefresh;
    @BindView(R.id.timer)
    Chronometer timer;
    @BindView(R.id.linearLayoutStop)
    LinearLayout linearLayoutStop;
    @BindView(R.id.ivTimer)
    ImageView ivTimer;

    Bitmap mBitmap;
    String name = "";

    boolean loadError = false;
    private static AlertDialogUtil alertDialogUtil;
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private static final int REQUEST_PERMISSIONS = 2;
    private MediaProjectionManager mMediaProjectionManager;
    private Notifications mNotifications;
    private ScreenRecorder mRecorder;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private Intent intent;
    private String haveAudio = "noAudio";
    static final String VIDEO_AVC = MIMETYPE_VIDEO_AVC; // H.264 Advanced Video Coding
    static final String AUDIO_AAC = MIMETYPE_AUDIO_AAC; // H.264 Advanced Audio Coding
    private String project = "", workName = "", workCode = "";
    boolean rbVideoV = true;
    boolean rbSoundV = true;
    private int clickNum = 0;
    File file = null;
    VideoEncodeConfig video;
    AudioEncodeConfig audio;
    String tag = "", log = "";
    String address = null;
    SimpleDateFormat format;
    private ImageReader mImageReader;
    private int mWindowWidth;
    private int mWindowHeight;
    private int mScreenDensity;
    private KProgressHUD progressHUD;
    private WindowManager mWindowManager;
    private boolean isScreenshot = false;
    private Handler handler = new Handler();
    private CustomToast toast;
    private final int BREATH_INTERVAL_TIME = 1000; //设置呼吸灯时间间隔
    private AlphaAnimation animationFadeIn;
    private AlphaAnimation animationFadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowWidth = mWindowManager.getDefaultDisplay().getWidth();
        mWindowHeight = mWindowManager.getDefaultDisplay().getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;
        mImageReader = ImageReader.newInstance(mWindowWidth, mWindowHeight, 0x1, 2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        Intent intent = getIntent();
        project = intent.getStringExtra("project");
        workName = intent.getStringExtra("etWorkName");
        workCode = intent.getStringExtra("etWorkCode");
        if (project.trim().equals("") && workName.trim().equals("") && workCode.trim().equals("")) {
            linearLayout1.setVisibility(View.GONE);
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
        alertDialogUtil = new AlertDialogUtil(this);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mNotifications = new Notifications(getApplicationContext());
        if (mMediaProjection == null) {
            requestMediaProjection();
        }

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
                                String [] GpsData = data.split(getResources().getString(R.string.special_data));
                                if (GpsData.length!=0){
                                    String lastData = GpsData[GpsData.length-1];
                                    String [] NEData = lastData.split(",");
                                    String firstN = NEData[1];
                                    String firstE = NEData[3];
                                    BigDecimal DataN = new BigDecimal(Double.valueOf(firstN)/100).setScale(6,BigDecimal.ROUND_HALF_UP);
                                    BigDecimal DataE = new BigDecimal(Double.valueOf(firstE)/100).setScale(6,BigDecimal.ROUND_HALF_UP);
                                    String s = DataE+","+DataN;
                                    tvGPS.setText(s);
                                }
                            }catch (Exception ex) {
                                Log.e("XXX", ex.toString());
                            }
                        }

                        @Override
                        public void error(String s) {
                            (MainActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
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

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
    }

    @OnClick({R.id.rbCamera, R.id.rbVideo, R.id.rbAlbum, R.id.rbSound, R.id.rbSetting, R.id.linearLayoutStop, R.id.rbRefresh})
    public void onClick(View view1) {
        switch (view1.getId()) {
            case R.id.rbCamera:
                radioGroup.setVisibility(View.GONE);
                new BottomUI().hideBottomUIMenu(this.getWindow());
                name = getNowDate();
                isScreenshot = true;
                if (mMediaProjection != null) {
                    setUpVirtualDisplay();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCapture();
                        }
                    }, 200);
                }
                break;
            case R.id.rbVideo:
                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                haveAudio = "noAudio";
                format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
                name = format.format(new Date()) + "(" + workCode + ")" + ".mp4";
                alertDialogUtil.showImageDialog(new AlertDialogCallBack() {
                    @Override
                    public void confirm(String name1) {
                    }

                    @Override
                    public void cancel() {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void save(String name1) {
                        if (!name1.equals("")) {
                            name = name1 + "(" + workCode + ")" + ".mp4";
                        }
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    MainActivity.this.runOnUiThread(() -> {
                                        startVideoCapturing(view1);
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void checkName(String name1) {
                    }
                });
                break;
            case R.id.rbSound:
                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                haveAudio = "Audio";
                format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
                name = format.format(new Date()) + "(" + workCode + ")" + ".mp4";
                alertDialogUtil.showImageDialog(new AlertDialogCallBack() {
                    @Override
                    public void confirm(String name1) {
                    }

                    @Override
                    public void cancel() {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void save(String name1) {
                        if (!name1.equals("")) {
                            name = name1 + "(" + workCode + ")" + ".mp4";
                        }
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    MainActivity.this.runOnUiThread(() -> {
                                        startVideoCapturing(view1);
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void checkName(String name1) {
                    }
                });
                break;
            case R.id.linearLayoutStop:
                new BottomUI().BottomUIMenu(this.getWindow());
                rbCamera.setVisibility(View.VISIBLE);
                rbAlbum.setVisibility(View.VISIBLE);
                rbSound.setVisibility(View.VISIBLE);
                rbSetting.setVisibility(View.VISIBLE);
                rbVideo.setVisibility(View.VISIBLE);
                rbRefresh.setVisibility(View.VISIBLE);
                linearLayoutStop.setVisibility(View.GONE);
                rbVideoV = true;
                rbSoundV = true;
                if (mRecorder != null) {
                    stopRecordingAndOpenFile(view1.getContext());
                } else if (hasPermissions()) {
                    if (mMediaProjection == null) {
                        requestMediaProjection();
                    } else {
                        startCapturing(mMediaProjection, view1);
                    }
                } else if (Build.VERSION.SDK_INT >= M) {
                    requestPermissions();
                } else {
                    Toast.makeText(mNotifications, "权限未允许", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rbAlbum:
                new MainUI().showPopupMenu(rbAlbum, this);
                break;
            case R.id.rbSetting:
                intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, Constant.TAG_TWO);
                break;
            case R.id.rbRefresh:
                ShowDialog("/etc/init.d/mjpg-streamer restart");
                break;
        }
    }

    public void checkTirem(){
        animationFadeIn = new AlphaAnimation(0.1f, 1.0f);
        animationFadeIn.setDuration(BREATH_INTERVAL_TIME);
//        animationFadeIn.setStartOffset(100);

        animationFadeOut = new AlphaAnimation(1.0f, 0.1f);
        animationFadeOut.setDuration(BREATH_INTERVAL_TIME);
//        animationFadeIn.setStartOffset(100);

        animationFadeIn.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                ivTimer.startAnimation(animationFadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });

        animationFadeOut.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                ivTimer.startAnimation(animationFadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });
        ivTimer.startAnimation(animationFadeOut);
    }

    /**
     * 重启服务刷新视频
     *
     * @param data1
     */
    private void ShowDialog(String data1) {
        try {
            address = new getIp().getConnectIp();
            progressHUD = ProgressHUD.show(MainActivity.this);
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

    private void startCapture() {
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            Log.e("MainActivity", "image is null.");
            return;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        mBitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(buffer);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height);
        image.close();
        stopScreenCapture();
        if (mBitmap != null) {
            alertDialogUtil.showImageDialog(new AlertDialogCallBack() {

                @Override
                public void confirm(String name1) {
                }

                @Override
                public void cancel() {
                    radioGroup.setVisibility(View.VISIBLE);
                }

                @Override
                public void save(String name1) {
                    if (!name1.equals("")) {
                        name = name1 + "(" + workCode + ")" + ".png";
                    }
                    saveImg(name, MainActivity.this);
                }

                @Override
                public void checkName(String name1) {
                }
            });
        } else {
            System.out.println("bitmap is NULL!");
        }
    }

    private void stopScreenCapture() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            isScreenshot = false;
            mVirtualDisplay = null;
        }
    }

    private void setUpVirtualDisplay() {
        if (isScreenshot) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                    mWindowWidth, mWindowHeight, mScreenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }
    }

    public void startVideoCapturing(View view1) {
        if (haveAudio.equals("noAudio")) {
            if (rbVideoV) {
                rbCamera.setVisibility(View.INVISIBLE);
                rbAlbum.setVisibility(View.INVISIBLE);
                rbSound.setVisibility(View.INVISIBLE);
                rbSetting.setVisibility(View.INVISIBLE);
                rbRefresh.setVisibility(View.GONE);
                rbVideo.setVisibility(View.GONE);
                linearLayoutStop.setVisibility(View.VISIBLE);
                new BottomUI().hideBottomUIMenu(this.getWindow());
                rbVideoV = false;
            }
        }

        if (haveAudio.equals("Audio")) {
            if (rbSoundV) {
                rbCamera.setVisibility(View.INVISIBLE);
                rbAlbum.setVisibility(View.INVISIBLE);
                rbSound.setVisibility(View.INVISIBLE);
                rbSetting.setVisibility(View.INVISIBLE);
                rbRefresh.setVisibility(View.GONE);
                rbVideo.setVisibility(View.GONE);
                linearLayoutStop.setVisibility(View.VISIBLE);
                new BottomUI().hideBottomUIMenu(this.getWindow());
                rbSoundV = false;
            }
        }

        if (mRecorder != null) {
            stopRecordingAndOpenFile(view1.getContext());
        } else if (hasPermissions()) {
            if (mMediaProjection == null) {
                requestMediaProjection();
            } else {
                startCapturing(mMediaProjection, view1);
            }
        } else if (Build.VERSION.SDK_INT >= M) {
            requestPermissions();
        } else {
            Toast.makeText(mNotifications, "权限未允许", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 获取当前时间,用来给文件夹命名
     */
    private String getNowDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
        return format.format(new Date()) + "(" + workCode + ")" + ".png";
    }

    /**
     * 保存图片方法
     */
    public boolean saveImg(String name, Context context) {
        try {
            String dir = Environment.getExternalStorageDirectory() + "/LUKEImage/" + project + "/" + "设备/" + workName + "/" + workCode + "/";//图片保存的文件夹名
            File file = new File(Environment.getExternalStorageDirectory() + "/LUKEImage/" + project + "/" + "设备/" + workName + "/" + workCode + "/");
            //如果不存在  就mkdirs()创建此文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
            //将要保存的图片文件
            File mFile = new File(dir + name);
            if (mFile.exists()) {
                selectDialog(mFile, name);
                return false;
            }
            FileOutputStream outputStream = new FileOutputStream(mFile);     //构建输出流
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);  //compress到输出outputStream
            Uri uri = Uri.fromFile(mFile);                                  //获得图片的uri
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)); //发送广播通知更新图库，这样系统图库可以找到这张图片
            outputStream.flush();
            outputStream.close();
            if (toast != null) {
                toast.hide();
            }
            toast = new CustomToast(MainActivity.this, (ViewGroup) this.findViewById(R.id.toast_custom_parent));
            toast.show("图片保存成功", 1000);
//            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
            radioGroup.setVisibility(View.VISIBLE);
            new BottomUI().BottomUIMenu(this.getWindow());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            radioGroup.setVisibility(View.VISIBLE);
            new BottomUI().BottomUIMenu(this.getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            radioGroup.setVisibility(View.VISIBLE);
            new BottomUI().BottomUIMenu(this.getWindow());
        }
        return false;
    }

    private void selectDialog(File mFile, String name) {
        alertDialogUtil.showImageNameSelect(new DialogCallBackTwo() {
            @Override
            public void confirm(String name1, Dialog dialog, EditText editText) {
                if (name1.trim().equals("")) {
                    SpannableString s = new SpannableString("请输入文件名");//这里输入自己想要的提示文字
                    editText.setText("");
                    editText.setHint(s);
                } else {
                    if (name.equals(name1 + "(" + workCode + ")" + ".png")) {
                        SpannableString s = new SpannableString("文件名已存在");//这里输入自己想要的提示文字
                        editText.setText("");
                        editText.setHint(s);
                    } else {
                        dialog.dismiss();
                        saveImg(name1 + "(" + workCode + ")" + ".png", MainActivity.this);
                    }
                }
            }

            @Override
            public void cancel(String name2, Dialog dialog) {
                Log.e("XXX", name2);
                Log.e("XXX", name);
                if (name.equals(name2 + "(" + workCode + ")" + ".png")) {
                    dialog.dismiss();
                    mFile.delete();
                    saveImg(name, MainActivity.this);
                } else if (name2.equals("")) {
                    dialog.dismiss();
                    mFile.delete();
                    saveImg(name, MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "不存在重复文件，请点击保存按钮", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectVideoDialog(File mFile, String name2, View view1) {
        alertDialogUtil.showImageNameSelect(new DialogCallBackTwo() {

            @Override
            public void confirm(String name1, Dialog dialog, EditText editText) {
                if (name1.trim().equals("")) {
                    SpannableString s = new SpannableString("请输入文件名");//这里输入自己想要的提示文字
                    editText.setText("");
                    editText.setHint(s);
                } else {
                    name1 = name1 + "(" + workCode + ")" + ".mp4";
                    if (name.equals(name1)) {
                        SpannableString s = new SpannableString("文件名已存在");//这里输入自己想要的提示文字
                        editText.setText("");
                        editText.setHint(s);
                    } else {
                        dialog.dismiss();
                        name = name1;
                        startCapturing(mMediaProjection, view1);
                    }
                }
            }

            @Override
            public void cancel(String name2, Dialog dialog) {
                Log.e("XXX", name2);
                Log.e("XXX", name);
                if (name.equals(name2 + "(" + workCode + ")" + ".mp4")) {
                    dialog.dismiss();
                    mFile.delete();
                    startCapturing(mMediaProjection, view1);
                } else if (name2.equals("")) {
                    dialog.dismiss();
                    mFile.delete();
                    startCapturing(mMediaProjection, view1);
                } else {
                    Toast.makeText(MainActivity.this, "不存在重复文件，请点击保存按钮", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startRecorder() {
        if (mRecorder == null) return;
        mRecorder.start();
        registerReceiver(mStopActionReceiver, new IntentFilter(ACTION_STOP));
    }

    private void stopRecorder() {
        mNotifications.clear();
        if (mRecorder != null) {
            mRecorder.quit();
        }
        mRecorder = null;
        try {
            unregisterReceiver(mStopActionReceiver);
        } catch (Exception e) {
            //ignored
        }
    }

    private void cancelRecorder() {
        if (mRecorder == null) return;
        Toast.makeText(this, "Permission denied! Screen recorder is cancel", Toast.LENGTH_SHORT).show();
        stopRecorder();
    }

    @TargetApi(M)
    private void requestPermissions() {
        String[] permissions = haveAudio.equals("Audio")
                ? new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}
                : new String[]{WRITE_EXTERNAL_STORAGE};
        boolean showRationale = false;
        for (String perm : permissions) {
            showRationale |= shouldShowRequestPermissionRationale(perm);
        }
        if (!showRationale) {
            requestPermissions(permissions, REQUEST_PERMISSIONS);
            return;
        }
        new AlertDialog.Builder(this)
                .setMessage("Using your mic to record audio and your sd card to save video file")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        requestPermissions(permissions, REQUEST_PERMISSIONS))
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    private boolean hasPermissions() {
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        int granted = (haveAudio.equals("Audio") ? pm.checkPermission(RECORD_AUDIO, packageName) : PackageManager.PERMISSION_GRANTED)
                | pm.checkPermission(WRITE_EXTERNAL_STORAGE, packageName);
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    private void stopRecordingAndOpenFile(Context context) {
        stopRecorder();
        StrictMode.VmPolicy vmPolicy = StrictMode.getVmPolicy();
        try {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        } finally {
            StrictMode.setVmPolicy(vmPolicy);
        }
    }

    public static final String ACTION_STOP = BuildConfig.APPLICATION_ID + ".action.STOP";

    private BroadcastReceiver mStopActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_STOP.equals(intent.getAction())) {
                stopRecordingAndOpenFile(context);
            }
        }
    };


    private void requestMediaProjection() {
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION);
    }

    private void startCapturing(MediaProjection mediaProjection, View view1) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                    MainActivity.this.runOnUiThread(() -> {
                        video = createVideoConfig();
                        audio = createAudioConfig(); // audio can be null
                        if (video == null) {
                            Toast.makeText(MainActivity.this, "Create ScreenRecorder failure", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        File dir = null;
                        if (haveAudio.equals("Audio")) {
                            dir = new File(Environment.getExternalStorageDirectory() + "/LUKEVideo/" + project + "/" + "设备/" + workName + "/" + workCode + "/");
                            if (!dir.exists() && !dir.mkdirs()) {
                                cancelRecorder();
                                return;
                            }
                        } else if (haveAudio.equals("noAudio")) {
                            dir = new File(Environment.getExternalStorageDirectory() + "/LUKENOVideo/" + project + "/" + "设备/" + workName + "/" + workCode + "/");
                            if (!dir.exists() && !dir.mkdirs()) {
                                cancelRecorder();
                                return;
                            }
                        }
                        file = new File(dir, name);
                        if (rbVideoV && rbSoundV) {
                            Log.e("MainActivity", "XXX");
                        } else {
                            if (haveAudio.equals("Audio")) {
                                //将要保存的图片文件
                                if (file.exists()) {
                                    selectVideoDialog(file, name, view1);
                                    return;
                                }
                            } else if (haveAudio.equals("noAudio")) {
                                if (file.exists()) {
                                    selectVideoDialog(file, name, view1);
                                    return;
                                }
                            }
                        }
                        checkTirem();
                        timer.start();
                        mRecorder = newRecorder(mediaProjection, video, audio, file);
                        if (hasPermissions()) {
                            startRecorder();
                        } else {
                            cancelRecorder();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private ScreenRecorder newRecorder(MediaProjection mediaProjection, VideoEncodeConfig video, AudioEncodeConfig audio, File output) {
        final VirtualDisplay display = getOrCreateVirtualDisplay(mediaProjection, video);
        ScreenRecorder r = new ScreenRecorder(video, audio, display, output.getAbsolutePath());
        r.setCallback(new ScreenRecorder.Callback() {
            long startTime = 0;

            @Override
            public void onStop(Throwable error) {
                runOnUiThread(() -> stopRecorder());
                if (error != null) {
                    error.printStackTrace();
                    output.delete();
                } else {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                            .addCategory(Intent.CATEGORY_DEFAULT)
                            .setData(Uri.fromFile(output));
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onStart() {
                mNotifications.recording(0);
            }

            @Override
            public void onRecording(long presentationTimeUs) {
                if (startTime <= 0) {
                    startTime = presentationTimeUs;
                }
                long time = (presentationTimeUs - startTime) / 1000;
                mNotifications.recording(time);
            }
        });
        return r;
    }

    private VirtualDisplay getOrCreateVirtualDisplay(MediaProjection mediaProjection, VideoEncodeConfig config) {
        if (mVirtualDisplay == null) {
            mVirtualDisplay = mediaProjection.createVirtualDisplay("ScreenRecorder-display0",
                    config.width, config.height, 1 /*dpi*/,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                    null /*surface*/, null, null);
        } else {
            // resize if size not matched
            Point size = new Point();
            mVirtualDisplay.getDisplay().getSize(size);
            if (size.x != config.width || size.y != config.height) {
                mVirtualDisplay.resize(config.width, config.height, 1);
            }
        }
        return mVirtualDisplay;
    }

    private AudioEncodeConfig createAudioConfig() {
        if (haveAudio.equals("noAudio")) return null;
        String codec = "c2.android.aac.encoder";
        int bitrate = 80000;
        int samplerate = 44100;
        int channelCount = 1;
        int profile = 1;
        return new AudioEncodeConfig(codec, AUDIO_AAC, bitrate, samplerate, channelCount, profile);
    }

    private VideoEncodeConfig createVideoConfig() {
        final String codec = "c2.android.avc.encoder";
        int height = 1080;
        int width = 1920;
        int framerate = 25;
        int iframe = 1;
        int bitrate = 800000;
        MediaCodecInfo.CodecProfileLevel profileLevel = null;
        return new VideoEncodeConfig(width, height, bitrate, framerate, iframe, codec, VIDEO_AVC, profileLevel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent backdata) {
        super.onActivityResult(requestCode, resultCode, backdata);
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode == Activity.RESULT_OK) {
                    MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, backdata);
                    if (mediaProjection == null) {
                        Log.e("@@", "media projection is null");
                        return;
                    }
                    mMediaProjection = mediaProjection;
                    mMediaProjection.registerCallback(mProjectionCallback, new Handler());
                } else {
                    finish();
                }
                break;
            case Constant.TAG_TWO:
                if (resultCode == Constant.TAG_ONE) {
                    Setting setting = (Setting) backdata.getSerializableExtra("data");
                    Gson gson = new Gson();
                    String obj2 = gson.toJson(setting);
                }
                break;
        }
    }

    private MediaProjection.Callback mProjectionCallback = new MediaProjection.Callback() {
        @Override
        public void onStop() {
            if (mRecorder != null) {
                stopRecorder();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            int granted = PackageManager.PERMISSION_GRANTED;
            for (int r : grantResults) {
                granted |= r;
            }
            if (granted == PackageManager.PERMISSION_GRANTED) {
                requestMediaProjection();
            } else {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecorder();
        if (mVirtualDisplay != null) {
            mVirtualDisplay.setSurface(null);
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;

        }
    }

    Handler handlerSetting = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TAG_ONE:
                    Toast.makeText(MainActivity.this, "重启成功", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                    address = "http://" + address + ":8080";
                    webView.loadUrl(address);
                    break;
                case Constant.TAG_TWO:
                    Toast.makeText(MainActivity.this, "重启失败", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
            }
        }
    };

}