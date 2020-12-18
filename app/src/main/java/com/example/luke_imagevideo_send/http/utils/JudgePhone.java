package com.example.luke_imagevideo_send.http.utils;

import android.text.TextUtils;

public class JudgePhone {
    public boolean isMobileNO(String mobiles) {
        String telRegex = "^((1[3,5,6,7,8,9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }
}
