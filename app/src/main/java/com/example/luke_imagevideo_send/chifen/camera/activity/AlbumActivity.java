package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.fragment.PhotoFragment;
import com.example.luke_imagevideo_send.chifen.camera.fragment.VideoHaveAudioFragment;
import com.example.luke_imagevideo_send.chifen.camera.fragment.VideoNoAudioFragment;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mFragments = new ArrayList<>();
        mFragments.add(new PhotoFragment());
        mFragments.add(new VideoHaveAudioFragment());
        mFragments.add(new VideoNoAudioFragment());
//      无需编写适配器，一行代码关联TabLayout与ViewPager
        tabLayout.setViewPager(viewPage, new String[]{"图片", "有声视频", "无声视频"}, this, mFragments);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_album;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}