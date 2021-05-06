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

public class CSContextActivity extends BaseActivity {

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
        dataList.add("承压设备用板材中部检测区域质量分级");
        dataList.add("承压设备用板材边缘或剖口预定线两侧质量分级");
        dataList.add("复合板超声检测质量分级");
        dataList.add("锻件超声检测缺陷质量分级");
        dataList.add("单个缺陷的质量分级");
        dataList.add("由缺陷引起底波降低量的质量分级");
        dataList.add("直探头检测的质量分级");
        dataList.add("斜探头检测的质量分级");
        dataList.add("无缝钢管超声检测质量分级");
//        dataList.add("锅炉、压力容器本体焊接接头超声检测质量分级");
//        dataList.add("锅炉、压力容器管子环或纵向焊接接头质量分级");
        dataList.add("压力管道环向或纵向对接接头超声检测质量分级");
        dataList.add("堆焊层超声检测质量分级");
        dataList.add("铝和铝合金制及钛制承压设备对接接头质量分级");
        dataList.add("奥氏体不锈钢对接接头超声检测质量分级");
        dataList.add("钢锻件缺陷质量分级");
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
                        if(o.equals("承压设备用板材中部检测区域质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","0");
                            startActivity(intent);
                        }else if(o.equals("承压设备用板材边缘或剖口预定线两侧质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","1");
                            startActivity(intent);
                        }else if(o.equals("复合板超声检测质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","2");
                            startActivity(intent);
                        }else if(o.equals("锻件超声检测缺陷质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","3");
                            startActivity(intent);
                        }else if(o.equals("单个缺陷的质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","4");
                            startActivity(intent);
                        }else if(o.equals("由缺陷引起底波降低量的质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","5");
                            startActivity(intent);
                        }else if(o.equals("直探头检测的质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","6");
                            startActivity(intent);
                        }else if(o.equals("斜探头检测的质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","7");
                            startActivity(intent);
                        }else if(o.equals("无缝钢管超声检测质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","8");
                            startActivity(intent);
                        }else if(o.equals("压力管道环向或纵向对接接头超声检测质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","9");
                            startActivity(intent);
                        }else if(o.equals("堆焊层超声检测质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","10");
                            startActivity(intent);
                        }else if(o.equals("铝和铝合金制及钛制承压设备对接接头质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","11");
                            startActivity(intent);
                        }else if(o.equals("奥氏体不锈钢对接接头超声检测质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","12");
                            startActivity(intent);
                        }else if(o.equals("钢锻件缺陷质量分级")){
                            intent = new Intent(CSContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","13");
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