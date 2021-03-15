package com.example.luke_imagevideo_send.main.presenter;

import android.content.Context;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseObserverNoEntry;
import com.example.luke_imagevideo_send.http.utils.RetrofitUtil;
import com.example.luke_imagevideo_send.main.bean.TokenTest;
import com.example.luke_imagevideo_send.main.module.TokenTestContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public class TokenTestPresenter implements TokenTestContract.presenter {

    private Context context;
    private TokenTestContract.View view;

    public TokenTestPresenter(Context context, TokenTestContract.View view) {
        this.context = context;
        this.view = view;
    }

    /**
     * 登录
     */
    @Override
    public void getTokenTest() {
        RetrofitUtil.getInstance().initRetrofitMainNoSSL().getTokenTest().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserverNoEntry<TokenTest>(context, context.getResources().getString(R.string.handler_data)) {
                    @Override
                    protected void onSuccees(TokenTest t) throws Exception {
                        view.setTokenTest(t);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.setTokenTestMessage(""+ e.getMessage());
                    }
                });
    }
}
