package com.example.luke_imagevideo_send;

import android.app.Application;
import android.content.Context;

import com.example.luke_imagevideo_send.http.network.CookieReadInterceptor;
import com.example.luke_imagevideo_send.http.network.CookiesSaveInterceptor;
import com.example.luke_imagevideo_send.http.okhttp.SSLSocketClient;
import com.example.luke_imagevideo_send.http.utils.InterceptorUtil;
import com.mob.MobSDK;

import java.util.concurrent.TimeUnit;

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

    /**
     * 全局httpclient
     *
     * @return
     */
    public static OkHttpClient initOKHttp() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                    .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                    //cookie
                    .addInterceptor(new CookieReadInterceptor())
                    .addInterceptor(new CookiesSaveInterceptor())
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                    .build();
        }
        return mOkHttpClient;
    }

    //获取全局的上下文
    public static Context getContext(){
        return context;
    }
}
