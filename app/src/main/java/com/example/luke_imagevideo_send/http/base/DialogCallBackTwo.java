package com.example.luke_imagevideo_send.http.base;

import android.app.Dialog;
import android.widget.EditText;

/**
 * Created by dell on 2017/4/25.
 */

public interface DialogCallBackTwo {
    void confirm(String name, Dialog dialog, EditText editText);

    void cancel(String name, Dialog dialog);
}