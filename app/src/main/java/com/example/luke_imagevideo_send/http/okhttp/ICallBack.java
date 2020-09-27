package com.example.luke_imagevideo_send.http.okhttp;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author huangxingwei(xwdz9989 @ gmail.com)
 * @since 2018/3/31
 */
public interface ICallBack {

    void onFailure(Call call, Exception e);

    void onNativeResponse(Call call, Response response, boolean isMainUIThread) throws Exception;
}
