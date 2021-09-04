package com.example.luke_imagevideo_send.cehouyi.util;

import android.widget.NumberPicker;

import java.lang.reflect.Field;

public class PickerDivider {
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
}
