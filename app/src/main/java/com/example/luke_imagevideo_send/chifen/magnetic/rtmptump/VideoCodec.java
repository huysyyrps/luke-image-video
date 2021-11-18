package com.example.luke_imagevideo_send.chifen.magnetic.rtmptump;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoCodec extends Thread {

    private static final String TAG = "------>dddd<---------";
    private final ScreenLive screenLive;

    private MediaProjection mediaProjection;

    private MediaCodec mediaCodec;//硬编
    private boolean isLiving;
    private VirtualDisplay virtualDisplay;//虚拟画布
    private long timeStamp;
    private long startTime;

    public VideoCodec(ScreenLive screenLive) {
        this.screenLive = screenLive;
    }

    public void startLive(MediaProjection mediaProjection) {
        this.mediaProjection = mediaProjection;
        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 1280, 640);
        //设置颜色格式
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        //码率，帧率，分辨率，关键帧间隔
        format.setInteger(MediaFormat.KEY_BIT_RATE, 400_000);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        // 当画面静止时,重复最后一帧，不影响界面显示
        format.setLong(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / 45);
        format.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR);
        //设置复用模式
        format.setInteger(MediaFormat.KEY_COMPLEXITY, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);//手机
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface surface = mediaCodec.createInputSurface();

            virtualDisplay = mediaProjection.createVirtualDisplay(
                    "screen-codec",
                    1280, 640, 1,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                    surface, null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        LiveTaskManager.getInstance().execute(this);
    }

    @Override
    public void run() {
        isLiving = true;
        mediaCodec.start();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        while (isLiving) {
            //2000毫秒 手动触发输出关键帧
            if (System.currentTimeMillis() - timeStamp >= 1500) {
                Bundle params = new Bundle();
                //立即刷新 让下一帧是关键帧
                params.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0);
                mediaCodec.setParameters(params);
                timeStamp = System.currentTimeMillis();
            }
            //查询解码输出此函数一直是返回-1，没有成功解码。
            int index = mediaCodec.dequeueOutputBuffer(bufferInfo, 100000);
            Log.i(TAG, "VideoCodec run: " + index);
            if (index >= 0) {
                ByteBuffer buffer = mediaCodec.getOutputBuffer(index);
                MediaFormat mediaFormat = mediaCodec.getOutputFormat(index);
                Log.i(TAG, "mediaFormat: " + mediaFormat.toString());
                byte[] outData = new byte[bufferInfo.size];
                buffer.get(outData);
                if (startTime == 0) {
                    // 微妙转为毫秒
                    startTime = bufferInfo.presentationTimeUs / 1000;
                }
//                writeContent(outData);
//                writeBytes(outData);
//                包含   分隔符
                RTMPPackage rtmpPackage = new RTMPPackage(outData, (bufferInfo.presentationTimeUs / 1000) - startTime);
                rtmpPackage.setType(RTMPPackage.RTMP_PACKET_TYPE_VIDEO);
                screenLive.addPackage(rtmpPackage);
                //对outputbuffer的处理完后，调用这个函数把buffer重新返回给codec类。
                //调用这个api之后，SurfaceView才有图像
                mediaCodec.releaseOutputBuffer(index, false);
            }
        }
        isLiving = false;
        startTime = 0;
        mediaCodec.stop();
        mediaCodec.release();
        mediaCodec = null;
        virtualDisplay.release();
        virtualDisplay = null;
        mediaProjection.stop();
        mediaProjection = null;
    }

    public void writeBytes(byte[] array) {
        FileOutputStream writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileOutputStream(Environment.getExternalStorageDirectory() + "/codec.h264", true);
            writer.write(array);
            writer.write('\n');


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String writeContent(byte[] array) {
        char[] HEX_CHAR_TABLE = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(HEX_CHAR_TABLE[(b & 0xf0) >> 4]);
            sb.append(HEX_CHAR_TABLE[b & 0x0f]);
        }

        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(Environment.getExternalStorageDirectory() + "/codec.txt", true);
            writer.write(sb.toString());
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
