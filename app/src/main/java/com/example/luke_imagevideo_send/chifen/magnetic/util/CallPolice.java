package com.example.luke_imagevideo_send.chifen.magnetic.util;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.activity.MainCHYActivity;

public class CallPolice {
    boolean shouldPlayBeep = true;
    public void sendPolice(MainCHYActivity mainCHYActivity){
        Vibrator vibrator = (Vibrator) mainCHYActivity.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 1000}, -1);
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();
        AudioManager audioService = (AudioManager) mainCHYActivity.getSystemService(Context.AUDIO_SERVICE);
        if (AudioManager.RINGER_MODE_NORMAL != audioService.getRingerMode()) {
            shouldPlayBeep = false;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
            }
        });
        AssetFileDescriptor file = mainCHYActivity.getResources().openRawResourceFd(R.raw.fengming);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(),file.getStartOffset(),file.getLength());
            file.close();
            mediaPlayer.setVolume(0.5f,0.5f);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            mediaPlayer=null;
        }
        if (shouldPlayBeep&&mediaPlayer!=null) {
            mediaPlayer.start();
        }
    }
}
