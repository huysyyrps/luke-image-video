package com.example.luke_imagevideo_send.cehouyi.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTime {
    Handler handler;
    // 初始化时间方法
    public void initTime(GetTimeCallBack callBack) {
        // 时间变化
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                tvTime.setText((String) msg.obj);
                callBack.backTime((String) msg.obj);
            }
        };
        Threads thread = new Threads();
        thread.start();
    }

    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String str = sdf.format(new Date());
                    handler.sendMessage(handler.obtainMessage(100, str));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
