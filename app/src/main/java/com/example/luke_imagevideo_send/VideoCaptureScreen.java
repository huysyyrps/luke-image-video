package com.example.luke_imagevideo_send;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoCaptureHandler;
import im.zego.zegoexpress.constants.ZegoPublishChannel;


/**
 * Copyright © 2016 Zego. All rights reserved.
 */
@TargetApi(21)
public
class VideoCaptureScreen extends IZegoCustomVideoCaptureHandler {

    private HandlerThread mHandlerThread = null;

    private Handler mHandler = null;

    private volatile MediaProjection mMediaProjection;

    private volatile VirtualDisplay mVirtualDisplay = null;

    private volatile boolean mIsCapturing = false;

    private volatile Surface mSurface = null;

    private static final int DEFAULT_VIDEO_WIDTH = 720;

    private static final int DEFAULT_VIDEO_HEIGHT = 1280;

    private volatile int mCaptureWidth;

    private volatile int mCaptureHeight;

    private ZegoExpressEngine mZegoEngine;

    public VideoCaptureScreen(MediaProjection mediaProjection, int captureWidth, int captureHeight, ZegoExpressEngine mZegoEngine) {
        mMediaProjection = mediaProjection;
        mCaptureWidth = captureWidth;
        mCaptureHeight = captureHeight;
        this.mZegoEngine = mZegoEngine;
    }

    @Override
    public void onStart(ZegoPublishChannel channel) {
        mHandlerThread = new HandlerThread("ZegoScreenCapture");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        startCapture();
        setMediaProjection(mMediaProjection);
    }

    @Override
    public void onStop(ZegoPublishChannel channel) {
        stopCapture();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
            mHandler = null;
        }

        mCaptureWidth = DEFAULT_VIDEO_WIDTH;
        mCaptureHeight = DEFAULT_VIDEO_HEIGHT;

    }


    private int startCapture() {

        if (mZegoEngine != null && !mIsCapturing && mMediaProjection != null) {
            mIsCapturing = true;

            SurfaceTexture texture = mZegoEngine.getCustomVideoCaptureSurfaceTexture();
            texture.setDefaultBufferSize(mCaptureWidth, mCaptureHeight);
            mSurface = new Surface(texture);

            mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                    mCaptureWidth, mCaptureHeight, 1,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, mSurface, null, mHandler);
        }
        return 0;
    }


    private int stopCapture() {
        mIsCapturing = false;

        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }

        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }

        return 0;
    }
    public void setMediaProjection(MediaProjection mediaProjection) {
        mMediaProjection = mediaProjection;

        if (mIsCapturing && mZegoEngine != null && mMediaProjection != null && mSurface != null) {
            if (mVirtualDisplay != null) {
                mVirtualDisplay.release();
            }
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                    mCaptureWidth, mCaptureHeight, 1,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, mSurface, null, mHandler);
        }
    }
}
