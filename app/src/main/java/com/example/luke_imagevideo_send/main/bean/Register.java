package com.example.luke_imagevideo_send.main.bean;

import java.io.Serializable;

public class Register implements Serializable {

    /**
     * msg : 保存成功
     * code : 1
     * success : true
     * extraData : null
     * noticeData : null
     * data : null
     * count : null
     * nextUrl : null
     */

    private String msg;
    private int code;
    private boolean success;
    private Object extraData;
    private Object noticeData;
    private Object data;
    private Object count;
    private Object nextUrl;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public Object getNoticeData() {
        return noticeData;
    }

    public void setNoticeData(Object noticeData) {
        this.noticeData = noticeData;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getCount() {
        return count;
    }

    public void setCount(Object count) {
        this.count = count;
    }

    public Object getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(Object nextUrl) {
        this.nextUrl = nextUrl;
    }
}
