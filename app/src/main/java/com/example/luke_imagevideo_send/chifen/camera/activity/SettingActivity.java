package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.Header;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.switchJL)
    Switch switchJL;
    @BindView(R.id.switchZL)
    Switch switchZL;
    @BindView(R.id.switchHG)
    Switch switchHG;
    @BindView(R.id.switchBG)
    Switch switchBG;
    @BindView(R.id.switchDD)
    Switch switchDD;
    @BindView(R.id.switchLD)
    Switch switchLD;
    @BindView(R.id.btnSure)
    Button btnSure;
    @BindView(R.id.etTime)
    EditText etTime;

    Intent intent;
    private String address = "";
    Setting setting = new Setting();
    SharePreferencesUtils sharePreferencesUtils;
    Setting.DataBean dataBean = new Setting.DataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        ButterKnife.bind(this);
        setOnCheckedChangeListener();
        sharePreferencesUtils = new SharePreferencesUtils();
        String acdc = sharePreferencesUtils.getString(this, "acdc", "");
        String auto = sharePreferencesUtils.getString(this, "auto", "");
        String auto_time = sharePreferencesUtils.getString(this, "auto_time", "");
        String bw = sharePreferencesUtils.getString(this, "bw", "");
        if (acdc.equals("0")) {
            switchJL.setChecked(true);
            switchZL.setChecked(false);
        } else if (acdc.equals("1")) {
            switchJL.setChecked(false);
            switchZL.setChecked(true);
        }

        if (auto.equals("0")) {
            switchDD.setChecked(true);
            switchLD.setChecked(false);
        } else if (acdc.equals("1")) {
            switchDD.setChecked(false);
            switchLD.setChecked(true);
        }

        if (bw.equals("0")) {
            switchHG.setChecked(true);
            switchBG.setChecked(false);
        } else if (acdc.equals("1")) {
            switchHG.setChecked(false);
            switchBG.setChecked(true);
        }

        etTime.setText(auto_time);

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    /**
     * 点击事件
     */
    private void setOnCheckedChangeListener() {
        switchJL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchZL.setChecked(false);
                    dataBean.setAcdc("0");
                }
            }
        });
        switchZL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchJL.setChecked(false);
                    dataBean.setAcdc("1");
                }
            }
        });
        switchHG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchBG.setChecked(false);
                    dataBean.setBw("0");
                }
            }
        });
        switchBG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchHG.setChecked(false);
                    dataBean.setBw("1");
                }
            }
        });
        switchDD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchLD.setChecked(false);
                    dataBean.setAuto("0");
                }
            }
        });
        switchLD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchDD.setChecked(false);
                    dataBean.setAuto("1");
                }
            }
        });
    }

    @OnClick(R.id.btnSure)
    public void onClick() {
        String time = etTime.getText().toString();
        dataBean.setAuto_time(time);
        setting.setData(dataBean);
        Gson gson = new Gson();
        String obj2 = gson.toJson(setting);
        try {
            address = new getIp().getConnectIp();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //发送设置数据
                    SSHExcuteCommandHelper.main(address, "cat > /date.json <<EOF", new SSHCallBack() {
                        @Override
                        public void confirm(String data) {
                            Gson gson = new Gson();
                            Setting setting = gson.fromJson(data, Setting.class);
                            new SSHExcuteCommandHelper(address).disconnect();
                            //发送结束指令
                            SSHExcuteCommandHelper.main(address, obj2+"EOF", new SSHCallBack() {
                                @Override
                                public void confirm(String data) {
                                    Gson gson = new Gson();
                                    Setting setting = gson.fromJson(data, Setting.class);
                                    new SSHExcuteCommandHelper(address).disconnect();
                                }
                            });
                            //重新读取数据，保存到sp
                            SSHExcuteCommandHelper.readAgain(address, SettingActivity.this, new SSHCallBack() {
                                @Override
                                public void confirm(String data) {
                                    Gson gson = new Gson();
                                    Setting setting = gson.fromJson(data, Setting.class);
                                    new SSHExcuteCommandHelper(address).disconnect();
                                }
                            });
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        intent = new Intent();
//        intent.putExtra("data", setting);
//        setResult(Constant.TAG_ONE, intent);
//        finish();
    }
}