package com.example.luke_imagevideo_send.cehouyi.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.example.luke_imagevideo_send.cehouyi.bean.Test;
import com.example.luke_imagevideo_send.cehouyi.module.MainContract;
import com.example.luke_imagevideo_send.http.base.BaseEntry;
import com.example.luke_imagevideo_send.http.base.BaseObserver;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry;
import com.example.luke_imagevideo_send.http.bean.Banner;
import com.example.luke_imagevideo_send.http.bean.Login;
import com.example.luke_imagevideo_send.http.bean.ZiXunAll;
import com.example.luke_imagevideo_send.http.utils.MainUtil;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public class MainPresenter implements MainContract.presenter {

    private Context context;
    private MainContract.View view;

    public MainPresenter(Context context, MainContract.View view) {
        this.context = context;
        this.view = view;
    }


    @Override
    public void getTest() {
        RetrofitUtil.getInstance().initRetrofit().getTset()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry<List<Test>>(context,MainUtil.loadTxt) {
                    @Override
                    protected void onSuccees(List<Test> t) throws Exception {
                        view.setTest(t);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setTestMessage("失败了----->"+e.getMessage());
                    }
                });

    }
}
