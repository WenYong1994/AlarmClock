package com.weny.mystudioapp.alarmclock.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.weny.mystudioapp.alarmclock.R;

public class MusicService extends Service {
    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(intent.getIntExtra("action",0)==0){//代表是开始播放
            if(player!=null&&player.isPlaying()){//释放上一次的
                player.stop();
            }
            player = MediaPlayer.create(this, R.raw.send);
            player.setLooping(true);
            player.start();
            //一分钟之后关闭
        }else if(intent.getIntExtra("action",0)==1){//停止播放
            if(player!=null&&player.isPlaying()){
                player.stop();
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
