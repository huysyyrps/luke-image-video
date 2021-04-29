package com.example.luke_imagevideo_send;

import com.example.luke_imagevideo_send.cehouyi.bean.SaveData;
import com.example.luke_imagevideo_send.cehouyi.bean.SaveDataBack;
import com.example.luke_imagevideo_send.chifen.camera.bean.HaveVideoUp;
import com.example.luke_imagevideo_send.chifen.camera.bean.PhotoUp;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Test01;
import com.example.luke_imagevideo_send.main.bean.CheckPassWord;
import com.example.luke_imagevideo_send.main.bean.Login;
import com.example.luke_imagevideo_send.main.bean.Register;
import com.example.luke_imagevideo_send.main.bean.TokenTest;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
     *  @GET(ApiAddress.login)
     */
    @FormUrlEncoded
    @POST(ApiAddress.login)
    @Headers({"Content-Type:application/x-www-form-urlencoded; charset=UTF-8"})
    Observable<Login> getLogin(@Field("username") String username, @Field("password") String password);


    /**
     * 图片上传
     *  @GET(ApiAddress.login)
     */
    @POST(ApiAddress.photoup)
    @Headers({"Content-Type:application/json; charset=UTF-8"})
    /*Observable<PhotoUp> getPhoto(@Field("company") String company, @Field("device") String device, @Field("pic") String pic);*/
    Observable<PhotoUp> getPhoto(@Body RequestBody body);

    /**
     * 视频上传
     *  @GET(ApiAddress.login)
     */
//    @POST(ApiAddress.havevideoup)
//    @Headers({"Content-Type:application/json; charset=UTF-8"})
//    Observable<HaveVideoUp> getHaveVideoUp(@Body RequestBody body);

    @Multipart
    @POST(ApiAddress.havevideoup)
    Observable<HaveVideoUp> getHaveVideoUp(@Part List<MultipartBody.Part> partList);

    /**
     * 测试token
     */
    @GET(ApiAddress.tokenTest)
    Observable<TokenTest> getTokenTest();

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
    @POST(ApiAddress.cedatasend)
    Observable<SaveDataBack> sendDataSave(@Body SaveData saveData);

//    //图片上传
//    @Multipart
//    @POST("face/uploadFile")
//    Observable<SaveDataBack> uploadFile (@Part List<MultipartBody.Part> partList);
//    @Multipart
//    @POST("v1/app/exception")
//    Observable<ResponseBean<List<String>>> uploadLog(@PartMap Map<String, RequestBody> map);

//    @GET(ApiAddress.LineDetial)
//    Observable<CustomizedLineDetailBean> sendRequestGetcustomizedLineDetail(@Query("id") int id);
}
