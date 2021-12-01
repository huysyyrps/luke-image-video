package com.example.luke_imagevideo_send.cehouyi.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.GetTime;
import com.example.luke_imagevideo_send.cehouyi.util.GetTimeCallBack;
import com.example.luke_imagevideo_send.cehouyi.util.NumberPickerDivider;
import com.example.luke_imagevideo_send.cehouyi.util.getMyTestData;
import com.example.luke_imagevideo_send.cehouyi.view.myView;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.DialogCallBackTwo;
import com.example.luke_imagevideo_send.http.views.StatusBarUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

public class MainLK600Activity extends BaseActivity {

    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.barChart)
    BarChart barChart;
    @BindView(R.id.btnA)
    Button btnA;
    @BindView(R.id.btnB)
    Button btnB;
    @BindView(R.id.btnSound)
    Button btnSound;
    @BindView(R.id.btnPass)
    Button btnPass;
    @BindView(R.id.btnTable)
    Button btnTable;
    @BindView(R.id.btnProbe)
    Button btnProbe;
    @BindView(R.id.tvElectric)
    TextView tvElectric;
    @BindView(R.id.btnPattern)
    Button btnPattern;
    @BindView(R.id.btnTime)
    Button btnTime;
    @BindView(R.id.tvMax)
    Button tvMax;
    @BindView(R.id.tvMin)
    Button tvMin;
    @BindView(R.id.btnData)
    Button btnData;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvGain)
    TextView tvGain;
    @BindView(R.id.lGain)
    LinearLayout lGain;
    @BindView(R.id.tvMove)
    TextView tvMove;
    @BindView(R.id.lMove)
    LinearLayout lMove;
    @BindView(R.id.tvGate)
    TextView tvGate;
    @BindView(R.id.lgate)
    LinearLayout lgate;
    @BindView(R.id.tvClear)
    TextView tvClear;
    @BindView(R.id.lClear)
    LinearLayout lClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        new StatusBarUtils().setWindowStatusBarColor(MainLK600Activity.this, R.color.black);
        new getMyTestData().initLineData(lineChart, barChart,tvMax,tvMin,btnData);
        new myView().setlineDataChar(lineChart);
        new myView().setbarDataChar(barChart);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_lk600;
    }

    @Override
    protected boolean isHasHeader() {
        return false;
    }

    @Override
    protected void rightClient() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取当前时间
        new GetTime().initTime(new GetTimeCallBack() {
            @Override
            public void backTime(String time) {
                btnTime.setText(time.split(" ")[1]+"");
            }
        });
    }

    @OnClick({R.id.btnA, R.id.btnB,R.id.btnSound, R.id.btnPass, R.id.btnProbe, R.id.btnPattern, R.id.btnSave, R.id.lGain, R.id.lMove, R.id.lgate, R.id.lClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnA:
                lineChart.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.GONE);
                break;
            case R.id.btnB:
                lineChart.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSound://声速
                break;
            case R.id.btnPass://通道
                break;
            case R.id.btnProbe://探头
                OptionPicker picker = new OptionPicker(this, NumberPickerDivider.TT);
                picker.setTextSize(15);
                picker.setCancelTextColor(Color.RED);
                picker.setAnimationStyle(R.style.PermissionAnimFade);
                picker.setSubmitTextColor(getResources().getColor(R.color.color_bg_selected1));
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        btnProbe.setText(option);
                    }
                });
                picker.show();
                break;
            case R.id.btnPattern://回波模式E
                if (btnPattern.getText().toString().equals("E")){
                    btnPattern.setText("EE");
                }else if (btnPattern.getText().toString().equals("EE")){
                    btnPattern.setText("E");
                }
                break;
            case R.id.btnSave://保存
                break;
            case R.id.lGain://增益
                showDialog("gain");
                break;
            case R.id.lMove://平移
                showDialog("move");
                break;
            case R.id.lgate://闸门
                showDialog("gate");
                break;
            case R.id.lClear://消隐
                showDialog("clear");
                break;
        }
    }

    private void showDialog(String tag){
        String title = "";
        if (tag.equals("gain")){
            title = "请输入增益值";
        }else if (tag.equals("move")){
            title = "请输入平移值";
        }else if (tag.equals("gate")){
            title = "请输入闸门值";
        }else if (tag.equals("clear")){
            title = "请输入消隐值";
        }
        new AlertDialogUtil(this).showLk600(title, new DialogCallBackTwo() {
            @Override
            public void confirm(String name, Dialog dialog, EditText editText) {
                if (tag.equals("gain")){
                    tvGain.setText(name);
                }else if (tag.equals("move")){
                    tvMove.setText(name);
                }else if (tag.equals("gate")){
                    tvGate.setText(name);
                }else if (tag.equals("clear")){
                    tvClear.setText(name);
                }
                dialog.dismiss();
            }

            @Override
            public void cancel(String name, Dialog dialog) {
                dialog.dismiss();
            }
        });

    }
}