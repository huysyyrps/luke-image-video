package com.example.luke_imagevideo_send.chifen.camera.presenter;

import android.content.Context;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.bean.HaveVideoUp;
import com.example.luke_imagevideo_send.chifen.camera.module.HaveVideoContract;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.http.Part;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public class HaveVideoPresenter implements HaveVideoContract.presenter {

    private Context context;
    private HaveVideoContract.View view;

    public HaveVideoPresenter(Context context, HaveVideoContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getHaveVideo(@Part List<MultipartBody.Part> partList) {
        RetrofitUtil.getInstance().initLoginRetrofitMainNoSSL().getHaveVideoUp(partList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry<HaveVideoUp>(context, context.getResources().getString(R.string.handler_data)) {
                    @Override
                    protected void onSuccees(HaveVideoUp t) throws Exception {
                        view.setHaveVideo(t);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setHaveVideoMessage(""+ e.getMessage());
                    }
                });
    }
}
