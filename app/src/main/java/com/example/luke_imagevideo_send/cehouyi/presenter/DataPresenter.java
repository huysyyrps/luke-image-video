package com.example.luke_imagevideo_send.cehouyi.presenter;

import android.content.Context;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.bean.SaveData;
import com.example.luke_imagevideo_send.cehouyi.bean.SaveDataBack;
import com.example.luke_imagevideo_send.cehouyi.module.DataContract;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry1;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public class DataPresenter implements DataContract.presenter {

    private Context context;
    private DataContract.View view;

    public DataPresenter(Context context, DataContract.View view) {
        this.context = context;
        this.view = view;
    }

    /**
     * 测厚数据上传
     */
    @Override
    public void getSaveData(SaveData saveData) {
        RetrofitUtil.getInstance().initRetrofitMainNoSSL().sendDataSave(saveData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry1<SaveDataBack>(context, context.getResources().getString(R.string.handler_data)) {
                    @Override
                    protected void onSuccees(SaveDataBack t) throws Exception {
                        view.setSaveData(t);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setSaveDataMessage(""+ e.getMessage());
                    }
                });

        RetrofitUtil.getInstance().initRetrofitMainNoSSL().sendDataSave(saveData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry1<SaveDataBack>(context, context.getResources().getString(R.string.handler_data)) {
                    @Override
                    protected void onSuccees(SaveDataBack t) throws Exception {
                        view.setSaveData(t);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setSaveDataMessage(""+ e.getMessage());
                    }
                });
    }
}
