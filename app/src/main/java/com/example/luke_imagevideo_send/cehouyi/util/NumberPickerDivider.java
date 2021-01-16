package com.example.luke_imagevideo_send.cehouyi.util;

import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.example.luke_imagevideo_send.cehouyi.activity.MainOutCHYActivity;

import java.lang.reflect.Field;

public class NumberPickerDivider {
    public static final  String[] TD = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static final  String[] EP = {"EP", "PE"};
    public static final  String[] TT = {"BT-08", "XT-06", "GT-350", "ZT-102", "15P6Y-H", "P-153-H"};
    /**
     * 设置picker分割线的宽度(分割线的粗细)
     */
    public void setNumberPickerDivider(NumberPicker picker) {
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

    public void init(NumberPicker onePicker, NumberPicker tenPicker, NumberPicker hundredPicker, NumberPicker thousandPicker,
                     MainOutCHYActivity mainOutCHYActivity, MainOutCHYActivity outCHYActivity, MainOutCHYActivity chyActivity, String tag) {
        String[] one = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        onePicker.setFormatter(mainOutCHYActivity);
        onePicker.setDisplayedValues(one);
        onePicker.setMinValue(0);
        onePicker.setMaxValue(one.length - 1);
        onePicker.setValue(1);
        //设置为对当前值不可编辑
        onePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] ten = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        tenPicker.setFormatter(mainOutCHYActivity);
        tenPicker.setDisplayedValues(ten);
        tenPicker.setMinValue(0);
        tenPicker.setMaxValue(ten.length - 1);
        tenPicker.setValue(4);
        //设置为对当前值不可编辑
        tenPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] hundred = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        hundredPicker.setFormatter(mainOutCHYActivity);
        hundredPicker.setDisplayedValues(hundred);
        hundredPicker.setMinValue(0);
        hundredPicker.setMaxValue(hundred.length - 1);
        hundredPicker.setValue(5);
        //设置为对当前值不可编辑
        hundredPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] thousand = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        thousandPicker.setFormatter(mainOutCHYActivity);
        thousandPicker.setDisplayedValues(thousand);
        thousandPicker.setMinValue(0);
        thousandPicker.setMaxValue(thousand.length - 1);
        thousandPicker.setValue(2);
        //设置为对当前值不可编辑
        thousandPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        //这里设置为不循环显示，默认值为true
        onePicker.setWrapSelectorWheel(false);
        tenPicker.setWrapSelectorWheel(false);
        hundredPicker.setWrapSelectorWheel(false);
        thousandPicker.setWrapSelectorWheel(false);
    }

    //通道
    public void initTD(NumberPicker TDPicker, MainOutCHYActivity mainOutCHYActivity, MainOutCHYActivity outCHYActivity, MainOutCHYActivity chyActivity, String tag) {
        TDPicker.setFormatter(mainOutCHYActivity);
        TDPicker.setDisplayedValues(TD);
        TDPicker.setMinValue(0);
        TDPicker.setMaxValue(TD.length - 1);
        TDPicker.setValue(1);
        //设置为对当前值不可编辑
        TDPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        //这里设置为不循环显示，默认值为true
        TDPicker.setWrapSelectorWheel(false);
    }

    //EP
    public void initEP(NumberPicker EPPicker, MainOutCHYActivity mainOutCHYActivity, MainOutCHYActivity outCHYActivity, MainOutCHYActivity chyActivity, String tag) {
        EPPicker.setFormatter(mainOutCHYActivity);
        EPPicker.setDisplayedValues(EP);
        EPPicker.setMinValue(0);
        EPPicker.setMaxValue(EP.length - 1);
        EPPicker.setValue(1);
        //设置为对当前值不可编辑
        EPPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        EPPicker.setWrapSelectorWheel(false);
    }

    //探头
    public void initTT(NumberPicker TTPicker, MainOutCHYActivity mainOutCHYActivity, MainOutCHYActivity outCHYActivity, MainOutCHYActivity chyActivity, String tag) {
        TTPicker.setFormatter(mainOutCHYActivity);
        TTPicker.setDisplayedValues(TT);
        TTPicker.setMinValue(0);
        TTPicker.setMaxValue(TT.length - 1);
        TTPicker.setValue(1);
        //设置为对当前值不可编辑
        TTPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        TTPicker.setWrapSelectorWheel(false);
    }
}
