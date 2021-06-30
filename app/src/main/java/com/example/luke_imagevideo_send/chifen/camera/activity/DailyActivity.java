package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.os.Bundle;
import android.os.Environment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.bean.Setting;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.views.Header;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class DailyActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRecyclerAdapter baseRecyclerAdapter;
    List<Setting> settingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        readTxt(Environment.getExternalStorageDirectory() + "/test.txt");
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
            settingList = new Gson().fromJson(str, new TypeToken<List<Setting>>() {
            }.getType());
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

    }

    public static String checkDate(String s){
        SimpleDateFormat sdf1=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        ParsePosition pos=new ParsePosition(0);
        Date d=sdf1.parse(s,pos);
        System.out.println(d);
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
        return sdf2.format(d);
    }

}