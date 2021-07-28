package com.example.luke_imagevideo_send.cehouyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.utils.time_select.CustomDatePickerMin;
import com.example.luke_imagevideo_send.http.views.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    Intent intent;
    private CustomDatePickerMin customDatePickerMin;
    List<String> dataList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dataList.add("数据管理");
        dataList.add("时间设置");
        dataList.add("凤鸣设置");
        dataList.add("阀值设置");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setAdapter();
    }

    private void setAdapter() {
        baseRecyclerAdapter = new BaseRecyclerAdapter<String>(MainMenuActivity.this, R.layout.recyclerview_easy_item, dataList) {
            @Override
            public void convert(BaseViewHolder holder, final String o) {
                holder.setText( R.id.textView, o);
                holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (o.equals("数据管理")){
                            intent = new Intent(MainMenuActivity.this,ValueActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (o.equals("时间设置")){
                            customDatePickerMin = new CustomDatePickerMin(MainMenuActivity.this, new CustomDatePickerMin.ResultHandler() {
                                @Override
                                public void handle(String time) {
                                    // 回调接口，获得选中的时间
                                    Toast.makeText(MainMenuActivity.this, time, Toast.LENGTH_SHORT).show();
                                }
                                // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                            }, "2000-01-01 00:00", "2030-01-01 00:00");
                            // 不显示时和分
                            customDatePickerMin.showSpecificTime(true);
                            // 不允许循环滚动
                            customDatePickerMin.setIsLoop(false);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                            String now = sdf.format(new Date());
                            customDatePickerMin.show(now);
                        }
//                            intent = new Intent(MainMenuActivity.this,ValueActivity.class);
//                            startActivity(intent);
//                            finish();
                        if (o.equals("凤鸣设置")){
                            intent = new Intent(MainMenuActivity.this,ValueActivity.class);
                            startActivity(intent);
                        }
                        if (o.equals("阀值设置")){
                            intent = new Intent(MainMenuActivity.this,ThresholdActivity.class);
                            startActivityForResult(intent, Constant.TAG_ONE);
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.TAG_ONE:
                if (data!=null){
                    String top = data.getStringExtra("top");
                    String button = data.getStringExtra("button");
                    Toast.makeText(this, top+"---"+button, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_menu;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}