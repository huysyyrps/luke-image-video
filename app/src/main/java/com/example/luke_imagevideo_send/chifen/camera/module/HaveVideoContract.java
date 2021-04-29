package com.example.luke_imagevideo_send.chifen.camera.module;


import com.example.luke_imagevideo_send.chifen.camera.bean.HaveVideoUp;
import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface HaveVideoContract {
    interface View extends BaseEView<presenter> {
        //设置查询线路
        void setHaveVideo(HaveVideoUp HaveVideoUp);
        void setHaveVideoMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getHaveVideo(@Part List<MultipartBody.Part> partList);
    }
}
