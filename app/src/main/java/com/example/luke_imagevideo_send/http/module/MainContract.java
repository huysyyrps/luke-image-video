package com.example.luke_imagevideo_send.http.module;

import android.graphics.Bitmap;

import com.example.luke_imagevideo_send.http.base.BaseDView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public interface MainContract {
    interface View extends BaseDView<presenter> {
        void setContent(String content);  //设置内容

        void setCode(Bitmap value);  //设置验证码
    }

    interface presenter extends BasePresenter {
        void getCode(); //获取验证码

        void userLogin(String user, String pwd, String code); //登录

        void getBannerData();  //banner

        void getLiveData();   //资讯信息
    }
}
