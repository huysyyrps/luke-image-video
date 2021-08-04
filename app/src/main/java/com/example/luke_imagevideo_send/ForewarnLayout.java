package com.example.luke_imagevideo_send;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ForewarnLayout extends RelativeLayout {
    private float mTouchX;
    private float mTouchY;
    private float x;
    private float y;
    private float mStartX;
    private float mStartY;

    // 窗口视图
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private OnClickListener mOnClickListener;
    //语音图片
    private ImageView voiceBtn = null;
    private View mView = null;
    private static int sign = 1;

    public ForewarnLayout(Context context) {
        super(context);

        WindowManager.LayoutParams myparams = new WindowManager.LayoutParams();
        myparams.height = LayoutParams.WRAP_CONTENT;
        myparams.width = LayoutParams.WRAP_CONTENT;
        this.setLayoutParams(myparams);

        // 加载布局
        LayoutInflater lf = LayoutInflater.from(context);
        mWindowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mView = lf.inflate(R.layout.dialog_with_wifisetting, null);
        this.addView(mView, myparams);

        voiceBtn = (ImageView) findViewById(R.id.ivClose);
    }

    /**
     * tounch事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取到状态栏的高度
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println("statusBarHeight:" + statusBarHeight);
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - statusBarHeight; // statusBarHeight是系统状态栏的高度
        Log.i("tag", "currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchX = event.getX();
                mTouchY = event.getY();
                mStartX = x;

                mStartY = y;
                Log.i("tag", "startX" + mTouchX + "====startY" + mTouchY);
                break;

            case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
                updateViewPosition();
                mTouchX = mTouchY = 0;
                if ((x - mStartX) < 2 && (y - mStartY) < 2) {
                    // 设置监听
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(this);
                    }
                }
                break;
        }
        return true;
    }

    // 更新浮动窗口位置参数
    private void updateViewPosition() {
        mLayoutParams.x = (int) (x - mTouchX);
        mLayoutParams.y = (int) (y - mTouchY);
        mWindowManager.updateViewLayout(this, mLayoutParams); // 刷新显示
    }

    // 获取界面布局参数对象
    public WindowManager.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    // 设置界面布局参数
    public void setWmParams(WindowManager.LayoutParams layoutParams) {
        mLayoutParams = layoutParams;
    }

    // 界面点击事件监听
    @Override
    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    /**
     * 设置语音浮动按钮状态
     *
     * @param type
     */
    public void setVoiceBtnType(int type) {
        // 录音
        if (type == 1) {
            voiceBtn.setImageResource(R.drawable.ic_up);
            // 暂停录音
        } else if (type == 0) {
            voiceBtn.setImageResource(R.drawable.back);
        }
    }

}