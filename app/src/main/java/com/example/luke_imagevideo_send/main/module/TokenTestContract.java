package com.example.luke_imagevideo_send.main.module;


import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;
import com.example.luke_imagevideo_send.main.bean.TokenTest;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface TokenTestContract {
    interface View extends BaseEView<presenter> {
        //设置查询线路
        void setTokenTest(TokenTest tokenTest);
        void setTokenTestMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getTokenTest();
    }
}
