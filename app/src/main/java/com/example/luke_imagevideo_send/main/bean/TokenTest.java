package com.example.luke_imagevideo_send.main.bean;

import java.io.Serializable;

public class TokenTest implements Serializable {

    /**
     * success  : true
     * data : 访问成功
     */

    private boolean success;
    private String data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
