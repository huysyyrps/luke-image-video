package com.example.luke_imagevideo_send.chifen.magnetic.rtmp;

public interface OnConntionListener {

    void onConntecting();

    void onConntectSuccess();

    void onConntectFail(String msg);
}
