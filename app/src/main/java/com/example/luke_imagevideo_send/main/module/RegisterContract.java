package com.example.luke_imagevideo_send.main.module;


import com.example.luke_imagevideo_send.http.base.BaseDView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;
import com.example.luke_imagevideo_send.main.bean.Register;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface RegisterContract {
    interface View extends BaseDView<presenter> {
        //注册
        void setRegister(Register bean);
        void setRegisterMessage(String s);
    }

    interface presenter extends BasePresenter {
        void getRegister(String account, String password);
    }
}
