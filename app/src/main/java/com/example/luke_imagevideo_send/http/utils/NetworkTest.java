package com.example.luke_imagevideo_send.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkTest {
    public boolean goToNetWork(Context context) {
        // TODO Auto-generated method stub
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }
}
