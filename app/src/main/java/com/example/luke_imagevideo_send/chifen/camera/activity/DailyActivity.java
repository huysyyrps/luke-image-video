package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
}