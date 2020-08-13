package com.example.luke_imagevideo_send.main.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class TestActivity1 extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvStart;
    private TextView mTvEnd;

    private TextView mTvTime;

    private int REQUEST_CODE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        CommonUtil.init(this);
        PermissionUtils.checkPermission(this);
        mTvStart = findViewById(R.id.tv_start);
        mTvStart.setOnClickListener(this);

        mTvTime = findViewById(R.id.tv_record_time);

        mTvEnd = findViewById(R.id.tv_end);
        mTvEnd.setOnClickListener(this);

        startScreenRecordService();

    }

    private ServiceConnection mServiceConnection;

    /**
     * 开启录制 Service
     */
    private void startScreenRecordService(){

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ScreenRecordService.RecordBinder recordBinder = (ScreenRecordService.RecordBinder) service;
                ScreenRecordService screenRecordService = recordBinder.getRecordService();
                ScreenUtil.setScreenService(screenRecordService);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent(this, ScreenRecordService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        ScreenUtil.addRecordListener(recordListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int temp : grantResults) {
            if (temp == PERMISSION_DENIED) {
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("申请权限").setMessage("这些权限很重要").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.show(TestActivity1.this, "取消");
                    }
                }).setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + TestActivity1.this.getPackageName()));
                        TestActivity1.this.startActivity(intent);
                    }
                }).create();
                dialog.show();
                break;
            }
        }
    }

    private ScreenUtil.RecordListener recordListener = new ScreenUtil.RecordListener() {
        @Override
        public void onStartRecord() {

        }

        @Override
        public void onPauseRecord() {

        }

        @Override
        public void onResumeRecord() {

        }

        @Override
        public void onStopRecord(String stopTip) {
            ToastUtils.show(TestActivity1.this,stopTip);
        }

        @Override
        public void onRecording(String timeTip) {
            mTvTime.setText(timeTip);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            try {
                ScreenUtil.setUpData(resultCode,data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.show(this,"拒绝录屏");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_start:{
                ScreenUtil.startScreenRecord(this,REQUEST_CODE);
                break;
            }
            case R.id.tv_end:{
                ScreenUtil.stopScreenRecord(this);
                break;
            }
        }

    }
}