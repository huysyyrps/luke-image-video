package com.example.luke_imagevideo_send.chifen.camera.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.example.luke_imagevideo_send.chifen.camera.bean.PointBean;

import java.util.LinkedList;

public class DrawView extends View {

    private LinkedList<PointBean> pointLists = new LinkedList<PointBean>();
    private LinkedList<Integer> rangeLists = new LinkedList<Integer>();

    private PointBean pointBean = new PointBean(-1, -1, -1, -1);
    private Path mPath = new Path();
    private int mPaintBarPenColor;
    private static int height = 30;
    private static int bottom = 10;
    int bitmapWidth = 0;
    int bitmapHeight = 0;
    private Bitmap mBitmap;
    private Bitmap mOriginBitmap;
    private Canvas mCanvas;
    boolean tag = false;
    private Matrix matrix;
    private float mProportion = 0;
    private Paint mBitmapPaint;
    private Paint paint = new Paint() {
        {
            setColor(Color.RED);
            setAntiAlias(true);
            setTextAlign(Paint.Align.CENTER);//居中显示
            setStrokeWidth(4.0f);
            setDither(true);
            setFilterBitmap(true);
            setStyle(Paint.Style.STROKE);
            setStrokeJoin(Paint.Join.ROUND);
            setStrokeCap(Paint.Cap.ROUND);
            setTextSize(44);
            setFakeBoldText(false);
            setTypeface(Typeface.DEFAULT);
            setTextAlign(Paint.Align.CENTER);//居中显示
            setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        }
    };

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        matrix = new Matrix();
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    public void clear() {
        if (pointBean != null) {
            pointBean.setStartX(-1);
            pointBean.setStartY(-1);
            pointBean.setEndX(-1);
            pointBean.setEndY(-1);
        }

        if (pointLists != null && pointLists.size() > 0) {
            pointLists.clear();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float proportion = (float) canvas.getHeight() / mBitmap.getWidth();
        if (proportion < 1) {
            mProportion = 0;
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            //绘制path
            canvas.drawPath(mPath, paint);
        } else {
            mProportion = proportion;
            matrix.reset();
            matrix.postScale(proportion, proportion);
            matrix.postTranslate((canvas.getWidth() - mBitmap.getWidth() * proportion) / 2, 0);
            canvas.drawBitmap(mBitmap, matrix, mBitmapPaint);
            //绘制path
            canvas.drawPath(mPath, paint);
        }
        if (pointLists != null && pointLists.size() > 0) {
            for (int i = 0; i < pointLists.size(); i++) {
                PointBean pb = pointLists.get(i);
                canvas.drawLine(pb.getStartX(), pb.getStartY(), pb.getEndX(), pb.getEndY(), paint);
                drawTrangle(canvas, paint, pb.getStartX(), pb.getStartY(), pb.getEndX(), pb.getEndY(), height, bottom);
                drawTrangle(canvas, paint, pb.getEndX(), pb.getEndY(), pb.getStartX(), pb.getStartY(), height, bottom);
            }
            if (tag) {
                for (int i = 0; i < pointLists.size(); i++) {
                    double a = (pointLists.get(i).getEndX() - pointLists.get(i).getStartX())
                            * (pointLists.get(i).getEndX() - pointLists.get(i).getStartX())
                            + (pointLists.get(i).getEndY() - pointLists.get(i).getStartY())
                            * (pointLists.get(i).getEndY() - pointLists.get(i).getStartY());
                    int length = (int) Math.sqrt(a);
                    if (i%2==0){
                        rangeLists.add(length);
                    }
                    Path mPath = new Path();
                    mPath.moveTo(pointLists.get(i).getStartX(), pointLists.get(i).getStartY());
                    mPath.lineTo(pointLists.get(i).getEndX(), pointLists.get(i).getEndY());
                    canvas.drawPath(mPath, paint);
                    canvas.drawTextOnPath("" + length, mPath, 0, -15, paint);
                }
            }
        }

        if (pointBean != null && pointBean.getStartX() != -1
                && pointBean.getStartY() != -1 && pointBean.getEndX() != -1
                && pointBean.getEndY() != -1) {
            canvas.drawLine(pointBean.getStartX(), pointBean.getStartY(), pointBean.getEndX(), pointBean.getEndY(), paint);
            drawTrangle(canvas, paint, pointBean.getStartX(), pointBean.getStartY(), pointBean.getEndX(), pointBean.getEndY(), height, bottom);
            drawTrangle(canvas, paint, pointBean.getEndX(), pointBean.getEndY(), pointBean.getStartX(), pointBean.getStartY(), height, bottom);
        }
    }

    /**
     * 绘制三角
     *
     * @param canvas
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param height
     * @param bottom
     */
    private void drawTrangle(Canvas canvas, Paint paintLine, float fromX, float fromY, float toX, float toY, int height, int bottom) {
        try {
            float juli = (float) Math.sqrt((toX - fromX) * (toX - fromX)
                    + (toY - fromY) * (toY - fromY));// 获取线段距离
            float juliX = toX - fromX;// 有正负，不要取绝对值
            float juliY = toY - fromY;// 有正负，不要取绝对值
            float dianX = toX - (height / juli * juliX);
            float dianY = toY - (height / juli * juliY);
            float dian2X = fromX + (height / juli * juliX);
            float dian2Y = fromY + (height / juli * juliY);
            //终点的箭头
            Path path = new Path();
            path.moveTo(toX, toY);// 此点为三边形的起点
            path.lineTo(dianX + (bottom / juli * juliY), dianY
                    - (bottom / juli * juliX));
            path.lineTo(dianX - (bottom / juli * juliY), dianY
                    + (bottom / juli * juliX));
            path.close(); // 使这些点构成封闭的三边形
            canvas.drawPath(path, paintLine);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp(event);
                break;
        }
        return true;
        //return super.onTouchEvent(event);
    }

    private void onActionDown(MotionEvent event) {
        try {
            if (pointBean == null) {
                pointBean = new PointBean(-1, -1, -1, -1);
            }
            pointBean.setStartX(event.getX());
            pointBean.setStartY(event.getY());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        invalidate();
    }

    private void onActionMove(MotionEvent event) {
        try {
            if (pointBean != null) {
                pointBean.setEndX(event.getX());
                pointBean.setEndY(event.getY());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        invalidate();
    }

    private void onActionUp(MotionEvent event) {
        try {
            if (pointBean != null) {
                pointBean.setEndX(event.getX());
                pointBean.setEndY(event.getY());
                PointBean pb = new PointBean();
                pb.setStartX(pointBean.getStartX());
                pb.setStartY(pointBean.getStartY());
                pb.setEndX(pointBean.getEndX());
                pb.setEndY(pointBean.getEndY());
                pointLists.add(pb);
                pointBean.setStartX(-1);
                pointBean.setStartY(-1);
                pointBean.setEndX(-1);
                pointBean.setEndY(-1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tag = true;
        invalidate();
    }

    /**
     * 撤回
     */
    public void undo() {
        if (rangeLists.size() > 0) {
            pointLists.removeLast();
            rangeLists.removeLast();
            invalidate();
        }
    }

    /**
     * 修改画笔颜色
     * @param color
     */
    public void setPenColor(@ColorInt int color) {
        mPaintBarPenColor = color;
        paint.setColor(color);
    }

    /**
     * 加载图片
     */
    public void loadImage(Bitmap bitmap, int width, int height) {
        bitmapHeight = height;
        bitmapWidth = width;
        mOriginBitmap = bitmap;
        mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Matrix matrix = new Matrix();
        matrix.setTranslate(10,10);
        matrix.postScale(height/mBitmap.getHeight(), width/mBitmap.getWidth()*1.8f);
        //获取新的bitmap
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//        mOriginBitmap = Bitmap.createBitmap(mOriginBitmap, 0, 0, mOriginBitmap.getWidth(), mOriginBitmap.getHeight(), matrix, true);
        mCanvas = new Canvas(mBitmap);
        invalidate();
    }

    /**
     * @return 当前画布上的内容
     */
    public Bitmap getImageBitmap() {
        return mBitmap;
    }
}