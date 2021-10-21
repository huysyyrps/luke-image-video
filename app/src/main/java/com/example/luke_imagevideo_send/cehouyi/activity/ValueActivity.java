package com.example.luke_imagevideo_send.cehouyi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerPositionAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.DialogCallBack;
import com.example.luke_imagevideo_send.http.views.Header;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ValueActivity extends BaseActivity {
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    String unit = "";
    List<String> valueList = new ArrayList<>();
    BaseRecyclerPositionAdapter baseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 注册订阅者
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        unit = intent.getStringExtra("unit");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 11));
        setSpinnerData();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_value;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(List<String> myValueList) {
        this.valueList = myValueList;
    }


    //设置spinner数据
    public void setSpinnerData() {
        baseRecyclerAdapter = new BaseRecyclerPositionAdapter<String>(ValueActivity.this, R.layout.swiperrecycler_item, valueList) {
            @Override
            public void convert(BaseViewHolder holder, final String o, int position) {
                if (position == 0) {
                    holder.setText(R.id.tv, 1 + "");
                } else if (position % 11 == 0) {
                    holder.setText(R.id.tv, position / 11 + 1 + "");
                } else {
                    if (unit.equals("MM")){
                        DecimalFormat df = new DecimalFormat("###.00");
                        String FTopData = "";
                        if (df.format((double) Long.parseLong(o, 16) / 1000).substring(0, 1).equals("\\.")) {
                            FTopData = "0" + (double) Long.parseLong(o, 16) / 1000 + "";
                        } else {
                            FTopData = (double) Long.parseLong(o, 16) / 1000 + "";
                        }
                        if (FTopData.split("\\.")[1].length() >= 2) {
                            String data = FTopData.substring(0, FTopData.split("\\.")[0].length() + 3);
                            holder.setText(R.id.tv,data);
                        } else {
                            holder.setText(R.id.tv,FTopData+"0");
                        }
                    }
                    if (unit.equals("IN")) {
                        String FTopData = "";
                        DecimalFormat df = new DecimalFormat("###.000");
                        if (df.format((double) Long.parseLong(o, 16) / 25400).substring(0, 1).equals(".")) {
                            FTopData = "0" + (double) Long.parseLong(o, 16) / 25400 + "";
                        } else {
                            FTopData = (double) Long.parseLong(o, 16) / 25400 + "";
                        }
                        if (FTopData.split("\\.")[1].length() > 3) {
                            String s = FTopData.split("\\.")[0];
                            if (s.equals("00")) {
                                String data = FTopData.substring(1, FTopData.split("\\.")[0].length() + 4);
                                holder.setText(R.id.tv,data);
                            } else {
                                String data = FTopData.substring(0, FTopData.split("\\.")[0].length() + 4);
                                holder.setText(R.id.tv,data);
                            }
                        } else {
                            holder.setText(R.id.tv,FTopData+"0");
                        }
                    }
                }

                holder.setOnClickListener(R.id.llItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position % 11 == 0) {
                            new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除这组数据吗", new DialogCallBack() {
                                @Override
                                public void confirm(String name, Dialog dialog) {
                                    Log.e("ValueActivity", position + "");
                                    int start = 0;
                                    if (position+10<=valueList.size()){
                                        start = position +10;
                                    }else {
                                        start = position +valueList.size()%11-1;
                                    }
                                    int end = position;
                                    Log.e("ValueActivity",start+"--"+end);
                                    for (int i = start; i >= end; i--) {
                                        valueList.remove(i);
                                    }
                                    baseRecyclerAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void cancel() {

                                }

                            });
                        } else {
                            new AlertDialogUtil(ValueActivity.this).showSmallDialog("您确定要删除这条数据吗", new DialogCallBack() {
                                @Override
                                public void confirm(String name, Dialog dialog) {
                                    if (position%11==1){
                                        valueList.remove(position) ;
                                        valueList.remove(position-1) ;
                                    }else {
                                        valueList.set(position,"") ;
                                    }
                                    baseRecyclerAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void cancel() {

                                }

                            });
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btnSave, R.id.btnCancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
//                myValueList.set(positionNum, valueList);
                break;
            case R.id.btnCancle:
                finish();
                break;
        }
    }
}