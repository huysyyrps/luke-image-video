package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;

import butterknife.ButterKnife;

public class ContextListItemActivity extends BaseActivity {
    String tag = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        ButterKnife.bind(this);
    }

    @Override
    protected int provideContentViewId() {
        tag = getIntent().getStringExtra("tag");
        if (tag.equals("0")){
            return R.layout.activity_cs_context_list_one;
        }else if (tag.equals("1")){
            return R.layout.activity_cs_context_list_two;
        }else if (tag.equals("2")){
            return R.layout.activity_cs_context_list_there;
        }else if (tag.equals("3")){
            return R.layout.activity_cs_context_list_four;
        }else if (tag.equals("4")){
            return R.layout.activity_cs_context_list_five;
        }else if (tag.equals("5")){
            return R.layout.activity_cs_context_list_six;
        }else if (tag.equals("6")){
            return R.layout.activity_cs_context_list_seven;
        }else if (tag.equals("7")){
            return R.layout.activity_cs_context_list_eight;
        }else if (tag.equals("8")){
            return R.layout.activity_cs_context_list_nine;
        }else if (tag.equals("9")){
            return R.layout.activity_cs_context_list_ten;
        }else if (tag.equals("10")){
            return R.layout.activity_cs_context_list_eleven ;
        }else if (tag.equals("11")){
            return R.layout.activity_cs_context_list_twelve ;
        }else if (tag.equals("12")){
            return R.layout.activity_cs_context_list_thirteen ;
        }else if (tag.equals("13")){
            return R.layout.activity_cs_context_list_fourteen ;
        }else if (tag.equals("14")){
            return R.layout.activity_cf_context_list_one ;
        }else if (tag.equals("15")){
            return R.layout.activity_cf_context_list_two ;
        }
        return R.layout.activity_cs_context_list_one;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}