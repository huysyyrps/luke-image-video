package com.example.luke_imagevideo_send.chifen.camera.presenter;

import android.content.Context;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.bean.Daily;
import com.example.luke_imagevideo_send.chifen.camera.module.DailyContract;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public class DailyPresenter implements DailyContract.presenter {

    private Context context;
    private DailyContract.View view;

    public DailyPresenter(Context context, DailyContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getDaily(List<MultipartBody.Part> partList) {
        RetrofitUtil.getInstance().initLoginRetrofitMainNoSSL().getDaily(partList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry<Daily>(context, context.getResources().getString(R.string.handler_data)) {
                    @Override
                    protected void onSuccees(Daily t) throws Exception {
                        if (t.result){
                            view.setDaily(t);
                        }else {
                            view.setDailyMessage("上传失败");
                        }
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setDailyMessage(""+ e.getMessage());
                    }
                });
    }
}
