package com.weny.mystudioapp.alarmclock.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.weny.mystudioapp.alarmclock.R;

public class MusicService extends Service {
    private MediaPlayer player;
    Vibrator vibrator;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
        vibrator= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if(intent.getIntExtra("action",0)==0){//代表是开始播放
            if(player!=null&&player.isPlaying()){//释放上一次的
                player.stop();
            }
            player = MediaPlayer.create(this, R.raw.send);
            player.setLooping(true);
            player.start();
            if(vibrator.hasVibrator()){
                vibrator.cancel();
                vibrator.vibrate(10*1000);
            }


            //一分钟之后关闭
        }else if(intent.getIntExtra("action",0)==1){//停止播放
            if(player!=null&&player.isPlaying()){
                player.stop();
                if(vibrator.hasVibrator()){
                    vibrator.cancel();
                }
            }
        }

    }





    public void onDestroy()
    {
        super.onDestroy();
        if(player != null){
            player.stop();
            player = null;
        }
    }
}
