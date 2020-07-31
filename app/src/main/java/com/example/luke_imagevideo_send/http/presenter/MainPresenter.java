package com.example.luke_imagevideo_send.http.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.luke_imagevideo_send.http.base.BaseEntry;
import com.example.luke_imagevideo_send.http.base.BaseObserver;
import com.example.luke_imagevideo_send.http.bean.Banner;
import com.example.luke_imagevideo_send.http.bean.Login;
import com.example.luke_imagevideo_send.http.bean.ZiXunAll;
import com.example.luke_imagevideo_send.http.module.MainContract;
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

    /**
     * 图片验证码
     */
    @Override
    public void getCode() {
        RetrofitUtil.getInstance().initRetrofit().getVerityCode().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResponseBody value) {
                InputStream is=value.byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                view.setCode(bitmap);
            }

            @Override
            public void onError(Throwable e) {
                view.setContent("失败了----->"+e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 登录
     * @param user
     * @param pwd
     * @param code
     */
    @Override
    public void userLogin(String user, String pwd, String code) {
        Map<String,String> map=new HashMap<>();
        map.put("userName",user);
        map.put("userPwd",pwd);
        map.put("verifyCode",code);
        RetrofitUtil.getInstance().initRetrofit().userLogin(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Login>(context, MainUtil.loadLogin) {
                    @Override
                    protected void onSuccees(BaseEntry<Login> t) throws Exception {
                       if(t.isSuccess()){
                           view.setContent("Hello---->"+t.getData().getName());
                       }else {
                           view.setContent("----->"+t.getMessage());
                       }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setContent("失败了----->"+e.getMessage());
                    }
                });
    }

    /**
     * 获取banner
     */
    @Override
    public void getBannerData() {
        RetrofitUtil.getInstance().initRetrofit().getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Banner>>(context,MainUtil.loadTxt) {
                    @Override
                    protected void onSuccees(BaseEntry<List<Banner>> t) throws Exception {
                        MainUtil.printLogger(t.getData().get(0).getTitle());
                        view.setContent(t.getData().get(0).getContent());
                    }


                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setContent("失败了----->"+e.getMessage());
                    }
                });
    }

    /**
     * 获取资讯
     */
    @Override
    public void getLiveData() {
        RetrofitUtil.getInstance().initRetrofit().getZixunData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<ZiXunAll>>(context,MainUtil.loadTxt) {
                    @Override
                    protected void onSuccees(BaseEntry<List<ZiXunAll>> t) throws Exception {
                        view.setContent("标题：" + t.getData().get(0).getTitle());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setContent("失败了----->"+e.getMessage());
                    }
                });
    }
}
