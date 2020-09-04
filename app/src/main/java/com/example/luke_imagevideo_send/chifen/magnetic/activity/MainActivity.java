package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaCodecInfo;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.luke_imagevideo_send.BuildConfig;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.activity.AlbumActivity;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.chifen.magnetic.util.AudioEncodeConfig;
import com.example.luke_imagevideo_send.chifen.magnetic.util.Notifications;
import com.example.luke_imagevideo_send.chifen.magnetic.util.ScreenRecorder;
import com.example.luke_imagevideo_send.chifen.magnetic.util.VideoEncodeConfig;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.LoadingDialog;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.modbus.ModbusCallback;
import com.example.luke_imagevideo_send.modbus.ModbusManager;
import com.licheedev.modbus4android.param.TcpParam;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.media.MediaFormat.MIMETYPE_AUDIO_AAC;
import static android.media.MediaFormat.MIMETYPE_VIDEO_AVC;
import static android.os.Build.VERSION_CODES.M;
import static com.example.luke_imagevideo_send.ApiAddress.api;

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
    @BindView(R.id.imageView)
    ImageView imageView;
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

    Bitmap mBitmap;
    String name = "";

    Handler handler;
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
    private LocationManager locationManager;
    private static boolean isExit = false;
    private String haveAudio = "noAudio";
    //磁粉
    short[] data = new short[30];
    static final String VIDEO_AVC = MIMETYPE_VIDEO_AVC; // H.264 Advanced Video Coding
    static final String AUDIO_AAC = MIMETYPE_AUDIO_AAC; // H.264 Advanced Audio Coding
    private AtomicBoolean mQuit = new AtomicBoolean(false);
    String zhiliu = "",jiaoliu = "",heiguang = "",baiguang = "",diandong = "",liandong = "",kaiguan = "";

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
            alertDialogUtil.showDialog("您确定要退出程序吗", new AlertDialogCallBack() {

                @Override
                public void confirm(String name) {
                    finish();
                }

                @Override
                public void cancel() {

                }

                @Override
                public void save(String name) {

                }

                @Override
                public void checkName(String name) {

                }
            });
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        header.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        frameLayout.setBackgroundColor(getResources().getColor(R.color.black));
        alertDialogUtil = new AlertDialogUtil(this);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initGPS();
        initTime();
        makeConnection();
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mNotifications = new Notifications(getApplicationContext());
        if (mMediaProjection == null) {
            requestMediaProjection();
        }

        webView.setBackgroundColor(Color.BLACK);
        webView.getSettings().setJavaScriptEnabled(true);
        //访问网页
        webView.loadUrl(api + "?action=stream");
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
                imageView.setVisibility(View.GONE);
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

    //建立modbus连接
    private void makeConnection() {
        LoadingDialog loadingDialog = new LoadingDialog(this, "连接服务中", R.mipmap.ic_dialog_loading);
        loadingDialog.show();
        // TCP
        TcpParam param;
        param = TcpParam.create("", 502)
                .setTimeout(1000)
                .setRetries(0)
                .setEncapsulated(false)
                .setKeepAlive(true);

        ModbusManager.get().closeModbusMaster(); // 先关闭一下
        ModbusManager.get().init(param, new ModbusCallback<ModbusMaster>() {
            @Override
            public void onSuccess(ModbusMaster modbusMaster) {
                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                ModbusManager.get()
                        .writeRegisters(0, 1, getData(),
                                new ModbusCallback<WriteRegistersResponse>() {
                                    @Override
                                    public void onSuccess(WriteRegistersResponse writeRegistersResponse) {
                                        // 发送成功
                                        Toast.makeText(MainActivity.this, "F16写入成功", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Throwable tr) {
                                        Toast.makeText(MainActivity.this, tr.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFinally() {

                                    }
                                });
            }

            @Override
            public void onFailure(Throwable tr) {
                AlertDialogUtil alertDialogUtil = new AlertDialogUtil(MainActivity.this);
                alertDialogUtil.showDialog("modbus连接失败，只能使用本地模式，是否继续使用", new AlertDialogCallBack() {
                    @Override
                    public void confirm(String name) {

                    }

                    @Override
                    public void cancel() {
                        finish();
                    }

                    @Override
                    public void save(String name) {

                    }

                    @Override
                    public void checkName(String name) {

                    }
                });
                loadingDialog.dismiss();
            }

            @Override
            public void onFinally() {
                // todo updateDeviceSwitchButton();
            }
        });
    }

    // 初始化时间方法
    public void initTime() {
        // 时间变化
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                tvTime.setText((String) msg.obj);
            }
        };
        Threads thread = new Threads();
        thread.start();
    }

    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String str = sdf.format(new Date());
                    handler.sendMessage(handler.obtainMessage(100, str));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 初始化GPS方法
    private void initGPS() {
        //获取定位管理器
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS模块", Toast.LENGTH_SHORT).show();
            return;
        }
        //设置定位信息
        LocationListener listener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                Log.e("XXX", "1");
                switch (status) {
                    //GPS状态为可见时
                    case LocationProvider.AVAILABLE:
                        Log.i("XXX", "当前GPS状态为可见状态");
                        break;
                    //GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        Log.i("XXX", "当前GPS状态为服务区外状态");
                        showGPSDialog();
                        break;
                    //GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.i("XXX", "当前GPS状态为暂停服务状态");
                        showGPSDialog();
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                Log.e("XXX", "11");
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                Log.e("XXX", "111");
            }

            //位置改变的时候调用，这个方法用于返回一些位置信息
            @Override
            public void onLocationChanged(Location location) {
                //获取位置变化结果
                float accuracy = location.getAccuracy();//精确度，以密为单位
                double altitude = location.getAltitude();//获取海拔高度
                double longitude = location.getLongitude();//经度
                double latitude = location.getLatitude();//纬度
                float speed = location.getSpeed();//速度

                BigDecimal bigDecimal = new BigDecimal(longitude);
                longitude = bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                BigDecimal bigDecimal1 = new BigDecimal(latitude);
                latitude = bigDecimal1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();

                //显示位置信息
                tvGPS.setText(longitude + "," + latitude);
//                tv_show_location.append("latitude:"+latitude+"\n");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 10000, 0, listener);//Register for location updates
    }

    private void showGPSDialog() {
        alertDialogUtil.showSmallDialogCli("当前区域无GPS信号", new AlertDialogCallBack() {
            @Override
            public void confirm(String name) {
                finish();
            }

            @Override
            public void cancel() {

            }

            @Override
            public void save(String name) {

            }

            @Override
            public void checkName(String name) {

            }
        });
    }

    /**
     * 数据组装
     */
    private short[] getData() {
        //传输标识
        data[0] = 0;
        data[1] = 0;
        //协议标识
        data[2] = 0;
        data[3] = 0;
        //字节长度
        data[4] = 0;
        data[5] = 19;
        //单位标识符
        data[6] = 1;
        //功能码
        data[7] = 10;
        //地址码
        data[8] = 0;
        data[9] = 1;
        //寄存器个数
        data[10] = 0;
        data[11] = 6;
        //字节数
        data[12] = 12;
        //型号
//        data[13] = 0x01;
        data[13] = 1;
        data[14] = 0;
        data[15] = 0;
        data[16] = 0;
        data[17] = 0;
        //时间
        data[18] = 2;
        data[19] = 0;
        data[20] = 0;
        data[21] = 0;
        data[22] = 0;

//        //mac
//        data[23] = 3;
//        data[24] = 0;
//        data[25] = 0;
//        data[26] = 0;
//        data[27] = 0;

        //交直模式
        data[23] = 6;
        data[24] = 0;
//        //电量
//        data[28] = 5;
//        data[29] = 0;
        //联动时长
        data[25] = 7;
        data[26] = 0;
        //黑白光切换
        data[27] = 8;
        data[28] = 0;
        //点动联动切换
        data[29] = 9;
        data[30] = 0;
        return data;
    }

    @OnClick({R.id.rbCamera, R.id.rbVideo, R.id.rbAlbum, R.id.rbSound, R.id.rbSetting})
    public void onClick(View view1) {
        switch (view1.getId()) {
            case R.id.rbCamera:
                radioGroup.setVisibility(View.GONE);
                name = getNowDate();
                View view = view1.getRootView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                mBitmap = view.getDrawingCache();
                if (mBitmap != null) {
                    webView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(mBitmap);

                    alertDialogUtil.showImageDialog(new AlertDialogCallBack() {

                        @Override
                        public void confirm(String name1) {
                            if (!name1.equals("")) {
                                name = name1 + ".jpg";
                            }
                            saveImg(mBitmap, name, MainActivity.this);
                        }

                        @Override
                        public void cancel() {
                            webView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            radioGroup.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void save(String name1) {
                            if (!name1.equals("")) {
                                name = name1 + ".jpg";
                            }
                            saveImg(mBitmap, name, MainActivity.this);
                        }

                        @Override
                        public void checkName(String name1) {
                            if (!name1.equals("")) {
                                name = name1 + ".jpg";
                            }
                            saveImg(mBitmap, name, MainActivity.this);
                        }
                    });
                } else {
                    System.out.println("bitmap is NULL!");
                }
                break;
            case R.id.rbVideo:
                haveAudio = "noAudio";
                if (mRecorder != null) {
                    stopRecordingAndOpenFile(view1.getContext());
                } else if (hasPermissions()) {
                    if (mMediaProjection == null) {
                        requestMediaProjection();
                    } else {
                        Toast.makeText(mNotifications, "开始录制", Toast.LENGTH_SHORT).show();
                        startCapturing(mMediaProjection);
                    }
                } else if (Build.VERSION.SDK_INT >= M) {
                    requestPermissions();
                } else {
                    Toast.makeText(mNotifications, "权限未允许", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rbAlbum:
                intent = new Intent(this, AlbumActivity.class);
                startActivity(intent);
                break;
            case R.id.rbSound:
                haveAudio = "Audio";
                if (mRecorder != null) {
                    stopRecordingAndOpenFile(view1.getContext());
                } else if (hasPermissions()) {
                    if (mMediaProjection == null) {
                        requestMediaProjection();
                    } else {
                        Toast.makeText(mNotifications, "开始录制", Toast.LENGTH_SHORT).show();
                        startCapturing(mMediaProjection);
                    }
                } else if (Build.VERSION.SDK_INT >= M) {
                    requestPermissions();
                } else {
                    Toast.makeText(mNotifications, "权限未允许", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rbSetting:
                intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, Constant.TAG_TWO);
                break;
        }
    }

    /**
     * 获取当前时间,用来给文件夹命名
     */
    private String getNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date()) + ".jpg";
    }

    /**
     * 保存图片方法
     */
    public boolean saveImg(Bitmap bitmap, String name, Context context) {
        try {
//            String sdcardPath = System.getenv("EXTERNAL_STORAGE");      //获得sd卡路径
            String dir = Environment.getExternalStorageDirectory() + "/LUKEImage/";//图片保存的文件夹名
//            File file = new File(dir);
            File file = new File(Environment.getExternalStorageDirectory() + "/LUKEImage/");
            if (!file.exists()) {                                     //如果不存在  就mkdirs()创建此文件夹
                file.mkdirs();
            }
            Log.i("SaveImg", "file uri==>" + dir);
            File mFile = new File(dir + name);                        //将要保存的图片文件
            if (mFile.exists()) {
                Toast.makeText(context, "该图片已存在!", Toast.LENGTH_SHORT).show();
                mFile.delete();
                return false;
            }

            FileOutputStream outputStream = new FileOutputStream(mFile);     //构建输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);  //compress到输出outputStream
            Uri uri = Uri.fromFile(mFile);                                  //获得图片的uri
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)); //发送广播通知更新图库，这样系统图库可以找到这张图片
            outputStream.flush();
            outputStream.close();
            //隐藏image显示webview
            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
            imageView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void requestMediaProjection() {
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION);
    }

    private void startCapturing(MediaProjection mediaProjection) {
        VideoEncodeConfig video = createVideoConfig();
        AudioEncodeConfig audio = createAudioConfig(); // audio can be null
        if (video == null) {
            Toast.makeText(this, "Create ScreenRecorder failure", Toast.LENGTH_SHORT).show();
            return;
        }
        File dir = null;
        if (haveAudio.equals("Audio")) {
            dir = new File(Environment.getExternalStorageDirectory() + "/LUKEVideo/");
            if (!dir.exists() && !dir.mkdirs()) {
                cancelRecorder();
                return;
            }
        } else if (haveAudio.equals("noAudio")) {
            dir = new File(Environment.getExternalStorageDirectory() + "/LUKENOVideo/");
            if (!dir.exists() && !dir.mkdirs()) {
                cancelRecorder();
                return;
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
        File file = null;
        if (haveAudio.equals("Audio")) {
            file = new File(dir, "LUKEVideo-" + format.format(new Date()) + "-" + video.width + "x" + video.height + ".mp4");
        } else if (haveAudio.equals("noAudio")) {
            file = new File(dir, "LUKENOVideo-" + format.format(new Date()) + "-" + video.width + "x" + video.height + ".mp4");
        }
//        final File file = new File(dir, "LUKEVideo-" + format.format(new Date()) + "-" + video.width + "x" + video.height + ".mp4");
        Log.d("@@", "Create recorder with :" + video + " \n " + audio + "\n " + file);
        mRecorder = newRecorder(mediaProjection, video, audio, file);
        if (hasPermissions()) {
            startRecorder();
        } else {
            cancelRecorder();
        }
    }

    private ScreenRecorder newRecorder(MediaProjection mediaProjection, VideoEncodeConfig video,
                                       AudioEncodeConfig audio, File output) {
        final VirtualDisplay display = getOrCreateVirtualDisplay(mediaProjection, video);
        ScreenRecorder r = new ScreenRecorder(video, audio, display, output.getAbsolutePath());
        r.setCallback(new ScreenRecorder.Callback() {
            long startTime = 0;

            @Override
            public void onStop(Throwable error) {
                runOnUiThread(() -> stopRecorder());
                if (error != null) {
                    toast("Recorder error ! See logcat for more details");
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
        if (!haveAudio.equals("Audio")) return null;
        String codec = "c2.android.aac.encoder";
        int bitrate = 80000;
        int samplerate = 44100;
        int channelCount = 1;
        int profile = 1;
        return new AudioEncodeConfig(codec, AUDIO_AAC, bitrate, samplerate, channelCount, profile);
    }

    private VideoEncodeConfig createVideoConfig() {
        final String codec = "OMX.hisi.video.encoder.avc";
        // video size
        int width = 570;
        int height = 350;
//        int width = 1080;
//        int height = 1920;
        int framerate = 15;
        int iframe = 1;
        int bitrate = 600000;
        MediaCodecInfo.CodecProfileLevel profileLevel = null;
        return new VideoEncodeConfig(width, height, bitrate, framerate, iframe, codec, VIDEO_AVC, profileLevel);
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
        Toast.makeText(mNotifications, "Permission denied! Screen recorder is cancel", Toast.LENGTH_SHORT).show();
        stopRecorder();
    }

    @TargetApi(M)
    private void requestPermissions() {
//        String[] permissions = new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO};
        String[] permissions = haveAudio.equals("noAudio")
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
//        int granted = pm.checkPermission(RECORD_AUDIO, packageName);
        int granted = (haveAudio.equals("noAudio") ? pm.checkPermission(RECORD_AUDIO, packageName) : PackageManager.PERMISSION_GRANTED)
                | pm.checkPermission(WRITE_EXTERNAL_STORAGE, packageName);
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    private void toast(String message, Object... args) {

        int length_toast = Locale.getDefault().getCountry().equals("BR") ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        // In Brazilian Portuguese this may take longer to read

        Toast toast = Toast.makeText(this,
                (args.length == 0) ? message : String.format(Locale.US, message, args),
                length_toast);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            runOnUiThread(toast::show);
        } else {
            toast.show();
        }
    }

    private void stopRecordingAndOpenFile(Context context) {
        File file = new File(mRecorder.getSavedPath());
        stopRecorder();
        Toast.makeText(context, "Using your mic to record audio and your sd card to save video file", Toast.LENGTH_SHORT).show();
        StrictMode.VmPolicy vmPolicy = StrictMode.getVmPolicy();
        try {
            // disable detecting FileUriExposure on public file
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
//            viewResult(file);
        } finally {
            StrictMode.setVmPolicy(vmPolicy);
        }
    }

    private void viewResult(File file) {
        Intent view = new Intent(Intent.ACTION_VIEW);
        view.addCategory(Intent.CATEGORY_DEFAULT);
        view.setDataAndType(Uri.fromFile(file), VIDEO_AVC);
        view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(view);
        } catch (ActivityNotFoundException e) {
            // no activity can open this video
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent backdata) {
        super.onActivityResult(requestCode, resultCode, backdata);
        switch (requestCode){
            case REQUEST_MEDIA_PROJECTION:
                MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, backdata);
                if (mediaProjection == null) {
                    Log.e("@@", "media projection is null");
                    return;
                }
                mMediaProjection = mediaProjection;
                mMediaProjection.registerCallback(mProjectionCallback, new Handler());
                break;
            case Constant.TAG_TWO:
                if (resultCode==Constant.TAG_ONE){
                    Setting setting = (Setting) backdata.getSerializableExtra("data");
                    jiaoliu = setting.getJiaoliu();
                    zhiliu = setting.getZhiliu();
                    heiguang = setting.getHeiguang();
                    baiguang = setting.getBaiguang();
                    diandong = setting.getDiandong();
                    liandong = setting.getLiandong();
                    kaiguan = setting.getKaiguan();
                    if (jiaoliu.equals("yes")){
                        data[24] = 0;
                    }
                    if (zhiliu.equals("yes")){
                        data[24] = 0;
                    }

                    if (kaiguan.equals("yes")){
                        data[26] = 0;
                    }

                    if (heiguang.equals("yes")){
                        data[28] = 0;
                    }
                    if (baiguang.equals("yes")){
                        data[28] = 0;
                    }

                    if (diandong.equals("yes")){
                        data[30] = 0;
                    }
                    if (liandong.equals("yes")){
                        data[30] = 0;
                    }
                    ModbusManager.get().release();
                    makeConnection();
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
    protected void onDestroy() {
        super.onDestroy();
        ModbusManager.get().release();
        // 关闭监听
//        locationManager.removeUpdates(locationListeners);
//        locationListeners = null;
    }

}