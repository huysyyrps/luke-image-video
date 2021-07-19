package com.example.luke_imagevideo_send.main.bean;

import java.io.Serializable;

public class Login implements Serializable {


//    /**
//     * token : {"tokenContent":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2F
//     * wLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoi5Yav5b2p57qiIiwiUm9sZSI6ImFkbWluIiwiZXhw
//     * IjoxNjE1MDA5Mzc1LCJpc3MiOiJtYWd1b3FpIiwiYXVkIjoi5Yav5b2p57qiIn0.fdJXv-g15QwMMHdlbkhQ8Z5V7xk_H4JqroLP43_6v3A","expires":"2021-03-06 13:42:55"}
//     * success : true
//     * data : 登录成功!
//     */
//
//    private TokenBean token;
//    private boolean success;
//    private String data;
//
//    public TokenBean getToken() {
//        return token;
//    }
//
//    public void setToken(TokenBean token) {
//        this.token = token;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
//
//    public static class TokenBean {
//        /**
//         * tokenContent : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoi5Yav5b2p57qiIiwiUm9sZSI6ImFkbWluIiwiZXhwIjoxNjE1MDA5Mzc1LCJpc3MiOiJtYWd1b3FpIiwiYXVkIjoi5Yav5b2p57qiIn0.fdJXv-g15QwMMHdlbkhQ8Z5V7xk_H4JqroLP43_6v3A
//         * expires : 2021-03-06 13:42:55
//         */
//
//        private String tokenContent;
//        private String expires;
//
//        public String getTokenContent() {
//            return tokenContent;
//        }
//
//        public void setTokenContent(String tokenContent) {
//            this.tokenContent = tokenContent;
//        }
//
//        public String getExpires() {
//            return expires;
//        }
//
//        public void setExpires(String expires) {
//            this.expires = expires;
//        }
//    }
    public boolean login;
    public String data;

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
