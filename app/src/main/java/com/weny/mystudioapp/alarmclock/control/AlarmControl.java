package com.weny.mystudioapp.alarmclock.control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.weny.mystudioapp.alarmclock.activity.MainActivity;
import com.weny.mystudioapp.alarmclock.app.App;
import com.weny.mystudioapp.alarmclock.model.DateHepler;
import com.weny.mystudioapp.alarmclock.receiver.Alarmreceiver;
import com.weny.mystudioapp.alarmclock.utils.NotificationUtils;
import com.weny.mystudioapp.alarmclock.utils.TimeUtils;
import com.weny.mystudioapp.alarmclock.utils.Utils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
public class AlarmControl {
    public static AlarmControl intence;

    public static AlarmControl getIntence() {
        if(intence==null){
            synchronized (AlarmControl.class){
                if(intence==null){
                    intence=new AlarmControl();
                }
            }
        }
        return intence;
    }

    public  boolean neeoNotification = false;

    private  final int REQUEST_CODE = 0;

    private  long lastTime;

    private  Handler handler;

    public  void setAlarmTime(Context context, String alarmTime){
        long date = TimeUtils.string2Date(alarmTime).getTime();
        lastTime = date;
        if(date!=0){//异常了不要设置
            Intent intent = new Intent(context, Alarmreceiver.class);
            intent.putExtra("time",date);
            intent.putExtra("id",REQUEST_CODE);
            PendingIntent sender = PendingIntent.getBroadcast(context,
                    REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            // Schedule the alarm!
            AlarmManager am = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,date , sender);
                }else {
                    am.setExact(AlarmManager.RTC_WAKEUP,date , sender);
                }
            }else {
                am.setRepeating(AlarmManager.RTC_WAKEUP,date , 1000*60, sender);
            }
            DateHepler.getInstence().setAlarmTime(alarmTime);
            DateHepler.getInstence().setAlarmCount(0);

//            cancleAlarm(context,REQUEST_CODE);
//
        }

    }

    public void cancleAlarm(Context context,int id){
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarmreceiver.class);
//        intent.putExtra("time",lastTime);
        PendingIntent sender = PendingIntent.getBroadcast(context,
                id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(sender);
    }



    public  void turnoffAlarm(Context context){
        DateHepler.getInstence().setAlarmCount(0);
        DateHepler.getInstence().setAlarmTime("");
        SoundContorl.getInstence().stopSound();
        cancleAlarm(context,REQUEST_CODE);
    }


    public  String getAlarmTime(){
       return DateHepler.getInstence().getAlarmTime();
    }



    public  void onReciver(Context context,Intent intent){
        int alarmCount = DateHepler.getInstence().getAlarmCount();
        DateHepler.getInstence().setAlarmCount(alarmCount+1);//让提示次数+1
        //执行Activity回调
        Log.e("test","alarmCount："+alarmCount);

        //然后开始响铃
        SoundContorl.getInstence().playSound();

        Log.e("test","neeoNotification："+neeoNotification);

        //如果在后台是否需要展示通知
        if(Utils.isBackground(App.getApp())){
            NotificationUtils.notification(App.getApp(),alarmCount+1);
        }


        if(handler==null){
            handler=new Handler(App.getApp().getMainLooper());
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SoundContorl.getInstence().stopSound();
            }
        }, 10*1000);




    }



    public static void relase(){
        if(intence!=null){
            intence.handler= null;
        }
        SoundContorl.realse();

    }








}
