package com.example.luke_imagevideo_send.cehouyi.util;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class getMyTestData {
    int i = 0;
    boolean setData = true;
    Handler handler;
    LineData lineData;
    BarData barData;
    LineDataSet lineDataSet;
    BarDataSet barDataSet;
    Threads thread = new Threads();
    List<Entry> entries = new ArrayList<>();
    List<BarEntry> barEntries = new ArrayList<>();
    public void initLineData(LineChart lineChart, BarChart barChart, Button tvMax, Button tvMin, Button btnData){
        for (int i = 0;i<100;i++){
            entries.add(new Entry(i, 0));
        }
        for (int i = 0;i<100;i++){
            barEntries.add(new BarEntry(i, 0));
        }
        initTime(lineChart,barChart,tvMax,tvMin,btnData);
    }
    // 初始化时间方法
    public void initTime(final LineChart lineChart, final BarChart barChart, Button tvMax, Button tvMin, Button btnData) {
        // 时间变化
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 生成随机测试数
                float data = ((float) ((Math.random()) * 2500) + 50);
                data = Float.parseFloat(String.format(String.format("%.2f", data)));
                btnData.setText(data+"m/s");
                if (setData){
                    if (tvMax.getText().toString().equals("0.00")&&tvMin.getText().toString().equals("0.00")){
                        tvMax.setText(data+"");
                        tvMin.setText(data+"");
                        setData = false;
                    }
                }
                if (i<100){
                    entries.set(i,new Entry(i, data));
                    barEntries.set(i,new BarEntry(i, data));
                }else {
                    entries.add(new Entry(i, data));
                    barEntries.set(i,new BarEntry(i, data));
                }
                if (data>Float.valueOf(tvMax.getText().toString())){
                    tvMax.setText(data+"");
                }
                if (data<Float.valueOf(tvMin.getText().toString())){
                    tvMin.setText(data+"");
                }
                lineDataSet = new LineDataSet(entries, "");
                //曲线
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setColor(Color.GREEN);
                //不显示数据点(圆圈)
                lineDataSet.setDrawCircles(false);
                lineData = new LineData(lineDataSet);
                //不显示曲线点的具体数值
                lineData.setDrawValues(false);
                lineChart.setData(lineData);
                // 像ListView那样的通知数据更新
                lineChart.notifyDataSetChanged();
                lineChart.setVisibleXRangeMaximum(100);
                lineChart.moveViewToX(i);

                barEntries.add(new BarEntry(i, data));
                barDataSet = new BarDataSet(barEntries, "");
                barDataSet.setColor(Color.GREEN);
                barData = new BarData(barDataSet);
                barData.setBarWidth(1.2f);
                barData.setDrawValues(false);
                barChart.setData(barData); //给柱状图添加数据
                barChart.setVisibleXRangeMaximum(100);
                barChart.setVisibleXRangeMinimum(100);
                barChart.invalidate(); //让柱状图填充数据后刷新
                barChart.moveViewToX(i);
                i++;
            }
        };
        thread.start();
    }

    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    handler.sendMessage(handler.obtainMessage(100, ""));
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
