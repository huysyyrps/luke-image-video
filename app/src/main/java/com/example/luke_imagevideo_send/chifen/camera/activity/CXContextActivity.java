package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.views.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CXContextActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Intent intent;
    List<String> dataList = new ArrayList<>();
    BaseRecyclerAdapter baseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dataList.add("钢、镍、铜制承压设备各级别熔化焊焊接接头允许的圆形缺陷点数");
        dataList.add("钢、镍、铜制承压设备各级别熔化焊焊接接头允许的条形缺陷长度");
        dataList.add("铝制承压设备各级别熔化焊焊接接头允许的圆形缺陷点数");
        dataList.add("钛及钛合金制承压设备各级别熔化焊焊接接头允许的圆形缺陷点数");
        dataList.add("钢、镍、铜制承压设备管子及压力管道外径D0>100mm时不加垫板单面焊未焊透的分级");
        dataList.add("钢、镍、铜制承压设备管子及压力管道外径D0≤100mm时不加垫板单面焊未焊透的分级");
        dataList.add("钢、镍、铜制承压设备管子及压力管道外径D0>100mm时根部内凹和根部咬边的分级");
        dataList.add("钢、镍、铜制承压设备管子及压力管道外径D0≤100mm时根部内凹和根部咬边的分级");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setAdapter();
    }

    /**
     * 添加适配器
     */
    private void setAdapter() {
        baseRecyclerAdapter = new BaseRecyclerAdapter<String>(this,R.layout.recyclerview_easy_item,dataList) {
            @Override
            public void convert(BaseViewHolder holder, String o) {
                holder.setText(R.id.textView,o);
                holder.setOnClickListener(R.id.linearLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(o.equals("钢、镍、铜制承压设备各级别熔化焊焊接接头允许的圆形缺陷点数")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","16");
                            startActivity(intent);
                        }else if(o.equals("钢、镍、铜制承压设备各级别熔化焊焊接接头允许的条形缺陷长度")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","17");
                            startActivity(intent);
                        }
                        else if(o.equals("铝制承压设备各级别熔化焊焊接接头允许的圆形缺陷点数")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","18");
                            startActivity(intent);
                        } else if(o.equals("钛及钛合金制承压设备各级别熔化焊焊接接头允许的圆形缺陷点数")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","19");
                            startActivity(intent);
                        } else if(o.equals("钢、镍、铜制承压设备管子及压力管道外径D0>100mm时不加垫板单面焊未焊透的分级")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","20");
                            startActivity(intent);
                        } else if(o.equals("钢、镍、铜制承压设备管子及压力管道外径D0≤100mm时不加垫板单面焊未焊透的分级")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","21");
                            startActivity(intent);
                        } else if(o.equals("钢、镍、铜制承压设备管子及压力管道外径D0>100mm时根部内凹和根部咬边的分级")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","22");
                            startActivity(intent);
                        }else if(o.equals("钢、镍、铜制承压设备管子及压力管道外径D0≤100mm时根部内凹和根部咬边的分级")){
                            intent = new Intent(CXContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","23");
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_context;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}