package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.views.Header;

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
    @BindView(R.id.switchKG)
    Switch switchKG;
    @BindView(R.id.btnSure)
    Button btnSure;
    Intent intent;
    Setting setting = new Setting();
    String zjliu = "",hbguang = "",dldong = "",kaiguan = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setOnCheckedChangeListener();
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
                    setting.setJiaoliu("yes");
                    setting.setZhiliu("no");
                    zjliu = "0";
                }
            }
        });
        switchZL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchJL.setChecked(false);
                    setting.setJiaoliu("no");
                    setting.setZhiliu("yes");
                    zjliu = "1";
                }
            }
        });
        switchHG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchBG.setChecked(false);
                    setting.setHeiguang("yes");
                    setting.setBaiguang("no");
                    hbguang = "0";
                }
            }
        });
        switchBG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchHG.setChecked(false);
                    setting.setHeiguang("no");
                    setting.setBaiguang("yes");
                    hbguang = "1";
                }
            }
        });
        switchDD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchLD.setChecked(false);
                    setting.setDiandong("yes");
                    setting.setLiandong("no");
                    dldong = "0";
                }
            }
        });
        switchLD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchDD.setChecked(false);
                    setting.setDiandong("no");
                    setting.setLiandong("yes");
                    dldong = "1";
                }
            }
        });
        switchKG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setting.setKaiguan("yes");
                    kaiguan = "1";
                }
            }
        });
    }

    @OnClick(R.id.btnSure)
    public void onClick() {
        intent = new Intent();
        intent.putExtra("data",setting);
        setResult(Constant.TAG_ONE, intent);
        finish();
    }
}