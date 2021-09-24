package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.util.DownloadManager;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.chifen.magnetic.util.MyCallBack;
import com.example.luke_imagevideo_send.chifen.magnetic.util.RegionalChooseUtil;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.dialog.ProgressHUD;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.Header;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.tvZS)
    TextView tvZS;
    @BindView(R.id.llZSSetting)
    LinearLayout llZSSetting;
    @BindView(R.id.tvXS)
    TextView tvXS;
    @BindView(R.id.llXSSetting)
    LinearLayout llXSSetting;
    @BindView(R.id.llPut)
    LinearLayout llPut;
    private String address = "";
    Setting setting = new Setting();
    SharePreferencesUtils sharePreferencesUtils;
    Setting.DataBean dataBean = new Setting.DataBean();
    List<String> listZS = new ArrayList<>();
    private KProgressHUD progressHUD;

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
        initListData();
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

    private void initListData() {
        listZS.add("20");
        listZS.add("25");
        listZS.add("30");
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

    @OnClick({R.id.llCSContext, R.id.btnSure, R.id.llCFContext, R.id.llCXContext, R.id.llDaily, R.id.llZSSetting, R.id.llXSSetting,R.id.llPut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llCSContext:
                intent = new Intent(this, CSContextActivity.class);
                intent.putExtra("tag", "超声文档");
                startActivity(intent);
                break;
            case R.id.llCFContext:
                intent = new Intent(this, CFContextActivity.class);
                intent.putExtra("tag", "磁粉文档");
                startActivity(intent);
                break;
            case R.id.llCXContext:
                intent = new Intent(this, CXContextActivity.class);
                intent.putExtra("tag", "射线文档");
                startActivity(intent);
                break;
            case R.id.llDaily:
                intent = new Intent(this, DailyActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSure:
                String time = etTime.getText().toString();
                dataBean.setId(sharePreferencesUtils.getString(this, "id", ""));
                dataBean.setDate(sharePreferencesUtils.getString(this, "date", ""));
                dataBean.setMac(sharePreferencesUtils.getString(this, "mac", ""));
                dataBean.setPower(sharePreferencesUtils.getString(this, "power", ""));
                dataBean.setMode(sharePreferencesUtils.getString(this, "mode", ""));
                dataBean.setIp(sharePreferencesUtils.getString(this, "ip", ""));
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
                                    if (data != null) {
                                        SSHExcuteCommandHelper.writeBefor(address, obj2, new SSHCallBack() {
                                            @Override
                                            public void confirm(String data) {
                                                Log.e("XXX", data);
//                                            new SSHExcuteCommandHelper(address).disconnect();
                                                Gson gson = new Gson();
//                                                Setting setting = gson.fromJson(data,Setting.class);
//                                                sharePreferencesUtils.setString(SettingActivity.this, "acdc", setting.getData().getAcdc());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "auto", setting.getData().getAuto());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "auto_time", setting.getData().getAuto_time());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "bw", setting.getData().getBw());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "id", setting.getData().getId());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "mac", setting.getData().getMac());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "power", setting.getData().getPower());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "ip", setting.getData().getIp());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "date", setting.getData().getDate());
//                                                sharePreferencesUtils.setString(SettingActivity.this, "mode", setting.getData().getMode());
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
            case R.id.llZSSetting:
                RegionalChooseUtil.initJsonData(SettingActivity.this,"frames");
                RegionalChooseUtil.showPickerView(SettingActivity.this, new MyCallBack() {
                    @Override
                    public void callBack(Object object) {
                        ShowDialog("uci set mjpg-streamer.core.fps=" + object.toString(), "uci commit", "/etc/init.d/mjpg-streamer restart");
                        new SharePreferencesUtils().setString(SettingActivity.this, "frames", object.toString());
                    }
                });

                break;
            case R.id.llXSSetting:
                RegionalChooseUtil.initJsonData(SettingActivity.this,"resolving");
                RegionalChooseUtil.showPickerView(SettingActivity.this, new MyCallBack() {
                    @Override
                    public void callBack(Object object) {
                        ShowDialog("uci set mjpg-streamer.core.resolution=" + object.toString(), "uci commit", "/etc/init.d/mjpg-streamer restart");
                        new SharePreferencesUtils().setString(SettingActivity.this, "resolving", object.toString());
                    }
                });
                break;
            case R.id.llPut:
                String url = "https://pic4.zhimg.com/03b2d57be62b30f158f48f388c8f3f33_b.png";
                DownloadManager.download(url, Environment.getExternalStorageDirectory()+"/LUKEUpdata", "test.mp4", new DownloadManager.OnDownloadListener() {
                    @Override
                    public void onSuccess(File file) {

                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onFail() {

                    }
                });
//                progressHUD = ProgressHUD.show(SettingActivity.this);
//                progressHUD.setLabel("设置中...");
//                try {
//                    address = new getIp().getConnectIp();
//                    if (address != null) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                SSHDataExcuteCommandHelper.getBefor(address, "s", new SSHCallBack() {
//                                    @Override
//                                    public void confirm(String data1) {
//                                        handler.sendEmptyMessage(Constant.TAG_ONE);
//                                    }
//
//                                    @Override
//                                    public void error(String s) {
//                                        handler.sendEmptyMessage(Constant.TAG_TWO);
//                                    }
//                                });
//                            }
//                        }).start();
//                    } else {
//                        Toast.makeText(this, "请检查设备是否连接到手机热点", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
        }
    }

    private void ShowDialog(String data1, String data2, String data3) {
        try {
            address = new getIp().getConnectIp();
            progressHUD = ProgressHUD.show(SettingActivity.this);
            progressHUD.setLabel("设置中");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SSHExcuteCommandHelper.writeBefor(address, data1, new SSHCallBack() {
                        @Override
                        public void confirm(String data) {
                            SSHExcuteCommandHelper.writeBefor(address, data2, new SSHCallBack() {
                                @Override
                                public void confirm(String data) {
                                    SSHExcuteCommandHelper.writeBefor(address, data3, new SSHCallBack() {
                                        @Override
                                        public void confirm(String data) {
                                            handler.sendEmptyMessage(Constant.TAG_ONE);
                                        }

                                        @Override
                                        public void error(String s) {
                                            handler.sendEmptyMessage(Constant.TAG_TWO);
                                        }
                                    });
                                }

                                @Override
                                public void error(String s) {
                                    handler.sendEmptyMessage(Constant.TAG_TWO);
                                }
                            });
                        }

                        @Override
                        public void error(String s) {
                            handler.sendEmptyMessage(Constant.TAG_TWO);
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TAG_ONE:
                    Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                    finish();
                    break;
                case Constant.TAG_TWO:
                    Toast.makeText(SettingActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
            }
        }
    };
}