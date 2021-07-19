package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.activity.MainCHYActivity;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.chifen.magnetic.view.RecyclerViewDelegate;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.DialogCallBack;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.StatusBarUtils;
import com.example.luke_imagevideo_send.main.activity.DefinedActivity;
import com.google.gson.Gson;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.SweetSheet;

import java.util.ArrayList;

/**
 * 磁粉检测上传方式选择页
 */
public class SendSelectActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private ImageView ivRight;
    private TextView tvHeader;
    private EditText etProject, etWorkName, etWorkCode;
    //富有动感的Sheet弹窗
    private SweetSheet sheet;
    Intent intent;
    private String address = "";
    private static boolean isExit = false;
    private static AlertDialogUtil alertDialogUtil;
    SharePreferencesUtils sharePreferencesUtils;
    MediaProjectionManager projectionManager;
    private WifiManager mWifiManager;
    private String sid = "", pwd = "";
    private WifiManager wifiMgr;
    private WifiManager.LocalOnlyHotspotReservation mReservation;
    //推出程序
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //推出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            alertDialogUtil.showDialog("您确定要退出程序吗", new AlertDialogCallBack() {

                @Override
                public void confirm(String name) {
                    finish();
                }

                @Override
                public void cancel() {

                }

                @Override
                public void save(String name) {

                }

                @Override
                public void checkName(String name) {

                }
            });
            mHandler.sendEmptyMessageDelayed(0, 1500);
        } else {
            finish();
            System.exit(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_select);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        alertDialogUtil = new AlertDialogUtil(this);
        sharePreferencesUtils = new SharePreferencesUtils();
        sid = sharePreferencesUtils.getString(this, "sid", "");
        pwd = sharePreferencesUtils.getString(this, "pwd", "");
        new StatusBarUtils().setWindowStatusBarColor(SendSelectActivity.this, R.color.color_bg_selected);
        relativeLayout = findViewById(R.id.relativeLayout);
        tvHeader = findViewById(R.id.tv_tittle);
        etProject = findViewById(R.id.etProject);
        etWorkName = findViewById(R.id.etWorkName);
        etWorkCode = findViewById(R.id.etWorkCode);
        etWorkCode = findViewById(R.id.etWorkCode);
        ivRight = findViewById(R.id.ivRight);

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendSelectActivity.this, DefinedActivity.class);
                startActivity(intent);
            }
        });
        etProject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etProject.getText().length() >= 15) {
                    Toast.makeText(SendSelectActivity.this, "当前项最多可以输入15字", Toast.LENGTH_SHORT).show();
                }
            }
        });
        etWorkCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etWorkCode.getText().length() > 15) {
                    Toast.makeText(SendSelectActivity.this, "当前项最多可以输入15字", Toast.LENGTH_SHORT).show();
                }
            }
        });
        etWorkName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etWorkName.getText().length() > 15) {
                    Toast.makeText(SendSelectActivity.this, "当前项最多可以输入15字", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initData();
//        getNewData();
        alertDialogUtil.showWifiSetting(this,"luke_office","42D63C0496C3", new DialogCallBack() {
            @Override
            public void confirm(String data,Dialog dialog) {
                Intent intent =  new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                startActivity(intent);
            }

            @Override
            public void cancel() {

            }
        });
    }

    //设置SweetSheet上的数据
    public void initData() {

        ArrayList<MenuEntity> list = new ArrayList<>();
        //添加测试数据
        MenuEntity bean1 = new MenuEntity();
        bean1.title = "上传方式";

        MenuEntity bean2 = new MenuEntity();
        bean2.iconId = R.drawable.ic_bendi;
        bean2.title = "本地存储";

        MenuEntity bean3 = new MenuEntity();
        bean3.iconId = R.drawable.ic_shishi;
        bean3.title = "实时上传";

        list.add(bean1);
        list.add(bean2);
        list.add(bean3);

        //声明SweetSheet 控件,根据 layout 确认位置
        sheet = new SweetSheet(relativeLayout);
        //设置数据源 (数据源支持设置 list 数组,也支持从menu 资源中获取)
        sheet.setMenuList(list);
        //根据设置不同的 Delegate 来显示不同的风格
        sheet.setDelegate(new RecyclerViewDelegate(true, this));
//        sheet.setDelegate(new ViewPagerDelegate(2));
        //根据设置不同Effect来设置背景效果:BlurEffect 模糊效果.DimEffect 变暗效果,NoneEffect 没有效果
        sheet.setBackgroundEffect(new DimEffect(8));
        //设置点击事件
        sheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
            @Override
            public boolean onItemClick(int position, MenuEntity menuEntity) {
                //根据返回值, true 会关闭 SweetSheet ,false 则不会
                sharePreferencesUtils.setString(SendSelectActivity.this, "project", etProject.getText().toString());
                sharePreferencesUtils.setString(SendSelectActivity.this, "workName", etWorkName.getText().toString());
                sharePreferencesUtils.setString(SendSelectActivity.this, "workCode", etWorkCode.getText().toString());
                if (etProject.getText().toString().trim().equals("")) {
                    Toast.makeText(SendSelectActivity.this, "请输入工程名称", Toast.LENGTH_SHORT).show();
                } else if (etWorkCode.getText().toString().trim().equals("")) {
                    Toast.makeText(SendSelectActivity.this, "请输入工件名称", Toast.LENGTH_SHORT).show();
                } else if (etWorkName.getText().toString().trim().equals("")) {
                    Toast.makeText(SendSelectActivity.this, "请输入工件编号", Toast.LENGTH_SHORT).show();
                } else {
                    if (menuEntity.title.equals("本地存储")) {
                        sharePreferencesUtils.setString(SendSelectActivity.this, "sendSelect", "本地存储");
                        intent = new Intent(SendSelectActivity.this, MainActivity.class);
                        intent.putExtra("project", etProject.getText().toString());
                        intent.putExtra("etWorkName", etWorkName.getText().toString());
                        intent.putExtra("etWorkCode", etWorkCode.getText().toString());
                        etProject.setText("");
                        etWorkCode.setText("");
                        etWorkName.setText("");
                        startActivity(intent);
                    } else if (menuEntity.title.equals("实时上传")) {
//                    intent = new Intent(SendSelectActivity.this, MainOutCHYActivity.class);
                        intent = new Intent(SendSelectActivity.this, MainCHYActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
        sheet.toggle();
    }

    //获取设备基础信息
    public void getNewData() {
        try {
            address = new getIp().getConnectIp();
            new Thread(new Runnable() {
                @Override
                public void run() {
//                            cat /data.json  /write_data.sh
                    SSHExcuteCommandHelper.writeBefor(address, "cat /data.json", new SSHCallBack() {
                        @Override
                        public void confirm(String data) {
                            if (data != null && !data.equals("\n") && !data.equals("")) {
                                Gson gson = new Gson();
//                                Setting setting = gson.fromJson(data, Setting.class);
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "acdc", setting.getData().getAcdc());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "auto", setting.getData().getAuto());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "auto_time", setting.getData().getAuto_time());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "bw", setting.getData().getBw());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "id", setting.getData().getId());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "mac", setting.getData().getMac());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "power", setting.getData().getPower());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "ip", setting.getData().getIp());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "date", setting.getData().getDate());
//                                sharePreferencesUtils.setString(SendSelectActivity.this, "mode", setting.getData().getMode());
                            }
                        }

                        @Override
                        public void error(String s) {
//                            Toast.makeText(SendSelectActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}