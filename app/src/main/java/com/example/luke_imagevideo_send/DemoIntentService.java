package com.example.luke_imagevideo_send;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import static android.media.AudioManager.RINGER_MODE_SILENT;

/**
 * 继承 GTIntentService 接收来自个推的消息，所有消息在线程中回调，如果注册了该服务，则务必要在 AndroidManifest 中声明，否则无法接受消息
 */
public class DemoIntentService extends GTIntentService {

    boolean shouldPlayBeep = true;
    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    // 处理透传消息
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);
            Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{0, 1000}, -1);
        }
    }

    // 接收 cid
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e("PUSH_LOG", "onReceiveClientId -> " + "clientid = " + clientid);
    }

    // cid 离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    // 各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e("PUSH_LOG处理回执", cmdMessage.getAction() + "");
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        Log.e("PUSH_LOG通知到达", msg.getContent() + "");
        Toast.makeText(context, "1111111", Toast.LENGTH_SHORT).show();
        new Notification.Builder(context).setDefaults(RINGER_MODE_SILENT);
        long[] pattern = {0, 100};
        Uri sound=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/"+R.raw.fengming);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setVibrate(pattern)
                .setSound(sound);
        mNotifyManager.notify(1, builder.build());
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
//        AudioManager audioService = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        if (AudioManager.RINGER_MODE_NORMAL != audioService.getRingerMode()) {
//            shouldPlayBeep = false;
//        }
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mp) {
//                mp.seekTo(0);
//            }
//        });
//        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.fengming);
//        try {
//            mediaPlayer.setDataSource(file.getFileDescriptor(),file.getStartOffset(),file.getLength());
//            file.close();
//            mediaPlayer.setVolume(0.5f,0.5f);
//            mediaPlayer.prepare();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mediaPlayer=null;
//        }
//        if (shouldPlayBeep&&mediaPlayer!=null) {
//            mediaPlayer.start();
//        }
    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
        Log.e("PUSH_LOG通知到达", msg.getContent() + "");
    }
}