package com.example.luke_imagevideo_send.http.base;

import android.app.Dialog;

/**
 * Created by dell on 2017/4/25.
 */

public interface DialogCallBack {
    void confirm(String name, Dialog dialog);

    void cancel();
}