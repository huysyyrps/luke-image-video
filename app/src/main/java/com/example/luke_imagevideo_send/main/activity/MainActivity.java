package com.example.luke_imagevideo_send.main.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.camera.activity.PhotoActivity;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.views.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    Bitmap mBitmap;
    String name = "";
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static AlertDialogUtil alertDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        radioGroup.setVisibility(View.GONE);
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
                        });
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
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

    @OnClick({R.id.rbCamera, R.id.rbVideo, R.id.rbAlbum, R.id.rbSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbCamera:
                name = getNowDate();
                //访问网页
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBitmapFromURL(api + "?action=snapshot");
                    }
                }).start();
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

    public void getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            mBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            Message message = new Message();
            message.what = Constant.TAG_ONE;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            Toast.makeText(this, "拍摄失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.TAG_ONE:
                    webView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(mBitmap);

                    if (mBitmap != null) {
                        alertDialogUtil.showImageDialog(new AlertDialogCallBack() {

                            @Override
                            public void confirm(String name1) {
                                if (!name1.equals("")) {
                                    name = name1 + ".jpg";
                                }
//                                saveBitmap(path,name,mBitmap);
                                saveImg(mBitmap,name,MainActivity.this);
                            }

                            @Override
                            public void cancel() {
                            }

                            @Override
                            public void save(String name1) {
                                if (!name1.equals("")) {
                                    name = name1 + ".jpg";
                                }
//                                saveBitmap(path, name, mBitmap);
                                saveImg(mBitmap,name,MainActivity.this);
                            }

                            @Override
                            public void checkName(String name1) {
                                if (!name1.equals("")) {
                                    name = name1 + ".jpg";
                                }
//                                saveBitmap(path, name, mBitmap);
                                saveImg(mBitmap,name,MainActivity.this);
                            }
                        });
                    }
            }
        }
    };
}