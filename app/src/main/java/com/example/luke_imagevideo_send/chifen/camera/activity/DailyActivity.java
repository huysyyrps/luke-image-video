package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.bean.Daily;
import com.example.luke_imagevideo_send.chifen.camera.bean.Setting;
import com.example.luke_imagevideo_send.chifen.camera.module.DailyContract;
import com.example.luke_imagevideo_send.chifen.camera.presenter.DailyPresenter;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHDataExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.example.luke_imagevideo_send.http.dialog.ProgressHUD;
import com.example.luke_imagevideo_send.http.views.Header;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DailyActivity extends BaseActivity implements DailyContract.View {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRecyclerAdapter baseRecyclerAdapter;
    List<Setting> settingList;
    private String address = "";
    private DailyPresenter dailyPresenter;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dailyPresenter = new DailyPresenter(this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        progressHUD = ProgressHUD.show(DailyActivity.this);
        progressHUD.setLabel("获取日志中");
        getDaily();

    }

    public String readTxt(String path) {
        String str = "";
        try {
            File urlFile = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String mimeTypeLine = null;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + mimeTypeLine;
            }
            settingList = new Gson().fromJson(str, new TypeToken<List<Setting>>() {}.getType());
            if (settingList.size() != 0) {
                setAdapter();
            } else {
                AlertDialogUtil alertDialogUtil = new AlertDialogUtil(DailyActivity.this);
                alertDialogUtil.showSmallDialog("暂无操作日志");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public void setAdapter() {
        baseRecyclerAdapter = new BaseRecyclerAdapter<Setting>(DailyActivity.this, R.layout.activity_daily_item, settingList) {
            @Override
            public void convert(BaseViewHolder holder, final Setting setting) {
                if (setting.getData().getDate() != null) {
                    holder.setText(R.id.tvTime, checkDate(setting.getData().getDate()));
                    holder.setText(R.id.tvMax, setting.getData().getMac());
                    holder.setText(R.id.tvIp, setting.getData().getIp());
                }
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_daily;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM); //表单类型
        File file = new File(Environment.getExternalStorageDirectory() + "/LUKEDaily.txt");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestBody);//"imgfile"+i 后台接收图片流的参数名
        builder.addFormDataPart("company", "compName");
        builder.addFormDataPart("project", "project");
        builder.addFormDataPart("device", "device");
        builder.addFormDataPart("workpiece", "workName");
        builder.addFormDataPart("workpiecenum", "workpiecenum");
        List<MultipartBody.Part> parts = builder.build().parts();
        dailyPresenter.getDaily(parts);
    }

    public static String checkDate(String s) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        ParsePosition pos = new ParsePosition(0);
        Date d = sdf1.parse(s, pos);
        System.out.println(d);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
        return sdf2.format(d);
    }

    //获取设备基础信息
    public void getDaily() {
        try {
            address = new getIp().getConnectIp();
            if (address != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHDataExcuteCommandHelper.writeBefor(address, "s", new SSHCallBack() {
                            @Override
                            public void confirm(String data1) {
                                handler.sendEmptyMessage(Constant.TAG_ONE);
                            }

                            @Override
                            public void error(String s) {
                                handler.sendEmptyMessage(Constant.TAG_TWO);
                            }
                        });
                    }
                }).start();
            } else {
                Toast.makeText(this, "请检查设备是否连接到手机热点", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDaily(Daily daily) {
        Toast.makeText(this, daily.isResult() + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDailyMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TAG_ONE:
                    readTxt(Environment.getExternalStorageDirectory() + "/LUKEDaily.txt");
                    progressHUD.dismiss();
                    break;
                case Constant.TAG_TWO:
                    Toast.makeText(DailyActivity.this, "日志获取失败", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
            }
        }
    };
}