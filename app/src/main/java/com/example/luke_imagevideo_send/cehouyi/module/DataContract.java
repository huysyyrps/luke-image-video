package com.example.luke_imagevideo_send.cehouyi.module;


import com.example.luke_imagevideo_send.cehouyi.bean.SaveData;
import com.example.luke_imagevideo_send.cehouyi.bean.SaveDataBack;
import com.example.luke_imagevideo_send.http.base.BaseEView;
import com.example.luke_imagevideo_send.http.base.BasePresenter;

/**
 * Created by Administrator on 2019/4/11.
 */

public interface DataContract {
    interface View extends BaseEView<presenter> {
        void setSaveData(SaveDataBack saveDataBack);
        void setSaveDataMessage(String message);
    }

    interface presenter extends BasePresenter {
        //线路回调
        void getSaveData(SaveData saveData);
    }
}
