package com.example.luke_imagevideo_send;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.camera.CameraUtils;

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
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private static final String TAG = "PlayActivity";
    private CameraUtils cameraUtils;
    private String path, name;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        surfaceView.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
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
                        cameraUtils = new CameraUtils();
                        cameraUtils.create(surfaceView, MainActivity.this);
                        surfaceView.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.VISIBLE);
                        header.setVisibility(View.GONE);
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        cameraUtils.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        cameraUtils.destroy();
    }

    @OnClick({R.id.rbCamera, R.id.rbVideo, R.id.rbAlbum, R.id.rbSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbCamera:
                cameraUtils.takePicture(path, getNowDate()+".png");
                break;
            case R.id.rbVideo:
                if (x == 0) {
                    // cameraUtils.changeCamera();
                    cameraUtils.startRecord(path, getNowDate());
//                    btn.setImageResource(R.drawable.video);
                    x = 1;
                } else if (x == 1) {
                    cameraUtils.stopRecord();
//                    btn.setImageResource(R.drawable.video1);
                    x=0;
                }
                break;
            case R.id.rbAlbum:
                break;
            case R.id.rbSetting:
                cameraUtils.changeCamera();
                break;
        }
    }

    /**
     * 获取当前时间,用来给文件夹命名
     */
    private String getNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
}