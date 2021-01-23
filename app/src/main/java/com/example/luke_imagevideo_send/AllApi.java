package com.example.luke_imagevideo_send;

import com.example.luke_imagevideo_send.cehouyi.bean.SaveData;
import com.example.luke_imagevideo_send.cehouyi.bean.SaveDataBack;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Test01;
import com.example.luke_imagevideo_send.main.bean.CheckPassWord;
import com.example.luke_imagevideo_send.main.bean.Login;
import com.example.luke_imagevideo_send.main.bean.Register;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
     * 测试01
     */
    @GET(ApiAddress.getData)
    Observable<Test01> getTset1();

    /**
     * 测厚数据上传
     */
//    @FormUrlEncoded
//    @Headers({"Content-Type:application/json;charset=UTF-8"})
//    @POST(ApiAddress.cedatasend)
//    Observable<SaveDataBack> sendDataSave(@FieldMap Map<String, Object> param);
//    @FormUrlEncoded
//    @Headers({"Content-Type:application/json;charset=UTF-8"})
//    @POST(ApiAddress.cedatasend)
//    Observable<SaveDataBack> sendDataSave(@Field("time") String time
//            , @Field("soundVelocity") String soundVelocity
//            ,@Field("data") String data);
//    @FormUrlEncoded
//    @Headers({"Content-Type:application/json;charset=UTF-8"})
//    @POST(ApiAddress.cedatasend)
//    Observable<SaveDataBack> sendDataSave(@Field("data") String data);
    @FormUrlEncoded
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    @POST(ApiAddress.cedatasend)
    Observable<SaveDataBack> sendDataSave(@Body SaveData saveData);
}
