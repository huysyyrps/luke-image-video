package com.example.luke_imagevideo_send.cehouyi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.NumberPickerDivider;
import com.example.luke_imagevideo_send.cehouyi.view.QNumberPicker;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.views.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainOutCHYActivity extends BaseActivity implements NumberPicker.Formatter {

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
    @BindView(R.id.llA)
    LinearLayout llA;
    @BindView(R.id.llSave)
    LinearLayout llSave;
    @BindView(R.id.llB)
    LinearLayout llB;
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
    String tag = "SS";
    Handler handler;
    List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTime();
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
    }

    @OnClick({R.id.ivBluetooth, R.id.tvSS, R.id.llTD, R.id.tvEP, R.id.tvTT, R.id.ivState, R.id.tvCancle,
            R.id.tvSure, R.id.tvMenu, R.id.rbA, R.id.llSave, R.id.rbB})
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
                Intent intent = new Intent(this, MenuActivity.class);
                startActivityForResult(intent, Constant.TAG_ONE);
                break;
            case R.id.rbA:
                llMax.setVisibility(View.VISIBLE);
                llMin.setVisibility(View.VISIBLE);
                break;
            case R.id.llSave:
                break;
            case R.id.rbB:
                llMax.setVisibility(View.VISIBLE);
                llMin.setVisibility(View.VISIBLE);
                break;
        }
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
//        Toast.makeText(this, dataList.size()+"", Toast.LENGTH_SHORT).show();
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