package com.example.luke_imagevideo_send.http.network;


import com.example.luke_imagevideo_send.MyApplication;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: Allen.
 * @date: 2018/7/27
 * @description: 读取cookie
 */

public class CookieReadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Cookie", SharePreferencesUtils.getString(MyApplication.myApp, "cookiess", ""));
        String s = "Bearer "+SharePreferencesUtils.getString(MyApplication.myApp, "tokenContent", "");
        builder.addHeader("Authorization", "Bearer "+SharePreferencesUtils.getString(MyApplication.myApp, "tokenContent", ""));
        return chain.proceed(builder.build());
    }
}
