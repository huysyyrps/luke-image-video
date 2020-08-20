package com.example.luke_imagevideo_send.chifen.camera;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by guoxw on 2017/10/31.
 * <!- 实现拍照,录像:  并保存图片，视频到本地></!->
 */

public class CameraUtils {

    private MediaRecorder mediaRecorder;
    private Camera camera;
    /*** 标识当前是前摄像头还是后摄像头  back:0  front:1*/
    private int backOrFtont = 0;
    private SurfaceHolder.Callback callback;
    private Context context;
    private SurfaceView surfaceView;
    /***录制视频的videoSize*/
    private int height, width;
    /***photo的height ,width*/
    private int heightPhoto, widthPhoto;
    private static AlertDialogUtil alertDialogUtil;

    public void create(SurfaceView surfaceView, Context context) {
        this.context = context;
        this.surfaceView = surfaceView;
        alertDialogUtil = new AlertDialogUtil(context);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setKeepScreenOn(true);
        callback = new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                camera = Camera.open();
                getVideoSize();
                mediaRecorder = new MediaRecorder();

            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                doChange(holder);
                focus();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    camera.release();
                    camera = null;
                }
            }
        };
        surfaceView.getHolder().addCallback(callback);

    }

    private void doChange(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(0);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换摄像头
     */
    public void changeCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && backOrFtont == 0) {
                camera.stopPreview();
                camera.release();
                camera = null;
                camera = Camera.open(i);
                try {
                    camera.setPreviewDisplay(surfaceView.getHolder());
                    camera.setDisplayOrientation(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                backOrFtont = 1;
                camera.startPreview();
                break;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK && backOrFtont == 1) {
                camera.stopPreview();
                camera.release();
                camera = null;
                camera = Camera.open(i);
                try {
                    camera.setPreviewDisplay(surfaceView.getHolder());
                    camera.setDisplayOrientation(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
                backOrFtont = 0;
                break;
            }
        }

    }

    public void stopRecord() {
        mediaRecorder.release();
        camera.release();
        mediaRecorder = null;
        camera = Camera.open();
        mediaRecorder = new MediaRecorder();
        doChange(surfaceView.getHolder());
    }


    public void stop() {
        if (mediaRecorder != null && camera != null) {
            mediaRecorder.release();
            camera.release();
        }
    }

    public void destroy() {
        if (mediaRecorder != null && camera != null) {
            mediaRecorder.release();
            camera.release();
            mediaRecorder = null;
            camera = null;
        }

    }

    /**
     * @param path 保存的路径
     * @param name 录像视频名称(不包含后缀)
     */
    public void startRecord(String path, String name) {
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
////        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_LOW));
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        mediaRecorder.setVideoEncodingBitRate(2048 * 2048);
////        mediaRecorder.setVideoSize(width, height);
////        mediaRecorder.setVideoFrameRate(60);
        mediaRecorder.setOutputFormat(mProfile.fileFormat);
        mediaRecorder.setAudioEncoder(mProfile.audioCodec);
        mediaRecorder.setVideoEncoder(mProfile.videoCodec);
        mediaRecorder.setVideoSize(mProfile.videoFrameWidth, mProfile.videoFrameHeight);
        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
        mediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
        mediaRecorder.setAudioEncodingBitRate(mProfile.audioBitRate);
        mediaRecorder.setAudioChannels(mProfile.audioChannels);
        mediaRecorder.setAudioSamplingRate(mProfile.audioSampleRate);
        //getVideoSize();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        mediaRecorder.setOutputFile(path + File.separator + name + ".mp4");
        File file1 = new File(path + File.separator + name + ".mp4");
        if (file1.exists()) {
            file1.delete();
        }
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        /***不设置时，录制的视频总是倒着，翻屏导致视频上下翻滚*/
        mediaRecorder.setOrientationHint(0);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 获取SupportedVideoSizes 控制输出视频width在300到600之间(尽可能小)
     * 获取PictureSize的大小(控制在w：1000-2000之间)
     */
    public void getVideoSize() {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> videoSize = parameters.getSupportedVideoSizes();
        for (int i = 0; i < videoSize.size(); i++) {
            int width1 = videoSize.get(i).width;
            int height1 = videoSize.get(i).height;
            if (width1 >= 300 && width1 <= 1000) {
                if (height1 >= 300 && height1 <= 1000) {
                    width = width1;
                    height = height1;
                }

            }
            Log.d(TAG, "getVideoSize:----w:-- " + videoSize.get(i).width + "---h:--" + videoSize.get(i).height);
        }
        List<Camera.Size> photoSize = parameters.getSupportedPictureSizes();
        for (int i = 0; i < photoSize.size(); i++) {
            int width1 = photoSize.get(i).width;
            int height1 = photoSize.get(i).height;
            if (width1 >= 1000 && width1 <= 2000) {
                if (height1 >= 600 && height1 <=2000) {
                    widthPhoto = width1;
                    heightPhoto = height1;
                }

            }
        }

    }


    public void takePicture(String photoPath, String photoName) {

        camera.takePicture(null, null, new PictureCallBack(photoPath, photoName));
    }

    /**
     * 聚焦
     */
    public void focus(){
        Camera.Parameters parameters=camera.getParameters();
        parameters.setPictureSize(widthPhoto,heightPhoto);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(parameters);
        camera.cancelAutoFocus();
    }

    /*** 拍照功能*/
    private class PictureCallBack implements Camera.PictureCallback {
        /*** 照片保存的路径和名称*/
        private String path;
        private String name;

        public PictureCallBack(String path, String name) {
            this.path = path;
            this.name = name;
        }

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            camera.stopPreview();
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            alertDialogUtil.showImageDialog(new AlertDialogCallBack() {

                @Override
                public void confirm(String name1) {
                    if (!name1.equals("")){
                        name = name1+".png";
                    }
                    File file = new File(path, name);
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        try {
                            fos.write(bytes);
                            Toast.makeText(context, "本地存储成功", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();
                }

                @Override
                public void cancel() {

                }

                @Override
                public void save(String name1) {
                    if (!name1.equals("")){
                        name = name1+".png";
                    }
                    File file = new File(path, name);
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        try {
                            fos.write(bytes);
                            Toast.makeText(context, "本地存储成功", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();
                }

                @Override
                public void checkName(String name1) {
                    if (!name1.equals("")){
                        name = name1+".png";
                    }
                    File file = new File(path, name);
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        try {
                            fos.write(bytes);
                            Toast.makeText(context, "本地存储成功", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();
                }
            });
        }
    }
}
