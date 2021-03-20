package com.example.luke_imagevideo_send;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;

import okhttp3.OkHttpClient;

/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description: application
 */

public class MyApplication extends Application {
    public static MyApplication myApp;
    public static final int TIMEOUT = 60;
    private static Context context;//全局上下文
    private static OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.submitPolicyGrantResult(true, null);
        myApp = this;
        context = getApplicationContext();
    }

    //获取全局的上下文
    public static Context getContext(){
        return context;
    }
}
