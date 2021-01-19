package com.example.luke_imagevideo_send.cehouyi.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.util.SSHExcuteCommandHelper;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;

import java.util.ArrayList;
import java.util.List;

public class JSCHActivity extends AppCompatActivity {
    private String address = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsch);
        try {
            ArrayList<String> connectIpList = new getIp().getConnectIp();
            address = connectIpList.get(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SSHExcuteCommandHelper.main(address, "root", "root", 22, new SSHCallBack() {
                        @Override
                        public void confirm(List<List<String>> parseResult) {
                            for (List<String> l : parseResult) {
                                System.out.println(l);
                            }
                            new SSHExcuteCommandHelper(address, "root", "root", 22).disconnect();
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}