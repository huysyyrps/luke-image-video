package com.example.luke_imagevideo_send.http.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author xingwei.huang (xwdz9989@gamil.com)
 * @since 2019/3/21
 */
public class QuietOkHttp {

    public static final String TAG = QuietOkHttp.class.getSimpleName();

    private static OkHttpClient sOkHttpClient;

    static {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLog(TAG));
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        sOkHttpClient = new OkHttpClient.Builder()
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .writeTimeout(20, TimeUnit.SECONDS).build();
    }


    private QuietOkHttp() { }


    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        sOkHttpClient = okHttpClient;
    }

    /**
     * 上传文件至服务器
     *
     * @param url 目标链接
     * @return
     */
    public static PostFileWrapper postFile(String url) {
        return new PostFileWrapper(sOkHttpClient, url);
    }

    /**
     * 取消一个正在进行的Http请求
     *
     * @param tag 标记
     */
    public static void cancel(Object tag) {
        Assert.checkNull(tag, "tag cannot not null!");

        RequestTraces.getImpl().cancel(tag);
    }

    /**
     * 取消所有正在进行的Http请求
     */
    public static void cancellAll() {
        RequestTraces.getImpl().cancelAll();
    }
}
