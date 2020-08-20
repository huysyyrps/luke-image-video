package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeeImageOrVideoActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        String path = getIntent().getStringExtra("path");
        Glide.with(this)
                .load(path)
                .centerCrop()
                .placeholder(R.color.app_color_f6)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_see_image_or_video;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}