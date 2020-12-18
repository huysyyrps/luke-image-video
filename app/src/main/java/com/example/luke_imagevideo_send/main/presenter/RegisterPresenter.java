package com.example.luke_imagevideo_send.main.presenter;

import android.content.Context;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry1;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;
import com.example.luke_imagevideo_send.main.bean.Register;
import com.example.luke_imagevideo_send.main.module.RegisterContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @date: 2018/7/25
 * @description:
 */

public class RegisterPresenter implements RegisterContract.presenter {

    private Context context;
    private RegisterContract.View view;

    public RegisterPresenter(Context context, RegisterContract.View view) {
        this.context = context;
        this.view = view;
    }

    /**
     * 注册
     */
    @Override
    public void getRegister(String account, String password) {
        RetrofitUtil.getInstance().initRetrofitMain().getLineStation(account,password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry1<Register>(context, context.getResources().getString(R.string.handler_data)) {
                    @Override
                    protected void onSuccees(Register t) throws Exception {
                        view.setRegister(t);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setRegisterMessage("reason:" + e.getMessage());
                    }
                });
    }
}
