package com.example.luke_imagevideo_send.cehouyi.view;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

public class myView {
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    public void setlineDataChar(LineChart lineChart) {
        lineChart.setBackgroundColor(Color.BLACK);
        lineChart.setNoDataText("暂无数据");   // 没有数据时样式
        // *********************滑动相关*************************** //
        lineChart.setDragEnabled(true);    // 可拖动,默认true
        lineChart.setScaleXEnabled(true);  // X轴上的缩放,默认true
        lineChart.setScaleYEnabled(false);  // Y轴上的缩放,默认true
        lineChart.setDoubleTapToZoomEnabled(true); // 双击缩放,默认true
        lineChart.setDragDecelerationEnabled(false);    // 抬起手指，继续滑动,默认true
        lineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setViewPortOffsets(0, 0, 0, 0); //控制与手机边缘距离

        xAxis = lineChart.getXAxis();
        // 是否绘制网格线，默认true
        xAxis.setDrawGridLines(true);
        // 是否绘制坐标轴,默认true
        xAxis.setDrawAxisLine(false);
        xAxis.setEnabled(true); // 轴线是否可编辑,默认true
        xAxis.setDrawLabels(true);  // 是否绘制标签,默认true
        // 此轴能显示的最大值
        xAxis.setGridColor(Color.GREEN);
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        rightYaxis.setDrawGridLines(false);
        rightYaxis.setDrawAxisLine(true);
        rightYaxis.setAxisLineColor(Color.GREEN);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setDrawAxisLine(true);
        leftYAxis.setAxisLineColor(Color.GREEN);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        leftYAxis.setGridColor(Color.GREEN);

        // *********************图例***************************** //
        Legend legend = lineChart.getLegend(); // 获取图例，但是在数据设置给chart之前是不可获取的
        legend.setEnabled(true);    // 是否绘制图例
//        mLineChart.notifyDataSetChanged();  // 通知有值变化，重绘，一般动态添加数据时用到

        // 像ListView那样的通知数据更新
        lineChart.notifyDataSetChanged();
    }

    public void setbarDataChar(BarChart barChart) {
        barChart.setBackgroundColor(Color.BLACK);
        barChart.setNoDataText("暂无数据");   // 没有数据时样式
        // *********************滑动相关*************************** //
        barChart.setDragEnabled(true);    // 可拖动,默认true
        barChart.setScaleXEnabled(true);  // X轴上的缩放,默认true
        barChart.setScaleYEnabled(false);  // Y轴上的缩放,默认true
        barChart.setDoubleTapToZoomEnabled(true); // 双击缩放,默认true
        barChart.setDragDecelerationEnabled(false);    // 抬起手指，继续滑动,默认true
        barChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        barChart.setViewPortOffsets(0, 0, 0, 0); //控制与手机边缘距离

        xAxis = barChart.getXAxis();
        // 是否绘制网格线，默认true
        xAxis.setDrawGridLines(true);
        // 是否绘制坐标轴,默认true
        xAxis.setDrawAxisLine(false);
        xAxis.setEnabled(true); // 轴线是否可编辑,默认true
        xAxis.setDrawLabels(true);  // 是否绘制标签,默认true
        // 此轴能显示的最大值
        xAxis.setGridColor(Color.GREEN);
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        leftYAxis = barChart.getAxisLeft();
        rightYaxis = barChart.getAxisRight();
        rightYaxis.setDrawGridLines(false);
        rightYaxis.setDrawAxisLine(true);
        rightYaxis.setAxisLineColor(Color.GREEN);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setDrawAxisLine(true);
        leftYAxis.setAxisLineColor(Color.GREEN);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        leftYAxis.setGridColor(Color.GREEN);
        leftYAxis.setSpaceTop(10);// 设置最大值到图标顶部的距离占所有数据范围的比例。默认10，y轴独有
        leftYAxis.setSpaceBottom(0);   // 同上，只不过是最小值距离底部比例。默认10，y轴独有
        leftYAxis.setInverted(true);// 反转轴,默认false
        rightYaxis.setInverted(true);

        // *********************图例***************************** //
        Legend legend = barChart.getLegend(); // 获取图例，但是在数据设置给chart之前是不可获取的
        legend.setEnabled(true);    // 是否绘制图例
//        mLineChart.notifyDataSetChanged();  // 通知有值变化，重绘，一般动态添加数据时用到

        // 像ListView那样的通知数据更新
        barChart.notifyDataSetChanged();
    }
}
