package com.example.luke_imagevideo_send.cehouyi.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by yuliang.zhao on 2015/3/5.
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    

    @Override
        public void scrollTo(int x, int y) {
         if (getAdapter()==null || x>getWidth()*(getAdapter().getCount()-2)){
            return;
        }
        super.scrollTo(x, y);
    }
}
