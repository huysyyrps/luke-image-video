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
import android.location.LocationManager;
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
import android.os.StrictMode;
import android.text.SpannableString;
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
import com.example.luke_imagevideo_send.cehouyi.util.GetGPS;
import com.example.luke_imagevideo_send.cehouyi.util.GetGPSCallBack;
import com.example.luke_imagevideo_send.cehouyi.util.GetTime;
import com.example.luke_imagevideo_send.cehouyi.util.GetTimeCallBack;
import com.example.luke_imagevideo_send.cehouyi.util.ModbusConnection;
import com.example.luke_imagevideo_send.chifen.camera.activity.SettingActivity;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.chifen.magnetic.util.AudioEncodeConfig;
import com.example.luke_imagevideo_send.chifen.magnetic.util.MainUI;
import com.example.luke_imagevideo_send.chifen.magnetic.util.Notifications;
import com.example.luke_imagevideo_send.chifen.magnetic.util.ScreenRecorder;
import com.example.luke_imagevideo_send.chifen.magnetic.util.VideoEncodeConfig;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.DialogCallBackTwo;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.modbus.ModbusManager;
import com.google.gson.Gson;
import com.licheedev.modbus4android.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class MainActivity extends BaseActivity implements View.OnLongClickListener, View.OnTouchListener {

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
    @BindView(R.id.rbSuspend)
    RadioButton rbSuspend;

    Bitmap mBitmap;
    String name = "";

    boolean loadError = false;
    private static AlertDialogUtil alertDialogUtil;
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private static final int REQUEST_PERMISSIONS = 2;
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
    @BindView(R.id.tvCE)
    TextView tvCE;
    @BindView(R.id.tvDG)
    TextView tvDG;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.llItem)
    LinearLayout llItem;
    @BindView(R.id.tvOpen)
    TextView tvOpen;
    @BindView(R.id.tvClose)
    TextView tvClose;
    private MediaProjectionManager mMediaProjectionManager;
    private Notifications mNotifications;
    private ScreenRecorder mRecorder;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private Intent intent;
    private LocationManager locationManager;
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
    SimpleDateFormat format;
    private ImageReader mImageReader;
    private int mWindowWidth;
    private int mWindowHeight;
    private int mScreenDensity;
    private WindowManager mWindowManager;
    private boolean isScreenshot = false;
    private Handler handler = new Handler();

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
        mWindowWidth = mWindowManager.getDefaultDisplay().getWidth();
        mWindowHeight = mWindowManager.getDefaultDisplay().getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;
        mImageReader = ImageReader.newInstance(mWindowWidth, mWindowHeight, 0x1, 2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
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

        new ModbusConnection().makeConnection(this);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mNotifications = new Notifications(getApplicationContext());
        if (mMediaProjection == null) {
            requestMediaProjection();
        }

        webView.setBackgroundColor(Color.BLACK);
        webView.getSettings().setJavaScriptEnabled(true);
        String address = null;
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

        //获取当前时间
        new GetTime().initTime(new GetTimeCallBack() {
            @Override
            public void backTime(String time) {
                tvTime.setText(time);
            }
        });

        //获取GPS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        new GetGPS().initGPS(MainActivity.this, locationManager, alertDialogUtil, new GetGPSCallBack() {
            @Override
            public void backGPS(String gps) {
                tvGPS.setText(gps);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @OnClick({R.id.rbCamera, R.id.rbVideo, R.id.rbAlbum, R.id.rbSound, R.id.rbSetting, R.id.rbSuspend
            , R.id.btnTop, R.id.btnLeft, R.id.btnLight, R.id.btnRight, R.id.btnBotton, R.id.tcCH,
            R.id.tvSDJia, R.id.tvSDJian, R.id.tvCE, R.id.tvDG, R.id.ivBack, R.id.tvOpen, R.id.tvClose})
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
                        startVideoCapturing(view1);
                    }

                    @Override
                    public void checkName(String name1) {
                    }
                });
                break;
            case R.id.rbSound:
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
                        startVideoCapturing(view1);
                    }

                    @Override
                    public void checkName(String name1) {
                    }
                });
                break;
            case R.id.rbSuspend:
                new BottomUI().BottomUIMenu(this.getWindow());
                rbCamera.setVisibility(View.VISIBLE);
                rbAlbum.setVisibility(View.VISIBLE);
                rbSound.setVisibility(View.VISIBLE);
                rbSetting.setVisibility(View.VISIBLE);
                rbVideo.setVisibility(View.VISIBLE);
                rbSuspend.setVisibility(View.GONE);
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
                Toast.makeText(mNotifications, "磁化", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvSDJia:
                Toast.makeText(mNotifications, "速度+", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvSDJian:
                Toast.makeText(mNotifications, "速度—", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvCE:
                Toast.makeText(mNotifications, "磁轭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvDG:
                if (tvDG.getText().equals("白光")) {
                    tvDG.setText("黑光");
                } else {
                    tvDG.setText("白光");
                }
                break;
            case R.id.tvOpen:
                Toast.makeText(mNotifications, "灯光开", Toast.LENGTH_SHORT).show();
                tvOpen.setVisibility(View.GONE);
                tvClose.setVisibility(View.VISIBLE);
                break;
            case R.id.tvClose:
                Toast.makeText(mNotifications, "灯光关", Toast.LENGTH_SHORT).show();
                tvOpen.setVisibility(View.VISIBLE);
                tvClose.setVisibility(View.GONE);
                break;
            case R.id.ivBack:
                llItem.setVisibility(View.GONE);
                break;
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
                rbVideo.setVisibility(View.GONE);
                rbSuspend.setVisibility(View.VISIBLE);
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
                rbVideo.setVisibility(View.GONE);
                rbSuspend.setVisibility(View.VISIBLE);
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
                    Toast.makeText(mNotifications, value1, Toast.LENGTH_SHORT).show();
                } else if (clickNum == 2) {
                    Toast.makeText(mNotifications, value2, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mNotifications, "向上长按", Toast.LENGTH_SHORT).show();
                tag = "longUp";
                log = "向上长按松开";
                break;
            case R.id.btnBotton:
                Toast.makeText(mNotifications, "向下长按", Toast.LENGTH_SHORT).show();
                tag = "longUp";
                log = "向下长按松开";
                break;
            case R.id.btnLeft:
                Toast.makeText(mNotifications, "向左长按", Toast.LENGTH_SHORT).show();
                tag = "longUp";
                log = "向左长按松开";
                break;
            case R.id.btnRight:
                Toast.makeText(mNotifications, "向右长按", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mNotifications, log, Toast.LENGTH_SHORT).show();
                    tag = "";
                    log = "";
                }
                break;
        }
        return false;
    }

    /**
     * 获取当前时间,用来给文件夹命名
     */
    private String getNowDate() {
        long time = System.currentTimeMillis();//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        return str + "(" + workCode + ")" + ".png";
    }

    /**
     * 保存图片方法
     */
    public boolean saveImg(String name, Context context) {
        try {
            String dir = Environment.getExternalStorageDirectory() + "/LUKEImage/" + project + "/" + "设备/" + workName + "/";//图片保存的文件夹名
            File file = new File(Environment.getExternalStorageDirectory() + "/LUKEImage/" + project + "/" + "设备/" + workName + "/");
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
//            int bmpWidth=mBitmap.getWidth();
//            int bmpHeight=mBitmap.getHeight();
//            /* 产生reSize后的Bitmap对象 */
//            Matrix matrix = new Matrix();
//            matrix.postScale(1920, 1080);
//            mBitmap = Bitmap.createBitmap(mBitmap,0,0,bmpWidth, bmpHeight,matrix,true);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);  //compress到输出outputStream
            Uri uri = Uri.fromFile(mFile);                                  //获得图片的uri
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)); //发送广播通知更新图库，这样系统图库可以找到这张图片
            outputStream.flush();
            outputStream.close();
            //隐藏image显示webview
            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
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
                    if (name.equals(name1+ "(" + workCode + ")" + ".png")) {
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
                if (name.equals(name2 + "(" + workCode + ")" + ".mp4")) {
                    dialog.dismiss();
                    mFile.delete();
                    saveImg(name, MainActivity.this);
                }else if (name2.equals("")){
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
                } else if (name2.equals("")){
                    dialog.dismiss();
                    mFile.delete();
                    startCapturing(mMediaProjection, view1);
                }else{
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
        video = createVideoConfig();
        audio = createAudioConfig(); // audio can be null
        if (video == null) {
            Toast.makeText(this, "Create ScreenRecorder failure", Toast.LENGTH_SHORT).show();
            return;
        }
        File dir = null;
        if (haveAudio.equals("Audio")) {
            dir = new File(Environment.getExternalStorageDirectory() + "/LUKEVideo/" + project + "/" + "设备/" + workName + "/");
            if (!dir.exists() && !dir.mkdirs()) {
                cancelRecorder();
                return;
            }
        } else if (haveAudio.equals("noAudio")) {
            dir = new File(Environment.getExternalStorageDirectory() + "/LUKENOVideo/" + project + "/" + "设备/" + workName + "/");
            if (!dir.exists() && !dir.mkdirs()) {
                cancelRecorder();
                return;
            }
        }
        file = new File(dir, name);
        if (rbVideoV && rbSoundV) {
            Log.e("XXX", "XXX");
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

        Log.d("@@", "Create recorder with :" + video + " \n " + audio + "\n " + file);
        mRecorder = newRecorder(mediaProjection, video, audio, file);
        if (hasPermissions()) {
            startRecorder();
        } else {
            cancelRecorder();
        }
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
                    ModbusManager.get().release();
                    new ModbusConnection().makeConnection(this);
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
        ModbusManager.get().release();
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

}