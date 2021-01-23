package com.example.luke_imagevideo_send.cehouyi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.bean.SaveData;
import com.example.luke_imagevideo_send.cehouyi.bean.SaveDataBack;
import com.example.luke_imagevideo_send.cehouyi.module.DataContract;
import com.example.luke_imagevideo_send.cehouyi.presenter.DataPresenter;
import com.example.luke_imagevideo_send.cehouyi.util.NumberPickerDivider;
import com.example.luke_imagevideo_send.cehouyi.view.QNumberPicker;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.views.Header;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainOutCHYActivity extends BaseActivity implements NumberPicker.Formatter , DataContract.View{

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.ivBluetooth)
    ImageView ivBluetooth;
    @BindView(R.id.tvSS)
    TextView tvSS;
    @BindView(R.id.tvTD)
    TextView tvTD;
    @BindView(R.id.tvEP)
    TextView tvEP;
    @BindView(R.id.tvElectric)
    TextView tvElectric;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.etZY)
    EditText etZY;
    @BindView(R.id.tvTT)
    TextView tvTT;
    @BindView(R.id.ivState)
    ImageView ivState;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvThickness)
    TextView tvThickness;
    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.onePicker)
    QNumberPicker onePicker;
    @BindView(R.id.tenPicker)
    QNumberPicker tenPicker;
    @BindView(R.id.hundredPicker)
    QNumberPicker hundredPicker;
    @BindView(R.id.thousandPicker)
    QNumberPicker thousandPicker;
    @BindView(R.id.llNumberPicker)
    LinearLayout llNumberPicker;
    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    @BindView(R.id.llTD)
    LinearLayout llTD;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.TDPicker)
    QNumberPicker TDPicker;
    @BindView(R.id.EPPicker)
    QNumberPicker EPPicker;
    @BindView(R.id.TTPicker)
    QNumberPicker TTPicker;
    @BindView(R.id.tvMax)
    RadioButton tvMax;
    @BindView(R.id.llMax)
    LinearLayout llMax;
    @BindView(R.id.tvMin)
    RadioButton tvMin;
    @BindView(R.id.llMin)
    LinearLayout llMin;
    @BindView(R.id.rbA)
    RadioButton rbA;
    @BindView(R.id.rbSave)
    RadioButton rbSave;
    @BindView(R.id.rbB)
    RadioButton rbB;
    @BindView(R.id.lineChartData)
    LineChart lineChartData;
    @BindView(R.id.etFW)
    EditText etFW;
    @BindView(R.id.etPY)
    EditText etPY;
    @BindView(R.id.etZM)
    EditText etZM;
    @BindView(R.id.etXY)
    EditText etXY;
    String tag = "SS";
    int i = 0;
    int Max = 0, Min = 0;
    boolean exit = false;
    Handler handler;
    List<Entry> entries = new ArrayList<>();
    List<String> dataList = new ArrayList<>();
    List<Integer> valueList = new ArrayList<>();
    List<List<Integer>> myValueList = new ArrayList<>();
    Threads thread = new Threads();
    DataPresenter dataPresenter;
    Map<String,Object> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTime();
        setlineDataChar();
        dataList.add("sengSu:5920m/s");
        dataList.add("tongDao:1");
        dataList.add("moShi:EE");
        dataList.add("zengYi:26");
        dataList.add("tanTou:BT-08");
        dataList.add("cunChu:f001");
        dataList.add("wenDuBC:关闭温度补偿");
        dataList.add("kuoZhi:500.00mm");
        dataList.add("jingDu:0.1mm");
        dataList.add("danWei:mm");
        dataList.add("jiaoZhun:单点校准");
        dataList.add("fanWei:50");
        dataList.add("pingYi:0");
        dataList.add("zaMen:22");
        dataList.add("xiaoYin:0.0");
        dataList.add("zuoBiaoSX:999.9mm");
        dataList.add("zuoBiaoXX:000.0mm");
        dataList.add("boXing:射频");
        dataList.add("yuYan:中文");
        new NumberPickerDivider().setNumberPickerDivider(onePicker);
        new NumberPickerDivider().setNumberPickerDivider(tenPicker);
        new NumberPickerDivider().setNumberPickerDivider(hundredPicker);
        new NumberPickerDivider().setNumberPickerDivider(thousandPicker);
        new NumberPickerDivider().setNumberPickerDivider(TDPicker);
        new NumberPickerDivider().setNumberPickerDivider(EPPicker);
        new NumberPickerDivider().setNumberPickerDivider(TTPicker);
        dataPresenter = new DataPresenter(this,this);
    }

    private void setlineDataChar() {
        // **************************图表本身一般样式**************************** //
        lineChartData.setBackgroundColor(Color.WHITE); // 整个图标的背景色
        Description description = new Description();  // 这部分是深度定制描述文本，颜色，字体等
        description.setText("厚度值表");
        description.setTextColor(Color.RED);
        lineChartData.setDescription(description);
        lineChartData.setNoDataText("暂无数据");   // 没有数据时样式
        lineChartData.setDrawGridBackground(false);    // 绘制区域的背景（默认是一个灰色矩形背景）将绘制，默认false
        lineChartData.setGridBackgroundColor(Color.WHITE);  // 绘制区域的背景色
        lineChartData.setDrawBorders(false);    // 绘制区域边框绘制，默认false
        lineChartData.setBorderColor(Color.GREEN); // 边框颜色
        lineChartData.setBorderWidth(2);   // 边框宽度,dp
        lineChartData.setMaxVisibleValueCount(14);  // 数据点上显示的标签，最大数量，默认100。且dataSet.setDrawValues(true);必须为true。只有当数据数量小于该值才会绘制标签
        // *********************滑动相关*************************** //
        lineChartData.setTouchEnabled(true); // 所有触摸事件,默认true
        lineChartData.setDragEnabled(true);    // 可拖动,默认true
        lineChartData.setScaleEnabled(true);   // 两个轴上的缩放,X,Y分别默认为true
        lineChartData.setScaleXEnabled(true);  // X轴上的缩放,默认true
        lineChartData.setScaleYEnabled(true);  // Y轴上的缩放,默认true
        lineChartData.setPinchZoom(true);  // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        lineChartData.setDoubleTapToZoomEnabled(true); // 双击缩放,默认true
        lineChartData.setDragDecelerationEnabled(true);    // 抬起手指，继续滑动,默认true
        lineChartData.setDragDecelerationFrictionCoef(0.9f);   // 摩擦系数,[0-1]，较大值速度会缓慢下降，0，立即停止;1,无效值，并转换为0.9999.默认0.9f.
        lineChartData.setOnChartValueSelectedListener(new OnChartValueSelectedListener() { // 值选择监听器
            @Override
            public void onValueSelected(Entry e, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
                // 未选中
            }
        });

        XAxis xAxis = lineChartData.getXAxis();    // 获取X轴
        YAxis yAxis = lineChartData.getAxisLeft(); // 获取Y轴,mLineChart.getAxis(YAxis.AxisDependency.LEFT);也可以获取Y轴
        lineChartData.getAxisRight().setEnabled(false);    // 不绘制右侧的轴线
        xAxis.setEnabled(true); // 轴线是否可编辑,默认true
        xAxis.setDrawLabels(true);  // 是否绘制标签,默认true
        xAxis.setDrawAxisLine(true);    // 是否绘制坐标轴,默认true
        xAxis.setDrawGridLines(false);   // 是否绘制网格线，默认true
        xAxis.setAxisMaximum(10); // 此轴能显示的最大值；
        xAxis.resetAxisMaximum();   // 撤销最大值；
        xAxis.setAxisMinimum(1);    // 此轴显示的最小值；
        xAxis.resetAxisMinimum();   // 撤销最小值；
//        yAxis.setStartAtZero(true);  // 从0开始绘制。已弃用。使用setAxisMinimum(float)；
//        yAxis.setInverted(true); // 反转轴,默认false
        yAxis.setSpaceTop(10);   // 设置最大值到图标顶部的距离占所有数据范围的比例。默认10，y轴独有
        // 算法：比例 = （y轴最大值 - 数据最大值）/ (数据最大值 - 数据最小值) ；
        // 用途：可以通过设置该比例，使线最大最小值不会触碰到图标的边界。
        // 注意：设置一条线可能不太好看，mLineChart.getAxisRight().setSpaceTop(34)也设置比较好;同时，如果设置最小值，最大值，会影响该效果
        yAxis.setSpaceBottom(10);   // 同上，只不过是最小值距离底部比例。默认10，y轴独有
        // yAxis.setShowOnlyMinMax(true);   // 没找到。。。，true, 轴上只显示最大最小标签忽略指定的数量（setLabelCount，如果forced = false).
        yAxis.setLabelCount(4, false); // 纵轴上标签显示的数量,数字不固定。如果force = true,将会画出明确数量，但是可能值导致不均匀，默认（6，false）
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);  // 标签绘制位置。默认再坐标轴外面
        xAxis.setGranularity(1); // 设置X轴值之间最小距离。正常放大到一定地步，标签变为小数值，到一定地步，相邻标签都是一样的。这里是指定相邻标签间最小差，防止重复。
        yAxis.setGranularity(1);    // 同上
        yAxis.setGranularityEnabled(false); // 是否禁用上面颗粒度限制。默认false
        // 轴颜色
        yAxis.setTextColor(Color.RED);  // 标签字体颜色
        yAxis.setTextSize(10);    // 标签字体大小，dp，6-24之间，默认为10dp
        yAxis.setTypeface(null);    // 标签字体
        yAxis.setGridColor(Color.GRAY);    // 网格线颜色，默认GRAY
        yAxis.setGridLineWidth(1);    // 网格线宽度，dp，默认1dp
        yAxis.setAxisLineColor(Color.RED);  // 坐标轴颜色，默认GRAY.测试到一个bug，假如左侧线只有1dp，
        // 那么如果x轴有线且有网格线，当刻度拉的正好位置时会覆盖到y轴的轴线，变为X轴网格线颜色，结局办法是，要么不画轴线，要么就是坐标轴稍微宽点
        xAxis.setAxisLineColor(Color.RED);
        yAxis.setAxisLineWidth(2);  // 坐标轴线宽度，dp，默认1dp
        yAxis.enableGridDashedLine(20, 10, 1);    // 网格线为虚线，lineLength，每段实线长度,spaceLength,虚线间隔长度，phase，起始点（进过测试，最后这个参数也没看出来干啥的）
        // 限制线
        LimitLine ll = new LimitLine(6.5f, "上限"); // 创建限制线, 这个线还有一些相关的绘制属性方法，自行看一下就行，没多少东西。
        yAxis.addLimitLine(ll); // 添加限制线到轴上
        yAxis.removeLimitLine(ll);  // 移除指定的限制线,还有其他的几个移除方法
        yAxis.setDrawLimitLinesBehindData(false); // 限制线在数据之后绘制。默认为false

        // X轴更多属性
        xAxis.setLabelRotationAngle(45);   // 标签倾斜
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // X轴绘制位置，默认是顶部

        yAxis.setDrawZeroLine(false);    // 绘制值为0的轴，默认false,其实比较有用的就是在柱形图，当有负数时，显示在0轴以下，其他的图这个可能会看到一些奇葩的效果
        yAxis.setZeroLineWidth(2);  // 0轴宽度
        yAxis.setZeroLineColor(Color.BLUE);   // 0轴颜色

        // *********************图例***************************** //
        Legend legend = lineChartData.getLegend(); // 获取图例，但是在数据设置给chart之前是不可获取的
        legend.setEnabled(true);    // 是否绘制图例
        legend.setTextColor(Color.GRAY);    // 图例标签字体颜色，默认BLACK
        legend.setTextSize(12); // 图例标签字体大小[6,24]dp,默认10dp
        legend.setTypeface(null);   // 图例标签字体
        legend.setWordWrapEnabled(false);    // 当图例超出时是否换行适配，这个配置会降低性能，且只有图例在底部时才可以适配。默认false
        legend.setMaxSizePercent(1f); // 设置，默认0.95f,图例最大尺寸区域占图表区域之外的比例
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);    // 图例显示位置，已弃用
        legend.setForm(Legend.LegendForm.CIRCLE);   // 设置图例的形状，SQUARE, CIRCLE 或者 LINE
        legend.setFormSize(8); // 图例图形尺寸，dp，默认8dp
        legend.setXEntrySpace(6);  // 设置水平图例间间距，默认6dp
        legend.setYEntrySpace(0);  // 设置垂直图例间间距，默认0
        legend.setFormToTextSpace(5);    // 设置图例的标签与图形之间的距离，默认5dp
        legend.setWordWrapEnabled(true);   // 图标单词是否适配。只有在底部才会有效，
        // 这个方法会把前面设置的图例都去掉，重置为指定的图例。
        legend.resetCustom();   // 去掉上面方法设置的图例，然后之前dataSet中设置的会重新显示。
        // ********************其他******************************* //
        lineChartData.setLogEnabled(false);    // 是否打印日志，默认false
