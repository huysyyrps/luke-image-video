package com.example.luke_imagevideo_send.main.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.camera.activity.PhotoActivity;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

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
    Bitmap mBitmap;
    String name = "";
    Handler handler;
    boolean loadError = false;
    private static AlertDialogUtil alertDialogUtil;

    // 位置管理
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        radioGroup.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        alertDialogUtil = new AlertDialogUtil(this);
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        List<PermissionItem> mList = new ArrayList<PermissionItem>();
        mList.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_phone));
        mList.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        mList.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "位置", R.drawable.permission_ic_location));
        mList.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取文件", R.drawable.permission_ic_storage));
        mList.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入文件", R.drawable.permission_ic_storage));

        HiPermission.create(MainActivity.this)
                .title("亲爱的用户")
                .permissions(mList)
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.green, getTheme()))//图标的颜色
                .animStyle(R.style.PermissionAnimScale)//设置动画
                .msg("此应用需要获取以下权限")
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.e("TAG", "close");
                    }

                    @Override
                    public void onFinish() {
                        //"所有权限申请完成"
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.black));
                        Toast.makeText(MainActivity.this, "所有权限申请完毕", Toast.LENGTH_SHORT).show();
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
                        radioGroup.setVisibility(View.VISIBLE);
                        header.setVisibility(View.GONE);
                        //设置自适应屏幕，两者合用
                        //将图片调整到适合webview的大小
                        webSettings.setUseWideViewPort(true);
                        // 缩放至屏幕的大小
                        webSettings.setLoadWithOverviewMode(true);
                        //关闭webview中缓存
                        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        //不使用缓存
                        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//                        //全屏设置
                        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                        //访问网页
                        webView.loadUrl(api + "?action=stream");
                        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
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
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                if (loadError != true) {
                                    webView.setVisibility(View.VISIBLE);
                                    imageView.setVisibility(View.GONE);
                                    linearLayout.setVerticalGravity(View.VISIBLE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                    }
                });

        initTime();
        initGPS();
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
        //设置定位信息
        //坐标位置改变，回调此监听方法
        LocationListener listener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭监听
//        locationManager.removeUpdates(locationListeners);
//        locationListeners = null;
    }


    @OnClick({R.id.rbCamera, R.id.rbVideo, R.id.rbAlbum, R.id.rbSetting})
    public void onClick(View view1) {
        switch (view1.getId()) {
            case R.id.rbCamera:
                if (webView.getVisibility() == View.GONE) {
                    Toast.makeText(this, "app未连接到设备,暂不能拍照", Toast.LENGTH_SHORT).show();
                    break;
                }
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
//                                saveBitmap(path,name,mBitmap);
                            saveImg(mBitmap, name, MainActivity.this);
                        }

                        @Override
                        public void cancel() {
                            webView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                        }

                        @Override
                        public void save(String name1) {
                            if (!name1.equals("")) {
                                name = name1 + ".jpg";
                            }
//                                saveBitmap(path, name, mBitmap);
                            saveImg(mBitmap, name, MainActivity.this);
                        }

                        @Override
                        public void checkName(String name1) {
                            if (!name1.equals("")) {
                                name = name1 + ".jpg";
                            }
//                                saveBitmap(path, name, mBitmap);
                            saveImg(mBitmap, name, MainActivity.this);
                        }
                    });
                } else {
                    System.out.println("bitmap is NULL!");
                }
                break;
            case R.id.rbVideo:
                break;
            case R.id.rbAlbum:
                Intent intent = new Intent(this, PhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.rbSetting:
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
     * 保存方法
     */
    public boolean saveImg(Bitmap bitmap, String name, Context context) {
        try {
            String sdcardPath = System.getenv("EXTERNAL_STORAGE");      //获得sd卡路径
            String dir = sdcardPath + "/LUKEImage/";                    //图片保存的文件夹名
            File file = new File(dir);                                 //已File来构建
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

}