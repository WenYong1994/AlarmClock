package com.weny.mystudioapp.alarmclock.control;

import android.content.Intent;
import android.os.Handler;

import com.weny.mystudioapp.alarmclock.app.App;
import com.weny.mystudioapp.alarmclock.service.MusicService;


public class SoundContorl {
    private static SoundContorl instence;

    public static SoundContorl getInstence() {
        if(instence==null){
            synchronized (SoundContorl.class){
                if(instence == null){
                    instence =new SoundContorl();
                }
            }
        }
        return instence;
    }


    private SoundContorl(){
        handler = new Handler(App.getApp().getMainLooper());
    }




    private boolean isPlaying = false;
    Handler handler;


    public void playSound(){
        if(!isPlaying){
            isPlaying = true;
            Intent intent =new Intent(App.getApp(),MusicService.class);
            intent.putExtra("action",0);
            App.getApp().startService(intent);
        }
    }

    public void stopSound(){
        Intent intent1 =  new Intent(App.getApp(),MusicService.class);
        intent1.putExtra("action",1);
        App.getApp().startService(intent1);
        isPlaying = false;
    }


    public static void realse(){

        instence=null;
    }
}