//        mLineChart.notifyDataSetChanged();  // 通知有值变化，重绘，一般动态添加数据时用到
        lineChartData.animateX(3000, Easing.EasingOption.EaseInQuad); // 动画播放随时间变化的速率，有点像插值器。后面这个有的不能用

        // **************************图表本身特殊样式******************************** //
        lineChartData.setAutoScaleMinMaxEnabled(false);    // y轴是否自动缩放；当缩放时，y轴的显示会自动根据x轴范围内数据的最大最小值而调整。财务报表比较有用，默认false
        lineChartData.setKeepPositionOnRotation(false); // 设置当屏幕方向变化时，是否保留之前的缩放与滚动位置，默认：false
        // 像ListView那样的通知数据更新
        lineChartData.notifyDataSetChanged();
    }

    @OnClick({R.id.ivBluetooth, R.id.tvSS, R.id.llTD, R.id.tvEP, R.id.tvTT, R.id.ivState, R.id.tvCancle,
            R.id.tvSure, R.id.tvMenu, R.id.rbA, R.id.rbSave, R.id.rbB})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBluetooth:
                //蓝牙
                if (ivBluetooth.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.ic_bluetooth_off).getConstantState())) {
                    ivBluetooth.setImageResource(R.drawable.ic_bluetooth_on);
                } else {
                    ivBluetooth.setImageResource(R.drawable.ic_bluetooth_off);
                }
                break;
            case R.id.tvSS:
                tag = "SS";
                linearLayout.setVisibility(View.VISIBLE);
                onePicker.setVisibility(View.VISIBLE);
                tenPicker.setVisibility(View.VISIBLE);
                hundredPicker.setVisibility(View.VISIBLE);
                thousandPicker.setVisibility(View.VISIBLE);
                TDPicker.setVisibility(View.GONE);
                EPPicker.setVisibility(View.GONE);
                TTPicker.setVisibility(View.GONE);
                new NumberPickerDivider().init(onePicker, tenPicker, hundredPicker, thousandPicker, this, this, this, "SS");
                break;
            case R.id.llTD:
                tag = "TD";
                linearLayout.setVisibility(View.VISIBLE);
                onePicker.setVisibility(View.GONE);
                tenPicker.setVisibility(View.GONE);
                hundredPicker.setVisibility(View.GONE);
                thousandPicker.setVisibility(View.GONE);
                TDPicker.setVisibility(View.VISIBLE);
                EPPicker.setVisibility(View.GONE);
                TTPicker.setVisibility(View.GONE);
                new NumberPickerDivider().initTD(TDPicker, this, this, this, "TD");
                break;
            case R.id.tvEP:
                tag = "EP";
                linearLayout.setVisibility(View.VISIBLE);
                onePicker.setVisibility(View.GONE);
                tenPicker.setVisibility(View.GONE);
                hundredPicker.setVisibility(View.GONE);
                thousandPicker.setVisibility(View.GONE);
                TDPicker.setVisibility(View.GONE);
                EPPicker.setVisibility(View.VISIBLE);
                TTPicker.setVisibility(View.GONE);
                new NumberPickerDivider().initEP(EPPicker, this, this, this, "EP");
                break;
            case R.id.tvTT:
                tag = "TT";
                linearLayout.setVisibility(View.VISIBLE);
                onePicker.setVisibility(View.GONE);
                tenPicker.setVisibility(View.GONE);
                hundredPicker.setVisibility(View.GONE);
                thousandPicker.setVisibility(View.GONE);
                TDPicker.setVisibility(View.GONE);
                EPPicker.setVisibility(View.GONE);
                TTPicker.setVisibility(View.VISIBLE);
                new NumberPickerDivider().initTT(TTPicker, this, this, this, "TT");
                break;
            case R.id.ivState:
                //蓝牙
                if (ivState.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.ic_down).getConstantState())) {
                    ivState.setImageResource(R.drawable.ic_up);
                } else {
                    ivState.setImageResource(R.drawable.ic_down);
                }
                break;
            case R.id.tvCancle:
                linearLayout.setVisibility(View.GONE);
                break;
            case R.id.tvSure:
                linearLayout.setVisibility(View.GONE);
                if (tag.equals("SS")) {
                    tvSS.setText(String.valueOf(onePicker.getValue())
                            + String.valueOf(tenPicker.getValue())
                            + String.valueOf(hundredPicker.getValue())
                            + String.valueOf(thousandPicker.getValue()) + "m/s");
                } else if (tag.equals("TD")) {
                    tvTD.setText(NumberPickerDivider.TD[TDPicker.getValue()]);
                } else if (tag.equals("EP")) {
                    tvEP.setText(NumberPickerDivider.EP[EPPicker.getValue()]);
                } else if (tag.equals("TT")) {
                    tvTT.setText(NumberPickerDivider.TT[TTPicker.getValue()]);
                }
                break;
            case R.id.tvMenu:
                exit = true;
                Intent intent = new Intent(this, MenuActivity.class);
                if (valueList.size()!=0){
                    myValueList.add(valueList);
                }
                EventBus.getDefault().postSticky(myValueList);
                startActivityForResult(intent, Constant.TAG_ONE);
                break;
            case R.id.rbA:
                llMax.setVisibility(View.VISIBLE);
                llMin.setVisibility(View.VISIBLE);
                tvData.setVisibility(View.GONE);
                lineChartData.setVisibility(View.VISIBLE);
                break;
            case R.id.rbSave:
                if (valueList.size()!=0){
                    myValueList.add(valueList);
                }
