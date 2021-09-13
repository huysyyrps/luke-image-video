package com.example.luke_imagevideo_send.http.base;

public interface ModbusInstanceCallBack {
    void confirm(String string);
    void error(String string);
}
