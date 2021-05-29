package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

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
    @BindView(R.id.llCSContext)
    LinearLayout llCSContext;
    @BindView(R.id.llCFContext)
    LinearLayout llCFContext;
    @BindView(R.id.llCXContext)
    LinearLayout llCXContext;

    Intent intent;
    @BindView(R.id.llDaily)
    LinearLayout llDaily;
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

    @OnClick({R.id.llCSContext, R.id.btnSure, R.id.llCFContext, R.id.llCXContext,R.id.llDaily})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llCSContext:
                intent = new Intent(this, CSContextActivity.class);
                startActivity(intent);
                break;
            case R.id.llCFContext:
                intent = new Intent(this, CFContextActivity.class);
                startActivity(intent);
                break;
            case R.id.llCXContext:
                intent = new Intent(this, CXContextActivity.class);
                startActivity(intent);
                break;
            case R.id.llDaily:
                intent = new Intent(this, DailyActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSure:
                String time = etTime.getText().toString();
                dataBean.setId(sharePreferencesUtils.getString(this,"id",""));
                dataBean.setDate(sharePreferencesUtils.getString(this,"date",""));
                dataBean.setMac(sharePreferencesUtils.getString(this,"mac",""));
                dataBean.setPower(sharePreferencesUtils.getString(this,"power",""));
                dataBean.setMode(sharePreferencesUtils.getString(this,"mode",""));
                dataBean.setIp(sharePreferencesUtils.getString(this,"ip",""));
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
                            SSHExcuteCommandHelper.writeBefor(address, "/write_data.sh", new SSHCallBack() {
                                @Override
                                public void confirm(String data) {
                                    if (data!=null){
                                        SSHExcuteCommandHelper.writeBefor(address, obj2, new SSHCallBack() {
                                            @Override
                                            public void confirm(String data) {
                                                Log.e("XXX",data);
//                                            new SSHExcuteCommandHelper(address).disconnect();
                                                Gson gson = new Gson();
                                                Setting setting = gson.fromJson(data,Setting.class);
                                                sharePreferencesUtils.setString(SettingActivity.this, "acdc", setting.getData().getAcdc());
                                                sharePreferencesUtils.setString(SettingActivity.this, "auto", setting.getData().getAuto());
                                                sharePreferencesUtils.setString(SettingActivity.this, "auto_time", setting.getData().getAuto_time());
                                                sharePreferencesUtils.setString(SettingActivity.this, "bw", setting.getData().getBw());
                                                sharePreferencesUtils.setString(SettingActivity.this, "id", setting.getData().getId());
                                                sharePreferencesUtils.setString(SettingActivity.this, "mac", setting.getData().getMac());
                                                sharePreferencesUtils.setString(SettingActivity.this, "power", setting.getData().getPower());
                                                sharePreferencesUtils.setString(SettingActivity.this, "ip", setting.getData().getIp());
                                                sharePreferencesUtils.setString(SettingActivity.this, "date", setting.getData().getDate());
                                                sharePreferencesUtils.setString(SettingActivity.this, "mode", setting.getData().getMode());
                                            }

                                            @Override
                                            public void error(String s) {
                                                Toast.makeText(SettingActivity.this, s, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void error(String s) {
                                    Toast.makeText(SettingActivity.this, s, Toast.LENGTH_SHORT).show();
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
                break;
        }
    }
}