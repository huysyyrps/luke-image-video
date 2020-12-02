package com.example.luke_imagevideo_send.cehouyi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.bean.Test;
import com.example.luke_imagevideo_send.cehouyi.module.MainContract;
import com.example.luke_imagevideo_send.cehouyi.presenter.MainPresenter;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainCHYActivity extends BaseActivity implements NumberPicker.OnValueChangeListener,
        NumberPicker.OnScrollListener, NumberPicker.Formatter{

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rb5)
    RadioButton rb5;
    @BindView(R.id.rb6)
    RadioButton rb6;
    @BindView(R.id.rb7)
    RadioButton rb7;
    @BindView(R.id.rb8)
    RadioButton rb8;
    @BindView(R.id.ivBluetooth)
    ImageView ivBluetooth;
    @BindView(R.id.tvSS)
    TextView tvSS;
    @BindView(R.id.tvSMZT)
    TextView tvSMZT;
    @BindView(R.id.ivElectric)
    TextView ivElectric;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.textViewSMZT)
    TextView textViewSMZT;
    @BindView(R.id.onePicker)
    NumberPicker onePicker;
    @BindView(R.id.tenPicker)
    NumberPicker tenPicker;
    @BindView(R.id.hundredPicker)
    NumberPicker hundredPicker;
    @BindView(R.id.thousandPicker)
    NumberPicker thousandPicker;
    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvSure)
    TextView tvSure;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setNumberPickerDivider(onePicker);
        setNumberPickerDivider(tenPicker);
        setNumberPickerDivider(hundredPicker);
        setNumberPickerDivider(thousandPicker);
        init();
//        mainPresenter = new MainPresenter(this,this);
    }

    private void init() {
        String[] one = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        onePicker.setFormatter(this);
        onePicker.setOnValueChangedListener(this);
        onePicker.setOnScrollListener(this);
        onePicker.setDisplayedValues(one);
        onePicker.setMinValue(0);
        onePicker.setMaxValue(one.length - 1);
        onePicker.setValue(1);
        //设置为对当前值不可编辑
        onePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] ten = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        tenPicker.setFormatter(this);
        tenPicker.setOnValueChangedListener(this);
        tenPicker.setOnScrollListener(this);
        tenPicker.setDisplayedValues(ten);
        tenPicker.setMinValue(0);
        tenPicker.setMaxValue(ten.length - 1);
        tenPicker.setValue(4);
        //设置为对当前值不可编辑
        tenPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] hundred = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        hundredPicker.setFormatter(this);
        hundredPicker.setOnValueChangedListener(this);
        hundredPicker.setOnScrollListener(this);
        hundredPicker.setDisplayedValues(hundred);
        hundredPicker.setMinValue(0);
        hundredPicker.setMaxValue(hundred.length - 1);
        hundredPicker.setValue(5);
        //设置为对当前值不可编辑
        hundredPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] thousand = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        thousandPicker.setFormatter(this);
        thousandPicker.setOnValueChangedListener(this);
        thousandPicker.setOnScrollListener(this);
        thousandPicker.setDisplayedValues(thousand);
        thousandPicker.setMinValue(0);
        thousandPicker.setMaxValue(thousand.length - 1);
        thousandPicker.setValue(2);
        //设置为对当前值不可编辑
        thousandPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        onePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        tenPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        hundredPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        thousandPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        //这里设置为不循环显示，默认值为true
        onePicker.setWrapSelectorWheel(false);
        tenPicker.setWrapSelectorWheel(false);
        hundredPicker.setWrapSelectorWheel(false);
        thousandPicker.setWrapSelectorWheel(false);
    }

    @Override
    public String format(int value) {
        Log.i("XXX", "format: value" + value);
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch (scrollState) {
            case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                Log.i("XXX", "onScrollStateChange: 后续滑动(飞呀飞，根本停下来)");
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                Log.i("XXX", "onScrollStateChange: 不滑动");
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                Log.i("XXX", "onScrollStateChange: 滑动中");
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("XXX", "onValueChange: 原来的值 " + oldVal + "--新值: " + newVal);
    }

    /**
     * 设置picker分割线的宽度(分割线的粗细)
     */
    private void setNumberPickerDivider(NumberPicker picker) {
        Field[] fields = NumberPicker.class.getDeclaredFields();
        for (Field f : fields) {
            if (f.getName().equals("mSelectionDividerHeight")) {
                f.setAccessible(true);
                try {
                    f.set(picker, 1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_chy;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @OnClick({R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4, R.id.rb5, R.id.rb6, R.id.rb7, R.id.rb8, R.id.ivBluetooth, R.id.tvSMZT, R.id.tvSS,R.id.tvCancle, R.id.tvSure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb1:
                tvData.setText("0.0");
                break;
            case R.id.rb2:
                mainPresenter.getTest();
                break;
            case R.id.rb3:
                break;
            case R.id.rb4:
                break;
            case R.id.rb5:
                break;
            case R.id.rb6:
                break;
            case R.id.rb7:
                if (rb7.getText().toString().equals("英寸")) {
                    rb7.setText("毫米");
                    tvUnit.setText("mm");
                } else if (rb7.getText().toString().equals("毫米")) {
                    rb7.setText("英寸");
                    tvUnit.setText("in");
                }
                break;
            case R.id.rb8:
                break;
            case R.id.ivBluetooth:
                if (ivBluetooth.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.ic_bluetooth_off).getConstantState())) {
                    ivBluetooth.setImageResource(R.drawable.ic_bluetooth_on);
                } else {
                    ivBluetooth.setImageResource(R.drawable.ic_bluetooth_off);
                }
                break;
            case R.id.tvSMZT:
                if (tvSMZT.getText().toString().equals("SCAN")) {
                    tvSMZT.setText("CLOSE");
                    tvSMZT.setTextColor(getResources().getColor(R.color.red));
                    textViewSMZT.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tvSMZT.setText("SCAN");
                    tvSMZT.setTextColor(getResources().getColor(R.color.color_bg_selected));
                    textViewSMZT.setTextColor(getResources().getColor(R.color.color_bg_selected));
                }
                break;
            case R.id.tvSS:
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCancle:
                linearLayout.setVisibility(View.GONE);
                break;
            case R.id.tvSure:
                linearLayout.setVisibility(View.GONE);
                tvSS.setText(String.valueOf(onePicker.getValue())
                        + String.valueOf(tenPicker.getValue())
                        + String.valueOf(hundredPicker.getValue())
                        + String.valueOf(thousandPicker.getValue()));
                break;
        }
    }

//    @Override
//    public void setTest(List<Test> test) {
//        Toast.makeText(this, test.get(0).getDate(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void setTestMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
}