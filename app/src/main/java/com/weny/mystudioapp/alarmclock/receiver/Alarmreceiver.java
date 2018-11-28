package com.weny.mystudioapp.alarmclock.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.weny.mystudioapp.alarmclock.control.AlarmControl;

public class Alarmreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //开始长达一分钟的响铃
        AlarmControl.getIntence().onReciver(context,intent);

        long time = intent.getLongExtra("time", 0);
        int id = intent.getIntExtra("id",0);
        if(time!=0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//重新设置闹钟达循环闹钟
                AlarmManager am = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                long newTime = time+1000*60;
                Intent intent1 = new Intent(context, Alarmreceiver.class);
                intent1.putExtra("time",newTime);
                PendingIntent sender = PendingIntent.getBroadcast(context,
                        id, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
                am.setExact(AlarmManager.RTC_WAKEUP,newTime , sender);
            }
        }
    }

}
