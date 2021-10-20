package com.example.luke_imagevideo_send.main.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.activity.SendSelectActivity;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.main.bean.Defined;
import com.example.luke_imagevideo_send.main.module.DefinedContract;
import com.example.luke_imagevideo_send.main.presenter.DefinedPresenter;
import com.huawei.hms.hmsscankit.OnLightVisibleCallBack;
import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;

import butterknife.ButterKnife;

public class DefinedActivity extends BaseActivity implements DefinedContract.View {
    private FrameLayout frameLayout;
    private RemoteView remoteView;
    private ImageView flushBtn;
    int mScreenWidth;
    int mScreenHeight;
    final int SCAN_FRAME_SIZE = 240;
    DefinedPresenter definedPresenter;
    SharePreferencesUtils sharePreferencesUtils;
    String tag = "first";

    private int[] img = {R.drawable.flashlight_on, R.drawable.flashlight_off};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // Bind the camera preview screen.
        sharePreferencesUtils = new SharePreferencesUtils();
        definedPresenter = new DefinedPresenter(this,this);
        frameLayout = findViewById(R.id.rim);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);
        Rect rect = new Rect();
        rect.left = mScreenWidth / 2 - scanFrameSize / 2;
        rect.right = mScreenWidth / 2 + scanFrameSize / 2;
        rect.top = mScreenHeight / 2 - scanFrameSize / 2;
        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2;
        remoteView = new RemoteView.Builder().setContext(this).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build();
        flushBtn = findViewById(R.id.flush_btn);
        remoteView.setOnLightVisibleCallback(new OnLightVisibleCallBack() {
            @Override
            public void onVisibleChanged(boolean visible) {
                if(visible){
                    flushBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {
                if (tag.equals("first")){
                    if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                        if (result[0].getOriginalValue()!=null){
                            String[] data=result[0].getOriginalValue().split("/");
                            sharePreferencesUtils.setString(DefinedActivity.this, "max", data[2]);
                            definedPresenter.getDefined(data[0]);
                            startActivity(new Intent(DefinedActivity.this, SendSelectActivity.class));
                            finish();
                            tag = "second";
                            return;
                        }
                    }
                }
            }
        });
        remoteView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(remoteView, params);
        setFlashOperation();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_defined;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    private void setFlashOperation() {
        flushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remoteView.getLightStatus()) {
                    remoteView.switchLight();
                    flushBtn.setImageResource(img[1]);
                } else {
                    remoteView.switchLight();
                    flushBtn.setImageResource(img[0]);
                }
            }
        });
    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        remoteView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        remoteView.onStop();
    }

    @Override
    public void setDefined(Defined defined) {
        if (defined.getResult()==null){
            Toast.makeText(this, "派工单为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String company = defined.getResult().getCompanyName();
        String deviceName = defined.getResult().getDeviceName();
        String deviceCode = defined.getResult().getDeviceCode();
        sharePreferencesUtils.setString(DefinedActivity.this, "company", company);
        sharePreferencesUtils.setString(DefinedActivity.this, "deviceName", deviceName);
        sharePreferencesUtils.setString(DefinedActivity.this, "deviceCode", deviceCode);
        startActivity(new Intent(this, SendSelectActivity.class));
        finish();
    }

    @Override
    public void setDefinedMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}