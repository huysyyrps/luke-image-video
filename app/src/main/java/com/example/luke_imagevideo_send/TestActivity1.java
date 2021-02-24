package com.example.luke_imagevideo_send;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity1 extends AppCompatActivity {
//    BarGraphView1 barChartView;
    int i = 0;
    List<Item> arrayList = new ArrayList<>();
    @BindView(R.id.mBarChart)
    BarChart mBarChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    BarData data;
    List<BarEntry> entries = new ArrayList<>(); //定义一个数据容器

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        ButterKnife.bind(this);
        Threads thread = new Threads();
        thread.start();
        initBarChart(mBarChart); //初始化一个柱状图
//        mBarChart.setData(setBarData()); //给柱状图添加数据
//        mBarChart.invalidate(); //让柱状图填充数据后刷新
//        barChartView = (BarGraphView1) findViewById(R.id.bar_chart);


//        BarGraphView1.BarChartItemBean[] items = new BarGraphView1.BarChartItemBean[]{
//                new BarGraphView1.BarChartItemBean("餐饮", 300),
//                new BarGraphView1.BarChartItemBean("学习", 200),
//                new BarGraphView1.BarChartItemBean("旅行", 270),
//                new BarGraphView1.BarChartItemBean("购物", 110),
//                new BarGraphView1.BarChartItemBean("人际关系", 120),
//                new BarGraphView1.BarChartItemBean("娱乐", 80),
//                new BarGraphView1.BarChartItemBean("投资", 110),
//                new BarGraphView1.BarChartItemBean("教育", 280),
//                new BarGraphView1.BarChartItemBean("教育", 820),
//                new BarGraphView1.BarChartItemBean("教育", 620)
//        };
//        barChartView.setItems(items);
    }
    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Item item = new Item("", new Random().nextInt(100));
                    arrayList.add(item);
                    entries.add(new BarEntry(i, new Random().nextInt(100)));
                    BarDataSet barDataSet = new BarDataSet(entries, "测试数据");
                    BarData barData = new BarData(barDataSet);
                    mBarChart.setData(barData); //给柱状图添加数据
                    barData.setBarWidth(1.2f);
                    mBarChart.setVisibleXRangeMaximum(20);
                    mBarChart.setVisibleXRangeMinimum(20);
//                    mBarChart.notifyDataSetChanged();
                    mBarChart.invalidate(); //让柱状图填充数据后刷新
                    mBarChart.moveViewToX(i);
                    i += 1;
//                    barChartView.setItems(arrayList);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BarChart initBarChart(BarChart barChart) {
        barChart.setDrawBarShadow(false); // 设置每条柱子的阴影不显示
        barChart.setDrawValueAboveBar(true); // 设置每条柱子的数值显示
        barChart.setNoDataText("暂无数据");   // 没有数据时样式
        XAxis xAxis = barChart.getXAxis(); // 获取柱状图的x轴
        YAxis yAxisLeft = barChart.getAxisLeft(); // 获取柱状图左侧的y轴
        YAxis yAxisRight = barChart.getAxisRight(); // 获取柱状图右侧的y轴
        setAxis(xAxis, yAxisLeft, yAxisRight); //调用方法设置柱状图的轴线
        return barChart;
    }

    public void setAxis(XAxis xAxis, YAxis leftAxis, YAxis rightAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 这里设置x轴在柱状图底部显示
        xAxis.setAxisLineWidth(1); //设置x轴宽度
        xAxis.setAxisMinimum(0); //设置x轴从0开始绘画
        xAxis.setDrawAxisLine(true); //设置x轴的轴线显示
        xAxis.setDrawGridLines(false);//设置x轴的表格线不显示
        xAxis.setEnabled(true); // 设置x轴显示

        leftAxis.setAxisMinimum(0); //设置y轴从0刻度开始
        leftAxis.setDrawGridLines(false); // 这里设置左侧y轴不显示表格线
        leftAxis.setDrawAxisLine(true); // 这里设置左侧y轴显示轴线
        leftAxis.setAxisLineWidth(1); //设置y轴宽度
        leftAxis.setEnabled(true); //设置左侧的y轴显示

        rightAxis.setAxisMinimum(0); //设置y轴从0刻度开始
        rightAxis.setDrawGridLines(false);// 这里设置右侧y轴不显示表格线
        rightAxis.setDrawAxisLine(true); // 这里设置右侧y轴显示轴线
        rightAxis.setAxisLineWidth(1); //设置右侧y轴宽度
        rightAxis.setEnabled(false); //设置右侧的y轴显示

    }
}