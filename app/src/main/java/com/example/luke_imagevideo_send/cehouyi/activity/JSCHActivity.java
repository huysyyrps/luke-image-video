package com.example.luke_imagevideo_send.cehouyi.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;

public class JSCHActivity extends AppCompatActivity {
    private String address = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsch);
        try {
            address = new getIp().getConnectIp();
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    SSHExcuteCommandHelper.main(address, new SSHCallBack() {
//                        @Override
//                        public void confirm(String string) {
//                            new SSHExcuteCommandHelper(address).disconnect();
//                        }
//
////                        @Override
////                        public void confirm(List<List<String>> parseResult) {
////                            for (List<String> l : parseResult) {
////                                System.out.println(l);
////                            }
////                            new SSHExcuteCommandHelper(address).disconnect();
////                        }
//                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}