package com.example.luke_imagevideo_send.chifen.camera.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import com.example.luke_imagevideo_send.R;

import java.util.LinkedList;

public class DrawingView extends ImageView {
    private static final String TAG = "DrawingView";
    private static final float TOUCH_TOLERANCE = 4;
    private Bitmap mBitmap;
    private Bitmap mOriginBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private boolean mDrawMode;
    private float mX, mY;
    private float s, e;
    private float mProportion = 0;
    private LinkedList<DrawPath> savePath;
    private DrawPath mLastDrawPath;
    private Matrix matrix;
    private float mPaintBarPenSize;
    private int mPaintBarPenColor;

    private int mode = 0;// 初始状态
    private static final int MODE_ZOOM = 2;//缩放
    private Rect mSrcRect, mDestRect;
    Bitmap bitmapLeft,bitmapRight;
    private static int height = 30;
    private static int bottom = 10;


    /** 两个手指的中间点 */
    private PointF midPoint = new PointF(0,0);

    public DrawingView(Context c) {
        this(c, null);
    }

    public DrawingView(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
    }

    public DrawingView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        bitmapLeft = BitmapFactory.decodeResource(getResources(),R.drawable.ic_downleft);
        bitmapRight = BitmapFactory.decodeResource(getResources(),R.drawable.ic_downright);
        init();
    }

    private void init() {
        Log.d(TAG, "init: ");
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mDrawMode = false;
        savePath = new LinkedList<>();
        matrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mBitmap != null) {
            if ((mBitmap.getHeight() > heightSize) && (mBitmap.getHeight() > mBitmap.getWidth())) {
                widthSize = heightSize * mBitmap.getWidth() / mBitmap.getHeight();
            } else if ((mBitmap.getWidth() > widthSize) && (mBitmap.getWidth() > mBitmap.getHeight())) {
                heightSize = widthSize * mBitmap.getHeight() / mBitmap.getWidth();
            } else {
                heightSize = mBitmap.getHeight();
                widthSize = mBitmap.getWidth();
            }
        }
        Log.d(TAG, "onMeasure: heightSize: " + heightSize + " widthSize: " + widthSize);
        setMeasuredDimension(widthSize, heightSize);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据图片尺寸缩放图片，同样只考虑了高大于宽的情况
//        float proportion = (float) canvas.getHeight() / mBitmap.getHeight();
//        if (proportion < 1) {
//            mProportion = proportion;
//            matrix.reset();
//            matrix.postScale(proportion, proportion);
//            matrix.postTranslate((canvas.getWidth() - mBitmap.getWidth() * proportion) / 2, 0);
//            canvas.drawBitmap(mBitmap, matrix, mBitmapPaint);
//        } else {
//            mProportion = 0;
//            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
//        }
        float proportion = (float) canvas.getHeight() / mBitmap.getWidth();
        if (proportion < 1) {
            mProportion = 0;
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        } else {
            mProportion = proportion;
            matrix.reset();
            matrix.postScale(proportion, proportion);
            matrix.postTranslate((canvas.getWidth() - mBitmap.getWidth() * proportion) / 2, 0);
            canvas.drawBitmap(mBitmap, matrix, mBitmapPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果你的界面有多个模式，你需要有个变量来判断当前是否可draw
        if (!mDrawMode) {
            return false;
        }
        float x;
        float y;
        if (mProportion != 0) {
            x = (event.getX()) / mProportion;
            y = event.getY() / mProportion;
        } else {
            x = event.getX();
            y = event.getY();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // This happens when we undo a path
                if (mLastDrawPath != null) {
                    mPaint.setColor(mPaintBarPenColor);
                    mPaint.setStrokeWidth(mPaintBarPenSize);
                }
                mPath = new Path();
                mPath.reset();
                mPath.moveTo(x, y);
                s = x;
                e = y;
                mX = x;
                mY = y;
                mCanvas.drawPath(mPath, mPaint);
//                mSrcRect = new Rect((int) s, (int)e, (int) s, (int)e);
//                mDestRect = new Rect((int) s, (int)e, (int) s, (int)e);
//                mCanvas.drawTextOnPath("111", mPath, s, e, mPaint);
//                drawTrangle(mCanvas, mPaint, s-15, s-10, s+15, s-10, height, bottom);
//                Path path1 = new Path();
//                path1.lineTo(s, e);
//                mCanvas.drawTextOnPath("qq", path1, 0, 0, mPaint);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
                mCanvas.drawPath(mPath, mPaint);
                mCanvas.drawLine(mX, mY, (x + mX) / 2, (y + mY) / 2, mPaint);
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(mX, mY);
                double a = (y - e) * (y - e) + (x - s) * (x - s);
                int length = (int) Math.sqrt(a);
                mCanvas.drawPath(mPath, mPaint);
//                mCanvas.drawBitmap(bitmapLeft, s, e, mPaint);
                mCanvas.drawTextOnPath("" + length, mPath, 0, -15, mPaint);
//                Path path1 = new Path();
//                path1.lineTo(x, y);
//                mCanvas.drawTextOnPath("" + length, path1, 0, 0, mPaint);//vOffset设置垂直方向位移的距离。
//                mLastDrawPath = new DrawPath(mPath, mPaint.getColor(), mPaint.getStrokeWidth());
//                savePath.add(mLastDrawPath);
                mPath = null;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 绘制三角
     * @param canvas
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param height
     * @param bottom
     */
    private void drawTrangle(Canvas canvas, Paint paintLine, float fromX, float fromY, float toX, float toY, int height, int bottom){
        try{
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
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void initializePen() {
        mDrawMode = true;
        mPaint = null;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(44);
        mPaint.setStrokeWidth((float) 0.5);
        mPaint.setFakeBoldText(false);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextAlign(Paint.Align.CENTER);//居中显示
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    @Override
    public void setBackgroundColor(int color) {
        mCanvas.drawColor(color);
        super.setBackgroundColor(color);
    }

    /**
     * This method should ONLY be called by clicking paint toolbar(outer class)
     */
    public void setPenSize(float size) {
        mPaintBarPenSize = size;
        mPaint.setStrokeWidth(size);
    }

    /**
     * This method should ONLY be called by clicking paint toolbar(outer class)
     */
    public void setPenColor(@ColorInt int color) {
        mPaintBarPenColor = color;
        mPaint.setColor(color);
    }

    public void loadImage(Bitmap bitmap,int width,int height) {
        Log.d(TAG, "loadImage: ");
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

    public void undo() {
        Log.d(TAG, "undo: recall last path");
        if (savePath != null && savePath.size() > 0) {
            // 清空画布
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            loadImage(mOriginBitmap,1,1);

            savePath.removeLast();

            // 将路径保存列表中的路径重绘在画布上 遍历绘制
            for (DrawPath dp : savePath) {
                mPaint.setColor(dp.getPaintColor());
                mPaint.setStrokeWidth(dp.getPaintWidth());
                mCanvas.drawPath(dp.path, mPaint);
            }
            invalidate();
        }
    }

    /**
     * 路径对象
     */
    private class DrawPath {
        Path path;
        int paintColor;
        float paintWidth;

        DrawPath(Path path, int paintColor, float paintWidth) {
            this.path = path;
            this.paintColor = paintColor;
            this.paintWidth = paintWidth;
        }

        int getPaintColor() {
            return paintColor;
        }

        float getPaintWidth() {
            return paintWidth;
        }
    }
}