package com.example.luke_imagevideo_send;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luke_imagevideo_send.test.AutoFitTextureView;
import com.example.luke_imagevideo_send.test.CameraController;
import com.example.luke_imagevideo_send.test.CameraUtils;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";
    private SurfaceView surfaceView;
    private CameraUtils cameraUtils;
    private String path, name;
    private ImageView btn;
    private ImageView camera;
    private ImageView change;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        Log.d(TAG, "onCreate: ");


        btn = (ImageView) findViewById(R.id.btn);
        camera = (ImageView) findViewById(R.id.camera);
        change = (ImageView) findViewById(R.id.change);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        cameraUtils = new CameraUtils();
        cameraUtils.create(surfaceView, this);
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        name = "Video";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x == 0) {
                    // cameraUtils.changeCamera();
                    cameraUtils.startRecord(path, name);
//                    btn.setImageResource(R.drawable.video);
                    x = 1;
                } else if (x == 1) {
                    cameraUtils.stopRecord();
//                    btn.setImageResource(R.drawable.video1);
                    x=0;
                }

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtils.takePicture(path, "name.png");
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtils.changeCamera();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
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
}