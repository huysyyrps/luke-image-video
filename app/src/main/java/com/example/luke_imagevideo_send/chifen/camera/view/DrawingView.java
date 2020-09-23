package com.example.luke_imagevideo_send.chifen.camera.view;

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

public class DrawingView extends View {
    private static final String TAG = "DrawingView";
    private static final float TOUCH_TOLERANCE = 4;
    private Bitmap mBitmap;
    private Bitmap mOriginBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private boolean mDrawMode;
    private float mX, mY;
    private float s, e;
    private float mProportion = 0;
    private LinkedList<DrawPath> savePath;
    private LinkedList<Integer> saveRange;
    private DrawPath mLastDrawPath;
    private Matrix matrix;
    private float mPaintBarPenSize;
    private int mPaintBarPenColor;

    private int mode = 0;// 初始状态
    private static final int MODE_ZOOM = 2;//缩放
    private Rect mSrcRect, mDestRect;
    Bitmap bitmapLeft,bitmapRight;
    private Path mPath = new Path();
    int bitmapWidth = 0;
    int bitmapHeight = 0;


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
        saveRange = new LinkedList<>();
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
        float proportion = (float) canvas.getHeight() / mBitmap.getWidth();
        if (proportion < 1) {
            mProportion = 0;
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            //绘制path
            canvas.drawPath(mPath, mPaint);
        } else {
            mProportion = proportion;
            matrix.reset();
            matrix.postScale(proportion, proportion);
            matrix.postTranslate((canvas.getWidth() - mBitmap.getWidth() * proportion) / 2, 0);
            canvas.drawBitmap(mBitmap, matrix, mBitmapPaint);
            //绘制path
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * @return 当前画布上的内容
     */
    public Bitmap getImageBitmap() {
        return mBitmap;
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
                mPath.moveTo(x, y);
                s = x;
                e = y;
                mX = x;
                mY = y;
//                mCanvas.drawPath(mPath, mPaint);
                break;
            case MotionEvent.ACTION_MOVE:
//                float dx = Math.abs(x - mX);
//                float dy = Math.abs(y - mY);
//                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//                    mX = x;
//                    mY = y;
//                }
                float endX=(mX+event.getX())/2;
                float endY=(mY+event.getY())/2;
                //quadTo前两个参数是控制点，后两个是终点
                mPath.quadTo(mX, mY, endX, endY);
                mX=event.getX();
                mY=event.getY();
                //触发view不断重绘
                invalidate();
//                mCanvas.drawPath(mPath, mPaint);
//                mCanvas.drawLine(mX, mY, (x + mX) / 2, (y + mY) / 2, mPaint);
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(mX, mY);
                double a = (y - e) * (y - e) + (x - s) * (x - s);
                int length = (int) Math.sqrt(a);
                mCanvas.drawPath(mPath, mPaint);
                mCanvas.drawTextOnPath("" + length, mPath, 0, -15, mPaint);
                mLastDrawPath = new DrawPath(mPath, mPaint.getColor(), mPaint.getStrokeWidth());
                savePath.add(mLastDrawPath);
                saveRange.add(length);
                drawArrow(s,e,mX,mY,5,mPaint);
                drawArrow(mX,mY,s,e,5,mPaint);
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 画箭头
     *
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param paint
     */
    private void drawArrow(float sx, float sy, float ex, float ey, int width, Paint paint) {
        int size = 3;
        int count = 20;
        float x = ex - sx;
        float y = ey - sy;
        double d = x * x + y * y;
        double r = Math.sqrt(d);
        float zx = (float) (ex - (count * x / r));
        float zy = (float) (ey - (count * y / r));
        float xz = zx - sx;
        float yz = zy - sy;
        double zd = xz * xz + yz * yz;
        double zr = Math.sqrt(zd);
        Path triangle = new Path();
        triangle.moveTo(sx, sy);
        triangle.lineTo((float) (zx + size * yz / zr), (float) (zy - size * xz / zr));
        triangle.lineTo((float) (zx + size * 2 * yz / zr), (float) (zy - size * 2 * xz / zr));
        triangle.lineTo(ex, ey);
        triangle.lineTo((float) (zx - size * 2 * yz / zr), (float) (zy + size * 2 * xz / zr));
        triangle.lineTo((float) (zx - size * yz / zr), (float) (zy + size * xz / zr));
        triangle.close();
        mCanvas.drawPath(triangle, paint);
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

    public void undo() {
        Log.d(TAG, "undo: recall last path");
        if (savePath != null && savePath.size() > 0) {
//            // 清空画布
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            loadImage(mOriginBitmap,bitmapWidth,bitmapHeight);

            savePath.removeLast();
            saveRange.removeLast();

            // 将路径保存列表中的路径重绘在画布上 遍历绘制
            for (int i = 0;i<savePath.size();i++){
                mPaint.setColor(savePath.get(i).getPaintColor());
                mPaint.setStrokeWidth(savePath.get(i).getPaintWidth());
                mCanvas.drawPath(savePath.get(i).path, mPaint);
                mCanvas.drawTextOnPath("" + saveRange.get(i), savePath.get(i).path, 0, -15, mPaint);//vOffset设置垂直方向位移的距离。
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