//                data.put("time",tvTime.getText().toString());
//                data.put("soundVelocity",tvSS.getText().toString());
//                String value  = myValueList.toString();
//                data.put("data",value);
//                Gson gson=new Gson();
//                String jsonImgList = gson.toJson(data).toString();
                SaveData saveData = new SaveData();
                saveData.setTime(tvTime.getText().toString());
                saveData.setSoundVelocity(tvSS.getText().toString());
                saveData.setData(myValueList.toString());
                dataPresenter.getSaveData(saveData);
                break;
            case R.id.rbB:
                llMax.setVisibility(View.VISIBLE);
                llMin.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void showChart(int f) {
        tvThickness.setText(f + "mm");
        entries.add(new Entry(i, f));
        if (valueList.size()<30){
            valueList.add(f);
        }else {
            myValueList.add(valueList);
            valueList = new ArrayList<>();
        }
        if (myValueList.size()>=100){
            Toast.makeText(this, "数据集合已满请删除数据", Toast.LENGTH_SHORT).show();
        }
        i++;

        if (tvMax.getText().toString().equals("最大值")) {
            tvMax.setText(f + "mm");
            Max = f;
        } else if (f > Max) {
            tvMax.setText(f + "mm");
            Max = f;
        }
        if (tvMin.getText().toString().equals("最小值")) {
            tvMin.setText(f + "mm");
            Min = f;
        } else if (f < Min) {
            tvMin.setText(f + "mm");
            Min = f;
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label1");
        LineData lineData = new LineData(dataSet);
        lineChartData.setData(lineData);
        // 像ListView那样的通知数据更新
        lineChartData.notifyDataSetChanged();
        // 当前统计图表中最多在x轴坐标线上显示的总量
        lineChartData.setVisibleXRangeMaximum(5);
        lineChartData.moveViewToX(i);
    }

    /**
     * 底部弹出数字框方法
     *
     * @param value
     * @return
     */
    @Override
    public String format(int value) {
        Log.i("XXX", "format: value" + value);
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    // 初始化时间方法
    public void initTime() {
        // 时间变化
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                tvTime.setText((String) msg.obj);
                // 生成随机测试数
                int f = (int) ((Math.random()) * 20 + 50);
                tvData.setText(f + "mm");
                showChart(f);
            }
        };
        thread.start();
    }

    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (!exit) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String str = sdf.format(new Date());
                    handler.sendMessage(handler.obtainMessage(100, str));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.TAG_ONE && resultCode == RESULT_OK) {
            String filed = data.getStringExtra("filed");
            String value = data.getStringExtra("data");
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).indexOf(filed) != -1) {
                    String s = filed + ":" + value;
                    dataList.set(i, s);
                }
            }
        }
    }

    //数据上传回调
    @Override
    public void setSaveData(SaveDataBack saveDataBack) {
        Toast.makeText(this, "111", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSaveDataMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_out_chy;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }
}