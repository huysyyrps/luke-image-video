package com.example.luke_imagevideo_send;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BarGraphView extends View {
    private List<Item> mItemList = new ArrayList<>();;
    private Paint mPaint;
    private Paint mPaintTiemText;
    private Paint mPaintCurrenNumText;
    private Path mPath;
    private Path mSrc;
    private float mWPadding;//宽边距 X边距
    private float mHPadding;//高边距 Y边距
    private float mItemWidth; //item 的宽度
    private float mItemHeight; //item 的高度
    private float mItemNum; // item 的数量
    private float mMaxValue;

    public BarGraphView(Context context) {
        super(context);
        initPaint();
    }

    public BarGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public BarGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 添加数据的方法
     * @param item  例子：view.addData(new BarGraphView.Item("12-7",1651));
     */
    public void addData(Item item){
        mItemList.add(item);
        //计算添加到List中的最大值，并且给最大值增加2000上限
        mMaxValue = 0;
        if(!mItemList.isEmpty()) {
            for (int j = 0; j < mItemList.size(); j++) {
                float numA = mItemList.get(j).currenNum;
                if (mMaxValue < numA) {
                    mMaxValue = numA;
                }
            }
//            mMaxValue = mMaxValue + 2000;
        }
        invalidate();
    }

    public List<Item> getData(){
        return mItemList;
    }

    private void initPaint(){
        this.mPaint = new Paint();
        this.mPaintTiemText = new Paint();
        this.mPaintCurrenNumText = new Paint();
        this.mPath = new Path();
        this.mSrc = new Path();
    }
    private void initData(){
        if(!mItemList.isEmpty()) {
            mItemNum = mItemList.size();
        }
        mWPadding = getWidth()/20; //宽度内边距
        mHPadding = getHeight()/10; //高度内边距
        mItemWidth = 80;  //圆柱体的宽度
    }

    /**
     * 画底部横线
     * @param canvas 画布
     */
    private void drawXline(Canvas canvas){
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);
        canvas.drawLine(mWPadding,26,getWidth()-26,26,mPaint);
        canvas.drawLine(mWPadding,26,mWPadding,getHeight()-26,mPaint);
    }

    /**
     * 画圆柱 画日期 画数值
     * @param canvas 画布
     */
    private void drawItem(Canvas canvas){
        mPaint.reset();
        mPaintTiemText.reset();
        mPaintCurrenNumText.reset();
        mPath.reset();
        //柱状图画笔
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        if(!mItemList.isEmpty()) {
            mPaintTiemText.setTextSize(26);//设置字体大小
            canvas.drawText("0.0",
                    24,
                    24,
                    mPaintTiemText);
            //画剩下的圆柱
            for (int i = 0; i < mItemList.size(); i++) {
                Item item = mItemList.get(i);
                mItemHeight = (getHeight() - mHPadding * 3) * (item.currenNum / mMaxValue);
                RectF rectF = new RectF();
                rectF.left = mWPadding + mItemWidth * i;
                rectF.top = mItemHeight;
//                rectF.top = mBottomLine - mItemHeight;
                rectF.right = mWPadding + mItemWidth * (i + 1);
                rectF.bottom = 28;
//                rectF.bottom = mBottomLine;
                mSrc = new Path();//画单个圆柱
                mSrc.addRect(rectF, Path.Direction.CW);
                mPath.addPath(mSrc);//将单个圆柱添加到mPath中
                while (mWPadding + mItemWidth * i>getWidth()){
                    canvas.translate(80,0);
                }
                canvas.drawPath(mPath, mPaint);
            }
        }else {
            mPaintTiemText.setTextSize(getWidth()/20);
            canvas.drawText("无数据",getWidth()/2,getHeight()/2,mPaintTiemText);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initData();
        drawXline(canvas);
        drawItem(canvas);
    }
}