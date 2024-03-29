package com.example.luke_imagevideo_send.main.module;


import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;
import com.example.luke_imagevideo_send.main.bean.Defined;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface DefinedContract {
    interface View extends BaseEView<presenter> {
        //设置查询线路
        void setDefined(Defined defined);
        void setDefinedMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getDefined(String pgd);
    }
}
