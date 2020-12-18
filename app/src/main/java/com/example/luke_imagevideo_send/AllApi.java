package com.example.luke_imagevideo_send;

import com.example.luke_imagevideo_send.cehouyi.bean.Test;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Test01;
import com.example.luke_imagevideo_send.main.bean.CheckPassWord;
import com.example.luke_imagevideo_send.main.bean.Login;
import com.example.luke_imagevideo_send.main.bean.Register;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description:
 */

public interface AllApi {


    /**
     * 获取图片验证码
     */
    @GET(ApiAddress.getVerifyCode)
    Observable<ResponseBody> getVerityCode();

    /**
     * 登录
     */
    @GET(ApiAddress.login)
    Observable<Login> getLogin(@Query("appLoginName") String appLoginName, @Query("appPassword") String appPassword
            , @Query("type") String type);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(ApiAddress.register)
    Observable<Register> getLineStation(@Field("account") String account, @Field("password") String password);

    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST(ApiAddress.checkpassword)
    Observable<CheckPassWord> getCheckPassWord(@Field("account") String account, @Field("newPwd") String newPwd);


    /**
     * 测试
     */
    @GET(ApiAddress.test)
    Observable<List<Test>> getTset();

    /**
     * 测试01
     */
    @GET(ApiAddress.getData)
    Observable<Test01> getTset1();
}
