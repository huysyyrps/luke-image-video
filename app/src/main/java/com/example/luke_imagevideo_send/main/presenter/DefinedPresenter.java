package com.example.luke_imagevideo_send.main.presenter;

import android.content.Context;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry1;
import com.example.luke_imagevideo_send.http.utils.NetStat;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;
import com.example.luke_imagevideo_send.main.bean.Defined;
import com.example.luke_imagevideo_send.main.module.DefinedContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public class DefinedPresenter implements DefinedContract.presenter {

    private Context context;
    private DefinedContract.View view;

    public DefinedPresenter(Context context, DefinedContract.View view) {
        this.context = context;
        this.view = view;
    }

    /**
     * 根据派工单获取信息
     */
    @Override
    public void getDefined(String pgd) {
        RetrofitUtil.getInstance().initLoginRetrofitMainNoSSL().getDefined(pgd).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry1<Defined>(context, context.getResources().getString(R.string.handler_data)) {
                    @Override
                    protected void onSuccees(Defined t) throws Exception {
                        view.setDefined(t);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (new NetStat().isNetworkConnected(context)){
                            view.setDefinedMessage(e.toString());
                        }else {
                            view.setDefinedMessage("网络异常");
                        }
                    }
                });
    }
}
