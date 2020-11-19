package com.example.luke_imagevideo_send.cehouyi.module;

import android.graphics.Bitmap;

import com.example.luke_imagevideo_send.cehouyi.bean.Test;
import com.example.luke_imagevideo_send.http.base.BaseDView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;

import java.util.List;

/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public interface MainContract {
    interface View extends BaseDView<presenter> {
        //设置查询线路
        void setTest(List<Test> test);
        void setTestMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getTest();
    }
}
