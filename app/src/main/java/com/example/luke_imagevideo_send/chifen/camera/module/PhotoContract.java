package com.example.luke_imagevideo_send.chifen.camera.module;


import com.example.luke_imagevideo_send.chifen.camera.bean.PhotoUp;
import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface PhotoContract {
    interface View extends BaseEView<presenter> {
        //设置查询线路
        void setPhoto(PhotoUp photoUp);
        void setPhotoMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getPhoto(RequestBody company);
    }
}
