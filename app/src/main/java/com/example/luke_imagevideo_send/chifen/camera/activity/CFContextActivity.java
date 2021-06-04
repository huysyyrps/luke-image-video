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

public class CFContextActivity extends BaseActivity {

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
        Intent intent = getIntent();
        String title = intent.getStringExtra("tag");
        header.setTvTitle(title);
        dataList.add("焊接接头的质量分级");
        dataList.add("其他部件的质量分级");
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
                        if(o.equals("焊接接头的质量分级")){
                            intent = new Intent(CFContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","14");
                            startActivity(intent);
                        }else if(o.equals("其他部件的质量分级")){
                            intent = new Intent(CFContextActivity.this, ContextListItemActivity.class);
                            intent.putExtra("tag","15");
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