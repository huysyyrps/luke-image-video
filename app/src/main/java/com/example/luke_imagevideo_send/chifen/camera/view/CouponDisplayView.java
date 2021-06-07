package com.example.luke_imagevideo_send.chifen.camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CouponDisplayView extends LinearLayout {

    private Paint mPaint;
    /**
     * 半径
     */
    private float radius;
    /**
     * 圆数量
     */
    private int circleNum;

    private float remain;


    public CouponDisplayView(Context context) {
        super(context);
    }

    public CouponDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }


    public CouponDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < circleNum; i++) {
            float x = radius + remain / 2 + ((radius * 2) * i);
//            canvas.drawCircle(x, 0, radius, mPaint);
            canvas.drawCircle(radius, getHeight(), radius, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = w/2;
        if (remain == 0) {
            remain = (int) (w) % (2 * radius);
        }
        circleNum = 1;
    }
}
