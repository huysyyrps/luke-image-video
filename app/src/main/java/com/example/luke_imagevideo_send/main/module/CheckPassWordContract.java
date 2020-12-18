package com.example.luke_imagevideo_send.main.module;


import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;
import com.example.luke_imagevideo_send.main.bean.CheckPassWord;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface CheckPassWordContract {
    interface View extends BaseEView<presenter> {
        //设置查询线路
        void setCheckPassWord(CheckPassWord checkPassWord);
        void setCheckPassWordMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getCheckPassWord(String userName, String newPwd);
    }
}
