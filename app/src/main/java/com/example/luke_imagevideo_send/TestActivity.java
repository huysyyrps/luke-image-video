package com.example.luke_imagevideo_send;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


public class TestActivity extends Activity {
    private BarGraphView barGraphView;
    Handler handler;
    int i= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        barGraphView = findViewById(R.id.barGraphView);
        Item item = new Item(i+"",i);
        barGraphView.addData(item);

        Threads thread = new Threads();
        thread.start();
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                i+=1;
//                Item item = new Item(i+"",i);
//                barGraphView.addData(item);
//            }
//        };

    }
    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
//                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String str = sdf.format(new Date());
//                    handler.sendMessage(handler.obtainMessage(100, str));
                    i+=1;
                    Item item = new Item(i+"",i);
                    barGraphView.addData(item);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
