package com.example.luke_imagevideo_send.main.module;


import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;
import com.example.luke_imagevideo_send.main.bean.Login;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface LoginContract {
    interface View extends BaseEView<presenter> {
        //设置查询线路
        void setLogin(Login loginBean);
        void setLoginMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getLogin(String userName, String passWord, String type);
    }
}
