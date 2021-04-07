package com.example.luke_imagevideo_send;

/**
 * @author: Allen.
 * @date: 2018/3/8
 * @description: 所有接口地址集
 */

public class ApiAddress {

    //生成环境
    public final static String api = "https://172.16.18.73:5001/";

    /***********************首页*******************************/
    //banner
    public final static String getBannerList = "mobile/listSlides.do";
    //获取直播数据
    public final static String getLivesList = "mobile/listLives.do";
    //最新资讯
    public final static String getZixunList = "mobile/listArticles.do";

    //测试查询地址
    public final static String getData = "/api/113";



    /**************************************个人中心************************************************/
    //注册
    public final static String userRegister = "mbregister.do";
    //发送验证码
    public final static String sendVerifyCode = "mbsend";

    //获取图片验证码
    public final static String getVerifyCode = "getVerityCode.do";

    public final static String test = "api/111";
    //登录
    public final static String login = "api/LoginAuth/Login";
    //图片上传https://172.16.18.73:5001/api/FileProccess/UpLoadPic
    public final static String photoup = "api/FileProccess/UpLoadPic";
    //测试token
    public final static String tokenTest = "api/Get/GetTwo";
    //修改密码
    public final static String checkpassword = "app_change_password";
    //注册
    public final static String register = "mobile_phone/save";
    //测厚数据上传
    public final static String cedatasend = "Post/PostNine";




}
