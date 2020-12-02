package com.example.luke_imagevideo_send.chifen.magnetic.presenter;

import android.content.Context;

import com.example.luke_imagevideo_send.cehouyi.bean.Test;
import com.example.luke_imagevideo_send.cehouyi.module.MainContract;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Test01;
import com.example.luke_imagevideo_send.chifen.magnetic.module.TestContract;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry;
import com.example.luke_imagevideo_send.http.utils.MainUtil;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public class TestPresenter implements TestContract.presenter {

    private Context context;
    private TestContract.View view;

    public TestPresenter(Context context, TestContract.View view) {
        this.context = context;
        this.view = view;
    }


    @Override
    public void getTest() {
        RetrofitUtil.getInstance().initRetrofit().getTset1()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry<Test01>(context,MainUtil.loadTxt) {
                    @Override
                    protected void onSuccees(Test01 t) throws Exception {
                        view.setTest(t);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setTestMessage("失败了----->"+e.getMessage());
                    }
                });
    }
}
