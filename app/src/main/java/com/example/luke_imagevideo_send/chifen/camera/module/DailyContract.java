package com.example.luke_imagevideo_send.chifen.camera.module;


import com.example.luke_imagevideo_send.chifen.camera.bean.Daily;
import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface DailyContract {
    interface View extends BaseEView<presenter> {
        void setDaily(Daily daily);
        void setDailyMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getDaily(@Part List<MultipartBody.Part> partList);
    }
}
