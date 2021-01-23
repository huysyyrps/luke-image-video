package com.example.luke_imagevideo_send.http.utils;

import com.example.luke_imagevideo_send.AllApi;
import com.example.luke_imagevideo_send.ApiAddress;
import com.example.luke_imagevideo_send.http.network.CookieReadInterceptor;
import com.example.luke_imagevideo_send.http.network.CookiesSaveInterceptor;
import com.example.luke_imagevideo_send.http.okhttp.SSLSocketClient;
import com.example.luke_imagevideo_send.http.utils.network.NoCookieReadInterceptor;
import com.example.luke_imagevideo_send.http.utils.network.NoCookiesSaveInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.luke_imagevideo_send.MyApplication.TIMEOUT;

/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description: retrofit请求工具类
 */

public class RetrofitUtil {
    /**
     * 超时时间
     */
    private static volatile RetrofitUtil mInstance;
    private AllApi allApi;
    private static Gson gson;
    private static OkHttpClient mOkHttpClient;

    /**
     * 单例封装
     *
     * @return
     */
    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                    gson = new GsonBuilder().setLenient().create();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化Retrofit(其他)
     */
    public AllApi initRetrofitMain() {
        if (allApi == null) {
            Retrofit mRetrofit = new Retrofit.Builder()
                    .client(initOKHttp())
                    // 设置请求的域名
                    .baseUrl(ApiAddress.api)
                    // 设置解析转换工厂，用自己定义的
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(LenientGsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            allApi = mRetrofit.create(AllApi.class);
        }
        return allApi;
    }

    /**
     * 屏蔽SSl证书
     * @return
     */
    public AllApi initRetrofitMainNoSSL() {
        if (allApi == null) {
            Retrofit mRetrofit = new Retrofit.Builder()
                    .client(initOKHttpNOSSL())
                    // 设置请求的域名
                    .baseUrl(ApiAddress.api)
                    // 设置解析转换工厂，用自己定义的
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(LenientGsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            allApi = mRetrofit.create(AllApi.class);
        }
        return allApi;
    }

    /**
     * 初始化Retrofit(没session)
     */
    public AllApi initRetrofitNoSession() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(initOKHttpNoSession())
                // 设置请求的域名
                .baseUrl(ApiAddress.api)
                // 设置解析转换工厂，用自己定义的
                .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(LenientGsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        allApi = mRetrofit.create(AllApi.class);
        return allApi;
    }

    /**
     * 初始化Retrofit(获取session)
     */
    public AllApi initRetrofitGetSession() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(initOKHttpGetSession())
                // 设置请求的域名
                .baseUrl(ApiAddress.api)
                // 设置解析转换工厂，用自己定义的
                .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(LenientGsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        allApi = mRetrofit.create(AllApi.class);
        return allApi;
    }

    /**
     * 初始化Retrofit(请求添加session)
     */
    public AllApi initRetrofitSetSession() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(initOKHttpSetSession())
                // 设置请求的域名
                .baseUrl(ApiAddress.api)
                // 设置解析转换工厂，用自己定义的
                .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(LenientGsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        allApi = mRetrofit.create(AllApi.class);
        return allApi;
    }


    /**
     * 全局httpclient
     *
     * @return
     */
    public static OkHttpClient initOKHttp() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                //cookie
                .addInterceptor(new CookieReadInterceptor())
                .addInterceptor(new CookiesSaveInterceptor())
                .build();
        return mOkHttpClient;
    }

    public static OkHttpClient initOKHttpNOSSL() {
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
        return mOkHttpClient;
    }

    public static OkHttpClient initOKHttpNoSession() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                //cookie
                .addInterceptor(new NoCookieReadInterceptor())
                .addInterceptor(new NoCookiesSaveInterceptor())
                .build();
        return mOkHttpClient;
    }


    public static OkHttpClient initOKHttpGetSession() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                //cookie
                .addInterceptor(new NoCookieReadInterceptor())
                .addInterceptor(new CookiesSaveInterceptor())
                .build();
        return mOkHttpClient;
    }

    public static OkHttpClient initOKHttpSetSession() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                //cookie
                .addInterceptor(new CookieReadInterceptor())
                .addInterceptor(new NoCookiesSaveInterceptor())
                .build();
        return mOkHttpClient;
    }
}
